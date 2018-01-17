package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TockenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by Administrator on 2017/12/1 0001.
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService{

    @Autowired
    private UserMapper userMapper;
    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount = userMapper. checkUserName(username);

        if(resultCount==0){
            return ServerResponse.createByErrorMessage("用户名不存在！");
        }
        //对密码进行加密处理
        password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username,password);
        if(user==null){
            return ServerResponse.createByErrorMessage("密码错误!");
        }
        //处理密码
        user.setPassword(StringUtils.EMPTY);

        return ServerResponse.createBySuccess(user);

    }

    @Override
    public ServerResponse<String> register(User user) {
//        int resultCount = userMapper.checkUsername(user.getUsername());
//        if(resultCount>0){
//            return ServerResponse.createByErrorMessage("用户名已存在！");
//        }
        ServerResponse validResponse = this.checkValid(user.getUsername(),Const.STRING_USERNAME);
        if(!validResponse.isSuccess()){
            return validResponse;
        }
//        resultCount = userMapper.checkEmail(user.getEmail());
//        if(resultCount>0){
//            return ServerResponse.createByErrorMessage("邮箱名已经存在！");
//        }
        validResponse = this.checkValid(user.getEmail(),Const.STRING_EMAIL);
        if(!validResponse.isSuccess()){
            return validResponse;
        }

        user.setRole(Const.Role.ROLE_CUSTOMER);
        //对密码进行md5加密处理
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        int resultCount=userMapper.insert(user);
        if(resultCount==0){
            return ServerResponse.createByErrorMessage("注册失败！");
        }
        return ServerResponse.createBySuccessMsg("注册成功");
    }

    @Override
    public ServerResponse<String> checkValid(String str, String type) {
        if(StringUtils.isNotBlank(type)){
            int resultCount=0;
            //开始校验
            if(Const.STRING_USERNAME.equals(type)){
                 resultCount= userMapper. checkUserName(str);
                if(resultCount>0){
                    return ServerResponse.createByErrorMessage("用户名已经存在！");
                }
            }

            if(Const.STRING_EMAIL.equals(type)){
                resultCount=userMapper.checkEmail(str);
                if(resultCount>0){
                    return ServerResponse.createByErrorMessage("邮箱已经存在！");
                }
            }

        }else{
            return ServerResponse.createByErrorMessage("参数错误");
        }

        return ServerResponse.createBySuccessMsg("校验成功！");
    }


    @Override
    public ServerResponse<String> selectQuestion(String username){
        ServerResponse validResponse = this.checkValid(username,Const.STRING_USERNAME);
        if(validResponse.isSuccess()){
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if(StringUtils.isNotBlank(question)){
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessage("找回问题密码是空的！");
    }

    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int resultCount = userMapper.checkAnswer(username,question,answer);
        if(resultCount>0){
            //说明用户问题和答案正确，返回一个token
            String forgetToken = UUID.randomUUID().toString();
            //放到本地cache中
            TockenCache.setKey(TockenCache.TOKEN_PREFIX+username,forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMessage("问题答案错误");
    }

    @Override
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        //校验用户名
        ServerResponse validResponse = this.checkValid(username,Const.STRING_USERNAME);
        if(validResponse.isSuccess()){
            return validResponse;
        }
        //判断并校验tocken
        if(StringUtils.isBlank(forgetToken)){
            return ServerResponse.createByErrorMessage("参数错误，token需要传递");
        }

        String token = TockenCache.getKey(TockenCache.TOKEN_PREFIX+username);

        if(StringUtils.isBlank(token)){
            return ServerResponse.createByErrorMessage("token无效或过期");
        }
        //比较
        if(StringUtils.equals(token,forgetToken)){
           //更新密码
            String md5Password=MD5Util.MD5EncodeUtf8(passwordNew);
            int resultCount = userMapper.updatePasswordByUsername(username,md5Password);
            if(resultCount>0){
                //todo 校验成功后 让token失效

             return ServerResponse.createBySuccessMsg("修改密码成功！");
            }
        }else{

            return ServerResponse.createByErrorMessage("token已经过期！请重新获取重置密码的token");
        }
        return ServerResponse.createByErrorMessage("修改失败！");
    }


    @Override
    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user) {
        //为了防止横向越权，要校验一下用户的旧密码，一直要指定是这个用户，因为我们会查询一个count（1）
        // ，如果暴不指定id，那么结果就是true count>0
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld),user.getId());

        if(resultCount==0){
            return ServerResponse.createByErrorMessage("密码错误！");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        resultCount = userMapper.updateByPrimaryKeySelective(user);
        if(resultCount>0){
            return ServerResponse.createBySuccessMsg("密码更新成功！");
        }else{
            return ServerResponse.createByErrorMessage("密码更新失败");
        }
    }

    @Override
    public ServerResponse<User> updateInformation(User user) {
        // 用户名不能更改，还要对email进行校验（不包含自己）
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(),user.getId());
        if(resultCount>0){
            return ServerResponse.createByErrorMessage("邮箱已存在，请更换后再试！");
        }

        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        updateUser.setEmail(user.getEmail());

        resultCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if(resultCount>0){
            return ServerResponse.createBySuccess("用户信息修改成功！",updateUser);
        }

        return ServerResponse.createByErrorMessage("用户信息更新失败");
    }

    @Override
    public ServerResponse<User> getInformation(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if(user==null){
            return ServerResponse.createByErrorMessage("找不到当前用户！");
        }
        user.setPassword(StringUtils.EMPTY);

        return ServerResponse.createBySuccess(user);
    }


    //backend
    @Override
    public   ServerResponse checkAdminRole(User user){
        if(user!=null&&user.getRole().intValue()==1){
            return ServerResponse.createBySuccess();
        }else{
            return ServerResponse.createByError();
        }

    }


}
