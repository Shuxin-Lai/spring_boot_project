package com.imooc.mall.service;

import com.imooc.mall.exception.ImoocException;
import com.imooc.mall.model.pojo.User;

public interface UserService {
    User getUser();

    void register(String userName, String password, String emailAddress) throws ImoocException;

    User login(String userName, String password) throws ImoocException;

    void updateInformation(User user) throws ImoocException;

    boolean checkAdmin(User user);

    User getCurrentUserFromSession();

  User getUserByEmail(String email);
}
