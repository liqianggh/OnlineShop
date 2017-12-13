package com.mmall.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CartMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Cart;
import com.mmall.pojo.Product;
import com.mmall.service.ICartService;
import com.mmall.util.BigDecimalUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.CartProductVo;
import com.mmall.vo.CartVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2017/12/13 0013.
 */
@Service("iCartService")
public class CartServiceImpl implements ICartService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CartMapper cartMapper;
    public ServerResponse<CartVo> add(Integer userId,Integer productId,Integer count){
        Cart cart = cartMapper.selectCarByUserIdProductId(userId,productId);
        //todo 检验商品是否存在
        if(cart==null){
            //这个产品不在购物车里
            Cart cartIntem = new Cart();
            cartIntem.setQuantity(count);
            cartIntem.setChecked(Const.Cart.CHECKED);
            cartIntem.setUserId(userId);
            cartIntem.setProductId(productId);
            int rowCount = cartMapper.insert(cartIntem);
            //TODO
            if(rowCount==0){
                return ServerResponse.createByErrorMessage("添加到购物车失败");
            }
        }else{
            //如果商品已经存在 数量相加
            count = cart.getQuantity()+count;
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }
        //使用购物车高复用封装（库存，数量等的判断）
        return this.list(userId);
    }


    //更新购物车项
    public ServerResponse<CartVo> update(Integer userId,Integer productId,Integer count){
        Cart cart = cartMapper.selectCarByUserIdProductId(userId,productId);
        if(cart!=null){
            cart.setQuantity(count);
            //更新库存
            cartMapper.updateByPrimaryKeySelective(cart);
        }

        return this.list(userId);
    }

    //删除购物车项
    public ServerResponse<CartVo> deleteProducts(Integer userId,String productIds){
        //使用guawa的spliter方法把字符串转化为集合
        List<String> productIdList = Splitter.on(",").splitToList(productIds);
        //如果为空 返回参数错误码
        if(CollectionUtils.isEmpty(productIdList)){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        int rowCount = cartMapper.deleteByUserIdProductIds(userId,productIdList);
        if(rowCount<1){
            return ServerResponse.createByErrorMessage("删除失败");
        }
        return this.list(userId);
    }

    //查询所有
    public ServerResponse<CartVo> list(Integer userId){
        CartVo cartVo = this.getCartVoLimit(userId);
        return ServerResponse.createBySuccess(cartVo);
    }

    //全选,反选
    public ServerResponse<CartVo> selectOrUnSelect(Integer userId,Integer productId,Integer checked){
        int rowCount = cartMapper.selectOrUnSelect(userId,productId,checked);
        if(rowCount<1){
            return ServerResponse.createByErrorMessage("选择出现错误！");
        }
        return this.list(userId);
    }

    //获取购物车中的 商品数量
    public ServerResponse<Integer> getCartProductCount(Integer userId){
        if(userId==null){
            return ServerResponse.createBySuccess(0);
        }

        return ServerResponse.createBySuccess(cartMapper.getCartProductCount(userId));
    }

  //返回购物车的信息(高复用)
    private CartVo getCartVoLimit(Integer userId){
        CartVo cartVo = new CartVo();
        //查询该用户购物车里的所有商品
        List<Cart> cartList = cartMapper.selectCartByUserId(userId);
        List<CartProductVo> cartProductVoList = Lists.newArrayList();
        BigDecimal cartTotalPrice = new BigDecimal("0");
        //如果 购物车项不为空
       if(CollectionUtils.isNotEmpty(cartList)){
           //遍历
            for(Cart cartItem : cartList){
                //属性设置到vo中
                CartProductVo cartProductVo = new CartProductVo();
                cartProductVo.setId(cartItem.getId());
                cartProductVo.setUserId(cartItem.getUserId());
                cartProductVo.setProductId(cartItem.getProductId());
                cartProductVo.setProductChecked(cartItem.getChecked());
                //查找出购物车商品的详细信息，设置到vo中
                Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
                if(product!=null){
                    cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductSubtitle(product.getSubtitle());
                    cartProductVo.setProductStatus(product.getStatus());
                    cartProductVo.setProductPrice(product.getPrice());
                    cartProductVo.setProductStock(product.getStock());

                    //判断库存情况
                    int buyLimitCount = 0;
                    //库存充足的时候
                    if(product.getStock()>cartItem.getQuantity()){
                        buyLimitCount = cartItem.getQuantity();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
                    }else{
                        //超出库存 设置数量为库存数
                        buyLimitCount=product.getStock();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);
                        //购物车中更新有效库存
                        Cart cartForQuantity = new Cart();
                        cartForQuantity.setId(cartItem.getId());
                        cartForQuantity.setQuantity(buyLimitCount);
                        cartMapper.updateByPrimaryKeySelective(cartForQuantity);
                    }

                    cartProductVo.setQuantity(buyLimitCount);
                    //计算总价
                    cartProductVo.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(),cartProductVo.getQuantity()));
                    //设置是否已经被勾选
                    cartProductVo.setProductChecked(cartItem.getChecked());
                }
                //如果被勾选了 则计算出价格
                if(cartItem.getChecked()==Const.Cart.CHECKED){
                    //添加到购物车总价中
                    cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(),cartProductVo.getProductTotalPrice().doubleValue());
                }
                //添加到CartVo中的cartProductVoList中
                cartProductVoList.add(cartProductVo);
            }
        }
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setCartProductVoList(cartProductVoList);
        cartVo.setAllChecked(getAllCheckedStatus(userId));
        cartVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        return cartVo;
    }

    private boolean getAllCheckedStatus(Integer userId){
        if(userId==null){
            return false;
        }
        return cartMapper.selectCartProductCheckedStatusByUserId(userId)==0;
    }

}
