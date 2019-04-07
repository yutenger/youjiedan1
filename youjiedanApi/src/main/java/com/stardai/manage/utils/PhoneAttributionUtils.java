/**
 * 
 */
package com.stardai.manage.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



/**
 * @author Administrator
 * 2018年5月2日
 */
@Component
@SuppressWarnings("all")

public class PhoneAttributionUtils {

	ObjectMapper MAPPER = new ObjectMapper();
	
	
	public String getPhoneAttribution(String phone) {
	    String host = "http://mobileas.market.alicloudapi.com";
	    String path = "/mobile";
	    String method = "GET";
	    String appcode = "cdd71003b13e4fa49d29a957b44dd28e";
	    Map<String, String> headers = new HashMap<String, String>();
	    //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
	    headers.put("Authorization", "APPCODE " + appcode);
	    Map<String, String> querys = new HashMap<String, String>();
	    querys.put("mobile", phone);
	    String attr = "其他";

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
	    	HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
	    	String jsonTree = EntityUtils.toString(response.getEntity());

			//转码
			JsonNode jsonNode = MAPPER.readTree(jsonTree);
			
			if (Integer.parseInt(jsonNode.get("error_code").toString()) == 0) {
				String city = jsonNode.get("result").get("city").toString();
				if(StringUtils.isNotBlank(city)){
					int length = jsonNode.get("result").get("city").toString().length();
					attr = jsonNode.get("result").get("city").toString().substring(1, length-1) + "市";
				}
				
			} 	
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
		return attr;
	}
}
