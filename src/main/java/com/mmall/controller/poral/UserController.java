package com.mmall.controller.poral;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpSession;

/**
 * Created by Administrator on 2017-12-4.
 */
@Controller
@RequestMapping("/user/")
//定义了一个用户接口目录
public class UserController {

    @Autowired
    private IUserService iUserService;
    /**
    *用户登录
     * @param username
     * @param password
     * @param session
     * @return
    **/
    @RequestMapping(value ="login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login (String username, String password, HttpSession session){
        //service-->mybatis-->dao
        ServerResponse<User> response = iUserService.login(username,password);
        if(response.isSuccess()){
                session.setAttribute(Const.CURRENT_USER,response.getData());
        }
         return response;
    }
    /**
     *用户登出
     * */
    @RequestMapping(value = "logout.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session){
      session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }
    /**
    用户注册
     */
    @RequestMapping(value = "register.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user){
        return iUserService.register(user);

    }
    /**
     * 用户校验
     */
    @RequestMapping(value = "check_valid.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str, String type){
        return iUserService.checkValid(str,type);

    }
    /**
     * 获取用户信息
     */
    @RequestMapping(value = "get_user_info.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user != null){
              return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorMessage("用户未登陆，无法获取当前用户信息！");
    }

    /**
     * 忘记密码找回问题
     */
    @RequestMapping(value = "forget_get_question.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username){
        return iUserService.selectQuestion(username);
    }

    /**
     * 忘记密码检验答案
     */
    @RequestMapping(value = "forget_check_answer.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer){

        return iUserService.checkAnswer(username,question,answer);
    }

    /**
     * 忘记密码的重设密码
     */
    @RequestMapping(value = "forget_reset_password.do",method = RequestMethod.POST)
    @ResponseBody
     public ServerResponse<String> ForgetResetPassword(String username, String passwordNew, String forgetToken){
            return iUserService.forgetResetPassword(username,passwordNew,forgetToken);
     }

    /**
     * 登录中状态重置密码
     */
    @RequestMapping(value = "reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpSession session, String passwordOld, String passwordNew){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
               return ServerResponse.createByErrorMessage("用户未登陆");
        }

        return iUserService.resetPassword(passwordOld,passwordNew,user);
    }

    /**
     * 更新个人信息
     */
    @RequestMapping(value = "update_information.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> update_information(HttpSession session, User user){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser == null){
               return ServerResponse.createByErrorMessage("用户未登陆");
        }
        user.setId(currentUser.getId());
        //为了防止越权问题
        ServerResponse<User> response = iUserService.updateInformation(user);
        if(response.isSuccess()){//如果response成功更新用户个人信息则
            response.getData().setUsername(currentUser.getUsername());
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return  response;//如果 response错误则返回到原来那个位置

    }

    /**
     * 获取用户信息
     */
    @RequestMapping(value = "get_information.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> get_information(HttpSession session){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser == null){
            return  ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登陆，需要强制登陆status = 10");
            //这里设置了当ResponCode为10的时候，前台强制登陆
        }
        return iUserService.getInfomation(currentUser.getId());
    }
}
