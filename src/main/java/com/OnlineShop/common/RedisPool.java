package com.OnlineShop.common;

import com.OnlineShop.util.PropertiesUtil;
import redis.clients.jedis.JedisPool;

import java.util.Properties;

public class RedisPool {
    private static JedisPool pool= //jedis连接池
    private static Integer maxTotal=Integer.parseInt(PropertiesUtil.getProperty("redis.max.total","20"));//最大连接数
    private static Integer maxIdel=Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle","20"));;//最大空闲Jedis实例个数
    private static Integer minIdel=Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle","20"));;//最小空闲Jedis实例个数
    private static Boolean testOnBorrow=Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow","true"));//获取jedis实例前是否要进行验证
    private static Boolean testOnReturn=Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return","false"));//归还时是否进行验证

}
