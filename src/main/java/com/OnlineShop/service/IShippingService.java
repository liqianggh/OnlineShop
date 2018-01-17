package com.OnlineShop.service;

import com.OnlineShop.common.ServerResponse;
import com.OnlineShop.pojo.Shipping;
import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * Created by Administrator on 2017/12/14 0014.
 */
public interface IShippingService {
    ServerResponse<Map> add(Integer userId, Shipping shipping);

    ServerResponse del(Integer userId, Integer shippingId);

    ServerResponse update(Integer userId,Shipping shipping);

    ServerResponse<Shipping> select(Integer id, Integer shippingId);

    ServerResponse<PageInfo> list(Integer userId, Integer pageNum, Integer pageSize);
}
