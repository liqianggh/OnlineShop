package com.mmall.test;

import org.junit.Test;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/12/13 0013.
 */
public class BigDecimalimalTest {

    @Test
    public void test1() {
        /**
         *   0.060000000000000005
         0.5800000000000001
         401.49999999999994
         0.12300000000000001
         */
        System.out.println(0.05 + 0.01);
        System.out.println(1.0 - 0.42);
        System.out.println(4.015 * 100);
        System.out.println(12.3 / 100);
    }


    @Test
    public void test2() {
        //0.06000000000000000298372437868010820238851010799407958984375
        BigDecimal b1 = new BigDecimal(0.05);
        BigDecimal b2 = new BigDecimal(0.01);

        System.out.println(b1.add(b2));

    }

    /**
     * 商业计算中一定要用BigDecimal的String构造器
     */
    @Test
    public void test3() {
        //0.06000000000000000298372437868010820238851010799407958984375
        BigDecimal b1 = new BigDecimal("0.05");
        BigDecimal b2 = new BigDecimal("0.01");

        System.out.println(b1.add(b2));

    }
}
