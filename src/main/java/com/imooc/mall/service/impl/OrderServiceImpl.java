package com.imooc.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.zxing.WriterException;
import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.filter.UserFilter;
import com.imooc.mall.model.dao.CartMapper;
import com.imooc.mall.model.dao.OrderItemMapper;
import com.imooc.mall.model.dao.OrderMapper;
import com.imooc.mall.model.dao.ProductMapper;
import com.imooc.mall.model.pojo.Order;
import com.imooc.mall.model.pojo.OrderItem;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.request.base.Pagination;
import com.imooc.mall.model.request.order.CreateOrderReq;
import com.imooc.mall.model.vo.CartVO;
import com.imooc.mall.model.vo.OrderItemVO;
import com.imooc.mall.model.vo.OrderVO;
import com.imooc.mall.service.CartService;
import com.imooc.mall.service.OrderService;
import com.imooc.mall.util.QRCodeGenerator;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
  @Autowired
  CartService cartService;
  @Autowired
  ProductMapper productMapper;
  @Autowired
  CartMapper cartMapper;
  @Autowired
  OrderMapper orderMapper;
  @Autowired
  OrderItemMapper orderItemMapper;

  @Value("${server.baseUrl}")
  String serverBaseUrl;

  @Transactional(rollbackFor = Exception.class)
  @Override
  public String create(CreateOrderReq createOrderReq) {
    // 获取用户 id,
    Integer userId = UserFilter.currentUser.getId();

    // 从购物车查询已勾选的商品
    //    购物车为空，抛出异常
    List<CartVO> list = cartService.list(userId);

    // 检查商品是否存在，上架状态，库存状态
    //    不合法，则抛出异常
    List<CartVO> listTmp = new ArrayList<>();
    for (CartVO cartVO : list) {
      if (cartVO.getSelected() == Constant.Cart.CHECKED) {
        listTmp.add(cartVO);
      }
    }
    list = listTmp;

    if (CollectionUtils.isEmpty(list)) {
      throw new ImoocException(ImoocMallExceptionEnum.CART_EMPTY);
    }

    validateSaleStatusAndStock(list);

    // 将购物车对象转化成商品对象
    List<OrderItem> orderItemList = cartVOListToOrderItemList(userId, list);
    // 扣库存

    for (OrderItem orderItem : orderItemList) {
      Product product = productMapper.selectByPrimaryKey(orderItem.getProductId());
      if (product == null) {
        throw new ImoocException(ImoocMallExceptionEnum.PRODUCT_NOT_EXISTS);
      }

      int stock = product.getStock() - orderItem.getQuantity();
      if (stock < 0) {
        throw new ImoocException(ImoocMallExceptionEnum.PRODUCT_OUT_OK_STOCK);
      }

      product.setStock(stock);
      productMapper.updateByPrimaryKey(product);
    }

    // 清除已勾选的商品
    cleanCart(list);
    // 生成订单
    Order order = new Order();
    order.setOrderNo(OrderCodeFactory.getOrderCode(userId));
    order.setUserId(userId);
    order.setTotalPrice(getTotalPrice(orderItemList));
    order.setReceiverName(createOrderReq.getReceiverName());
    order.setReceiverMobile(createOrderReq.getReceiverMobile());
    order.setReceiverAddress(createOrderReq.getReceiverAddress());
    order.setOrderStatus(Constant.OrderStatusEmum.NOT_PAID.getCode());

    order.setPostage(0);
    order.setPaymentType(1);
    // 保存每一个商品到 order_item 中
    int i = orderMapper.insertSelective(order);
    if (i != 1) {
      ImoocException.throwInsertError();
    }

    for (OrderItem orderItem : orderItemList) {
      orderItem.setOrderNo(order.getOrderNo());

      orderItemMapper.insertSelective(orderItem);
    }
    // 结果返回
    return order.getOrderNo();
  }

  @Override
  public OrderVO detail(String orderNo) {
    Order order = orderMapper.selectByOrderNo(orderNo);
    if (order == null) {
      throw new ImoocException(ImoocMallExceptionEnum.NO_ORDER);
    }
    if (!order.getUserId().equals(UserFilter.currentUser.getId())) {
      throw new ImoocException(ImoocMallExceptionEnum.NOT_YOUR_ORDER);
    }

    return getOrderVO(order);
  }

  @Override
  public PageInfo listForCustomer(Pagination pagination) {
    Integer pageNum = pagination.getPageNum();
    Integer pageSize = pagination.getPageSize();
    Integer userId = UserFilter.currentUser.getId();
    PageHelper.startPage(pageNum, pageNum, "create_time");

    List<Order> orderList = orderMapper.selectForCustomer(userId);
    List<OrderVO> orderVOList = transformOrderListToOrderVOList(orderList);

    PageInfo pageInfo = new PageInfo(orderList);
    pageInfo.setList(orderVOList);
    return pageInfo;
  }

  private List<OrderVO> transformOrderListToOrderVOList(List<Order> orderList) {
    List<OrderVO> list = new ArrayList<>();
    for (Order order : orderList) {
      OrderVO orderVO = getOrderVO(order);
      list.add(orderVO);
    }
    return list;
  }

  public OrderVO getOrderVO(Order order) {
    OrderVO orderVO = new OrderVO();
    BeanUtils.copyProperties(order, orderVO);
    List<OrderItemVO> list = new ArrayList<>();
    List<OrderItem> orderItemList = orderItemMapper.selectByOrderINo(order.getOrderNo());
    for (OrderItem orderItem : orderItemList) {
      OrderItemVO orderItemVO = new OrderItemVO();
      BeanUtils.copyProperties(orderItem, orderItemVO);
      list.add(orderItemVO);
//      orderItemVO.setOrderStatusName(Constant.OrderStatusEmum.codeOf(orderVO.getOrderStatus()).getValue());
    }

    orderVO.setOrderItemVOList(list);
    orderVO.setOrderStatusName(Constant.OrderStatusEmum.codeOf(orderVO.getOrderStatus()).getValue());
    return orderVO;
  }

  @Override
  public void cancel(String orderNo) {
    Order order = orderMapper.selectByOrderNo(orderNo);
    if (order == null) {
      throw new ImoocException(ImoocMallExceptionEnum.NO_ORDER);
    }

    if (!order.getUserId().equals(UserFilter.currentUser.getId())) {
      throw new ImoocException(ImoocMallExceptionEnum.NOT_YOUR_ORDER);
    }

    if (!order.getOrderStatus().equals(Constant.OrderStatusEmum.NOT_PAID.getCode())) {
      throw new ImoocException(ImoocMallExceptionEnum.ORDER_STATUS_ERROR);
    }

    order.setOrderStatus(Constant.OrderStatusEmum.CANCELED.getCode());
    order.setEndTime(new Date());
    orderMapper.updateByPrimaryKeySelective(order);
  }

  @Override
  public String qrcode(String orderNo) {
    checkOrder(orderNo);

    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    String payUrl = serverBaseUrl + "/pay?orderNo=" + orderNo;
    try {
      QRCodeGenerator.generatorQRCodeImage(payUrl, 320, 320,
          new File(Constant.FILE_UPDATE_DIR, orderNo + ".png").getAbsolutePath());
    } catch (Exception e) {
      throw new ImoocException(ImoocMallExceptionEnum.CREATE_QRCODE_FAILED);
    }
    String imgUrl = serverBaseUrl + "/images/" + orderNo + ".png";
    return imgUrl;
  }

  private Integer getTotalPrice(List<OrderItem> orderItemList) {
    int total = 0;
    for (OrderItem orderItem : orderItemList) {
      total += orderItem.getTotalPrice();
    }
    return total;
  }

  private void cleanCart(List<CartVO> list) {
    for (CartVO cartVO : list) {
      cartMapper.deleteByPrimaryKey(cartVO.getId());
    }
  }

  private List<OrderItem> cartVOListToOrderItemList(Integer userId, List<CartVO> list) {
    List<OrderItem> orderItemList = new ArrayList<>();

    for (CartVO cartVO : list) {
      OrderItem orderItem = new OrderItem();
      orderItem.setProductId(cartVO.getProductId());
      orderItem.setQuantity(cartVO.getQuantity());
      orderItem.setProductImg(cartVO.getProductImage());
      orderItem.setProductName(cartVO.getProductName());
      orderItem.setUnitPrice(cartVO.getPrice());
      orderItem.setTotalPrice(cartVO.getTotalPrice());

      orderItemList.add(orderItem);
    }

    return orderItemList;
  }

  private Order getOrder(String orderNo, boolean checkUser) {
    Order order = orderMapper.selectByOrderNo(orderNo);
    if (order == null) {
      throw new ImoocException(ImoocMallExceptionEnum.NO_ORDER);
    }

    if (checkUser) {
      if (!order.getUserId().equals(UserFilter.currentUser.getId())) {
        throw new ImoocException(ImoocMallExceptionEnum.NOT_YOUR_ORDER);
      }
    }

    return order;
  }


  private boolean checkOrder(String orderNo) {
    return checkOrder(orderNo, true);
  }

  private boolean checkOrder(String orderNo, boolean checkUser) {
    getOrder(orderNo, checkUser);
    return true;
  }

  private Order getOrder(String orderNo) {
    return getOrder(orderNo, true);
  }

  private void validateSaleStatusAndStock(List<CartVO> list) {
    for (CartVO cartVO : list) {
      Product product = productMapper.selectByPrimaryKey(cartVO.getProductId());
      if (product == null) {
        throw new ImoocException(ImoocMallExceptionEnum.PRODUCT_NOT_EXISTS);
      }
      if (!product.getStatus().equals(Constant.SaleStatus.SALE)) {
        throw new ImoocException(ImoocMallExceptionEnum.PRODUCT_NOT_SALE);
      }
      if (product.getStock() < cartVO.getQuantity()) {
        throw new ImoocException(ImoocMallExceptionEnum.PRODUCT_OUT_OK_STOCK);
      }
    }
  }
}
