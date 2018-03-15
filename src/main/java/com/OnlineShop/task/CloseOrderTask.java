package com.OnlineShop.task;

import com.OnlineShop.common.Const;
import com.OnlineShop.service.IOrderService;
import com.OnlineShop.util.PropertiesUtil;
import com.OnlineShop.util.RedisShardedPoolUtil;
import com.sun.org.glassfish.external.probe.provider.annotations.ProbeParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

/**
  * @Description: 定时任务
  * Created by Jann Lee on 2018/3/14  21:22.
  */
 @Component
 @Slf4j
public class CloseOrderTask {

  @Autowired
  private IOrderService iOrderService;

//  @Scheduled(cron = "0 */1 * * * ?")//一分钟的整数倍来执行
  public void closeOrderTaskV1(){
   log.info("执行关闭订单定时任务开始！");

   int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour","2"));
   iOrderService.closeOrder(hour);
   log.info("执行关闭订单定时任务结束！");
  }



//@Scheduled(cron = "0 */1 * * * ?")
  public void closeOrderTaskV2(){
      log.info("关闭订单开始");
      long lockTimeout= Long.parseLong(PropertiesUtil.getProperty("lock.timeout","50000")) ;
      Long setnxResult = RedisShardedPoolUtil.setNx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,String.valueOf(System.currentTimeMillis()+lockTimeout));
   if(setnxResult!=null&&setnxResult==1){//代表设置成功  获取到锁
        closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
   }else{
       log.info("没有获得分布式锁");
   }
      log.info("关闭订单结束");
  }


    @Scheduled(cron = "0 */1 * * * ?")
    public void closeOrderTaskV3(){
        log.info("关闭订单开始");
        long lockTimeout= Long.parseLong(PropertiesUtil.getProperty("lock.timeout","50000")) ;
        Long setnxResult = RedisShardedPoolUtil.setNx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,String.valueOf(System.currentTimeMillis()+lockTimeout));
        if(setnxResult!=null&&setnxResult==1){//代表设置成功  获取到锁
            closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        }else{
            //未获得分布式锁 进行主动判断
            String lockStr = RedisShardedPoolUtil.get(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
            if(lockStr!=null&&System.currentTimeMillis()>Long.parseLong(lockStr)+lockTimeout){
                closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
            }
        }
        log.info("关闭订单结束");
    }


  //获取到锁  释放锁
  public void closeOrder(String lockName){
      RedisShardedPoolUtil.expire(lockName,50);//有效期50s
      log.info("获取{},ThreadName：{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, Thread.currentThread().getName());
      int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour","2"));
      iOrderService.closeOrder(hour);
    RedisShardedPoolUtil.del(lockName);
    log.info("主动释放分布式锁成功！");
    log.info("========================");
  }

//  //关闭容器前执行的方法
//  @PreDestroy
//  public void delLock(){
//      RedisShardedPoolUtil.del(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
//  }

}
