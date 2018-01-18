package com.mmall.common;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Created by Jann Lee on 2017/12/1 0001.
 */
public class Const {

    public static final String CURRENT_USER="currentUser";

    public static final String STRING_EMAIL="email";

    public static  final String STRING_USERNAME="username";
    //代替枚举，由于枚举繁重
    public interface Role{
        int ROLE_CUSTOMER=0;
        int ROLE_ADMIN=1;
    }

    public interface ProductListOrderBy{
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc","price_asc");
    }

    public enum ProductStatusEnum{
        ON_SALE(1,"在线");
        private String value;
        private int code;

        ProductStatusEnum(int code,String value) {
            this.value = value;
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }
    }

    public interface Cart{
        int CHECKED=1;//未选中
        int UN_CHECKED=0;//选中
        String LIMIT_NUM_FAIL="LIMIT_NUM_FAIL";//库存不够
        String LIMIT_NUM_SUCCESS="LIMIT_NUM_SUCCESS";
    }
}

