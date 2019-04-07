/**
 * 
 */
package com.stardai.manage.utils;
import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.ServiceHelper;
import cn.jiguang.common.connection.ApacheHttpClient;
import cn.jiguang.common.resp.ResponseWrapper;
import cn.jsms.api.SendSMSResult;
import cn.jsms.api.ValidSMSResult;
import cn.jsms.api.account.AccountBalanceResult;
import cn.jsms.api.account.AppBalanceResult;
import cn.jsms.api.common.model.BatchSMSPayload;
import cn.jsms.api.common.model.BatchSMSResult;
import cn.jsms.api.common.model.RecipientPayload;
import cn.jsms.api.schedule.model.ScheduleResult;
import cn.jsms.api.schedule.model.ScheduleSMSPayload;
import cn.jsms.api.template.SendTempSMSResult;
import cn.jsms.api.template.TempSMSResult;
import cn.jsms.api.template.TemplatePayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jsms.api.common.SMSClient;
import cn.jsms.api.common.model.SMSPayload;

import java.util.ArrayList;
import java.util.List;
/**
 * @author 29271
 * 2018年6月22日
 */
public class JiguangSMS {
	protected static final Logger LOG = LoggerFactory.getLogger(JiguangSMS.class);

    private static final String appkey = "86b153c4e333a8cd359e4a58";
    private static final String masterSecret = "f4e803d130558fece7c93c07";

    private static final String devKey = "242780bfdd7315dc1989fedb";
    private static final String devSecret = "2f5ced2bef64167950e63d13";
    
    public static void main(String[] args) {
//    	testSendSMSCode();
        //testSendTemplateSMS();
    }

    //验证码类短信，需要客户端调极光的接口进行验证
    public static void testSendSMSCode() {
    	SMSClient client = new SMSClient(masterSecret, appkey);
    	SMSPayload payload = SMSPayload.newBuilder()
				.setMobileNumber("18375337360")
				.setTempId(1)
				.build();
    	try {
			SendSMSResult res = client.sendSMSCode(payload);

			LOG.info(res.toString());
		} catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Message: " + e.getMessage());
        }
    }


    //通知类短信，可自己生成验证码验证
	public static SendSMSResult testSendTemplateSMS(String phone,String vertifyCode,int tempId) {
	    SMSClient client = new SMSClient(masterSecret, appkey);
        SendSMSResult res = new SendSMSResult();
        SMSPayload payload = SMSPayload.newBuilder()
                .setMobileNumber(phone)
                .setTempId(tempId)
                .addTempPara("code", vertifyCode)
                .build();
        try {
            res = client.sendTemplateSMS(payload);
            LOG.info(res.toString());
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Message: " + e.getMessage());
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
        }
        return res;


    }

}
