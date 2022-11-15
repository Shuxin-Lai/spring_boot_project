package com.imooc.mall.common;

import com.google.common.collect.Sets;
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
}
