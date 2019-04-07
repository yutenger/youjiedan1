/**
 * 
 */
package com.stardai.manage.controller;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.stardai.manage.bean.ResponseModel;
import com.stardai.manage.request.RequestAddCreditByNewEdition;
import com.stardai.manage.request.RequestAllIncome;
import com.stardai.manage.request.RequestCouponByUserId;
import com.stardai.manage.request.RequestCreditDetail;
import com.stardai.manage.response.ResponseCreditCenterInfo;
import com.stardai.manage.response.ResponseGetCoupon;
import com.stardai.manage.response.ResponsePopupInfo;
import com.stardai.manage.service.CreditService;
import com.stardai.manage.service.EventService;
import com.stardai.manage.service.VersionService;
import com.stardai.manage.utils.DateUtil;

/**
 * @author Tina 2018年5月30日
 */

@Controller
@RequestMapping(value = "MZCredit")
@SuppressWarnings("all")
public class CreditController extends BaseController {

	@Autowired
	private CreditService creditService;

	@Autowired
	private VersionService versionService;

	@Autowired
	private EventService eventService;

	// 用户点击积分中心时，如果当天是第一次点击，就自动签到获取积分，如果不是第一次，就显示今日获取的积分
	@RequestMapping(value = "goCreditCenter", method = RequestMethod.GET)
	@ResponseBody
	public ResponseModel goCreditCenter(@RequestParam String userId) {
		if (StringUtils.isBlank(userId)) {
			return ResponseModel.fail("查询失败");
		}
		ResponseCreditCenterInfo responseCreditCenterInfo = new ResponseCreditCenterInfo();

		Integer signinCredit;

		String currentTime = DateUtil.getDay(); // 获取当前时间，格式：2018-05-01

		String yesterdayTime = DateUtil.getBeforeDay(currentTime); // 获取前一天的日期
		// 查看此人当天有没有签到
		Integer isSignin = creditService.queryIsSigninByUserId(userId, currentTime);
		// 当天没有签到的话
		if (isSignin == null) {
			// 查询昨天有没有签到过，并获取到昨天为止连续签到的天数，进行签到
			Integer signinCount = creditService.queryIsSigninByUserId(userId, yesterdayTime); // 昨天为止的连续签到天数

			// 如果昨天签到过，并且昨天和今天是同一个月
			if (signinCount != null && currentTime.substring(5, 6).equals(yesterdayTime.substring(5, 6))) {
				// 获取连续签到天数对应的积分值
				signinCredit = creditService.getValueBySigninCount(signinCount + 1);
				// 获取之前的连续签到天数+1， 插入当天的签到记录
				Integer result = creditService.addNewSigninRecord(userId, signinCredit, signinCount + 1);
				if (result != null && result == 1) {
					// 设置当前签到获取的积分值
					responseCreditCenterInfo.setSigninCredit(signinCredit);
					responseCreditCenterInfo.setSigninStatus(1);
				}
			} else {// 如果昨天没有签到过，或者昨天和今天不在同一个月

				signinCount = 0;

				// 获取连续签到天数对应的积分值
				signinCredit = creditService.getValueBySigninCount(signinCount + 1);
				Integer result = creditService.addNewSigninRecord(userId, signinCredit, 1);
				if (result != null && result == 1) {
					// 设置当前签到获取的积分值
					responseCreditCenterInfo.setSigninCredit(signinCredit);
					responseCreditCenterInfo.setSigninStatus(1);
				}
			}
			RequestCreditDetail requestCreditDetail = new RequestCreditDetail();
			requestCreditDetail.setUserId(userId);
			requestCreditDetail.setCreditPathway("签到");
			requestCreditDetail.setCreditDetail("签到");
			requestCreditDetail.setCreditValue(signinCredit);
			requestCreditDetail.setType(2); // 2为获取的积分
			// 先查积分总表里是否存在，不存在，先插入一条数据，存在的话，就更新
			Integer isGetCredit = creditService.queryIsEverGetCredit(userId);
			if (isGetCredit != null) {
				// 更新用户积分
				creditService.updateCreditWallet(userId, signinCredit);
			} else {
				// 插入一条新的记录
				creditService.addCreditWallet(userId, signinCredit);
			}

			// 添加一条积分明细
			creditService.addCreditDetail(requestCreditDetail);

		} else {
			// 当天签到了
			responseCreditCenterInfo.setSigninStatus(0);

		}
		// 获取该用户的当前剩余积分和当天获取的积分
		HashMap<String, Object> totalAndtoday = creditService.queryTotalAndTodayCredit(userId);
		responseCreditCenterInfo.setTodayTotalCredit((int) totalAndtoday.get("todayCredit"));
		responseCreditCenterInfo.setTotalCredit((int) totalAndtoday.get("totalCredit"));

		return ResponseModel.success(responseCreditCenterInfo);

	}

	// 获取用户积分明细列表
	@RequestMapping(value = "getCreditDetails", method = RequestMethod.POST)
	@ResponseBody
	public ResponseModel getCreditDetails(@RequestBody RequestAllIncome requestAllIncome, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResponseModel.error(bindingResult);
		}
		if (StringUtils.isBlank(requestAllIncome.getUserId())) {
			return ResponseModel.fail("查询失败");
		}
		List<RequestCreditDetail> rcdList = creditService.getCreditDetails(requestAllIncome);
		if (rcdList != null && rcdList.size() > 0) {
			return ResponseModel.success(rcdList);
		}

		return ResponseModel.success();

	}

	// 获取积分兑换列表
	@RequestMapping(value = "getCreditForExchangeList", method = RequestMethod.POST)
	@ResponseBody
	public ResponseModel getCreditForExchangeList(@RequestBody RequestCouponByUserId requestCouponByUserId, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResponseModel.error(bindingResult);
		}
		
		List<ResponseGetCoupon> allCouponList = creditService.getCreditForExchangeList(requestCouponByUserId); // 所有优惠券列表
		if (allCouponList.size() > 0 && allCouponList != null) {
			return ResponseModel.success(allCouponList);
		}

		return ResponseModel.success();

	}
	
	//点击立即兑换时，调用的接口
	@RequestMapping(value = "goCreditForExchange", method = RequestMethod.POST)
	@ResponseBody
	public ResponseModel goCreditForExchange(@RequestBody RequestCouponByUserId requestCouponByUserId, BindingResult bindingResult) {
		
		if (bindingResult.hasErrors()) {
			return ResponseModel.error(bindingResult);
		}
		int result = 0;
		String couponCode = requestCouponByUserId.getCouponCode();
		synchronized (POOL.intern(couponCode)) {
			// 根据标识码查询要兑换东西的剩余数量
			int count = creditService.getExchangeCountByCode(couponCode);
			if (count > 0) {
				//用户点击立即兑换时进行的操作
				result = creditService.goCreditForExchange(requestCouponByUserId); 
				if(result == 1){
					creditService.setExchangeCount(couponCode);
					//获取兑换之后剩余的积分
					HashMap<String, Object> totalAndtoday = creditService.queryTotalAndTodayCredit(requestCouponByUserId.getUserId());
					totalAndtoday.remove("todayCredit");
					return ResponseModel.success(totalAndtoday);
				}else if(result == 2){
					return ResponseModel.fail("对不起，抵扣券已经被抢光了，请明天再来");
				}else if(result == 3){
					return ResponseModel.fail("对不起，您的积分余额不足");
				}
				
				}
			else{
				return ResponseModel.fail("对不起，抵扣券已经被抢光了，请明天再来");
			}
		}
		
		
		return  ResponseModel.fail("兑换失败");
		
		
		
		
	}
	
	

	// 充值，浏览活动，分享邀请加积分调的接口
	@RequestMapping(value = "addCreditByChannels", method = RequestMethod.POST)
	@ResponseBody
	public ResponseModel addCreditByChannels(@RequestBody RequestCreditDetail requestCreditDetail,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return ResponseModel.error(bindingResult);
		}

		if (StringUtils.isBlank(requestCreditDetail.getUserId())) {
			return ResponseModel.fail("参数错误");
		}
		creditService.addCreditByChannels(requestCreditDetail);
		return ResponseModel.success();

	}

	// 版本更新时，每次更新版本之后可获得100积分(安卓)
	@RequestMapping(value = "addCreditForNewEditionAndroid", method = RequestMethod.POST)
	@ResponseBody // 参数是versionCode,userId
	public ResponseModel addCreditForNewEditionAndroid(@RequestBody RequestAddCreditByNewEdition versionInfo,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResponseModel.error(bindingResult);
		}
		String userId = versionInfo.getUserId();
		String versionCode = versionInfo.getVersionCode();
		String iosVersion = eventService.getIosVersionByVersionCode(versionCode);
		if(iosVersion == null){
			iosVersion = "";
		}else{
			iosVersion = iosVersion.substring(1,6);
		}
		
		versionInfo.setIosVersion(iosVersion);
		// 查询安卓端当前最新的版本
		ResponsePopupInfo version = eventService.getAndroidVersionInfo();
		if (versionCode.equals(version.getVersionCode())) { // 用户当前是最新版本
			Integer isGetCredit = creditService.isGetCreditByNewEdition(versionInfo);// 查询用户更新到此版本是否获取过积分
			if (isGetCredit == null) {// 没有获取过积分
				RequestCreditDetail requestCreditDetail = new RequestCreditDetail();
				requestCreditDetail.setUserId(userId);
				requestCreditDetail.setType(2);
				requestCreditDetail.setCreditValue(100);
				requestCreditDetail.setCreditPathway("版本更新");
				requestCreditDetail.setCreditDetail("版本更新");
				creditService.addCreditByNewEdition(versionInfo);
				creditService.addCreditDetail(requestCreditDetail);
				// 先查积分总表里是否存在，不存在，先插入一条数据，存在的话，就更新
				Integer result = creditService.queryIsEverGetCredit(userId);
				if (result != null) {
					// 更新用户积分
					creditService.updateCreditWallet(userId, 100);
				} else {
					// 插入一条新的记录
					creditService.addCreditWallet(userId, 100);
				}
				
			}
		}

		return ResponseModel.success();

	}

	// 版本更新时，每次更新版本之后可获得100积分(iOS)
	@RequestMapping(value = "addCreditForNewEditionIOS", method = RequestMethod.POST)
	@ResponseBody // 参数是iosVersion,userId
	public ResponseModel addCreditForNewEditionIOS(@RequestBody RequestAddCreditByNewEdition versionInfo,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResponseModel.error(bindingResult);
		}
		String userId = versionInfo.getUserId();
		String iosVersion = versionInfo.getIosVersion();
		
		String versionCode = eventService.getVersionCodeByIosVersion("v" + iosVersion);
		if(versionCode == null){
			versionCode = "";
		}
		versionInfo.setVersionCode(versionCode);
		// 查询ios当前最新的版本
		ResponsePopupInfo version = eventService.getiOSVersionInfo();
		if (iosVersion.equals(version.getIosVersion())) { // 用户当前是最新版本
			Integer isGetCredit = creditService.isGetCreditByNewEdition(versionInfo);// 查询用户更新到此版本是否获取过积分
			if (isGetCredit == null) {// 没有获取过积分
				RequestCreditDetail requestCreditDetail = new RequestCreditDetail();
				requestCreditDetail.setUserId(userId);
				requestCreditDetail.setType(2);
				requestCreditDetail.setCreditValue(100);
				requestCreditDetail.setCreditPathway("版本更新");
				requestCreditDetail.setCreditDetail("版本更新");
				creditService.addCreditByNewEdition(versionInfo);
				creditService.addCreditDetail(requestCreditDetail);
				// 先查积分总表里是否存在，不存在，先插入一条数据，存在的话，就更新
				Integer result = creditService.queryIsEverGetCredit(userId);
				if (result != null) {
					// 更新用户积分
					creditService.updateCreditWallet(userId, 100);
				} else {
					// 插入一条新的记录
					creditService.addCreditWallet(userId, 100);
				}
			}
		}

		return ResponseModel.success();

	}
}
