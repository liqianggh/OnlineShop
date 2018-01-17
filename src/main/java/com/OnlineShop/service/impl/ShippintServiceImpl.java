package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ShippingMapper;
import com.mmall.pojo.Shipping;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/14 0014.
 */
@Service("iShippingService")
public class ShippintServiceImpl implements IShippingService{

    @Autowired
    private ShippingMapper shippingMapper;

    public ServerResponse<Map> add(Integer userId,Shipping shipping){
        shipping.setUserId(userId);
        //插入并且获取主键
        int rowCount = shippingMapper.insert(shipping);
        if(rowCount>0){
             Map result = Maps.newHashMap();
            result.put("shippingId",shipping.getId());
            return ServerResponse.createBySuccess("新建地址成功！",result);
        }

        return ServerResponse.createByErrorMessage("地址添加失败！");
    }


    public  ServerResponse del(Integer userId, Integer shippingId){
        //防止横向越权，此处应该校验userid
        int resultCount = shippingMapper.deleteByShippingIdAndUserId(userId,shippingId);
        if(resultCount>0){
            return ServerResponse.createBySuccess("删除地址成功！");
        }
        return ServerResponse.createByErrorMessage("删除地址失败!");
    }

    public ServerResponse update(Integer userId,Shipping shipping){
        shipping.setUserId(userId);
        int rowCount = shippingMapper.updateByShipping(shipping);
        if(rowCount>0){
            return ServerResponse.createBySuccess("更新地址成功！");
        }
        return ServerResponse.createByErrorMessage("更新地址失败!");

    }

    @Override
    public ServerResponse<Shipping> select(Integer id, Integer shippingId) {
        Shipping shipping=shippingMapper.selectByShippingIdAndUserId(id,shippingId);
        if(shipping==null){
            return ServerResponse.createByErrorMessage("查询改地址失败！");
        }
        return ServerResponse.createBySuccess(shipping);
    }


    @Override
    public ServerResponse<PageInfo> list(Integer userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);

        List<Shipping> shippingList = shippingMapper.selectAllShippingByUserId(userId);

        PageInfo pageInfo = new PageInfo(shippingList);

        return ServerResponse.createBySuccess(pageInfo);
    }
}
