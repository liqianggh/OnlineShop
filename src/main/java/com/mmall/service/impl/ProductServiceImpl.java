package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.IProductService;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import com.mmall.vo.ProductListVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/6 0006.
 */
@Service("iProductService")
public class ProductServiceImpl  implements IProductService{

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    public ServerResponse saveOrUpdateProduct(Product product) {

        if (product != null) {
            if (StringUtils.isNotBlank(product.getSubImages())) {
                String[] subImagesArray = product.getSubImages().split(",");
                if (subImagesArray.length > 0) {
                    product.setMainImage(subImagesArray[0]);
                }

            }
            if (product.getId() != null) {
                int x = productMapper.updateByPrimaryKey(product);
                if (x > 0) {
                    return ServerResponse.createBySuccessMsg("更新产品成功！");
                }
                return ServerResponse.createByErrorMessage("更新产品失败！");
            }else{
                int rowCount = productMapper.insert(product);
                if(rowCount>0){
                    return ServerResponse.createBySuccess("新增产品成功");
                }

                return ServerResponse.createBySuccessMsg("新增产品失败！");
            }

        }

        return ServerResponse.createByErrorMessage("新增或修改产品参数错误！");

    }

    public ServerResponse<String> setSaleStatus (Integer productId,Integer status){
        if(productId==null||status==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);

        int rowCount = productMapper.updateByPrimaryKeySelective(product);

        if(rowCount>0){
            return ServerResponse.createBySuccessMsg("修改商品状态成功！");
        }

        return ServerResponse.createByErrorMessage("修改商品状态失败！");
    }


    public ServerResponse<ProductDetailVo> manageProductDetail(Integer productId){
        if(productId==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product==null){
            return ServerResponse.createByErrorMessage("商品已经下架或删除！");
        }
        ProductDetailVo productDetailVo = assembleProductDetailVoe(product);
        return ServerResponse.createBySuccess(productDetailVo);
    }


    private ProductDetailVo assembleProductDetailVoe(Product product){
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setName(product.getName());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImage(product.getSubImages());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setStock(product.getStock());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setSubtitle(product.getSubtitle());

        //imageHost,parentCategoryId,createTieme upateTime
        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.ip","http://img.happymmall.com/"));
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if(category==null){
            productDetailVo.setParentCategoryId(0);
        }else{
            productDetailVo.setParentCategoryId(category.getParentId());
        }

        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));

        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));

        return productDetailVo;
    }

    //分页查询product
    public ServerResponse<PageInfo> getProductList(Integer pageNum,Integer pageSize){
        //start >end
        //填充sql逻辑
        //pageHelper收尾

        PageHelper.startPage(pageNum,pageSize);
        List<Product> productList = productMapper.selectList();
        List<ProductListVo> listVoList = new ArrayList<ProductListVo>();
        ProductListVo productListVo=null;
        for(Product productItem:productList){
             productListVo = assembleProductListVo(productItem);
            listVoList.add(productListVo);
        }
        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(listVoList);

        return ServerResponse.createBySuccess(pageResult);

    }
    private ProductListVo assembleProductListVo(Product product){
        ProductListVo productListVo = new ProductListVo();

        productListVo.setId(product.getId());
        productListVo.setName(product.getName());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setMainImage(product.getMainImage());
        productListVo.setStatus(product.getStatus());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.ip","http://img.happymmall.com/"));
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setPrice(product.getPrice());
        return productListVo;
    }


    public ServerResponse<PageInfo> searchProduct(String productName,Integer productId,int pageNum,int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        if(StringUtils.isNoneBlank(productName)){
            productName=new StringBuilder().append("%").append(productName).append("%").toString();
        }

        List<Product> productList = productMapper.selectByNameAndProductId(productName,productId);
        List<ProductListVo> listVoList = new ArrayList<ProductListVo>();
        ProductListVo productListVo=null;
        for(Product productItem:productList){
            productListVo = assembleProductListVo(productItem);
            listVoList.add(productListVo);
        }
        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(listVoList);
        return ServerResponse.createBySuccess(pageResult);
    }


}
