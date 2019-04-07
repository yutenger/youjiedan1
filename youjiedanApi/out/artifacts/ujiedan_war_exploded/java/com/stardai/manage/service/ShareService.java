package com.stardai.manage.service;

import com.stardai.manage.response.ResponseShareUrl;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.stardai.manage.mapper.PayMoneyMapper;
import com.stardai.manage.mapper.ShareMapper;
import com.stardai.manage.mapper.UserMapper;
import com.stardai.manage.response.ResponseShareUser;
import com.stardai.manage.utils.Base64Image;
import com.stardai.manage.utils.OssClientConstants;
import com.stardai.manage.utils.TwoDimensionCode;
import com.stardai.manage.utils.Utils;
import org.apache.commons.lang.StringUtils;

/**
 * @author jdw
 * @date 2017/10/16
 */
@Service
@SuppressWarnings("all")
public class ShareService {
	
	@Autowired
	private Base64Image base;

	@Autowired
	private ShareMapper shareMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private PayMoneyMapper payMoneyMapper;

	@Autowired
	private PayMoneyService payMoneyService;

	@Autowired
	private CouponService couponService;

	// 查询金额与邀请的人数
	public ResponseShareUser updateSharePerson(String userId, HttpServletRequest request) {
		ResponseShareUser result = shareMapper.updateSharePerson(userId);
		// 设置分享出去的链接
		String url = shareMapper.queryShareUrl(2);
		url = url + "?parameter=" + userId;
		String twoDimensionUrl = "";
		try{
			if (null == result) {
				//根据分享出去的链接生成二维码,把二维码图片传到oss上，获取oss上的链接
				twoDimensionUrl = this.getTwoDimensionCodeUrl(userId,url,request);
				// 没有数据添加一条
				shareMapper.addSharePerson(userId, url,twoDimensionUrl);
				ResponseShareUser re = new ResponseShareUser();
				re.setShareMoney(0);
				re.setShareCount(0);
				re.setShareGive(30);
				re.setShareUrl(url);
				re.setTwoDimensionUrl(twoDimensionUrl);
				return re;
			}else{
				twoDimensionUrl = result.getTwoDimensionUrl();
				if(StringUtils.isBlank(twoDimensionUrl)){
					//根据分享出去的链接生成二维码,把二维码图片传到oss上，获取oss上的链接
					twoDimensionUrl = this.getTwoDimensionCodeUrl(userId,url,request);
					result.setTwoDimensionUrl(twoDimensionUrl);
					//把这条信息更新数据库里
					shareMapper.updateTwoDimensionUrl(userId,twoDimensionUrl);
				}
			}
			result.setShareUrl(url);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
		
	}

	/**
	 * @throws Exception 
	 * @Author:Tina
	 * @Description:把分享的url转化成二维码图片，并把图片上传到oss获得一个二维码图片的url
	 * @Date:2018年5月29日上午9:31:01
	 * @Param:
	 * @Return:String
	 */
	private String getTwoDimensionCodeUrl(String userId,String url, HttpServletRequest request) throws Exception {
		//链接生成二维码之后，获取二维码图片的base64编码
		String twoDimensionBase64Code = TwoDimensionCode.createQRImageBuffer(url,200,200);
		//把图片上传到阿里云oss上，并返回图片的链接
		String imgurl = base.GenerateImageForTwoDimension(twoDimensionBase64Code, request, OssClientConstants.TWODIMENSIONCODE,userId);
		return imgurl;
	}



	// 添加数据
	public String addUserInfoForCoupon(String mobileNumber, String encodepassword, String tuiJianRenUserId
			,HttpServletRequest request) {
		String userId = Utils.getUUID();
		// 密码密文
		// 将注册的人的userId和推荐人的userId一起保存到用户信息表
		userMapper.addUserInfoAndSharedId(userId, encodepassword, mobileNumber, tuiJianRenUserId);
		// 将推荐人的表中的次数+1和赠送金额增加(需求修改,这时候不加了,要等实名认证通过再加)
		// shareMapper.updateShareUserMoney(tuiJianRenUserId);
		// 查询赠送的金额
		// double giveMoney = shareMapper.queryShareGive(tuiJianRenUserId);
		// 添加推荐信息
		// payMoneyMapper.addPayPresentMessage(tuiJianRenUserId, "推荐新人赠送星币",
		// "推荐新人", giveMoney, 0);
		// 推荐人表中加上推荐的金额
		// payMoneyMapper.updatePresentMoney(tuiJianRenUserId, giveMoney);

		/*
		 * // 注册赠送星币 向统计收入表添加数据 payMoneyService.addPayPresentMessage(userId); //
		 * 添加个人注册的消息 payMoneyService.addMessage(userId, 1, "注册成功",
		 * "欢迎使用本APP，您已注册成功！快去抢单吧！");
		 */

		// 用户注册成功，送5张优惠券
		couponService.addCouponForNewUser(userId);
		// 用户注册，送优惠券的同时，在用户余额表里插入一条记录，方便后面充值，更新不会报错
		payMoneyService.addPayRecord(userId);

		// 将注册成功的消息添加到数据库
		payMoneyService.addMessage(userId, 1, "恭喜您注册成功", "小优在此恭候多时了，送您5张无门槛抢单券作为见面礼，快去首页撩撩海量客户吧！");

		// 查询金额与邀请的人数 没有就添加一条数据
		this.updateSharePerson(userId,request);
		return userId;
	}

	// 查询邀请链接与文案
	public ResponseShareUrl queryShareUrlAndCopy() {
		ResponseShareUrl urlAndCopy = shareMapper.queryShareUrlAndCopy(1);
		return urlAndCopy;
	}
}
