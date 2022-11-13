package com.imooc.mall.service;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.model.request.category.AddCategoryReq;
import com.imooc.mall.model.vo.CategoryVO;

import java.util.List;

public interface CategoryService {
  void add(AddCategoryReq addCategoryReq);

  void delete(Integer id);

  PageInfo listCategoriesForAdmin(Integer pageNum, Integer pageSize);

  List<CategoryVO> listCategoriesForCustomer(Integer parentId);
}
