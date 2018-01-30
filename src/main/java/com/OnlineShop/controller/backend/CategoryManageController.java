package com.OnlineShop.controller.backend;

import com.OnlineShop.common.Const;
import com.OnlineShop.common.ResponseCode;
import com.OnlineShop.common.ServerResponse;
import com.OnlineShop.pojo.User;
import com.OnlineShop.service.ICategoryService;
import com.OnlineShop.service.IUserService;
import com.OnlineShop.util.CookiesUtil;
import com.OnlineShop.util.JsonUtil;
import com.OnlineShop.util.RedisPoolUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by Administrator on 2017/12/2 0002.
 */
@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private ICategoryService iCategoryService;


    //添加分类
    @RequestMapping(value="add_category.do",method=RequestMethod.POST)
    @ResponseBody
    public ServerResponse addCategory(HttpServletRequest request, String categoryName, @RequestParam(value="parentId" ,defaultValue ="0" ) int parentId){
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
            return iCategoryService.addCategory(categoryName,parentId);
        }else{
            return ServerResponse.createByErrorMessage("当前用户无权限操作！");
        }

    }

    //修改分类
    @RequestMapping(value="set_category_name.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse updateCategory(HttpServletRequest request,Integer categoryId,String categoryName){
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
            //更新品类
            return iCategoryService.updateCategory(categoryId,categoryName);
        }else{
            return ServerResponse.createByErrorMessage("当前用户无权限操作！");
        }

    }

    @RequestMapping(value="get_category.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getChildParablelCategory(HttpServletRequest request, @RequestParam(value="categoryId",defaultValue="0")Integer categoryId){
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
            //查询子节点的category 并且不digui

            return iCategoryService.getChildrenParallelCategory(categoryId);
        }else{
            return ServerResponse.createByErrorMessage("当前用户无权限操作！");
        }

    }

    @RequestMapping(value="get_deep_category.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory(HttpServletRequest request, @RequestParam(value="categoryId",defaultValue="0")Integer categoryId){
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
            return iCategoryService.selectCateogryAndChildrenById(categoryId);
        }else{
            return ServerResponse.createByErrorMessage("当前用户无权限操作！");
        }
    }


}
