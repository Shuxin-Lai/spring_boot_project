package com.imooc.mall.exception;

public enum ImoocMallExceptionEnum {
  NEED_USER_NAME(10001, "用户名不能为空"),
  NEED_PASSWORD(10002, "密码不能为空"),
  PASSWORD_TOO_SHORT(10003, "密码长度小于 6 位"),
  USER_EXISTED(10004, "用户已存在"),
  INSERT_FAILED(10005, "创建用户失败"),
  USER_NOT_EXISTS(10006, "用户不存在"),
  WRONG_PASSWORD(10007, "密码错误"),

  SYSTEM_ERROR(20000, "系统异常"),

  NEED_LOGIN(10008, "用户未登录"),
  UPDATE_FAILED(10009, "更新失败"),
  NEED_ADMIN(10010, "非管理员账号"),
  PARA_NOT_NULL(10011, "参数不能为空"),
  NAME_EXISTED(10012, "不允许重名"), REQUEST_PARAM_ERROR(10013, "参数异常");

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
