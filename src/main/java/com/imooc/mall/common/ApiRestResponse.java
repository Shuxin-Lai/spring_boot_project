package com.imooc.mall.common;

import com.imooc.mall.exception.ImoocMallExceptionEnum;

public class ApiRestResponse<T> {
  private final Integer status;
  private final String message;
  private T data;

  private static final int OK_CODE = 10000;
  private static final String OK_MSG = "SUCCESS";

  public ApiRestResponse(Integer status, String message, T data) {
    this.status = status;
    this.message = message;
    this.data = data;
  }

  public ApiRestResponse(T data) {
    this(OK_CODE, OK_MSG, data);
  }

  public ApiRestResponse(Integer status, String message) {
    this.status = status;
    this.message = message;
  }

  public ApiRestResponse() {
    this(OK_CODE, OK_MSG);
  }

  public static <T> ApiRestResponse<T> success() {
    return new ApiRestResponse<>();
  }

  public static <T> ApiRestResponse<T> success(T result) {
    return new ApiRestResponse<T>(result);
  }

  public static <T> ApiRestResponse<T> error(Integer code, String message) {
    return new ApiRestResponse<>(code, message);
  }

  public static <T> ApiRestResponse<T> error(ImoocMallExceptionEnum e) {
    return new ApiRestResponse<>(e.getCode(), e.getMessage());
  }

  public Integer getStatus() {
    return status;
  }

  public String getMessage() {
    return message;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  @Override
  public String toString() {
    return "ApiRestResponse{" +
        "status=" + status +
        ", message='" + message + '\'' +
        ", data=" + data +
        '}';
  }
}
