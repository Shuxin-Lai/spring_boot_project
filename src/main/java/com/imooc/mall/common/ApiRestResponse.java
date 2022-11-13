package com.imooc.mall.common;

import com.imooc.mall.exception.ImoocMallExceptionEnum;

import java.util.Date;

public class ApiRestResponse<T> {
  private final Integer code;
  private final String message;
  private T data;
  private Long timestamp;

  private static final int OK_CODE = 10000;
  private static final String OK_MSG = "SUCCESS";

  public ApiRestResponse(Integer code, String message, T data) {
    this.code = code;
    this.message = message;
    this.data = data;
    this.timestamp = new Date().getTime();
  }

  public ApiRestResponse(T data) {
    this(OK_CODE, OK_MSG, data);
  }

  public ApiRestResponse(Integer code, String message) {
    this(code, message, null);
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

  public Integer getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  public Long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Long timestamp) {
    this.timestamp = timestamp;
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
        "status=" + code +
        ", message='" + message + '\'' +
        ", data=" + data +
        '}';
  }
}
