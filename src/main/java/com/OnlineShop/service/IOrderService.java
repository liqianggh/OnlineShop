package com.OnlineShop.service;

import com.OnlineShop.common.ServerResponse;
import com.OnlineShop.vo.OrderVo;
import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * Created by Administrator on 2017/12/22 0022.
 */
public interface IOrderService {
    ServerResponse pay(Long orderNo, Integer userId, String path);
    ServerResponse aliCallback(Map<String, String> params);
    ServerResponse queryOrderPayStatus(Integer userId,Long orderNo);
    ServerResponse<OrderVo> createOrder(Integer userId, Integer shippingId);
    ServerResponse<String> cancel(Integer userId,Long orderNo);
    ServerResponse getOrderCartProduct(Integer userId );
    ServerResponse<OrderVo> getOrderDetail(Integer userId,Long orderNo);
    ServerResponse<PageInfo> getOrderList(Integer userId,int pageNum,int pageSize);

    //backend
    ServerResponse<PageInfo> manageList(int pageNum,int pageSize);

    ServerResponse<OrderVo> manageDetail(Long orderNo);

    ServerResponse<PageInfo> manageSearch(Long orderNo,int pageNum,int pageSize);

    ServerResponse<String> manageSendGoods(Long orderNo);

    //hour个小时以内未付款的订单进行关闭
    void closeOrder(int hour);
}
