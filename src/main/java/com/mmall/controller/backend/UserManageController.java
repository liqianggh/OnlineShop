package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by Administrator on 2017/12/1 0001.
 */
@Controller
@RequestMapping("/manage/user")
public class UserManageController {


    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password,HttpSession session){

        ServerResponse<User> response = iUserService.login(username,password);

        if(response.isSuccess()){
            User user = response.getData();
            //对用户角色进行判断
            if(user.getRole()== Const.Role.ROLE_ADMIN){
                //登陆的管理员
                session.setAttribute(Const.CURRENT_USER,user);

            }else{
               return ServerResponse.createByErrorMessage("不是管理员无法登陆！");
            }
        }
        return response;

    }
}
