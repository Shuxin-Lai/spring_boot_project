package com.imooc.mall.model.request.user;

import javax.validation.constraints.NotNull;

public class RegisterReq {
  @NotNull
  String userName;

  @NotNull
  String password;

  @NotNull(message = "验证码不能为空")
  String verificationCode;

  @NotNull(message = "邮箱不能为空")
  String emailAddress;

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getVerificationCode() {
    return verificationCode;
  }

  public void setVerificationCode(String verificationCode) {
    this.verificationCode = verificationCode;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }
}
