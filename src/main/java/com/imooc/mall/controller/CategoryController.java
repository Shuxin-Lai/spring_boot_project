package com.imooc.mall.controller;

import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.pojo.User;
import com.imooc.mall.model.request.category.AddCategoryReq;
import com.imooc.mall.service.CategoryService;
import com.imooc.mall.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class CategoryController {
  @Autowired
  UserService userService;

  @Autowired
  CategoryService categoryService;

  @PostMapping("/admin/category/add")
  @ResponseBody
  public ApiRestResponse addCategory(HttpSession session, @Valid @RequestBody AddCategoryReq addCategoryReq) {
    User currentUser = (User) session.getAttribute(Constant.IMOOC_MALL_USER);
    categoryService.add(addCategoryReq);
    return ApiRestResponse.success();
  }

  @PostMapping("/admin/category/update")
  @ResponseBody
  public ApiRestResponse addCategory(@RequestBody AddCategoryReq addCategoryReq) {
    return ApiRestResponse.success();
  }
}
