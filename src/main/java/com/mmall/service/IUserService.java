package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

/**
 * Created by Administrator on 2017-12-4.
 */
public interface IUserService {
    ServerResponse<User> login(String Username, String password);//创建通用数据响应对象

    ServerResponse<String> register(User user);//保存user对象

    ServerResponse<String> checkValid(String str, String type);

    ServerResponse selectQuestion(String username);

    ServerResponse checkAnswer(String username, String question, String answer);

    ServerResponse forgetResetPassword(String username, String passwordNew, String forgetToken);

    ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user);

    ServerResponse<User> updateInformation(User user);

    ServerResponse<User> getInfomation(Integer userId);

    ServerResponse checkAdminRole(User user);

}
