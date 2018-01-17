package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static com.mmall.common.ServerResponse.createByErrorMessage;

/**
 * Created by Administrator on 2017/12/2 0002.
 */

@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService{

    //打印日志
    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ServerResponse addCategory(String categoryName, Integer parentId) {
        //参数校验
        if(parentId==null|| StringUtils.isBlank(categoryName)){
            return createByErrorMessage("添加品类参数错误");
        }

        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        //当前分类是否启用
        category.setStatus(true);

        int rowCount = categoryMapper.insert(category);
        if(rowCount>0){
            return ServerResponse.createBySuccessMsg("添加品类成功");
        }

        return createByErrorMessage("添加品类失败！");
    }


    @Override
    public ServerResponse updateCategory(Integer categoryId, String categoryName) {
        //参数校验
        if(categoryId==null||StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMessage("更新品类参数错误！");
        }

        Category category = new Category();
        category.setName(categoryName);
        category.setId(categoryId);

        int rowConnt= categoryMapper.updateByPrimaryKeySelective(category);
        if(rowConnt>0){
            return ServerResponse.createBySuccessMsg("品类更新成功！");
        }
        return ServerResponse.createByErrorMessage("品类更新失败！");
    }

    @Override
    public ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId) {
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);

        if(CollectionUtils.isEmpty(categoryList)){
            logger.info("未找到当前分类的子分类");
        }

        return ServerResponse.createBySuccess(categoryList);
    }


    /**
     * 递归查找本结点的id和孩子节点的id
     * @param categoryId
     * @return
     */
    public ServerResponse selectCateogryAndChildrenById(Integer categoryId){
        //初始化集合
        Set<Category> categorySet= Sets.newHashSet();
        findChildCategory(categorySet,categoryId);

        //把set集合中的categoryId存入list中
        List<Integer> categoryIdList = Lists.newArrayList();
        if(categoryId!=null){
            for(Category categoryItem : categorySet){
                categoryIdList.add(categoryItem.getId());
            }
        }

        return ServerResponse.createBySuccess(categoryIdList);
    }


    //递归算法算出子节点
    private Set<Category> findChildCategory(Set<Category> categorySet, Integer categoryId){
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if(category!=null){
            categorySet.add(category);
        }
        //查询出该节点的所有子节点
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        for(Category categoryItem:categoryList){
            //如果id为null 递归结束
            findChildCategory(categorySet,categoryItem.getId());
        }

        return categorySet;

    }

}
