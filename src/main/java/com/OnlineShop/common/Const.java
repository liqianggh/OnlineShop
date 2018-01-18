package com.OnlineShop.common;

import com.google.common.collect.Sets;

import java.util.Set;

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


    public enum OrderStatusEnum{
        CANCELED(0,"已取消"),
        NO_PAY(10,"未支付"),
        PAID(20,"已支付"),
        SHIPPED(40,"已发货"),
        ORDER_SUCCESS(50,"订单完成"),
        Order_ClOSE(60,"订单关闭");
        private String value;
        private int code;

        OrderStatusEnum(int code,String value) {
            this.value = value;
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }


        public static String codeOf(int code){
            for(OrderStatusEnum orderStatusEnum:values()){
                if(orderStatusEnum.getCode()==code){
                    return orderStatusEnum.getValue();
                }
            }
            throw new RuntimeException("没有找到该枚举");
        }
    }


    public interface AlipayCallback{
        String TRADE_STATUS_WAIT_BUYER_PAY="WAIT_BUYER_PAY";
        String TRADE_STATUS_TRADE_SUCCESS="TRADE_SUCCESS";

        String RESPONSE_SUCCESS="success";
        String RESPONSE_FAILED="failed";
    }


    public enum PayPlatformEnum{
        AlIPAY(1,"支付宝");

        private String value;
        private int code;
        PayPlatformEnum(int code,String value) {
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

    public enum PaymentTypeEnum{
        ONLINE_PAY(1,"在线支付");
        private String value;
        private int code;
        PaymentTypeEnum(int code,String value) {
            this.value = value;
            this.code = code;
        }
        public String getValue() {
            return value;
        }
        public int getCode() {
            return code;
        }
        public static String codeOf(int code){
            for(PaymentTypeEnum paymentTypeEnum:values()){
                if(paymentTypeEnum.getCode()==code){
                    return paymentTypeEnum.getValue();
                }
            }
            throw new RuntimeException("没有找到该枚举");
        }
    }
}

