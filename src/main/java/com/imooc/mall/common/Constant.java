package com.imooc.mall.common;

import com.google.common.collect.Sets;
import com.imooc.mall.exception.ImoocException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class Constant {
  public static final String SALT = "imooc_mall_1129*.";
  public static final String IMOOC_MALL_USER = "IMOOC_MALL_USER";

  public static String FILE_UPDATE_DIR;

  @Value("${file.upload.dir}")
  public void setFileUpdateDir(String fileUpdateDir) {
    FILE_UPDATE_DIR = fileUpdateDir;
  }

  public interface ProductListOrderBy {
    Set<String> PRICE_ASC_DESC = Sets.newHashSet("price asc", "price desc");
  }

  public interface SaleStatus {
    int NOT_SALE = 0;
    int SALE = 1;
  }

  public interface Cart {
    int CHECKED = 1;
    int UN_CHECKED = 0;
  }

  public enum OrderStatusEmum {
    CANCELED(0, "用户已取消"),
    NOT_PAID(10, "未付款"),
    PAID(20, "已付款"),
    DELIVERED(30, "已发货"),
    FULFILLED(40, "已完成");

    private final String value;
    private final int code;

    OrderStatusEmum(int code, String value) {
      this.value = value;
      this.code = code;
    }

    public static OrderStatusEmum codeOf(int code) {
      for (OrderStatusEmum o : OrderStatusEmum.values()) {
        if (o.code == code) return o;
      }
      throw new ImoocException(ImoocMallExceptionEnum.NO_ENUM);
    }

    public String getValue() {
      return value;
    }

    public int getCode() {
      return code;
    }
  }
}
