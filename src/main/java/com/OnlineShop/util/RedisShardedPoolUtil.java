package com.OnlineShop.util;

import com.OnlineShop.common.RedisPool;
import com.OnlineShop.common.RedisShardedPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;
import redis.clients.util.Sharded;

/**
  * @Description: 封装Redis工具类
  * Created by Jann Lee on 2018/1/28  1:08.
  */
@Slf4j
public class RedisShardedPoolUtil {

    //set方法
    public static String set(String key,String value){
        ShardedJedis jedis = null;
        String result = null;
        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.set(key,value);
        } catch (Exception e) {
            log.error("set key:{} value:{} error",key,value,e);
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    //get方法
     public static String get(String key){
         ShardedJedis jedis = null;
         String result = null;
         try {
             jedis = RedisShardedPool.getJedis();
             result = jedis.get(key);
         } catch (Exception e) {
             log.error("get key:{} error",key,e);
             RedisShardedPool.returnBrokenResource(jedis);
             return result;
         }
         RedisShardedPool.returnResource(jedis);
         return result;
     }

     //设置过期时间 秒
     public static String setEx(String key,String value,int exTime){
         ShardedJedis jedis = null;
         String result = null;
         try {
             jedis = RedisShardedPool.getJedis();
             result = jedis.setex(key,exTime,value);
         } catch (Exception e) {
             log.error("setex:{} value:{} exTime{} error",key,value,exTime,e);
             RedisShardedPool.returnBrokenResource(jedis);
             return result;
         }
         RedisShardedPool.returnResource(jedis);
         return result;
     }



     //设置有效期  单位是秒
     public static Long expire(String key,int exTime){
         ShardedJedis jedis = null;
         Long result = null;
         try {
             jedis = RedisShardedPool.getJedis();
             result = jedis.expire(key,exTime);
         } catch (Exception e) {
             log.error("expire key:{} exTIme:{} error",key,exTime,e);
             RedisShardedPool.returnBrokenResource(jedis);
             return result;
         }
         RedisShardedPool.returnResource(jedis);
         return result;
     }

     //设置有效期  单位是秒
     public static Long del(String key){
         ShardedJedis jedis = null;
         Long result = null;
         try {
             jedis = RedisShardedPool.getJedis();
             result = jedis.del(key);
         } catch (Exception e) {
             log.error("del key:{} error",key,e);
             RedisShardedPool.returnBrokenResource(jedis);
             return result;
         }
         RedisShardedPool.returnResource(jedis);
         return result;
     }
     public static void main(String [] args){
         ShardedJedis jedis = RedisShardedPool.getJedis();
         RedisShardedPoolUtil.set("keyTest","valueTest");
         String value = RedisShardedPoolUtil.get("keyTest");

         RedisShardedPoolUtil.setEx("keyex","valuesEx",60*10);

         RedisShardedPoolUtil.expire("keyTest",60*20);

         RedisShardedPoolUtil.del("keyTest");

         System.out.println("end");
     }
 }
