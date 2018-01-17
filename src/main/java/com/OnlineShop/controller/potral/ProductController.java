package com.OnlineShop.controller.potral;

import com.github.pagehelper.PageInfo;
import com.OnlineShop.common.ServerResponse;
import com.OnlineShop.service.IProductService;
import com.OnlineShop.vo.ProductDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2017/12/9 0009.
 */
@Controller
@RequestMapping("/product/")
public class ProductController {

    @Autowired
    private IProductService iProductService;

    /**
     * 获取商品详情
     * @param productId
     * @return
     */
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse<ProductDetailVo> detail (Integer productId){
        return iProductService.getProductDetail(productId);
    }

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo> list(
            @RequestParam(value="keyword" ,required = false)String keyword,
            @RequestParam(value="categoryId",required = false) Integer categoryId,
            @RequestParam(value="pageNum", defaultValue = "1",required = false) Integer pageNum,
            @RequestParam(value="pageSize",defaultValue = "10",required = false) Integer pageSize,
            @RequestParam(value="orderBy",defaultValue = "",required = false) String orderBy
            ){
        return iProductService.getProductByKeywordCategory(keyword, orderBy, categoryId, pageNum, pageSize);

    }
}
