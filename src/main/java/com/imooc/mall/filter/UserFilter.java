package com.imooc.mall.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.pojo.User;
import com.imooc.mall.model.vo.CurrentUserVO;
import com.imooc.mall.service.UserService;
import com.imooc.mall.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class UserFilter implements Filter {
  public static CurrentUserVO currentUser;
  @Autowired
  UserService userService;
  @Autowired
  JwtUtils jwtUtils;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
      ServletException {
    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    HttpServletResponse httpServletResponse = (HttpServletResponse) response;
    ObjectMapper objectMapper = new ObjectMapper();

    String jwtToken = httpServletRequest.getHeader("Authorization");
    if (StringUtils.isEmpty(jwtToken)) {
      jwtToken = httpServletRequest.getHeader("authorization");
    }

    if (StringUtils.isEmpty(jwtToken)) {
      PrintWriter writer = new HttpServletResponseWrapper(httpServletResponse).getWriter();
      String s = objectMapper.writeValueAsString(ApiRestResponse.error(ImoocMallExceptionEnum.INVALID_TOKEN));
      writer.write(s);
      writer.flush();
      writer.close();
      return;
    }

    CurrentUserVO userVO = jwtUtils.decodeToken(jwtToken);
    if (userVO == null) {
      PrintWriter writer = new HttpServletResponseWrapper(httpServletResponse).getWriter();
      String s = objectMapper.writeValueAsString(ApiRestResponse.error(ImoocMallExceptionEnum.NEED_LOGIN));
      writer.write(s);
      writer.flush();
      writer.close();
      return;
    }

    currentUser = userVO;
    chain.doFilter(request, response);
  }


  @Override
  public void destroy() {

  }
}
