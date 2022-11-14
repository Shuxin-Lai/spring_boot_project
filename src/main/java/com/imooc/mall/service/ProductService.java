package com.imooc.mall.service;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.model.request.category.AddCategoryReq;
import com.imooc.mall.model.request.category.AddProductReq;
import com.imooc.mall.model.vo.CategoryVO;

import java.util.List;

public interface ProductService {

  void adminAddProduct(AddProductReq addProductReq);
}
