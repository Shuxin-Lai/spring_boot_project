package com.imooc.mall.controller;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.request.product.AddProductReq;
import com.imooc.mall.model.request.product.ProductListReq;
import com.imooc.mall.model.request.product.UpdateProductReq;
import com.imooc.mall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

@RestController
public class ProductController {
  @Autowired
  ProductService productService;

  @GetMapping("/product/{id}")
  public ApiRestResponse getProduct(@PathVariable("id") Integer id) {
    Product product = productService.selectById(id);
    return ApiRestResponse.success(product);
  }

  @GetMapping("/product/list")
  public ApiRestResponse getProductList(ProductListReq productListReq) {
    PageInfo res =  productService.list(productListReq);
    return ApiRestResponse.success(res);
  }
}
