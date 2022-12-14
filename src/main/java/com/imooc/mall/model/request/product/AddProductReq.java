package com.imooc.mall.model.request.product;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class AddProductReq {

  @NotNull
  private String name;

  @NotNull
  private String image;

  @NotNull
  private String detail;

  @NotNull
  private Integer categoryId;

  @Min(0)
  private Integer price;

  @Min(0)
  @Max(10000)
  private Integer stock;

  private Integer status;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name == null ? null : name.trim();
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image == null ? null : image.trim();
  }

  public String getDetail() {
    return detail;
  }

  public void setDetail(String detail) {
    this.detail = detail == null ? null : detail.trim();
  }

  public Integer getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Integer categoryId) {
    this.categoryId = categoryId;
  }

  public Integer getPrice() {
    return price;
  }

  public void setPrice(Integer price) {
    this.price = price;
  }

  public Integer getStock() {
    return stock;
  }

  public void setStock(Integer stock) {
    this.stock = stock;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }
}