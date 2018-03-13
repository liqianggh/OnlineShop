package com.OnlineShop.controller.backend;

import com.OnlineShop.common.Const;
import com.OnlineShop.common.ServerResponse;
import com.OnlineShop.pojo.User;
import com.OnlineShop.service.IUserService;
import com.OnlineShop.util.CookiesUtil;
import com.OnlineShop.util.JsonUtil;
import com.OnlineShop.util.RedisShardedPoolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by Administrator on 2017/12/1 0001.
 */
@Controller
@RequestMapping("/manage/user")
public class UserManageController {


    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "login.do",method =  {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session, HttpServletResponse httpServletresponse){

        ServerResponse<User> response = iUserService.login(username,password);

        if(response.isSuccess()){
            User user = response.getData();
            //对用户角色进行判断
            if(user.getRole()== Const.Role.ROLE_ADMIN){
                //登陆的管理员
//                session.setAttribute(Const.CURRENT_USER,user);

                //Redis单点登录
                CookiesUtil.writeLoginToken(httpServletresponse,session.getId());
                RedisShardedPoolUtil.setEx(session.getId(), JsonUtil.objToString(response.getData()),Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
            }else{
               return ServerResponse.createByErrorMessage("不是管理员无法登陆！");
            }
        }
        return response;

    }
}
