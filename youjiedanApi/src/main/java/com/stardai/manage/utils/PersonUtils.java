package com.stardai.manage.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * @author jdw
 * @date 2017/10/16
 */
@Component
@SuppressWarnings("all")
public class PersonUtils {

	ObjectMapper MAPPER = new ObjectMapper();

	//身份证号与姓名二要素认证
	public Map approvePerson(String name, String idCard) {
		String host = "http://idcard.market.alicloudapi.com";
		String path = "/lianzhuo/idcard";
		String method = "GET";
		String appcode = "cdd71003b13e4fa49d29a957b44dd28e";
		Map<String, String> headers = new HashMap<String, String>();
		// 最后在header中的格式(中间是英文空格)为Authorization:APPCODE
		// 83359fd73fe94948385f570e3c139105
		headers.put("Authorization", "APPCODE " + appcode);
		Map<String, String> querys = new HashMap<String, String>();
		querys.put("cardno", idCard);
		querys.put("name", name);

		try {
			/**
			 * 重要提示如下: HttpUtils请从
			 * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/
			 * src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java 下载
			 *
			 * 相应的依赖请参照
			 * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/
			 * pom.xml
			 */
			HttpResponse response = PersonHttpUtils.doGet(host, path, method, headers, querys);
			// 获取response的body
			// System.out.println(EntityUtils.toString(response.getEntity()));
			String jsonTree = EntityUtils.toString(response.getEntity());

			//转码
			//JsonNode jsonNode = MAPPER.readTree(new String(jsonTree.getBytes("ISO-8859-1")));
			JsonNode jsonNode = MAPPER.readTree(jsonTree);
			
			if (Integer.parseInt(jsonNode.get("resp").get("code").toString()) == 0) {
				JsonNode jsonNode2 = jsonNode.get("data");
				Map map = new HashMap();
				map.put("sex", jsonNode2.get("sex").toString().replaceAll("\"", ""));
				map.put("address", jsonNode2.get("address").toString().replaceAll("\"", ""));
				return map;
			} else {
				return new HashMap();
			}
		} catch (Exception e) {
			return new HashMap();
		}
	}

	/**
	 * @Author : jokery
	 * @Description : 身份证号与姓名和手机号三要素认证
	 * @Date : 2018/3/5 15:01
	 * @Param :name 信贷经理姓名
	* @Param :idCard 信贷经理身份证号
	* @Param :mobileNumber 信贷经理注册用的手机号
	 * @Return :
	 */
	public HashMap<String,String> approvePersonByMobileNumber(String name,String idCard,String mobileNumber) {
		String host = "http://telecom-ali.juheapi.com";
		String path = "/telecom/query";
		String method = "GET";
		String appcode = "cdd71003b13e4fa49d29a957b44dd28e";
		Map<String, String> headers = new HashMap<String, String>();
		//最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
		headers.put("Authorization", "APPCODE " + appcode);
		Map<String, String> querys = new HashMap<String, String>();
		querys.put("idcard",idCard);
		querys.put("mobile",mobileNumber);
		querys.put("realname",name);

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
			//获取response的body
			String jsonTree = EntityUtils.toString(response.getEntity());
			JsonNode jsonNode = MAPPER.readTree(jsonTree);
			if (Integer.parseInt(jsonNode.get("error_code").toString()) == 0) {
				JsonNode jsonNode2 = jsonNode.get("result");
				HashMap map = new HashMap<String,String>();
				map.put("res", jsonNode2.get("res").toString().replaceAll("\"", ""));
				map.put("resmsg", jsonNode2.get("resmsg").toString().replaceAll("\"", ""));
				return map;
			}else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
