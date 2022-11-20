package com.imooc.mall.service;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.model.pojo.Order;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.request.base.Pagination;
import com.imooc.mall.model.request.order.CreateOrderReq;
import com.imooc.mall.model.request.product.AddProductReq;
import com.imooc.mall.model.request.product.ProductListReq;
import com.imooc.mall.model.request.product.UpdateProductReq;
import com.imooc.mall.model.vo.OrderVO;

public interface OrderService {
  String create(CreateOrderReq createOrderReq);

  OrderVO detail(String orderNo);

  PageInfo listForCustomer(Pagination pagination);

  void cancel(String orderNo);
}
