package com.imooc.mall.util;

import com.imooc.mall.common.Constant;
import org.apache.tomcat.util.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
  public static String getMD5String(String value) throws NoSuchAlgorithmException {
    MessageDigest md5 = MessageDigest.getInstance("MD5");
    value = value + Constant.SALT;
    byte[] digest = md5.digest(value.getBytes());

    return Base64.encodeBase64String(digest);
  }

  public static void main(String[] args) throws NoSuchAlgorithmException {
    String md5String = getMD5String("1234");
    System.out.println(md5String);
  }
}
