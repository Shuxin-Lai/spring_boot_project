package com.imooc.mall.controller;

import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.pojo.User;
import com.imooc.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller()
@RequestMapping("/user")
public class UserController {
  @Autowired
  UserService userService;

  @GetMapping("/test")
  @ResponseBody
  public User personalPage() {
    return userService.getUser();
  }

  @PostMapping("/register")
  @ResponseBody
  public ApiRestResponse register(@RequestParam("userName") String userName,
                                  @RequestParam("password") String password) throws ImoocException {
    if (StringUtils.isEmpty(userName)) {
      return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_USER_NAME);
    }
    if (StringUtils.isEmpty(password)) {
      return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_PASSWORD);
    }
    if (password.length() < 6) {
      return ApiRestResponse.error(ImoocMallExceptionEnum.PASSWORD_TOO_SHORT);
    }
    userService.register(userName, password);
    return ApiRestResponse.success();
  }

  @PostMapping("/login")
  @ResponseBody
  public ApiRestResponse login(@RequestParam("userName") String userName,
                               @RequestParam("password") String password, HttpSession session) throws ImoocException {
    if (StringUtils.isEmpty(userName)) {
      return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_USER_NAME);
    }
    if (StringUtils.isEmpty(password)) {
      return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_PASSWORD);
    }

    User user = userService.login(userName, password);
    user.setPassword(null);
    session.setAttribute(Constant.IMOOC_MALL_USER, user);
    return ApiRestResponse.success(user);
  }

  @PostMapping("/admin_login")
  @ResponseBody
  public ApiRestResponse adminLogin(@RequestParam("userName") String userName,
                               @RequestParam("password") String password, HttpSession session) throws ImoocException {
    if (StringUtils.isEmpty(userName)) {
      return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_USER_NAME);
    }
    if (StringUtils.isEmpty(password)) {
      return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_PASSWORD);
    }

    User user = userService.login(userName, password);
    if (!userService.checkAdmin(user)) {
      return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_ADMIN);
    }
    user.setPassword(null);
    session.setAttribute(Constant.IMOOC_MALL_USER, user);
    return ApiRestResponse.success(user);
  }

  @PostMapping("/update")
  @ResponseBody
  public ApiRestResponse updateUser(HttpSession session, @RequestParam String signature) throws ImoocException {
    User currentUser = (User) session.getAttribute(Constant.IMOOC_MALL_USER);
    if (currentUser == null) {
      return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_LOGIN);
    }
    User user = new User();
    user.setId(currentUser.getId());
    user.setPersonalizedSignature(signature);
    userService.updateInformation(user);
    return ApiRestResponse.success();
  }

  @PostMapping("/logout")
  @ResponseBody
  public ApiRestResponse logout(HttpSession session) {
    session.removeAttribute(Constant.IMOOC_MALL_USER);
    return ApiRestResponse.success();
  }
}
