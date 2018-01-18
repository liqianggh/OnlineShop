package com.OnlineShop.service;

import com.OnlineShop.common.ServerResponse;
import com.OnlineShop.pojo.Category;

import java.util.List;

/**
 * Created by Administrator on 2017/12/2 0002.
 */
public interface ICategoryService {

    ServerResponse addCategory(String categoryName, Integer parentId);
    ServerResponse updateCategory(Integer categoryId,String categoryName);

    ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);
      ServerResponse<List<Integer>> selectCateogryAndChildrenById(Integer categoryId);

}
