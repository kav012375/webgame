package com.wulin.web.controller.data;

import com.wulin.biz.common.service.CheckService;
import com.wulin.biz.role.service.RoleDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by zeusw on 2016/11/12.
 */
@Controller
@RequestMapping(value = "/data")
public class DataAction {
    @Autowired
    CheckService checkService;
    @Autowired
    RoleDataService roleDataService;
    private static Logger logger = LoggerFactory.getLogger("CONSOLE");
    @RequestMapping(value = "getname.do")
    public String getRandomName(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            HttpSession httpSession){
        String name = roleDataService.getRandomName();
        try {
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.getWriter().println(name);
        }catch (Exception e){
            logger.error(e.toString());
            e.printStackTrace();
        }

        return null;
    }

}
