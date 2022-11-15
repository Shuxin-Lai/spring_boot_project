package com.imooc.mall.controller;

import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.request.product.AddProductReq;
import com.imooc.mall.model.request.product.UpdateProductReq;
import com.imooc.mall.service.ProductService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
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

  @PostMapping("/admin/product/update")
  @ResponseBody
  public ApiRestResponse updateProduct(@Valid @RequestBody UpdateProductReq updateProductReq) {
    productService.update(updateProductReq);
    return ApiRestResponse.success();
  }

  @DeleteMapping("/admin/product/delete/{id}")
  @ResponseBody
  public ApiRestResponse deleteProduct(@PathVariable("id") Integer id) {
    productService.delete(id);
    return ApiRestResponse.success();
  }

  @PostMapping("/admin/product/batch_update_sell_status")
  @ResponseBody
  public ApiRestResponse batchUpdateSellStatus(@RequestParam("ids") Integer [] ids,
                                               @RequestParam("sell_status")Integer sellStatus) {
    productService.batchUpdateSellStatus(ids, sellStatus);
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
