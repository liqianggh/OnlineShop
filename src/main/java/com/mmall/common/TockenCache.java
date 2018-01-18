package com.mmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by Jann Lee on 2017/12/1 0001.
 */
public class TockenCache {

    private static Logger logger = LoggerFactory.getLogger(TockenCache.class);

    //常量 前缀
    public static final String TOKEN_PREFIX="token_";

    private static LoadingCache<String, String> localCache = CacheBuilder.newBuilder()
            .initialCapacity(1000)//初始容量
            .maximumSize(10000)//最大容量
            .expireAfterAccess(12, TimeUnit.HOURS)//缓存有效期 12h
            .build(new CacheLoader<String, String>() {
                //默认的数据加载实现，当调用get取值的时候，
                // 如果key没有对应的值，就调用这个方法进行加载
                @Override
                public String load(String s) throws Exception {
                    return "null";
                }
            });

    //TODO
    public static void setKey(String key,String value){
        localCache.put(key,value);
    }

    public static String getKey(String key){
        String value = null;
        try{
            value = localCache.get(key);
            if("null".equals(value)){
                return null;
            }
        }catch (Exception e){
            logger.error("localCache get error",e);
        }
        //todo
        return value;
    }

    public static void destoryToken(String username){

        //TODO 让token失效
    }

}
