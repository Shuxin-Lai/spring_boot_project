package com.imooc.mall.exception;

public enum ImoocMallExceptionEnum {
  NEED_USER_NAME(10001, "用户名不能为空"),
  NEED_PASSWORD(10002, "密码不能为空"),
  PASSWORD_TOO_SHORT(10002, "密码长度小于 6 位");


  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  ImoocMallExceptionEnum(Integer code, String message) {
    this.code = code;
    this.message = message;
  }

  Integer code;
  String message;
}
