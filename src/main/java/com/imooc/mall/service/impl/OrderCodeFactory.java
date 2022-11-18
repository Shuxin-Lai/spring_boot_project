package com.imooc.mall.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class OrderCodeFactory {
  private static final Random random = new Random();

  private static String getDateTime() {
    SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
    return fmt.format(new Date());
  }

  private static int getRandom() {
    return (int) ((random.nextDouble() * 90000) + 10000);
  }

  public static String getOrderCode(Integer userId) {
    return getDateTime() + getRandom();
  }
}
