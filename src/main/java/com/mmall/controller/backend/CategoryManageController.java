package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/manage/category/")
public class CategoryManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;

    //新增種類
    @RequestMapping("add_category.do")
    @ResponseBody
    public ServerResponse addCategory(HttpSession session,String categoryName,@RequestParam(value = "parentId",defaultValue="0") int parentId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用戶未登錄，請登錄");

        }
        //校驗一下是否是管理員
        if(iUserService.checkAdminRole(user).isSuccess()){
            //是管理員 處理分類
            return iCategoryService.addCategory(categoryName,parentId);
        }else{
            return  ServerResponse.createByErrorMessage("需管理員權限");
        }
    }
    //修改種類
    @RequestMapping("set_category_name.do")
    @ResponseBody
    public  ServerResponse setCategoryName(HttpSession session,Integer categoryId,String categoryName){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用戶未登錄，請登錄");

        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //更新categoryName
            return  iCategoryService.updateCategoryName(categoryId,categoryName);
        }else{
            return  ServerResponse.createByErrorMessage("需管理員權限");
        }
    }
    //取得水平種類
    @RequestMapping("get_category.do")
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(HttpSession session,@RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用戶未登錄，請登錄");

        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //查詢子節點的category資訊，無遞迴，水平
            return  iCategoryService.getChildrenParallelCategory(categoryId);
        }else{
            return  ServerResponse.createByErrorMessage("需管理員權限");
        }
    }
    //取得垂直種類
    @RequestMapping("get_deep_category.do")
    @ResponseBody
    public  ServerResponse getCategoryAndDeepChildrenCategory(HttpSession session,@RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId)
        {
            User user = (User) session.getAttribute(Const.CURRENT_USER);
            if (user == null) {
                return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用戶未登錄，請登錄");

            }
            if (iUserService.checkAdminRole(user).isSuccess()) {
                //查詢當前節點id 和所有深處子節點
                return  iCategoryService.selectCategoryAndChildrenById(categoryId);
            } else {
                return ServerResponse.createByErrorMessage("需管理員權限");
            }
        }
    }