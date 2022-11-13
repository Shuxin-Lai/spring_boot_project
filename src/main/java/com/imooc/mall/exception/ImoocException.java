package com.imooc.mall.exception;

public class ImoocException extends RuntimeException {
  private final Integer code;
  private final String message;

  public ImoocException(Integer code, String message) {
    this.code = code;
    this.message = message;
  }

  public ImoocException(ImoocMallExceptionEnum e) {
    this(e.getCode(), e.getMessage());
  }

  @Override
  public String toString() {
    return "ImoocException{" +
        "code=" + code +
        ", message='" + message + '\'' +
        '}';
  }

  public Integer getCode() {
    return code;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
