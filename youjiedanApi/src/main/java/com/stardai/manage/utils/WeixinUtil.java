package com.stardai.manage.utils;

import com.alibaba.fastjson.JSONObject;
import okhttp3.FormBody;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * 微信工具类
 * @author yax
 */
@Component
public class WeixinUtil {
	//网页授权access_token
private static String accessTokenUrl="https://api.weixin.qq.com/sns/oauth2/access_token";
   //获取微信用户信息
private static String userInfoUrl="https://api.weixin.qq.com/sns/userinfo";
   //微信发送模板信息
private static String msgSendUrl="https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";
   //微信基础接口的access_token
private static String baseAccessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=";
  //微信分享票据接口
private static String ticketUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=";
//获取所有用户的微信openId
private static String allOpenId="https://api.weixin.qq.com/cgi-bin/user/get?access_token=";
//private static String appid="wx3b17d8e2e4c5b6e4";//测试appid
//private static String secret="bc5d4c13b0efa226b8b0d642f7bdf48e";//测试密钥
private static String noncestr = "zyq";
private static String token="wechat";
private static String appid="wxdfd3dbe1813c4259";
private static String secret="bfbd32207163e508fd340ea199583236";
/**
 * @param code
 * @return
 * @throws ClientProtocolException
 * @throws IOException
 * 获取微信openId和accessToken
 */
public static JSONObject getOpenId(String code) throws ClientProtocolException, IOException{
	FormBody formBody = new FormBody.Builder()
			.add("appid", appid)
			.add("secret", secret)
			.add("code",code)
			.add("grant_type","authorization_code")
			.build();
	 String result =HttpUtil2.post(accessTokenUrl,formBody);
	 JSONObject jobject= JSONObject.parseObject(result);
	return jobject;
}

	/**
	 * 校验签名
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @return
	 */
public static boolean checkSignature(String signature, String timestamp, String nonce){
	String[] arr = new String[] { token, timestamp, nonce };
	// 将token、timestamp、nonce三个参数进行字典序排序
	Arrays.sort(arr);
	StringBuilder content = new StringBuilder();
	for (int i = 0; i < arr.length; i++) {
		content.append(arr[i]);
	}
	String tmpStr = null;
	try {
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		// 将三个参数字符串拼接成一个字符串进行sha1加密
		byte[] digest = md.digest(content.toString().getBytes());
		tmpStr = Sha1Util.byteToStr(digest);
	} catch (NoSuchAlgorithmException e) {
		e.printStackTrace();
	}
	// 将sha1加密后的字符串可与signature对比，标识该请求来源于微信
	return tmpStr != null ? tmpStr.equals(signature.toUpperCase()) : false;
}




public static void main(String[] args){

}
}
