package com.imooc.mall.service.impl;

import com.imooc.mall.exception.ImoocException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.dao.ProductMapper;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.request.product.AddProductReq;
import com.imooc.mall.model.request.product.UpdateProductReq;
import com.imooc.mall.service.ProductService;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {
  @Autowired
  ProductMapper productMapper;

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
  public int batchUpdateSellStatus(Integer[] ids, Integer sellStatus) {

    return productMapper.batchUpdateSellStatus(ids, sellStatus);
  }
}
