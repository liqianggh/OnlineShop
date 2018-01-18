package com.OnlineShop.controller;

import com.OnlineShop.dao.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ntp.TimeStamp;
import org.slf4j.log;
import org.slf4j.logFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2017/12/22 0022.
 */
@Controller
@RequestMapping("/test")
@Slf4j
public class TestController {

    private UserMapper userMapper;
//    private final log log = logFactory.getlog(TestController.class);

    public static void main(String [] args){
        TimeStamp tm = new TimeStamp(System.currentTimeMillis());
        System.out.println(tm);

    }

    @RequestMapping("test.do")
    @ResponseBody
    public String test(String str){

        log.info("textinfo");
        log.warn("testwarn");
        log.error("testerror");
        return "testvalue:"+str;
    }
}
