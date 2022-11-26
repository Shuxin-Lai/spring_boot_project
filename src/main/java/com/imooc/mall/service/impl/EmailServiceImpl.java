package com.imooc.mall.service.impl;

import com.imooc.mall.common.Constant;
import com.imooc.mall.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
  @Autowired
  private JavaMailSender mailSender;

  @Override
  public void sendSimpleEmail(String to, String subject, String text) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(Constant.EMAIL_FROM);
    message.setTo(to);
    message.setSubject(subject);
    message.setText(text);
    mailSender.send(message);
  }
}
