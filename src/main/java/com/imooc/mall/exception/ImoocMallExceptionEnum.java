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
  NAME_EXISTED(10012, "不允许重名"),
  REQUEST_PARAM_ERROR(10013, "参数异常"),
  DELETE_FAILED(10014, "删除失败"),
  MKDIR_FAILED(10015, "创建文件夹失败"),
  UPLOAD_FAILED(10016, "文件上传失败"),
  PRODUCT_NOT_EXISTS(10017, "商品不存在"),
  PRODUCT_NOT_SALE(10018, "商品状态异常"),
  PRODUCT_OUT_OK_STOCK(10018, "商品库存不足"),
  PARA_ERROR(10019, "参数错误"),
  CART_NOT_EXISTED(10020, "找不到购物车"),
  CART_EMPTY(10021, "购物车没有勾选的商品"),
  CART_NOT_SELECTED(10022, "商品未选中"),
  NO_ENUM(10023, "枚举不存在"),
  NO_ORDER(10024, "订单不存在"),
  NOT_YOUR_ORDER(10025, "此订单并非您的"),
  ORDER_STATUS_ERROR(10026, "订单状态异常"),
  CREATE_QRCODE_FAILED(10027, "创建二维码失败");

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
