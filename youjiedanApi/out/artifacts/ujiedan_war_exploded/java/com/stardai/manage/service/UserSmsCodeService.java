package com.stardai.manage.service;

import java.util.HashMap;
import java.util.Random;
import cn.jsms.api.SendSMSResult;
import com.stardai.manage.utils.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.aliyun.oss.ClientException;
import com.stardai.manage.response.ResponseSms;
import com.stardai.manage.mapper.UserSmsCodeMapper;
import com.stardai.manage.pojo.UserSmsCode;

/**
 * @author jdw
 * @date 2017/10/16
 */
@Service
@SuppressWarnings("all")
public class UserSmsCodeService {

	// 产品名称:云通信短信API产品,开发者无需替换
	private static String PRODUCT;

	// 产品域名,开发者无需替换
	private static String DOMAIN;

	// 阿里云API的密钥Access Key ID
	public static String ACCESS_KEY_ID;

	// 阿里云API的密钥Access Key Secret
	public static String ACCESS_KEY_SECRET;

	// 短信模板code
	public static String TemplateCode;

	// APP名称
	public static String APPNAME;

	// 初始化属性
	static {
		PRODUCT = AliSMSConstants.PRODUCT;
		DOMAIN = AliSMSConstants.DOMAIN;
		ACCESS_KEY_ID = AliSMSConstants.ACCESS_KEY_ID;
		ACCESS_KEY_SECRET = AliSMSConstants.ACCESS_KEY_SECRET;
		TemplateCode = AliSMSConstants.TemplateCode;
		APPNAME = AliSMSConstants.APPNAME;
	}

	@Autowired
	private UserSmsCodeMapper userSmsCodeMapper;

	// 验证码查询
	public UserSmsCode querySmsCode(String mobileNumber) {
		return userSmsCodeMapper.querySmsCode(mobileNumber);
	}

	// 极光发送短信验证
	public Integer sendSmsCode(String mobileNumber, String appName) throws ClientException, Exception {
		UserSmsCode record = new UserSmsCode();
		String verifyCode = String.valueOf((new Random()).nextInt(9000) + 1000);
		UserSmsCode smsCode = this.querySmsCode(mobileNumber);
		Long createTime = System.currentTimeMillis();
		if (smsCode == null) {
			userSmsCodeMapper.addSmsCodeBymoblie(mobileNumber, verifyCode, createTime);
		} else {
			Integer updateSmsCode = userSmsCodeMapper.updateSmsCode(mobileNumber, verifyCode, createTime);
		}
		//使用极光发送短信验证码
		SendSMSResult ssr = JiguangSMS.testSendTemplateSMS(mobileNumber,verifyCode,152717);

		if (StringUtils.isNotBlank(ssr.getMessageId())) {
			// 请求成功
			return 1;
		} else {
			//先查下该手机号码有没有在数据表里
			String isExist = userSmsCodeMapper.isOnceSaved(mobileNumber);
			if(StringUtils.isBlank(isExist)){
				// 极光发送短信不成功的话，使用云片发送短信验证，并在数据库记下时间，以便及时发现
				userSmsCodeMapper.addUnNaturalMessage(mobileNumber);
			}
			//云片发送短信
			//SmsCode recordYunPian = new SmsCode();
			String content = "【" + appName + "】欢迎使用" + appName + "，您的手机验证码是" + verifyCode + "。本条信息无需回复！";
			HashMap<String, String> requestParams = new HashMap<>();
			requestParams.put("apikey", "4e2bc3e7d93e529e99a25cf43f8a5a90");
			requestParams.put("text", content);
			requestParams.put("mobile", mobileNumber);
			String resJson = HttpUtil.requestByPost(requestParams, "https://sms.yunpian.com/v1/sms/send.json");
			ResponseSms smsResponse = GsonUtil.toObject(resJson, ResponseSms.class);
			if (smsResponse.getCode() == 22) {
				return 2;// 2表示"同一手机号1小时内短信发送次数不能超过3次"
			} else if (smsResponse.getCode() != 0) {
				return 3;// 3用来统一表示各种未知错误
			} else {
				return 1;// 1表示发送成功
			}

		}

	}

}
