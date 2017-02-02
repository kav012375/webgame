package com.wulin.biz.common.service;

import javax.servlet.http.HttpSession;

/**
 * Created by fengguoyang on 17/1/29.
 */
public interface SmsSendService {
    /**
     * 发送短信验证码
     * @param userName 用户名
     * @param vCode 验证码
     * @param mobileNumber 手机号码
     * @param httpSession 当前用户session
     * @return
     */
    String sendVerifyCode(String userName, String vCode, String mobileNumber, String ReqIp);
    boolean isMobileVerifyCodeVaild(String vcode,String userName);
}
