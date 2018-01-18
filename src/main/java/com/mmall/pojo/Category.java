package com.mmall.pojo;

import lombok.*;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="id")
@ToString(exclude = "updateTime")
public class Category {
    private Integer id;

    private Integer parentId;

    private String name;

    private Boolean status;

    private Integer sortOrder;

    private Date createTime;

    private Date updateTime;
//
//    //为了 使该数据在set集合中有效去重
//   @Override
//    public boolean equals(Object obj) {
//        if(this==obj)return true;
//        if(obj==null||getClass()!= obj.getClass())return false;
//        Category category = (Category) obj;
//
//        return !(id!=null?!id.equals(category.id):category.id!=null);
//    }
//
//    @Override
//    public int hashCode() {
//
//        return id!=null?id.hashCode():0;
//    }
}