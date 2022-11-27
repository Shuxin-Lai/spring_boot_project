package com.imooc.mall.service;

public interface EmailService {
  void sendSimpleEmail(String to, String subject, String text);

  boolean saveValidationCodeToRedis(String email, String validationCode);

  boolean validateCode(String email, String code);
}
