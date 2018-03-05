package com.OnlineShop.controller.potral;

import com.OnlineShop.common.Const;
import com.OnlineShop.common.ResponseCode;
import com.OnlineShop.common.ServerResponse;
import com.OnlineShop.pojo.User;
import com.OnlineShop.service.IUserService;
import com.OnlineShop.util.CookiesUtil;
import com.OnlineShop.util.JsonUtil;
import com.OnlineShop.util.RedisShardedPoolUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user/springsession/")
public class UserSpringSessionController {
    @Autowired
    private IUserService iUserService;

    /**
     * 用户登陆
     *
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session, HttpServletRequest httpServletRequest, HttpServletResponse httpresponse) {
        ServerResponse<User> response = iUserService.login(username, password);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());

            //redis实现方案
            //user存入redis，设置过期时间为30分钟
//                RedisShardedPoolUtil.setEx(Const.CURRENT_USER, JsonUtil.objToString(response.getData()),Const.RedisCacheExtime.REDIS_SESSION_EXTIME);

//            CookiesUtil.writeLoginToken(httpresponse,session.getId());
//                CookiesUtil.readLoginToken(httpServletRequest);
//                CookiesUtil.delLoginToken(httpServletRequest,httpresponse);
            //sessionId存入redis
//            RedisShardedPoolUtil.setEx(session.getId(), JsonUtil.objToString(response.getData()), Const.RedisCacheExtime.REDIS_SESSION_EXTIME);

        }
        return response;
    }

    /**
     * 退出登陆
     *
     * @return
     */
    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        String loginToken = CookiesUtil.readLoginToken(request);
        //Redis cookie实现方案
//        CookiesUtil.delLoginToken(request,response);
//        RedisShardedPoolUtil.del(loginToken);


//        session.removeAttribute(Const.CURRENT_USER);
        //spring-session实现方案

        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }
    @RequestMapping(value="get_information.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> get_information(HttpServletRequest request,HttpSession session){
        User user  = null;
        //redis 实现
//        String loginToken = CookiesUtil.readLoginToken(request);
//        if (StringUtils.isNotEmpty(loginToken)){
//            String userJsonStr = RedisShardedPoolUtil.get(loginToken);
//            user = JsonUtil.stringToObj(userJsonStr,User.class);

//            if(user!=null){
//                RedisShardedPoolUtil.expire(loginToken, Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
//            }
//        }
        //spring session 实现
        user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录需要强制登陆status=10");

        }
        return  iUserService.getInformation(user.getId());
    }
}
