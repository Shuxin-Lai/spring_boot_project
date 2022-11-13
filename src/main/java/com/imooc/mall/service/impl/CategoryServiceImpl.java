package com.imooc.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.mall.exception.ImoocException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.dao.CategoryMapper;
import com.imooc.mall.model.pojo.Category;
import com.imooc.mall.model.request.category.AddCategoryReq;
import com.imooc.mall.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

  @Override
  public void delete(Integer id) {
    int i = categoryMapper.deleteByPrimaryKey(id);
    if (i != 1) {
      throw new ImoocException(ImoocMallExceptionEnum.DELETE_FAILED);
    }
  }

  @Override
  public PageInfo listCategoryForAdmin(Integer pageNum, Integer pageSize) {
    PageHelper.startPage(pageNum, pageSize);
    List<Category> categories = categoryMapper.selectList();
    return new PageInfo(categories);
  }
}
