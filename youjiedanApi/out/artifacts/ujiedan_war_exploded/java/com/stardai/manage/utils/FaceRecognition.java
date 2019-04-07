/**
 * 
 */
package com.stardai.manage.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stardai.manage.request.RequestPersonVerifyInfo;
import com.stardai.manage.utils.BaiDuHttpUtil;
import com.stardai.manage.utils.GsonUtils;
import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * @author 29271 2018年6月20日
 */
public class FaceRecognition {

	static ObjectMapper MAPPER = new ObjectMapper();
	
	public static boolean personverify(RequestPersonVerifyInfo requestPersonVerifyInfo) {
		boolean result = false;
		// 请求url
		String url = "https://aip.baidubce.com/rest/2.0/face/v3/person/verify";
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("image", requestPersonVerifyInfo.getImage());
			map.put("image_type", "URL");
			map.put("id_card_number", requestPersonVerifyInfo.getIdCardNumber());
			map.put("liveness_control", "NORMAL");
			map.put("name", requestPersonVerifyInfo.getName());
			map.put("quality_control", "NORMAL");

			String param = GsonUtils.toJson(map);

			// 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间，
			// 客户端可自行缓存，过
			// 期后重新获取。
			String accessToken = GetAccessToken.getAuth();;
			if(StringUtils.isBlank(accessToken)){
				return false;
			}

			String jsonTree = BaiDuHttpUtil.post(url, accessToken, "application/json", param);
			
			JsonNode jsonNode = MAPPER.readTree(jsonTree);
			//int score = (new Double(Double.parseDouble(jsonNode.get("result").get("score").toString()))).intValue();
			if(Integer.parseInt(jsonNode.get("error_code").toString()) == 0 &&
					(new Double(Double.parseDouble(jsonNode.get("result").get("score").toString()))).intValue() >= 80){
				result = true;
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}

