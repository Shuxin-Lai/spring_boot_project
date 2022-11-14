package com.imooc.mall.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Constant {
  public static final String SALT = "imooc_mall_1129*.";
  public static final String IMOOC_MALL_USER = "IMOOC_MALL_USER";

  public static String FILE_UPDATE_DIR;

  @Value("${file.upload.dir}")
  public void setFileUpdateDir(String fileUpdateDir) {
    FILE_UPDATE_DIR = fileUpdateDir;
  }
}
