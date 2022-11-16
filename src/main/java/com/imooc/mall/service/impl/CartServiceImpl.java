package com.imooc.mall.service.impl;

import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.dao.CartMapper;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.request.cart.AddCartReq;
import com.imooc.mall.model.vo.CartVO;
import com.imooc.mall.service.CartService;
import com.imooc.mall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
  @Autowired
  ProductService productService;

  @Autowired
  CartMapper cartMapper;

  @Override
  public List<CartVO> addCart(Integer userId, AddCartReq addCartReq) {
    validProduct(addCartReq.getProductId(), addCartReq.getCount());
    Integer productId = addCartReq.getProductId();
    Integer count = addCartReq.getCount();

    com.imooc.mall.model.pojo.Cart cartOld = cartMapper.selectByUserIdAndProductId(userId, addCartReq.getProductId());

    if (cartOld != null) {
      com.imooc.mall.model.pojo.Cart cart = new com.imooc.mall.model.pojo.Cart();
      cart.setId(cartOld.getId());
      cart.setSelected(Constant.Cart.CHECKED);
      cart.setQuantity(count + cartOld.getQuantity());
      cartMapper.updateByPrimaryKeySelective(cart);
    } else {
      com.imooc.mall.model.pojo.Cart cart = new com.imooc.mall.model.pojo.Cart();
      cart.setProductId(productId);
      cart.setUserId(userId);
      cart.setQuantity(count);
      cart.setSelected(Constant.Cart.CHECKED);

      cartMapper.insertSelective(cart);
    }
    return list(userId);
  }

  private void validProduct(Integer productId, Integer count) {
    Product product = productService.selectById(productId);
    if (product == null) {
      throw new ImoocException(ImoocMallExceptionEnum.PRODUCT_NOT_EXISTS);
    }

    if (!product.getStatus().equals(Constant.SaleStatus.SALE)) {
      throw new ImoocException(ImoocMallExceptionEnum.PRODUCT_NOT_SALE);
    }

    if (product.getStock() < count) {
      throw new ImoocException(ImoocMallExceptionEnum.PRODUCT_OUT_OK_STOCK);
    }
  }

  @Override
  public List<CartVO> list(Integer userId) {
    List<CartVO> cartVOS = cartMapper.selectList(userId);

    for (CartVO c : cartVOS) {
      c.setTotalPrice(c.getPrice() * c.getQuantity());
    }

    return cartVOS;
  }
}
