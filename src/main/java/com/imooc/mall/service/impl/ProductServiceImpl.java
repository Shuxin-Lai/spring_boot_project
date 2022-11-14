package com.imooc.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.mall.exception.ImoocException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.dao.CategoryMapper;
import com.imooc.mall.model.dao.ProductMapper;
import com.imooc.mall.model.pojo.Category;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.request.category.AddCategoryReq;
import com.imooc.mall.model.request.category.AddProductReq;
import com.imooc.mall.model.vo.CategoryVO;
import com.imooc.mall.service.CategoryService;
import com.imooc.mall.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
  @Autowired
  ProductMapper productMapper;

  @Override
  public void adminAddProduct(AddProductReq addProductReq) {
    Product productOld = productMapper.selectByName(addProductReq.getName());
    if (productOld != null) {
      ImoocException.throwNameExisted();
    }

    Product product = new Product();
    BeanUtils.copyProperties(addProductReq, product);

    int i = productMapper.insertSelective(product);
    if (i != 1) {
      ImoocException.throwInsertError();
    }
  }

}
