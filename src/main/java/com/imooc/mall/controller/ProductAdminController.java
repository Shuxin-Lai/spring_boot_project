package com.imooc.mall.controller;

import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.model.request.category.AddProductReq;
import com.imooc.mall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller
public class ProductAdminController {
  @Autowired
  ProductService productService;

  @PostMapping("/admin/product/add")
  @ResponseBody
  public ApiRestResponse addProduct(@Valid @RequestBody AddProductReq addProductReq) {
    productService.adminAddProduct(addProductReq);
    return ApiRestResponse.success();
  }
}
