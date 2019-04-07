package com.stardai.manage.constants;

/**
 * 
 * @author Tina
 * 2018年6月13日
 */
public class Constants {
	// 设置订单状态为被抢时，需要有key值验证
	public static final String UPDATE_STATUS_KEY = "e033f5d7af85dc20b8bb75fa101bdaf4";

	//api查看单子卖出情况时，安全密钥
	//public  static  final String API_ChECK_RESULT_KEY = "eN9UCXLH1W5wPBr4";
	public  static  final String API_ChECK_RESULT_KEY = "80ae035566e5dcbd";
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

	//抽奖活动需要的星币
	public static final Double  MONEYFORLOTTERY = 10.0;

	//调用百度人脸识别的错误码,从图片的url下载 图片失败
	public  static  final int image_url_download_fail = 222204;

	//身份证号码正确，公安库里没有对应的图片
	public  static  final int  police_picture_not_exist = 222355;

	//公安网图片不存在或质量过低
	public  static  final int police_picture_is_none = 222350;

	//大数据查询调的借款端的接口(测试)
	//public static final String data_query = "https://apiloan.stardai.com/creditdata/api/query";

	//大数据查询调的借款端的接口（正式）
	public static final String data_query = "https://loan.stardai.com/creditdata/api/query";

	//大数据查询查询历史记录时的密钥
	public static final String QUERY_DATA_HISTORY = "zx";





}
