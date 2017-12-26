package com.mmall.test;

import com.mmall.util.PropertiesUtil;

/**
 * Created by Administrator on 2017/12/24 0024.
 */
public class PropertiesUtilsTest {

    public static void main(String[]args){
        String str = PropertiesUtil.getProperty("alipay.callback.url");
        System.out.println(str+"哈哈哈");
    }


}
