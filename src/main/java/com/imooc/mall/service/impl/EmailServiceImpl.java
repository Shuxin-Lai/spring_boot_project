package com.imooc.mall.service.impl;

import com.imooc.mall.common.Constant;
import com.imooc.mall.service.EmailService;
import com.imooc.mall.service.RedissonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class EmailServiceImpl implements EmailService {
  @Autowired
  private JavaMailSender mailSender;
  @Autowired
  private RedissonService redissonService;

  @Override
  public void sendSimpleEmail(String to, String subject, String text) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(Constant.EMAIL_FROM);
    message.setTo(to);
    message.setSubject(subject);
    message.setText(text);
    mailSender.send(message);
  }

  @Override
  public boolean saveValidationCodeToRedis(String email, String verficationCode) {
    return redissonService.set(email, verficationCode, 2 * 60L, TimeUnit.SECONDS);
  }

  @Override
  public boolean validateCode(String email, String code) {
    Object o = redissonService.get(email);
    if (o == null) return false;

    return code.equals(o);
  }
}
