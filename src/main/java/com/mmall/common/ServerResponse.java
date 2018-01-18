package com.mmall.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

  /**
   * @Description:
   * Created by Jann Lee on 2018/1/18  18:56.
   */
//保证序列化的时候空的数据不会序列化
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> implements Serializable{

    private int status;
    private String msg;
    private T data;

    private ServerResponse(int status){
        this.status=status;
    }
    private ServerResponse(int status ,T data){
        this.data=data;
        this.status=status;
    }
    private ServerResponse(int status,T data,String msg){
        this.data=data;
        this.status=status;
        this.msg=msg;
    }
    private ServerResponse(int status,String msg){
        this.status=status;
        this.msg=msg;
    }


    //序列化之后忽略
    @JsonIgnore
    public  boolean isSuccess(){
        return this.status==ResponseCode.SUCCESS.getCode() ;
    }

    public int getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }


    //请求成功时的一系列响应
    public static <T> ServerResponse<T> createBySuccess(){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }
    //返回msg
    public static <T> ServerResponse<T> createBySuccessMsg(String msg){
        return new ServerResponse(ResponseCode.SUCCESS.getCode(),msg);
    }
    //返回data（解决了构造方法传参冲突的问题，传入两个参数时，只能是 ServerResponse(int status ,T data)
    public static <T> ServerResponse<T> createBySuccess(T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),data);
    }
    //返回消息和数据
    public static <T> ServerResponse<T> createBySuccess(String msg,T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),data,msg);
    }


    //失败时的响应
     public static <T> ServerResponse<T> createByError(){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode());
    }

    public static <T> ServerResponse<T> createByErrorMessage(String errorMsg){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),errorMsg);
    }


    public static <T> ServerResponse<T> createByErrorCodeMessage(int errorCode,String errorMessage){
        return new ServerResponse<T>(errorCode,errorMessage);
    }


}
