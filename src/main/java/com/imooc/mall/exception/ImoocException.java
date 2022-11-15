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

  public static void throwInsertError() {
    throw new ImoocException(ImoocMallExceptionEnum.INSERT_FAILED);
  }

  public static void throwDeleteError() {
    throw new ImoocException(ImoocMallExceptionEnum.DELETE_FAILED);
  }

  public static void throwUpdateError() {
    throw new ImoocException(ImoocMallExceptionEnum.UPDATE_FAILED);
  }

  public static void throwNameExisted() {
    throw new ImoocException(ImoocMallExceptionEnum.NAME_EXISTED);
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
