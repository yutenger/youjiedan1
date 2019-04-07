package com.stardai.manage.service;

import com.stardai.manage.response.ResponseShareDetail;
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

import java.util.List;

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
	public ResponseShareUser querySharePerson(String userId, HttpServletRequest request) {
		ResponseShareUser result = shareMapper.querySharePerson(userId);
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
				result.setShareUrl(url);
				//查询用户本月邀请的人数和获取的星币
				ResponseShareUser re = shareMapper.queryShareCountAndMoney(userId);
				if(re != null){
					result.setShareCount(re.getShareCount());
					result.setShareMoney(re.getShareMoney());
				}

			}

			
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

		// 用户注册成功，送5张优惠券
		couponService.addCouponForNewUser(userId);
		// 用户注册，送优惠券的同时，在用户余额表里插入一条记录，方便后面充值，更新不会报错
		payMoneyService.addPayRecord(userId);

		// 将注册成功的消息添加到数据库
		payMoneyService.addMessage(userId, 1, "恭喜您注册成功", "小优在此恭候多时了，送您5张无门槛抢单券作为见面礼，快去首页撩撩海量客户吧！");
		return userId;
	}

	// 查询邀请链接与文案
	public ResponseShareUrl queryShareUrlAndCopy() {
		ResponseShareUrl urlAndCopy = shareMapper.queryShareUrlAndCopy(1);
		return urlAndCopy;
	}

	//用户邀请的明细
    public List<ResponseShareDetail> queryShareDetailList(String userId,int type,int page,int pageSize) {
		page = page * pageSize;
		List<ResponseShareDetail> shareDetailList = shareMapper.queryShareDetailList(userId,type,page,pageSize);
		if(type == 1){
			for(ResponseShareDetail rsd : shareDetailList){
				//手机号码隐藏中间四位
				rsd.setMobileNumber(rsd.getMobileNumber().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
				//如果是已认证状态，要算出邀请这个人获取的星币
				int getMoney = shareMapper.getShareMoneyByUserId(userId,rsd.getUserId());
				rsd.setGetMoney(getMoney);
			}
		}else{
			for(ResponseShareDetail rsd : shareDetailList){
				//手机号码隐藏中间四位
				rsd.setMobileNumber(rsd.getMobileNumber().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
			}
		}


		return shareDetailList;
    }
}
