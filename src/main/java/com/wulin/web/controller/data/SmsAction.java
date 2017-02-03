package com.wulin.web.controller.data;

import com.wulin.biz.common.service.SecurityService;
import com.wulin.biz.common.service.SmsSendService;
import com.wulin.biz.core.user.constants.UserReturnCodeEnum;
import com.wulin.utils.HttpServletResponseOutputUtils;
import com.wulin.utils.VerifyCodeGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by fengguoyang on 17/1/30.
 */
@Controller
@RequestMapping(value = "/sms",method = RequestMethod.POST)
public class SmsAction {
    @Autowired
    SmsSendService smsSendService;
    @Autowired
    private SecurityService securityService;
    private static final Logger logger = LoggerFactory.getLogger("LOGGER");

    @RequestMapping(value = "/send")
    public void sendVcodeToUserMobileInRegister(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            HttpSession httpSession
    ) {
        String userName = httpServletRequest.getParameter("USER_ACCT");
        String vCode = httpServletRequest.getParameter("USER_VCODE");
        String mobileNumber = httpServletRequest.getParameter("USER_MOBILE");
        String ReqIp = securityService.GetRealIpAddr(httpServletRequest);
        try{
            if (userName == null || vCode == null || mobileNumber == null){
                HttpServletResponseOutputUtils.printMsgToWeb(
                        httpServletResponse,
                        "参数错误！");
                return;
            }
            //检查页面验证码是否正确
            if(vCode.toLowerCase().compareTo(httpSession.getAttribute("validateCode").toString().toLowerCase())!=0){
                HttpServletResponseOutputUtils.printMsgToWeb(
                        httpServletResponse,
                        UserReturnCodeEnum.USER_CCODE_IS_NULL_OR_INCORRECT.getIndex().toString());
                return;
            }
            //获取随机数
            String verifyCode = VerifyCodeGenerator.generateNormalVerifyCodeWithSixLength();
            //发送短信
            String reslut_code = smsSendService.sendVerifyCode(userName,verifyCode,mobileNumber,ReqIp);
            if (reslut_code == "OK"){
                HttpServletResponseOutputUtils.printMsgToWeb(
                        httpServletResponse,
                        "发送成功！");
                return;
            }
            HttpServletResponseOutputUtils.printMsgToWeb(
                    httpServletResponse,
                    "发送失败，请联系管理员！"
            );
            return;
        }catch (Exception e){
            logger.error("发送验证码错误，用户名："+userName+",手机号码为："+mobileNumber);
            return;
        }

    }
}
