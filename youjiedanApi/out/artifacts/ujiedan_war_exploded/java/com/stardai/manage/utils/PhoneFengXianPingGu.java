package com.stardai.manage.utils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


//手机风险评估
@SuppressWarnings("all")
public class PhoneFengXianPingGu {
	
	public  static boolean PhoneFengXianPingGu(String phoneNumber) {
		
		
		String host = "https://rain.market.alicloudapi.com";
	    String path = "/alipay/security/risk/rainScore/query.json";
	    String method = "POST";
	    String appcode = "3fd3e8c1243b4301978be51628406cca";
	    Map<String, String> result = new HashMap<String, String>();
	    //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
	    result.put("Authorization","APPCODE " + appcode);
	    //根据API的要求，定义相对应的Content-Type
	    result.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
	    Map<String, String> querys = new HashMap<String, String>();
	    Map<String, String> bodys = new HashMap<String, String>();
	    bodys.put("account",phoneNumber);
	    bodys.put("account_type","MOBILE_NO");
	    bodys.put("version", "2.0");


	    try {
	    	/**
	    	* 重要提示如下:
	    	* HttpUtils请从
	    	* https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
	    	* 下载
	    	*
	    	* 相应的依赖请参照
	    	* https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
	    	*/
	    	HttpResponse response =  PhoneFengXianPingGuUtils.doPost(host, path, method, result, querys, bodys);
	    	String body = EntityUtils.toString(response.getEntity());
	    	JSONObject event = JSON.parseObject(body);
	    	if ( (boolean) event.get("success")) {
	    		JSONObject data = JSON.parseObject(event.get("model").toString());
	    		BigDecimal bigDecimal = data.getBigDecimal("score");
	    		int value = bigDecimal.intValue();
	    		if(value>60){
	    			return false;
	    		}else {
					return true;
				}
	    	}
	    	//获取response的body
	    	//System.out.println(EntityUtils.toString(response.getEntity()));
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
		return false;
		
	}

}
