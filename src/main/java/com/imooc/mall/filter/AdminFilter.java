package com.imooc.mall.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.pojo.User;
import com.imooc.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

public class AdminFilter implements Filter {
  @Autowired
  UserService userService;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
      ServletException {
    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    HttpServletResponse httpServletResponse= (HttpServletResponse) response;
//    httpServletResponse.setCharacterEncoding("UTF-8");

    HttpSession session = httpServletRequest.getSession();

    User currentUser = (User) session.getAttribute(Constant.IMOOC_MALL_USER);
    ObjectMapper objectMapper = new ObjectMapper();

    if (currentUser == null) {
      PrintWriter writer = new HttpServletResponseWrapper(httpServletResponse).getWriter();
      String s = objectMapper.writeValueAsString(ApiRestResponse.error(ImoocMallExceptionEnum.NEED_LOGIN));
      writer.write(s);
      writer.flush();
      writer.close();
      return;
    }

    if (!userService.checkAdmin(currentUser)) {
      PrintWriter writer = new HttpServletResponseWrapper(httpServletResponse).getWriter();

      String s = objectMapper.writeValueAsString(ApiRestResponse.error(ImoocMallExceptionEnum.NEED_ADMIN));
      writer.write(s);
      writer.flush();
      writer.close();
      return;
    }

    chain.doFilter(request, response);
  }


  @Override
  public void destroy() {

  }
}
