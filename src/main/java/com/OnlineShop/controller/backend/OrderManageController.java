package com.OnlineShop.controller.backend;

import com.OnlineShop.util.CookiesUtil;
import com.OnlineShop.util.JsonUtil;
import com.OnlineShop.util.RedisPoolUtil;
import com.github.pagehelper.PageInfo;
import com.OnlineShop.common.Const;
import com.OnlineShop.common.ResponseCode;
import com.OnlineShop.common.ServerResponse;
import com.OnlineShop.pojo.User;
import com.OnlineShop.service.IOrderService;
import com.OnlineShop.service.IUserService;
import com.OnlineShop.vo.OrderVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by Administrator on 2017/12/24 0024.
 */
@Controller
@RequestMapping("/manage/order/")
public class OrderManageController {

    @Autowired
    private IOrderService iOrderService;
    @Autowired
    private IUserService iUserService;

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo>  list(HttpServletRequest  request, @RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum,
                                          @RequestParam(value="pageSize",defaultValue = "10")Integer pageSIze){
        User user  = null;
        String loginToken = CookiesUtil.readLoginToken(request);
        if (StringUtils.isNotEmpty(loginToken)){
            String userJsonStr = RedisPoolUtil.get(loginToken);
            user = JsonUtil.stringToObj(userJsonStr,User.class);
            if(user!=null){
                RedisPoolUtil.expire(loginToken, Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
            }
        }
        if(user==null) return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请重新登陆！");
        if(iUserService.checkAdminRole(user).isSuccess()){


            return iOrderService.manageList(pageNum,pageSIze);
        }else{
            return ServerResponse.createByErrorMessage("当前用户无权限操作！");
        }
    }

    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse<OrderVo>  detail(HttpServletRequest request, Long orderNo){
        User user  = null;
        String loginToken = CookiesUtil.readLoginToken(request);
        if (StringUtils.isNotEmpty(loginToken)){
            String userJsonStr = RedisPoolUtil.get(loginToken);
            user = JsonUtil.stringToObj(userJsonStr,User.class);
            if(user!=null){
                RedisPoolUtil.expire(loginToken, Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
            }
        }
        if(user==null) return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请重新登陆！");
        if(iUserService.checkAdminRole(user).isSuccess()){


            return iOrderService.manageDetail(orderNo);
        }else{
            return ServerResponse.createByErrorMessage("当前用户无权限操作！");
        }
    }

    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse<PageInfo>  manageSearch(HttpServletRequest request, Long orderNo, @RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum,
                                        @RequestParam(value="pageSize",defaultValue = "10")Integer pageSIze){
        User user  = null;
        String loginToken = CookiesUtil.readLoginToken(request);
        if (StringUtils.isNotEmpty(loginToken)){
            String userJsonStr = RedisPoolUtil.get(loginToken);
            user = JsonUtil.stringToObj(userJsonStr,User.class);
            if(user!=null){
                RedisPoolUtil.expire(loginToken, Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
            }
        }
        if(user==null) return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请重新登陆！");
        if(iUserService.checkAdminRole(user).isSuccess()){


            return iOrderService.manageSearch(orderNo,pageNum,pageSIze);
        }else{
            return ServerResponse.createByErrorMessage("当前用户无权限操作！");
        }
    }

    //发货
    @RequestMapping("send_goods.do")
    @ResponseBody
    public ServerResponse<String>  sendGoods(HttpServletRequest request, Long orderNo){
        User user  = null;
        String loginToken = CookiesUtil.readLoginToken(request);
        if (StringUtils.isNotEmpty(loginToken)){
            String userJsonStr = RedisPoolUtil.get(loginToken);
            user = JsonUtil.stringToObj(userJsonStr,User.class);
            if(user!=null){
                RedisPoolUtil.expire(loginToken, Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
            }
        }
        if(user==null) return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请重新登陆！");
        if(iUserService.checkAdminRole(user).isSuccess()){

            return iOrderService.manageSendGoods(orderNo);
        }else{
            return ServerResponse.createByErrorMessage("当前用户无权限操作！");
        }
    }
}
