package com.imooc.mall.service.impl;

import com.imooc.mall.exception.ImoocException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.dao.UserMapper;
import com.imooc.mall.model.pojo.User;
import com.imooc.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import util.MD5Utils;

import java.security.NoSuchAlgorithmException;

@Service
public class UserServiceImpl implements UserService {
  @Autowired
  UserMapper userMapper;

  @Override
  public User getUser() {
    return userMapper.selectByPrimaryKey(1);
  }

  @Override
  public User login(String userName, String password) throws ImoocException {
    User user = userMapper.selectByName(userName);
    if (user == null) {
      throw new ImoocException(ImoocMallExceptionEnum.USER_NOT_EXTSTS);
    }

    try {
      String md5String = MD5Utils.getMD5String(password);
      if (!md5String.equals(user.getPassword())) {
        throw new ImoocException(ImoocMallExceptionEnum.WRONG_PASSWORD);
      }
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }

    return user;
  }

  @Override
  public void register(String userName, String password) throws ImoocException {
    User result = this.userMapper.selectByName(userName);
    if (result != null) {
      throw new ImoocException(ImoocMallExceptionEnum.USER_EXSISTED);
    }
    User user = new User();
    user.setUsername(userName);
    try {
      user.setPassword(MD5Utils.getMD5String(password));
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    int i = this.userMapper.insertSelective(user);
    if (i == 0) {
      throw new ImoocException(ImoocMallExceptionEnum.INSERT_FAILED);
    }
  }

  @Override
  public void updateInformation(User user) throws ImoocException {
    int i = userMapper.updateByPrimaryKeySelective(user);
    if (i != 1) {
      throw new ImoocException(ImoocMallExceptionEnum.UPDATE_FAILED);
    }
  }

  @Override
  public boolean checkAdmin(User user) {
    return user.getRole().equals(2);
  }
}
