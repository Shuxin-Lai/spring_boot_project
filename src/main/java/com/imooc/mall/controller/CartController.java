package com.imooc.mall.controller;

import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.filter.UserFilter;
import com.imooc.mall.model.request.cart.AddCartReq;
import com.imooc.mall.model.request.cart.UpdateCartReq;
import com.imooc.mall.model.vo.CartVO;
import com.imooc.mall.service.CartService;
import com.imooc.mall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {
  @Autowired
  ProductService productService;

  @Autowired
  CartService cartService;

  @GetMapping("/list")
  public ApiRestResponse list() {
    Integer userId = UserFilter.currentUser.getId();

    List<CartVO> cartVOS = cartService.list(userId);
    return ApiRestResponse.success(cartVOS);
  }

  @PostMapping("/add")
  public ApiRestResponse addCart(@Valid @RequestBody AddCartReq addCartReq) {
    Integer userId = UserFilter.currentUser.getId();

    List<CartVO> cartVOS = cartService.addCart(userId, addCartReq);
    return ApiRestResponse.success(cartVOS);
  }

  @PostMapping("/update")
  public ApiRestResponse update(@Valid @RequestBody UpdateCartReq updateCartReq) {
    Integer userId = UserFilter.currentUser.getId();

    List<CartVO> cartVOS = cartService.update(userId, updateCartReq);
    return ApiRestResponse.success(cartVOS);
  }

  @PostMapping("/delete/{id}")
  public ApiRestResponse delete(@PathVariable("id") Integer id) {
    Integer userId = UserFilter.currentUser.getId();

    List<CartVO> cartVOS = cartService.delete(userId, id);
    return ApiRestResponse.success(cartVOS);
  }

  @PostMapping("/select")
  public ApiRestResponse selectOrNotSelect(@RequestParam("productId") Integer productId,
                                           @RequestParam("status") Integer status) {
    Integer userId = UserFilter.currentUser.getId();
    List<CartVO> select = cartService.selectOrNotSelect(userId, productId, status);
    return ApiRestResponse.success(select);
  }

  @PostMapping("/select_all")
  public ApiRestResponse selectOrNotSelectAll(
      @RequestParam("status") Integer status) {
    Integer userId = UserFilter.currentUser.getId();
    List<CartVO> select = cartService.selectOrNotSelectAll(userId, status);
    return ApiRestResponse.success(select);
  }

}
