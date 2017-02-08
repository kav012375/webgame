package com.wulin.biz.common.service.impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sms.model.v20160927.SingleSendSmsRequest;
import com.aliyuncs.sms.model.v20160927.SingleSendSmsResponse;
import com.wulin.biz.common.service.SmsSendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by fengguoyang on 17/1/29.
 */
public class SmsSendImpl implements SmsSendService {

    private final String accessKeyId = "LTAI9oMUXX0lLUHN";
    private final String accessKeySecret = "lzUSkbiubTmsyS2aZbiG9CmdqODw16";
    private final String regionId = "cn-hangzhou";
    private final String SignName = "武侠风云录";
    private final String TemplateCode = "SMS_44410039";

    private static final List<SmsVCodeAndUserNamePair> smsVCodeAndUserNamePairList
            =new ArrayList<SmsVCodeAndUserNamePair>();

    private static final Logger logger = LoggerFactory.getLogger("LOGGER");

    public String sendVerifyCode(String userName, String vCode, String mobileNumber, String ReqIp) {
        try {
            //检查是否在60s内重复发送验证码
            for(SmsVCodeAndUserNamePair s : smsVCodeAndUserNamePairList){
                if(s.getReqIp().equals(ReqIp)){
                    if(System.currentTimeMillis() - s.getSendTime() < 60000){
                        logger.error("发送验证码错误，用户名："+userName+",手机号码为："+mobileNumber+"错误原因：60s内连续发送验证码");
                        return "NOK60S";
                    }else{
                        smsVCodeAndUserNamePairList.remove(s);
                    }
                }
            }
            IClientProfile profile = DefaultProfile.getProfile(regionId,accessKeyId,accessKeySecret);
            DefaultProfile.addEndpoint(
                    "cn-hangzhou",
                    "cn-hangzhou",
                    "Sms",
                    "sms.aliyuncs.com");
            IAcsClient client = new DefaultAcsClient(profile);
            SingleSendSmsRequest request = new SingleSendSmsRequest();
            request.setSignName(SignName);
            request.setTemplateCode(TemplateCode);
            request.setParamString("{'userName':'"+userName+"','vcode':'"+vCode+"'}");
            request.setRecNum(mobileNumber);
            SingleSendSmsResponse httpResponse = client.getAcsResponse(request);
            if (httpResponse.getModel()!=null){
                //将已经发送的验证码存入List中
                SmsVCodeAndUserNamePair smsVCodeAndUserNamePair = new SmsVCodeAndUserNamePair();
                smsVCodeAndUserNamePair.setVcode(vCode);
                smsVCodeAndUserNamePair.setUserName(userName);
                smsVCodeAndUserNamePair.setSendTime(System.currentTimeMillis());
                smsVCodeAndUserNamePair.setReqIp(ReqIp);
                smsVCodeAndUserNamePairList.add(smsVCodeAndUserNamePair);
                return "OK";
            }else{
                logger.error("发送验证码错误，用户名："+userName+",手机号码为："+mobileNumber+"错误原因："+httpResponse.toString());
                return "NOK";
            }

        }catch (Exception e){
            e.printStackTrace();
            logger.error("发送验证码错误，用户名："+userName+",手机号码为："+mobileNumber+"错误原因："+e.getMessage());
            return "NOK";
        }

    }

    /**
     * 检查手机验证码是否有效
     * @param vcode
     * @param userName
     * @return 如果验证码有效，则返回true，如果无效返回false
     */
    public boolean isMobileVerifyCodeVaild(String vcode,String userName){
        for(SmsVCodeAndUserNamePair s:smsVCodeAndUserNamePairList){
            if(s.getUserName().equals(userName) ){
                if (s.getVcode().equals(vcode) ){
                    if(System.currentTimeMillis() - s.getSendTime()>120000){
                        smsVCodeAndUserNamePairList.remove(s);
                        return false;
                    }else{
                        smsVCodeAndUserNamePairList.remove(s);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private class SmsVCodeAndUserNamePair{
        String vcode;
        String UserName;
        Long sendTime;
        String reqIp;

        public String getVcode() {
            return vcode;
        }

        public void setVcode(String vcode) {
            this.vcode = vcode;
        }

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String userName) {
            UserName = userName;
        }

        public Long getSendTime() {
            return sendTime;
        }

        public void setSendTime(Long sendTime) {
            this.sendTime = sendTime;
        }

        public String getReqIp() {
            return reqIp;
        }

        public void setReqIp(String reqIp) {
            this.reqIp = reqIp;
        }
    }
}
