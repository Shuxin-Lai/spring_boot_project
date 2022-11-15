package com.imooc.mall.model.request.product;

import com.imooc.mall.model.request.base.Pagination;

public class ProductListReq extends Pagination {
  private String keyword;
  private Integer categoryId;
  private String orderBy;

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public Integer getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Integer categoryId) {
    this.categoryId = categoryId;
  }

  public String getOrderBy() {
    return orderBy;
  }

  public void setOrderBy(String orderBy) {
    this.orderBy = orderBy;
  }
}
