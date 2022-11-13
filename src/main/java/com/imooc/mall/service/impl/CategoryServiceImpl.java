package com.imooc.mall.service.impl;

import com.imooc.mall.exception.ImoocException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.dao.CategoryMapper;
import com.imooc.mall.model.pojo.Category;
import com.imooc.mall.model.request.category.AddCategoryReq;
import com.imooc.mall.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {
  @Autowired
  CategoryMapper categoryMapper;

  @Override
  public void add(AddCategoryReq addCategoryReq) {
    if (categoryMapper.selectByName(addCategoryReq.getName()) != null) {
      throw new ImoocException(ImoocMallExceptionEnum.NAME_EXISTED);
    }

    Category category = new Category();
    BeanUtils.copyProperties(addCategoryReq, category);

    categoryMapper.insertSelective(category);
  }
}
