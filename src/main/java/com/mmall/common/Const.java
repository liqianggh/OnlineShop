package com.mmall.common;

/**
 * Created by Administrator on 2017/12/1 0001.
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
}

