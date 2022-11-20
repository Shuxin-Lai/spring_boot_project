package com.imooc.mall.controller;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.model.request.base.Pagination;
import com.imooc.mall.service.OrderService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/order")
public class OrderAdminController {
  @Autowired
  OrderService orderService;

  @GetMapping("/list")
  public ApiRestResponse list(Pagination pagination) {
    PageInfo pageInfo = orderService.listForAdmin(pagination);
    return ApiRestResponse.success(pageInfo);
  }

  @PostMapping("/delivered")
  public ApiRestResponse deliver(String orderNo) {

    orderService.deliver(orderNo);
    return ApiRestResponse.success();
  }

  @PostMapping("/finish")
  public ApiRestResponse finish(String orderNo) {

    orderService.fulfill(orderNo);
    return ApiRestResponse.success();
  }

}
