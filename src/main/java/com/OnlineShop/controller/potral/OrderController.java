package com.OnlineShop.controller.potral;

import com.OnlineShop.util.CookiesUtil;
import com.OnlineShop.util.JsonUtil;
import com.OnlineShop.util.RedisPoolUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.OnlineShop.common.Const;
import com.OnlineShop.common.ResponseCode;
import com.OnlineShop.common.ServerResponse;
import com.OnlineShop.pojo.User;
import com.OnlineShop.service.IOrderService;
import com.OnlineShop.util.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/22 0022.
 */
@Controller
@RequestMapping("/order/")
@Slf4j
public class OrderController {

//    private static final log log = logFactory.getlog(OrderController.class);
    @Autowired
    private IOrderService iOrderService;

    //创建订单
    @RequestMapping(value = "create.do",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ServerResponse create(HttpServletRequest request, @RequestParam(value="shippingId") Integer shippingId) {
log.info("哈哈哈哈"+shippingId);
        User user  = null;
        String loginToken = CookiesUtil.readLoginToken(request);
        if (StringUtils.isNotEmpty(loginToken)){
            String userJsonStr = RedisPoolUtil.get(loginToken);
            user = JsonUtil.stringToObj(userJsonStr,User.class);
            if(user!=null){
                RedisPoolUtil.expire(loginToken, Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
            }
        }        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        //返回给前端数据
        return  iOrderService.createOrder(user.getId(),shippingId);
//        return ServerResponse.createBySuccess("哈哈哈");
    }
    //取消订单
    @RequestMapping("cancel.do")
    @ResponseBody
    public ServerResponse cancel(HttpServletRequest request, Long orderNo  ) {
        User user  = null;
        String loginToken = CookiesUtil.readLoginToken(request);
        if (StringUtils.isNotEmpty(loginToken)){
            String userJsonStr = RedisPoolUtil.get(loginToken);
            user = JsonUtil.stringToObj(userJsonStr,User.class);
            if(user!=null){
                RedisPoolUtil.expire(loginToken, Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
            }
        }        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.cancel(user.getId(),orderNo);
    }
    //获取商品（购物车中的）
    @RequestMapping("get_order_cart_product.do")
    @ResponseBody
    public ServerResponse getOrderCartProduct(HttpServletRequest request, Long orderNo  ) {
        User user  = null;
        String loginToken = CookiesUtil.readLoginToken(request);
        if (StringUtils.isNotEmpty(loginToken)){
            String userJsonStr = RedisPoolUtil.get(loginToken);
            user = JsonUtil.stringToObj(userJsonStr,User.class);
            if(user!=null){
                RedisPoolUtil.expire(loginToken, Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
            }
        }        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.getOrderCartProduct(user.getId());
    }


    //订单详情
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse detail(HttpServletRequest request, Long orderNo  ) {
        User user  = null;
        String loginToken = CookiesUtil.readLoginToken(request);
        if (StringUtils.isNotEmpty(loginToken)){
            String userJsonStr = RedisPoolUtil.get(loginToken);
            user = JsonUtil.stringToObj(userJsonStr,User.class);
            if(user!=null){
                RedisPoolUtil.expire(loginToken, Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
            }
        }        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.getOrderDetail(user.getId(),orderNo);
    }


    //订单列表
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse list(HttpServletRequest request, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value="pageSize" ,defaultValue = "10") int pageSize ) {
        User user  = null;
        String loginToken = CookiesUtil.readLoginToken(request);
        if (StringUtils.isNotEmpty(loginToken)){
            String userJsonStr = RedisPoolUtil.get(loginToken);
            user = JsonUtil.stringToObj(userJsonStr,User.class);
            if(user!=null){
                RedisPoolUtil.expire(loginToken, Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
            }
        }        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.getOrderList(user.getId(),pageNum,pageSize);
    }



















    @RequestMapping("pay.do")
    @ResponseBody
    public ServerResponse pay( long orderNo, HttpServletRequest request) {
        User user  = null;
        String loginToken = CookiesUtil.readLoginToken(request);
        if (StringUtils.isNotEmpty(loginToken)){
            String userJsonStr = RedisPoolUtil.get(loginToken);
            user = JsonUtil.stringToObj(userJsonStr,User.class);
            if(user!=null){
                RedisPoolUtil.expire(loginToken, Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
            }
        }        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        //获取servlet上下文路径
        //这种方式不行String path = request.getSession().getServletContext().getRealPath("upload");
        String path = this.getClass().getResource("/").getPath();

        return iOrderService.pay(orderNo, user.getId(), path);
    }

    @RequestMapping("alipay_callback.do")
    @ResponseBody
    public Object alipayCallback(HttpServletRequest request ){
        Map requeestParams = request.getParameterMap();
        Map<String,String> params = Maps.newHashMap();
        for(Iterator iterator =requeestParams.keySet().iterator();iterator.hasNext();){
            String name = (String) iterator.next();
            String []values = (String[]) requeestParams.get(name);

            String valueStr = "";
            for(int i =0;i<values.length;i++){
                valueStr=(i==values.length-1)?valueStr+values[i]:valueStr+values[i]+",";
            }
            params.put(name,valueStr);
        }
        log.info("支付宝回调，sign{},trade_status:{},参数：{}",params.get("sign"),params.get("trade_status"),params.toString());

        //验证回调是否是支付宝发的,并且要避免重复通知
        params.remove("sign_type");
        try {
            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(),"utf-8",Configs.getSignType());
            if(!alipayRSACheckedV2){
                return ServerResponse.createByErrorMessage("非法请求验证不通过！");
            }

        } catch (AlipayApiException e) {
            log.error("支付宝验证异常：",e);
        }
        //todo 验证支付宝返回的各种数据
        String tradeStatus = params.get("trade_status");
        //订单号
        String orderNo = params.get("out_trade_no");
        String sbuject = params.get("subject");
        String body = params.get("body");
        //交易号
        String plutfromNum = params.get("trade_no");
        //回调时间
        Date notifyTime = DateTimeUtil.strToDate(params.get("notify_time"));

        //
        ServerResponse serverResponse = iOrderService.aliCallback(params);
        if(serverResponse.isSuccess()){
            return Const.AlipayCallback.RESPONSE_SUCCESS;

        }
        return Const.AlipayCallback.RESPONSE_FAILED;
    }

    //轮询查询订单支付状态

    @RequestMapping("query_order_pay_status.do")
    @ResponseBody
    public ServerResponse<Boolean> queryOrderPayStatus(HttpServletRequest request, long orderNo) {
        User user  = null;
        String loginToken = CookiesUtil.readLoginToken(request);
        if (StringUtils.isNotEmpty(loginToken)){
            String userJsonStr = RedisPoolUtil.get(loginToken);
            user = JsonUtil.stringToObj(userJsonStr,User.class);
            if(user!=null){
                RedisPoolUtil.expire(loginToken, Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
            }
        }        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }


       ServerResponse serverResponse =  iOrderService.queryOrderPayStatus(user.getId(),orderNo);
        if(serverResponse.isSuccess()){
            return ServerResponse.createBySuccess(true);
        }

        return ServerResponse.createBySuccess(false);
    }


}
