package com.imooc.mall.controller;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.filter.UserFilter;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.pojo.User;
import com.imooc.mall.model.request.cart.AddCartReq;
import com.imooc.mall.model.request.product.ProductListReq;
import com.imooc.mall.service.CartService;
import com.imooc.mall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {
  @Autowired
  ProductService productService;

  @Autowired
  CartService cartService;

  @PostMapping("/add")
  public ApiRestResponse addCart(@Valid @RequestBody AddCartReq addCartReq) {
    Integer userId = UserFilter.currentUser.getId();

    cartService.addCart(userId, addCartReq);
    return ApiRestResponse.success();
  }
}
