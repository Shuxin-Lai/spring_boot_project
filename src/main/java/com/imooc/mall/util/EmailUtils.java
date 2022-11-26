package com.imooc.mall.util;


import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class EmailUtils {
  public static boolean isValidEmail(String email) {
    try {
      InternetAddress internetAddress = new InternetAddress(email);
      internetAddress.validate();
    } catch (AddressException e) {
      return false;
    }

    return true;
  }
}
