package com.imooc.mall.exception;

import com.imooc.mall.common.ApiRestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {
  private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(Exception.class)
  @ResponseBody
  public Object handleException(Exception e) {
    logger.error("Default Exception: ", e);
    return ApiRestResponse.error(ImoocMallExceptionEnum.SYSTEM_ERROR);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseBody
  public Object handleMessageNotReadableException(HttpMessageNotReadableException e) {
    logger.error("Default Exception: ", e);
    return ApiRestResponse.error(ImoocMallExceptionEnum.PARA_ERROR);
  }


  @ExceptionHandler(ImoocException.class)
  @ResponseBody
  public Object handleImoocException(ImoocException e) {
    logger.error("Imooc Exception: ", e);
    return ApiRestResponse.error(e.getCode(), e.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseBody
  public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    logger.error("MethodArgumentNotValidException: ", e);
    return handleBindingResult(e.getBindingResult());
  }

  private ApiRestResponse handleBindingResult(BindingResult result) {
    List<String> list = null;
    if (result.hasErrors()) {
      list = new ArrayList<>();
      for (FieldError fieldError : result.getFieldErrors()) {
        list.add(fieldError.getField() + ": " + fieldError.getDefaultMessage());
      }
    }

    if (list == null) {
      return ApiRestResponse.error(ImoocMallExceptionEnum.REQUEST_PARAM_ERROR);
    }
    return ApiRestResponse.error(ImoocMallExceptionEnum.REQUEST_PARAM_ERROR.getCode(), list.toString());
  }
}
