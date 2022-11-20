package com.imooc.mall.controller;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.model.request.base.Pagination;
import com.imooc.mall.model.request.order.CreateOrderReq;
import com.imooc.mall.model.vo.OrderVO;
import com.imooc.mall.service.OrderService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class OrderController {
  @Autowired
  OrderService orderService;

  @PostMapping("/order/create")
  public ApiRestResponse create(@Valid @RequestBody CreateOrderReq createOrderReq) {

    String orderNo = orderService.create(createOrderReq);
    return ApiRestResponse.success(orderNo);
  }

  @GetMapping("/order/detail")
  public ApiRestResponse detail(@RequestParam("orderNo") String orderNo) {
    OrderVO orderVO = orderService.detail(orderNo);
    return ApiRestResponse.success(orderVO);
  }

  @GetMapping("/order/list")
  public ApiRestResponse list(Pagination pagination) {
    PageInfo pageInfo = orderService.listForCustomer(pagination);
    return ApiRestResponse.success(pageInfo);
  }

  @PostMapping("/order/cancel")
  public ApiRestResponse cancel(@RequestParam("orderNo") String orderNo) {
    orderService.cancel(orderNo);
    return ApiRestResponse.success();
  }

  @GetMapping("/order/qrcode")
  public ApiRestResponse qrcode(@RequestParam("orderNo") String orderNo) {
    String url = orderService.qrcode(orderNo);
    return ApiRestResponse.success(url);
  }
}
