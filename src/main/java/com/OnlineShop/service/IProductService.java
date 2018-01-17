package com.OnlineShop.service;

import com.OnlineShop.common.ServerResponse;
import com.OnlineShop.pojo.Product;
import com.OnlineShop.vo.ProductDetailVo;
import com.github.pagehelper.PageInfo;

/**
 * Created by Administrator on 2017/12/6 0006.
 */
public interface IProductService {
    ServerResponse saveOrUpdateProduct(Product product);
    ServerResponse<String> setSaleStatus (Integer productId,Integer status);
    ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);
    ServerResponse<PageInfo> getProductList(Integer pageNum, Integer pageSize);
    ServerResponse<PageInfo> searchProduct(String productName,Integer productId,int pageNum,int pageSize);
    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);
    ServerResponse<PageInfo> getProductByKeywordCategory(String keyword,String orderBy,Integer categoryId,Integer pageNum,Integer pageSize);
}
