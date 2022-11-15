package com.imooc.mall.service;

import com.imooc.mall.model.request.product.AddProductReq;
import com.imooc.mall.model.request.product.UpdateProductReq;

public interface ProductService {

  void adminAddProduct(AddProductReq addProductReq);

  void update(UpdateProductReq updateProductReq);

  void delete(Integer id);

  int batchUpdateSellStatus(Integer[] ids, Integer sellStatus);
}
