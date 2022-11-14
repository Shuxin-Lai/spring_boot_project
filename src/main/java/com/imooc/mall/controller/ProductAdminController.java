package com.imooc.mall.controller;

import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.request.category.AddProductReq;
import com.imooc.mall.service.ProductService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

@Controller
public class ProductAdminController {
  @Autowired
  ProductService productService;

  @PostMapping("/admin/product/add")
  @ResponseBody
  public ApiRestResponse addProduct(@Valid @RequestBody AddProductReq addProductReq) {
    productService.adminAddProduct(addProductReq);
    return ApiRestResponse.success();
  }

  @PostMapping("/upload")
  @ResponseBody
  public ApiRestResponse upload(HttpServletRequest httpServletRequest, @RequestParam("file") MultipartFile file) {
    String originalFileName = file.getOriginalFilename();
    String fileExt = StringUtils.getFilenameExtension(originalFileName);
//    String filename = originalFileName.substring(0, originalFileName.lastIndexOf("."));

    UUID uuid = UUID.randomUUID();
    String newFileName = uuid.toString() + "." + fileExt;

    File destDir = new File(Constant.FILE_UPDATE_DIR);
    File destFile = new File(Constant.FILE_UPDATE_DIR + "/" + newFileName);

    if (!destDir.exists()) {
      boolean mkdir = destDir.mkdir();
      if (!mkdir) {
        throw new ImoocException(ImoocMallExceptionEnum.MKDIR_FAILED);
      }
    }

    try {
      file.transferTo(destFile);
    } catch (IOException e) {
      e.printStackTrace();
      return ApiRestResponse.error(ImoocMallExceptionEnum.UPLOAD_FAILED);
    }

    try {
      return ApiRestResponse.success(
          getHost(
              new URI(httpServletRequest.getRequestURL() + "")
          ) + "/images/" + newFileName
      );
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }

    return ApiRestResponse.error(ImoocMallExceptionEnum.UPLOAD_FAILED);
  }

  private URI getHost(URI uri) {
    URI uri1 = null;
    try {
      uri1 = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), null, null, null);
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    return uri1;
  }
}
