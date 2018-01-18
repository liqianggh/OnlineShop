package com.OnlineShop.service;

import com.OnlineShop.common.ServerResponse;
import com.OnlineShop.pojo.User;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/12/1 0001.
 */
@Service
public interface IUserService {

    ServerResponse<User> login(String username , String password);

    ServerResponse<String> register(User user);

    ServerResponse<String> checkValid(String str,String type);

    ServerResponse<String> selectQuestion(String username);

    ServerResponse<String> checkAnswer(String username,String question,String answer);

    ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken);

    ServerResponse<String> resetPassword(String passwordOld,String passwordNew,User user);

    ServerResponse<User> updateInformation(User user);

    ServerResponse<User> getInformation(Integer userId);

    ServerResponse checkAdminRole(User user);


}
