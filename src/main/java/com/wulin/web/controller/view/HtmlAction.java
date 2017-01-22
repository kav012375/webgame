package com.wulin.web.controller.view;

import com.wulin.dal.Prosession.dao.ProsessionDAO;
import com.wulin.dal.Role.dao.RoleDAO;
import com.wulin.dal.Prosession.entity.ProsessionDO;
import com.wulin.biz.user.service.UserService;
import com.wulin.biz.common.constants.SessionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by zeusw on 2016/10/23.
 */
@Controller
@RequestMapping(value = "/html")
public class HtmlAction {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleDAO roleDAO;
    @Autowired
    private ProsessionDAO prosessionDAO;

    @RequestMapping(value = "main")
    public ModelAndView MainPage(HttpSession httpSession){
        ModelAndView mav = new ModelAndView();
        mav.addObject("pageTitle","热血魔兽");
        mav.setViewName("mainpage");
        try{
            //判断用户是否有角色，如果没有就跳转到角色创建页面
            String loginUserAcct = (String) httpSession.getAttribute(SessionEnum.CurrentLoginUserAcct.getEnumKey());
            if(roleDAO.checkIfHaveRoleViaUserAcct(loginUserAcct) == 0){
                return createRolePage();
            }
            return mav;
        }catch (Exception e){
            e.printStackTrace();
            mav.addObject("pageTitle","系统异常");
            return mav;
        }
    }
    @RequestMapping(value = "login")
    public ModelAndView LoginPage(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/user/login");
        mav.addObject("pageTitle","热血魔兽-登陆");
        mav.addObject("gameTitle","热血魔兽");
        return mav;
    }
    @RequestMapping(value = "register")
    public ModelAndView RegisterPage(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/user/register");
        mav.addObject("gameTitle","热血魔兽");
        mav.addObject("pageTitle","热血魔兽-注册");
        return mav;
    }
    /**
     * @Date 2016/11/12 14:23
     * @Author guoyang.fgy
     * 创建角色请求，如果有角色则直接进入游戏界面，如果没角色则返回角色创建界面，如果没登陆，就直接返回
     * @return
     */
    @RequestMapping(value = "createrole")
    public ModelAndView createRolePage(){
        ModelAndView mav = new ModelAndView();
        mav.addObject("pageTitle","热血魔兽-创建角色");
        mav.setViewName("/role/createrole");
        try{
            //获取职业列表
            List<ProsessionDO> proList = prosessionDAO.getProsessionList();
            mav.addObject("proList",proList);
            return mav;
        }catch (Exception e){
            mav.addObject("pageTitle","系统异常！");
            e.printStackTrace();
            return mav;
        }

    }
}
