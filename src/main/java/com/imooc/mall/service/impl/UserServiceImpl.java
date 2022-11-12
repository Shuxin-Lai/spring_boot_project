package com.imooc.mall.service.impl;

import com.imooc.mall.model.dao.UserMapper;
import com.imooc.mall.model.pojo.User;
import com.imooc.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
  @Autowired
  UserMapper userMapper;

  @Override
  public User getUser() {
    return userMapper.selectByPrimaryKey(1);
  }

  @Override
  public void register(String userName, String password) {
    User result = this.userMapper.selectByName(userName);
    if (result != null) {
      throw new IllegalArgumentException("用户已存在");
    }
  }

}
