package com.OnlineShop.controller.potral;

import com.OnlineShop.common.Const;
import com.OnlineShop.common.ResponseCode;
import com.OnlineShop.common.ServerResponse;
import com.OnlineShop.pojo.User;
import com.OnlineShop.service.ICartService;
import com.OnlineShop.util.CookiesUtil;
import com.OnlineShop.util.JsonUtil;
import com.OnlineShop.util.RedisPoolUtil;
import com.OnlineShop.vo.CartVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by Administrator on 2017/12/13 0013.
 */
@Controller
@RequestMapping("/cart/")
public class CartController {

    //添加商品到购物车 并返回购物车详情
    @Autowired
    private ICartService iCartService;

    @RequestMapping("add.do")
    @ResponseBody
    public ServerResponse<CartVo> add(HttpServletRequest request, Integer count, Integer productId) {
        if (productId == null || count == null) {
            return ServerResponse.createByErrorMessage("参数错误");
        }

        User user  = null;
        String loginToken = CookiesUtil.readLoginToken(request);
        if (org.apache.commons.lang.StringUtils.isNotEmpty(loginToken)){
            String userJsonStr = RedisPoolUtil.get(loginToken);
            user = JsonUtil.stringToObj(userJsonStr,User.class);
            if(user!=null){
                RedisPoolUtil.expire(loginToken, Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
            }
        }        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.add(user.getId(), productId, count);
    }

    @RequestMapping("update.do")
    @ResponseBody
    public ServerResponse<CartVo> update(HttpServletRequest request, Integer count, Integer productId) {
        if (productId == null || count == null) {
            return ServerResponse.createByErrorMessage("参数错误");
        }

        User user  = null;
        String loginToken = CookiesUtil.readLoginToken(request);
        if (org.apache.commons.lang.StringUtils.isNotEmpty(loginToken)){
            String userJsonStr = RedisPoolUtil.get(loginToken);
            user = JsonUtil.stringToObj(userJsonStr,User.class);
            if(user!=null){
                RedisPoolUtil.expire(loginToken, Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
            }
        }        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.update(user.getId(), productId, count);
    }

    @RequestMapping("delete_product.do")
    @ResponseBody
    public ServerResponse<CartVo> delete(HttpServletRequest request, String productIds) {
System.out.println(productIds+"接收到的参数");
        if (StringUtils.isBlank(productIds)) {
            return ServerResponse.createByErrorMessage("参数错误");
        }

        User user  = null;
        String loginToken = CookiesUtil.readLoginToken(request);
        if (org.apache.commons.lang.StringUtils.isNotEmpty(loginToken)){
            String userJsonStr = RedisPoolUtil.get(loginToken);
            user = JsonUtil.stringToObj(userJsonStr,User.class);
            if(user!=null){
                RedisPoolUtil.expire(loginToken, Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
            }
        }
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.deleteProducts(user.getId(), productIds);
    }


    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<CartVo> list(HttpServletRequest request) {
        User user  = null;
        String loginToken = CookiesUtil.readLoginToken(request);
        if (org.apache.commons.lang.StringUtils.isNotEmpty(loginToken)){
            String userJsonStr = RedisPoolUtil.get(loginToken);
            user = JsonUtil.stringToObj(userJsonStr,User.class);
            if(user!=null){
                RedisPoolUtil.expire(loginToken, Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
            }
        }
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.list(user.getId());
    }

    //全选
    @RequestMapping("select_all.do")
    @ResponseBody
    public ServerResponse<CartVo> selectAll(HttpServletRequest request) {
        User user  = null;
        String loginToken = CookiesUtil.readLoginToken(request);
        if (org.apache.commons.lang.StringUtils.isNotEmpty(loginToken)){
            String userJsonStr = RedisPoolUtil.get(loginToken);
            user = JsonUtil.stringToObj(userJsonStr,User.class);
            if(user!=null){
                RedisPoolUtil.expire(loginToken, Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
            }
        }
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelect(user.getId(),null,Const.Cart.CHECKED);
    }

    //全反选
    @RequestMapping("un_select_all.do")
    @ResponseBody
    public ServerResponse<CartVo> unSelectAll(HttpServletRequest request) {
        User user  = null;
        String loginToken = CookiesUtil.readLoginToken(request);
        if (org.apache.commons.lang.StringUtils.isNotEmpty(loginToken)){
            String userJsonStr = RedisPoolUtil.get(loginToken);
            user = JsonUtil.stringToObj(userJsonStr,User.class);
            if(user!=null){
                RedisPoolUtil.expire(loginToken, Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
            }
        }        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelect(user.getId(),null,Const.Cart.UN_CHECKED);
    }
    //单独选
    @RequestMapping("un_select.do")
    @ResponseBody
    public ServerResponse<CartVo> select(HttpServletRequest request,Integer productId) {
        if (productId==null) {
            return ServerResponse.createByErrorMessage("参数错误");
        }

        User user  = null;
        String loginToken = CookiesUtil.readLoginToken(request);
        if (org.apache.commons.lang.StringUtils.isNotEmpty(loginToken)){
            String userJsonStr = RedisPoolUtil.get(loginToken);
            user = JsonUtil.stringToObj(userJsonStr,User.class);
            if(user!=null){
                RedisPoolUtil.expire(loginToken, Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
            }
        }
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelect(user.getId(),productId,Const.Cart.UN_CHECKED);
    }
    //单独反选
    @RequestMapping("select.do")
    @ResponseBody
    public ServerResponse<CartVo> unSelect(HttpServletRequest request,Integer productId) {
        if (productId==null) {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        User user  = null;
        String loginToken = CookiesUtil.readLoginToken(request);
        if (org.apache.commons.lang.StringUtils.isNotEmpty(loginToken)){
            String userJsonStr = RedisPoolUtil.get(loginToken);
            user = JsonUtil.stringToObj(userJsonStr,User.class);
            if(user!=null){
                RedisPoolUtil.expire(loginToken, Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
            }
        }        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelect(user.getId(),productId,Const.Cart.CHECKED);
    }
    //查询当前用户的购物车商品数量
    @RequestMapping("get_cart_product_count.do")
    @ResponseBody
    public ServerResponse<Integer> getCartProductCount(HttpServletRequest request) {
        User user  = null;
        String loginToken = CookiesUtil.readLoginToken(request);
        if (org.apache.commons.lang.StringUtils.isNotEmpty(loginToken)){
            String userJsonStr = RedisPoolUtil.get(loginToken);
            user = JsonUtil.stringToObj(userJsonStr,User.class);
            if(user!=null){
                RedisPoolUtil.expire(loginToken, Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
            }
        }        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.getCartProductCount(user.getId());
    }

}