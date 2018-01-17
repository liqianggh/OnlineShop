package com.OnlineShop.controller;

import com.OnlineShop.dao.UserMapper;
import org.apache.commons.net.ntp.TimeStamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2017/12/22 0022.
 */
@Controller
@RequestMapping("/test")
public class TestController {

    private UserMapper userMapper;
    private final Logger logger = LoggerFactory.getLogger(TestController.class);

    public static void main(String [] args){
        TimeStamp tm = new TimeStamp(System.currentTimeMillis());
        System.out.println(tm);

    }

    @RequestMapping("test.do")
    @ResponseBody
    public String test(String str){

        logger.info("textinfo");
        logger.warn("testwarn");
        logger.error("testerror");
        return "testvalue:"+str;
    }
}
