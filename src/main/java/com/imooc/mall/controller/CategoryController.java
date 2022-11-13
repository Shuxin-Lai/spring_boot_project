package com.imooc.mall.controller;

import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.pojo.User;
import com.imooc.mall.model.request.category.AddCategoryReq;
import com.imooc.mall.service.CategoryService;
import com.imooc.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/category")
public class CategoryController {
  @Autowired
  UserService userService;

  @Autowired
  CategoryService categoryService;

  @PostMapping("/admin/add")
  @ResponseBody
  public ApiRestResponse addCategory(HttpSession session, @RequestBody AddCategoryReq addCategoryReq) {
    if (addCategoryReq.getName() == null) {
      return ApiRestResponse.error(ImoocMallExceptionEnum.PARA_NOT_NULL);
    }
    User currentUser = (User) session.getAttribute(Constant.IMOOC_MALL_USER);
    if (currentUser == null) {
      return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_LOGIN);
    }
    if (!userService.checkAdmin(currentUser)) {
      return ApiRestResponse.error((ImoocMallExceptionEnum.NEED_ADMIN));
    }

    categoryService.add(addCategoryReq);

    return ApiRestResponse.success();
  }
}
