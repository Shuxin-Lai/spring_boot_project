package com.imooc.mall.service.impl;

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
import com.imooc.mall.model.request.order.CreateOrderReq;
import com.imooc.mall.model.vo.CartVO;
import com.imooc.mall.service.CartService;
import com.imooc.mall.service.OrderService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
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
