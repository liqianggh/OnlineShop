package com.mmall.controller.backend;

import com.github.pagehelper.PageInfo;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import com.mmall.service.IUserService;
import com.mmall.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

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
    public ServerResponse<PageInfo>  list(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum,
                                               @RequestParam(value="pageSize",defaultValue = "10")Integer pageSIze){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null) return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请重新登陆！");
        if(iUserService.checkAdminRole(user).isSuccess()){


            return iOrderService.manageList(pageNum,pageSIze);
        }else{
            return ServerResponse.createByErrorMessage("当前用户无权限操作！");
        }
    }

    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse<OrderVo>  detail(HttpSession session, Long orderNo){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null) return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请重新登陆！");
        if(iUserService.checkAdminRole(user).isSuccess()){


            return iOrderService.manageDetail(orderNo);
        }else{
            return ServerResponse.createByErrorMessage("当前用户无权限操作！");
        }
    }

    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse<PageInfo>  manageSearch(HttpSession session, Long orderNo, @RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum,
                                        @RequestParam(value="pageSize",defaultValue = "10")Integer pageSIze){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
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
    public ServerResponse<String>  sendGoods(HttpSession session, Long orderNo){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null) return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请重新登陆！");
        if(iUserService.checkAdminRole(user).isSuccess()){

            return iOrderService.manageSendGoods(orderNo);
        }else{
            return ServerResponse.createByErrorMessage("当前用户无权限操作！");
        }
    }
}
