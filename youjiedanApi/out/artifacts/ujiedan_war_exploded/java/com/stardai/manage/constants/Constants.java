package com.stardai.manage.constants;

/**
 * 
 * @author Tina
 * 2018年6月13日
 */
public class Constants {
	// 设置订单状态为被抢时，需要有key值验证
	public static final String UPDATE_STATUS_KEY = "e033f5d7af85dc20b8bb75fa101bdaf4";
	
	//人脸识别获取Access Token 调用的服务地址
	public static final String GET_ACCESS_TOKEN = "https://aip.baidubce.com/oauth/2.0/token";
	
	//调用上个地址需要的参数值
	public static final String GRANT_TYPE = "client_credentials";

	//调用百度云人脸识别使用的API Key
	public static final String API_KEY = "uqkApRfLrqeYNk71Fc0GEU1h";
	
	//调用百度云人脸识别使用的Secret Key
	public static final String 	SECRET_KEY = "0UnQHoM5cI2vEr0lxEW29l6oqGoZKMRl";

	//黑名单处罚措施--只能验证码登录
	public static final Integer PutInBlackForVerifyCodeLogin = 1001;

	//黑名单处罚措施--取消公司认证
	public static final Integer  PutInBlackForCancelApprove = 1002;

	//黑名单处罚措施--封号
	public static final Integer  PutInBlackForForbidLogin = 1003;
}
