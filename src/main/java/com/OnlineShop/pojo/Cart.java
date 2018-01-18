package com.OnlineShop.pojo;

import lombok.*;

import java.util.Date;
 /**
  * @Description: 使用lombok简化代码
  * Created by Jann Lee on 2018/1/18  19:33.
  */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    private Integer id;

    private Integer userId;

    private Integer productId;

    private Integer quantity;

    private Integer checked;

    private Date createTime;

    private Date updateTime;
}