package com.imooc.mall.controller;

import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.model.request.order.CreateOrderReq;
import com.imooc.mall.model.vo.OrderVO;
import com.imooc.mall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {
  @Autowired
  OrderService orderService;

  @PostMapping("/order/create")
  public ApiRestResponse create(@RequestBody CreateOrderReq createOrderReq) {

    String orderNo = orderService.create(createOrderReq);
    return ApiRestResponse.success(orderNo);
  }

  @GetMapping("/order/detail")
  public ApiRestResponse detail(@RequestParam("orderNo") String orderNo) {
    OrderVO orderVO = orderService.detail(orderNo);
    return ApiRestResponse.success(orderVO);
  }
}
