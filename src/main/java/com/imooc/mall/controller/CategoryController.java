package com.imooc.mall.controller;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.common.Constant;
import com.imooc.mall.model.pojo.User;
import com.imooc.mall.model.request.category.AddCategoryReq;
import com.imooc.mall.model.vo.CategoryVO;
import com.imooc.mall.service.CategoryService;
import com.imooc.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

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


  @PostMapping("/admin/category/delete/{id}")
  @ResponseBody
  public ApiRestResponse removeCategory(@PathVariable("id") Integer id) {
    categoryService.delete(id);

    return ApiRestResponse.success();
  }

  @GetMapping("/admin/category/list")
  @ResponseBody
  public ApiRestResponse categoryListForAdmin(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                              @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
    PageInfo pageInfo = categoryService.listCategoriesForAdmin(pageNum, pageSize);
    return ApiRestResponse.success(pageInfo);
  }

  @GetMapping("/category/list")
  @ResponseBody
  public ApiRestResponse categoryListForCustomer() {
    List<CategoryVO> categoryVOS = categoryService.listCategoriesForCustomer(0);
    return ApiRestResponse.success(categoryVOS);
  }
}
