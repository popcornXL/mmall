package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import net.sf.jsqlparser.schema.Server;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;
    /**
     * 用戶登入
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session){
        ServerResponse<User> response = iUserService.login(username,password);
        if(response.isSuccess()){
            System.out.println("使用者: "+Const.CURRENT_USER+"! user 資料!!"+response.getData());
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
            return response;
    }

    //用戶登出
    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session){
        session.removeAttribute(Const.CURRENT_USER);
        return  ServerResponse.createBySuccess();
    }

    //用戶註冊
    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    @ResponseBody
    public  ServerResponse<String> register(User user){
        return iUserService.register(user);
    }

    //校驗功能
    @RequestMapping(value = "check_valid.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String>  checkValid(String str, String type){
        return  iUserService.checkValid(str,type);
    }

    //獲得登入用戶資料
    @RequestMapping(value = "get_user_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);

        if(user != null)
        {
            return  ServerResponse.createBySuccess(user);
        }
        return  ServerResponse.createByErrorMessage("用戶未登入無法獲得當前用戶資料");
    }

    //忘記密碼
    @RequestMapping(value = "forget_get_question.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username){
        return  iUserService.selectQuestion(username);
    }

    //使用本地緩存 檢查問題答案
    @RequestMapping(value = "forget_check_answer.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username,String question,String answer){
        return  iUserService.checkAnswer(username,question,answer);
    }

    //未登入重置密碼
    @RequestMapping(value = "forget_reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken){
        return  iUserService.forgetResetPassword(username,passwordNew,forgetToken);
    }

    //登入重置密碼
    @RequestMapping(value = "reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpSession session,String passwordOld,String passwordNew){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorMessage("用戶未登錄");
        }
        return iUserService.resetPassword(passwordOld,passwordNew,user);
    }

    //更新個人資料
    @RequestMapping(value = "update_information.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> update_information(HttpSession session,User user){
        User current_User = (User)session.getAttribute(Const.CURRENT_USER);
        if(current_User ==null){
            return ServerResponse.createByErrorMessage("用戶未登錄");
        }
        user.setId(current_User.getId());//從session取id
        user.setUsername(current_User.getUsername());//從session取用戶名
        ServerResponse<User> response = iUserService.updateInformation(user);
        if(response.isSuccess()){
            response.getData().setUsername(current_User.getUsername());
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }

    //獲得用戶資訊
    @RequestMapping(value = "get_information.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> get_information(HttpSession session){
        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);
        if(currentUser ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登錄，需要強制登錄status=10");
        }
        return  iUserService.getInformation(currentUser.getId());
    }

}
