package com.OnlineShop.common;

import redis.clients.jedis.JedisPool;

public class RedisPool {
    private static JedisPool pool=;//jedis连接池
    private static Integer maxTotal=;//最大连接数
    private static Integer maxIdel=;//最大空闲Jedis实例个数
    private static Integer minIdel=;//最小空闲Jedis实例个数
    private static Boolean testOnBorrow=//获取jedis实例前是否要进行验证
    private static Boolean testOnReturn=//归还时是否进行验证
}
