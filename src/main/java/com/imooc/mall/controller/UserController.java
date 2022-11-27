package com.imooc.mall.controller;

import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.pojo.User;
import com.imooc.mall.model.request.user.RegisterReq;
import com.imooc.mall.model.vo.CurrentUserVO;
import com.imooc.mall.service.EmailService;
import com.imooc.mall.service.UserService;
import com.imooc.mall.util.Core;
import com.imooc.mall.util.EmailUtils;
import com.imooc.mall.util.JwtUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller()
public class UserController {
  @Autowired
  UserService userService;
  @Autowired
  EmailService emailService;
  @Autowired
  JwtUtils jwtUtils;

  @GetMapping("/test")
  @ResponseBody
  public User personalPage() {
    return userService.getUser();
  }

  @PostMapping("/user/register")
  @ResponseBody
  public ApiRestResponse register(@Valid @RequestBody RegisterReq registerReq) {
    String userName = registerReq.getUserName();
    String password = registerReq.getPassword();
    String verificationCode = registerReq.getVerificationCode();
    String email = registerReq.getEmailAddress();

    if (password.length() < 6) {
      return ApiRestResponse.error(ImoocMallExceptionEnum.PASSWORD_TOO_SHORT);
    }

    if (!emailService.validateCode(email, verificationCode)) {
      throw new ImoocException(ImoocMallExceptionEnum.WRONG_VERFICATION_CODE);
    }

    userService.register(userName, password, email);
    return ApiRestResponse.success();
  }

  @PostMapping("/user/login")
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

  @PostMapping("/admin/login")
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

  @PostMapping("/user/update")
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

  @PostMapping("/user/logout")
  @ResponseBody
  public ApiRestResponse logout(HttpSession session) {
    session.removeAttribute(Constant.IMOOC_MALL_USER);
    return ApiRestResponse.success();
  }

  @PostMapping("/user/sendEmail")
  @ResponseBody
  public ApiRestResponse sendEmail(@RequestParam String emailAddress) {
    if (!EmailUtils.isValidEmail(emailAddress)) {
      throw new ImoocException(ImoocMallExceptionEnum.INVALID_EMAIL);
    }

    User user = userService.getUserByEmail(emailAddress);
    if (user != null) {
      throw new ImoocException(ImoocMallExceptionEnum.EMAIL_EXIST);
    }

    String code = Core.validationCode();
    boolean ok = emailService.saveValidationCodeToRedis(emailAddress, code);
    if (ok) {
      emailService.sendSimpleEmail(emailAddress, Constant.EMAIL_SUBJECT, "欢迎注册，您的验证码是：" + code);
    } else {
      throw new ImoocException(ImoocMallExceptionEnum.VALIDATION_CODE_HAS_BEEN_SENT);
    }

    return ApiRestResponse.success();
  }

  @PostMapping("/user/loginWithJwt")
  @ResponseBody
  public ApiRestResponse loginWithJwt(@RequestParam("userName") String userName,
                                      @RequestParam("password") String password, HttpSession session) throws ImoocException {
    if (StringUtils.isEmpty(userName)) {
      return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_USER_NAME);
    }
    if (StringUtils.isEmpty(password)) {
      return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_PASSWORD);

    }

    User user = userService.login(userName, password);
    CurrentUserVO userVO = new CurrentUserVO();
    BeanUtils.copyProperties(user, userVO);

    Map<String, Object> data = jwtUtils.generateToken(userVO);
    return ApiRestResponse.success(data);
  }
}
