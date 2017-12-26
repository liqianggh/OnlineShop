package com.mmall.controller.potral;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import com.mmall.util.DateTimeUtil;
import org.apache.commons.lang.time.DateUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    @Autowired
    private IOrderService iOrderService;

    @RequestMapping("pay.do")
    @ResponseBody
    public ServerResponse pay(HttpSession session, long orderNo, HttpServletRequest request) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
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
        logger.info("支付宝回调，sign{},trade_status:{},参数：{}",params.get("sign"),params.get("trade_status"),params.toString());

        //验证回调是否是支付宝发的,并且要避免重复通知
        params.remove("sign_type");
        try {
            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(),"utf-8",Configs.getSignType());
            if(!alipayRSACheckedV2){
                return ServerResponse.createByErrorMessage("非法请求验证不通过！");
            }

        } catch (AlipayApiException e) {
            logger.error("支付宝验证异常：",e);
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
    public ServerResponse<Boolean> queryOrderPayStatus(HttpSession session, long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }


       ServerResponse serverResponse =  iOrderService.queryOrderPayStatus(user.getId(),orderNo);
        if(serverResponse.isSuccess()){
            return ServerResponse.createBySuccess(true);
        }

        return ServerResponse.createBySuccess(false);
    }


}
