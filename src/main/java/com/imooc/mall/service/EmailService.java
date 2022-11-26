package com.imooc.mall.service;

public interface EmailService {
  void sendSimpleEmail(String to, String subject, String text);
}
