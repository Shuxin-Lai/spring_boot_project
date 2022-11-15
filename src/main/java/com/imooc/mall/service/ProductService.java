package com.imooc.mall.service;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.request.product.AddProductReq;
import com.imooc.mall.model.request.product.ProductListReq;
import com.imooc.mall.model.request.product.UpdateProductReq;

public interface ProductService {

  void adminAddProduct(AddProductReq addProductReq);

  void update(UpdateProductReq updateProductReq);

  void delete(Integer id);

  int batchUpdateSellStatus(Integer[] ids, Integer sellStatus);

  PageInfo<Product> listForAdmin(Integer pageNum, Integer pageSize);

  Product selectById(Integer id);

  PageInfo list(ProductListReq productListReq);
}
