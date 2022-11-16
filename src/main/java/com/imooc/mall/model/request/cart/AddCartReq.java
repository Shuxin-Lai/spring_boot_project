package com.imooc.mall.model.request.cart;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class AddCartReq {
  @NotNull
  private Integer productId;
  @NotNull
  @Min(1)
  private Integer count;

  public Integer getProductId() {
    return productId;
  }

  public void setProductId(Integer productId) {
    this.productId = productId;
  }

  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }
}
