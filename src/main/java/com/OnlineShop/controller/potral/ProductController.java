package com.OnlineShop.controller.potral;

import com.github.pagehelper.PageInfo;
import com.OnlineShop.common.ServerResponse;
import com.OnlineShop.service.IProductService;
import com.OnlineShop.vo.ProductDetailVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Administrator on 2017/12/9 0009.
 */
@Controller
@RequestMapping("/product/")
public class ProductController {

    @Autowired
    private IProductService iProductService;

//    /**
//     * 获取商品详情
//     * @param productId
//     * @return
//     */
//    @RequestMapping("detail.do")
//    @ResponseBody
//    public ServerResponse<ProductDetailVo> detail (Integer productId){
//        return iProductService.getProductDetail(productId);
//    }

    @RequestMapping(value="/{productId}",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<ProductDetailVo> detailRestful (@PathVariable Integer productId){
        return iProductService.getProductDetail(productId);
    }

//    @RequestMapping("list.do")
//    @ResponseBody
//    public ServerResponse<PageInfo> list(
//            @RequestParam(value="keyword" ,required = false)String keyword,
//            @RequestParam(value="categoryId",required = false) Integer categoryId,
//            @RequestParam(value="pageNum", defaultValue = "1",required = false) Integer pageNum,
//            @RequestParam(value="pageSize",defaultValue = "10",required = false) Integer pageSize,
//            @RequestParam(value="orderBy",defaultValue = "",required = false) String orderBy
//            ){
//        return iProductService.getProductByKeywordCategory(keyword, orderBy, categoryId, pageNum, pageSize);
//
//    }

    @RequestMapping(value="/{keyword}/{categoryId}/{pageNum}/{pageSize}/{orderBy}",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> restfulList(
            @PathVariable(value="keyword")String keyword,
            @PathVariable(value="categoryId") Integer categoryId,
            @PathVariable(value="pageNum") Integer pageNum,
            @PathVariable(value="pageSize") Integer pageSize,
            @PathVariable(value="orderBy") String orderBy
    ){
        if(pageNum==null){
            pageNum=1;
        }
        if(pageSize==null){
            pageSize=10;
        }
        if(StringUtils.isBlank(orderBy)){
            orderBy="price_asc";
        }

        return iProductService.getProductByKeywordCategory(keyword, orderBy, categoryId, pageNum, pageSize);

    }


    @RequestMapping(value="/category/{categoryId}/{pageNum}/{pageSize}/{orderBy}",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> restfulList(
            @PathVariable(value="categoryId") Integer categoryId,
            @PathVariable(value="pageNum") Integer pageNum,
            @PathVariable(value="pageSize") Integer pageSize,
            @PathVariable(value="orderBy") String orderBy
    ){
        if(pageNum==null){
            pageNum=1;
        }
        if(pageSize==null){
            pageSize=10;
        }
        if(StringUtils.isBlank(orderBy)){
            orderBy="price_asc";
        }

        return iProductService.getProductByKeywordCategory("", orderBy, categoryId, pageNum, pageSize);

    }
    @RequestMapping(value="/keyword/{keyword}/{pageNum}/{pageSize}/{orderBy}",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> restfulList(
            @PathVariable(value="keyword")String keyword,
            @PathVariable(value="pageNum") Integer pageNum,
            @PathVariable(value="pageSize") Integer pageSize,
            @PathVariable(value="orderBy") String orderBy
    ){
        if(pageNum==null){
            pageNum=1;
        }
        if(pageSize==null){
            pageSize=10;
        }
        if(StringUtils.isBlank(orderBy)){
            orderBy="price_asc";
        }

        return iProductService.getProductByKeywordCategory(keyword, orderBy, null, pageNum, pageSize);

    }
}
