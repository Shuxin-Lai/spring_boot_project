package com.imooc.mall.service;

import com.imooc.mall.model.pojo.Cart;
import com.imooc.mall.model.request.cart.AddCartReq;
import com.imooc.mall.model.request.cart.UpdateCartReq;
import com.imooc.mall.model.vo.CartVO;

import java.util.List;

public interface CartService {
  List<CartVO> addCart(Integer userId, AddCartReq addCartReq);

  List<CartVO> list(Integer userId);

  List<CartVO> update(Integer userId, UpdateCartReq updateCartReq);

  List<CartVO> delete(Integer userId, Integer productId);

  List<CartVO> selectOrNotSelect(Integer userId, Integer productId, Integer selected);

  List<CartVO> selectOrNotSelectAll(Integer userId, Integer selected);
}
