package com.imooc.mall.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.dao.CategoryMapper;
import com.imooc.mall.model.dao.ProductMapper;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.query.ProductListQuery;
import com.imooc.mall.model.request.product.AddProductReq;
import com.imooc.mall.model.request.product.ProductListReq;
import com.imooc.mall.model.request.product.UpdateProductReq;
import com.imooc.mall.service.CategoryService;
import com.imooc.mall.service.ProductService;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
  @Autowired
  ProductMapper productMapper;
  @Autowired
  CategoryService categoryService;

  @Override
  public void adminAddProduct(AddProductReq addProductReq) {
    Product productOld = productMapper.selectByName(addProductReq.getName());
    if (productOld != null) {
      ImoocException.throwNameExisted();
    }

    Product product = new Product();
    BeanUtils.copyProperties(addProductReq, product);

    int i = productMapper.insertSelective(product);
    if (i != 1) {
      ImoocException.throwInsertError();
    }
  }

  @Override
  public void update(UpdateProductReq updateProductReq) {
    Product productOld = productMapper.selectByPrimaryKey(updateProductReq.getId());
    if (productOld != null && updateProductReq.getName().equals(productOld.getName())) {
      throw new ImoocException(ImoocMallExceptionEnum.NAME_EXISTED);
    }
    Product product = new Product();
    BeanUtils.copyProperties(updateProductReq, product);
    int i = productMapper.updateByPrimaryKeySelective(product);
    if (i != 1) {
      ImoocException.throwUpdateError();
    }
  }

  @Override
  public void delete(Integer id) {
    int i = productMapper.deleteByPrimaryKey(id);
    if (i != 1) {
      ImoocException.throwDeleteError();
    }
  }

  @Override
  public PageInfo<Product> listForAdmin(Integer pageNum, Integer pageSize) {
    PageHelper.startPage(pageNum, pageSize, "update_time");
    List<Product> products = productMapper.selectListForAdmin();
    PageInfo<Product> productPageInfo = new PageInfo<>(products);
    return productPageInfo;
  }

  @Override
  public int batchUpdateSellStatus(Integer[] ids, Integer sellStatus) {

    return productMapper.batchUpdateSellStatus(ids, sellStatus);
  }

  @Override
  public Product selectById(Integer id) {
    return productMapper.selectByPrimaryKey(id);
  }

  @Override
  public PageInfo list(ProductListReq productListReq) {
    ProductListQuery productListQuery = new ProductListQuery();


    if (!StringUtils.isEmpty(productListReq.getKeyword())) {
      String s = new StringBuilder().append("%").append(productListReq.getKeyword()).append("%").toString();
      productListQuery.setKeyword(s);
    }

    Integer categoryId = productListReq.getCategoryId();
    if (categoryId != null && categoryId != 0) {
      productListQuery.setCategoryIds(categoryService.getCategoryIds(categoryId));
    }

    String orderBy = productListReq.getOrderBy();
    if (Constant.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)) {
      PageHelper.startPage(productListReq.getPageNum(), productListReq.getPageSize(), orderBy);
    } else {
      PageHelper.startPage(productListReq.getPageNum(), productListReq.getPageSize());
    }

    List<Product> products = productMapper.selectList(productListQuery);
    PageInfo<Product> productPageInfo = new PageInfo<>(products);
    return productPageInfo;
  }
}
