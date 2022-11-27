package com.imooc.mall.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Core {
  public static String validationCode() {
    return Core.validationCode(4);
  }

  public static String validationCode(int count) {
    List<String> strings = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
    Collections.shuffle(strings);
    String res = "";


    for (int i = 0; i < count; i++) {
      res += strings.get(i % 9);
    }

    return res;
  }
}
