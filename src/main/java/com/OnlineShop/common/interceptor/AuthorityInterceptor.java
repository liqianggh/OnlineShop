package com.OnlineShop.common.interceptor;

import com.OnlineShop.common.Const;
import com.OnlineShop.common.ServerResponse;
import com.OnlineShop.pojo.User;
import com.OnlineShop.util.CookiesUtil;
import com.OnlineShop.util.JsonUtil;
import com.OnlineShop.util.RedisShardedPoolUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
  * @Description: 权限检验拦截去
  * Created by Jann Lee on 2018/3/11  9:27.
  */
 @Slf4j
public class AuthorityInterceptor  implements HandlerInterceptor{
    @Override
        public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
            //请求中Controller中的方法名
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            //解析HandlerMethod
        String methodName = handlerMethod.getMethod().getName();
        String className = handlerMethod.getBean().getClass().getSimpleName();

        //解析参数
//        StringBuffer requestParamBuffer = new StringBuffer();
//        Map paramMap = httpServletRequest.getParameterMap();
//        Iterator iterator = paramMap.entrySet().iterator();
//        while(iterator.hasNext()){
//            Map.Entry entry = (Map.Entry) iterator.next();
//            String mapKey = (String)entry.getKey();
//            String mapValue = StringUtils.EMPTY;
//
//            //request这个参数的map，里面的value返回的是一个String数组
//            Object obj = entry.getValue();
//            if(obj instanceof String[]){
//                String[] strs = (String[]) obj;
//                mapValue = Arrays.toString(strs);
//            }
//            requestParamBuffer.append(mapKey).append("=").append(mapValue);
//        }
//        log.info(requestParamBuffer.toString()+"这是请求  ");

        User user = null;
        String loginToken =  CookiesUtil.readLoginToken(httpServletRequest);
      if(StringUtils.isNotEmpty(loginToken)){
          String userJsonStr = RedisShardedPoolUtil.get(loginToken);
          user = JsonUtil.stringToObj(userJsonStr,User.class);
      }

      if(user==null||(user.getRole().intValue()!= Const.Role.ROLE_ADMIN)){
          httpServletResponse.reset();//这里要添加reset 否则会报异常 getWriter（）has already called for the response
          httpServletResponse.setCharacterEncoding("UTF-8");//一定要设置 不然会乱码
          httpServletResponse.setContentType("application/json;charset=UTF-8");//这里要设置返回值类型，因为是json接口
          PrintWriter out = httpServletResponse.getWriter();
          if(user==null){
              //放行上传付文本文件
              if(StringUtils.equals(className,"ProductManageController")&&StringUtils.equals(methodName,"richTextUpload")){
                  Map resultMap = Maps.newHashMap();
                  resultMap.put("success",false);
                  resultMap.put("msg","请登录管理员");
                  out.print(JsonUtil.objToString(resultMap));
              }else{
                  out.print(JsonUtil.objToString(ServerResponse.createByErrorMessage("拦截器拦截，用户未登录！")));

              }
          }else{
              if(StringUtils.equals(className,"ProductManageController")&&StringUtils.equals(methodName,"richTextUpload")){
                  Map resultMap = Maps.newHashMap();
                  resultMap.put("success",false);
                  resultMap.put("msg","当前用户无权限操作！");
                  out.print(JsonUtil.objToString(resultMap));
              }else{
                  out.print(JsonUtil.objToString(ServerResponse.createByErrorMessage("当前用户无权限操作！")));
              }
          }
          out.flush();
          out.close();//清空 关闭流
          return false;
      }
            return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        log.info("postHandle");

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        log.info("afterCompletion");
    }
}
