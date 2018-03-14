package com.OnlineShop.task;

import com.OnlineShop.service.IOrderService;
import com.OnlineShop.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
  * @Description: 定时任务
  * Created by Jann Lee on 2018/3/14  21:22.
  */
 @Component
 @Slf4j
public class CloseOrderTask {

  @Autowired
  private IOrderService iOrderService;

  @Scheduled(cron = "0 */1 * * * ?")//一分钟的整数倍来执行
  public void closeOrderTaskV1(){
   log.info("执行关闭订单定时任务开始！");

   int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour","2"));
   iOrderService.closeOrder(hour);
   log.info("执行关闭订单定时任务结束！");
  }
}
