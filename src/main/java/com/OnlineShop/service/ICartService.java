package com.OnlineShop.service;

import com.OnlineShop.common.ServerResponse;
import com.OnlineShop.vo.CartVo;

/**
 * Created by Administrator on 2017/12/13 0013.
 */
public interface ICartService {
    ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count);

    ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count);

    ServerResponse<CartVo> list(Integer userId);

    ServerResponse<CartVo> deleteProducts(Integer userId, String productIds);

    ServerResponse<CartVo> selectOrUnSelect(Integer userId, Integer productId, Integer checked);

    ServerResponse<Integer> getCartProductCount(Integer userId);

}
