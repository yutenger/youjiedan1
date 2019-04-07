package com.stardai.manage.bean;

import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.stardai.manage.constants.OssConstants;

import net.sf.json.JSONObject;

@Component
@SuppressWarnings("all")
public class OssService {
public JSONObject getOssSignature(){
	JSONObject jobject=new JSONObject();
	jobject.accumulate("accessid", OssConstants.accessid);
	jobject.accumulate("host", "http://"+OssConstants.bucket+"."+OssConstants.endpoint);
	jobject.accumulate("dir", OssConstants.dir);
	OSSClient client = new OSSClient(OssConstants.endpoint, OssConstants.accessid, OssConstants.accessKey);
	try{
	long expireTime = 30;
	long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
    Date expiration = new Date(expireEndTime);
    PolicyConditions policyConds = new PolicyConditions();
    policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
    policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, OssConstants.dir);
    String postPolicy = client.generatePostPolicy(expiration, policyConds);
    byte[] binaryData = postPolicy.getBytes("utf-8");
    String encodedPolicy = BinaryUtil.toBase64String(binaryData);
    String postSignature = client.calculatePostSignature(postPolicy);
    jobject.accumulate("signature", postSignature);
    jobject.accumulate("policy", encodedPolicy);
	jobject.accumulate("expire", String.valueOf(expireEndTime / 1000));
	}catch(Exception e){
		e.printStackTrace();
		return null;
	}finally{
		client.shutdown();
	}
	return jobject;
}
}
