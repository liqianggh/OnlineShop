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

/**
 * Created by Administrator on 2018/1/5
 */
@Controller
@RequestMapping("/user/")
public class UserController {

    //注入service iUserService和@Service中指定一致
    @Autowired
    private IUserService iUserService;
    /**
     * 用户登陆
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value="login.do",method = RequestMethod.POST)
        @ResponseBody
        public  ServerResponse<User> login(String username, String password, HttpSession session, HttpServletRequest httpServletRequest, HttpServletResponse httpresponse){
            ServerResponse<User> response = iUserService.login(username,password);
            if(response.isSuccess()){
//            session.setAttribute(Const.CURRENT_USER,response.getData());
                //user存入redis，设置过期时间为30分钟
//                RedisShardedPoolUtil.setEx(Const.CURRENT_USER, JsonUtil.objToString(response.getData()),Const.RedisCacheExtime.REDIS_SESSION_EXTIME);

                CookiesUtil.writeLoginToken(httpresponse,session.getId());
//                CookiesUtil.readLoginToken(httpServletRequest);
//                CookiesUtil.delLoginToken(httpServletRequest,httpresponse);
                //sessionId存入redis
                RedisShardedPoolUtil.setEx(session.getId(),JsonUtil.objToString(response.getData()),Const.RedisCacheExtime.REDIS_SESSION_EXTIME);

            }
        return response;
    }

    /**
     * 退出登陆
     * @return
     */
    @RequestMapping(value="logout.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout( HttpServletRequest request,HttpServletResponse response){
            String loginToken = CookiesUtil.readLoginToken(request);
            CookiesUtil.delLoginToken(request,response);
            RedisShardedPoolUtil.del(loginToken);
//        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }


    /**
     * 注册用户
     * @param user
     * @return
     */
    @RequestMapping(value="register.do",method=RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user){
        return iUserService.register(user);
    }

    /**
     * 校验字段是否有效
     * @param str
     * @param type
     * @return
     */
    @RequestMapping(value="check_valid.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str,String type){
        return iUserService.checkValid(str,type);
    }


    @RequestMapping(value="get_user_info.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session,HttpServletRequest request){
//        User user = (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken =  CookiesUtil.readLoginToken(request);
        if(StringUtils.isBlank(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取用户信息！");

        }
        String userJson = RedisShardedPoolUtil.get(loginToken);
        User user  = JsonUtil.stringToObj(userJson,User.class);

        return ServerResponse.createBySuccess(user);
    }


    @RequestMapping(value="forget_get_question.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username){

        return iUserService.selectQuestion(username);
    }


    @RequestMapping(value="forget_check_answer.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username,String question,String answer){
        return iUserService.checkAnswer(username,question,answer);
    }


    /**
     * 忘记密码修改密码
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    @RequestMapping(value="forget_reset_password.do",method=RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken){
        return iUserService.forgetResetPassword(username,passwordNew,forgetToken);
    }


    @RequestMapping(value="reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpServletRequest request,String passwordOld,String passwordNew){
        User user  = null;
        String loginToken = CookiesUtil.readLoginToken(request);
        if (StringUtils.isNotEmpty(loginToken)){
            String userJsonStr = RedisShardedPoolUtil.get(loginToken);
            user = JsonUtil.stringToObj(userJsonStr,User.class);
            if(user!=null){
                RedisShardedPoolUtil.expire(loginToken, Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
            }
        }
        if(user==null){
            return ServerResponse.createByErrorMessage("用户未登录！");
        }

        return iUserService.resetPassword(passwordOld,passwordNew,user);
    }




    @RequestMapping(value="update_information.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateInformation(HttpServletRequest request,User user){
        User currentUser  = null;
        String loginToken = CookiesUtil.readLoginToken(request);
        if (StringUtils.isNotEmpty(loginToken)){
            String userJsonStr = RedisShardedPoolUtil.get(loginToken);
            currentUser = JsonUtil.stringToObj(userJsonStr,User.class);
            if(currentUser!=null){
                RedisShardedPoolUtil.expire(loginToken, Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
            }
        }
        if(currentUser==null){
            return ServerResponse.createByErrorMessage("用户未登录！");
        }
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());

        ServerResponse<User> response =iUserService.updateInformation(user);
        if(response.isSuccess()){
//             session.setAttribute(Const.CURRENT_USER,response.getData());
             String userStr = JsonUtil.objToString(user);
             RedisShardedPoolUtil.setEx(loginToken,userStr,Const.RedisCacheExtime.REDIS_SESSION_EXTIME);

        }

        return response;
    }

    @RequestMapping(value="get_information.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> get_information(HttpServletRequest request){
        User user  = null;
        String loginToken = CookiesUtil.readLoginToken(request);
        if (StringUtils.isNotEmpty(loginToken)){
            String userJsonStr = RedisShardedPoolUtil.get(loginToken);
            user = JsonUtil.stringToObj(userJsonStr,User.class);
            if(user!=null){
                RedisShardedPoolUtil.expire(loginToken, Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
            }
        }         if(user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录需要强制登陆status=10");

        }
        return  iUserService.getInformation(user.getId());
    }


}
