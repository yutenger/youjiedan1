/**
 * 
 */
package com.stardai.manage.controller;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.stardai.manage.config.Log;
import com.stardai.manage.request.RequestAllIncome;
import com.stardai.manage.request.RequestCouponByRechargingMoney;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.stardai.manage.bean.ResponseModel;
import com.stardai.manage.pojo.CouponInfo;
import com.stardai.manage.request.RequestCouponByUserId;
import com.stardai.manage.service.CouponService;
import com.stardai.manage.response.ResponseGetCoupon;

@Controller
@RequestMapping(value = "MZcoupon")
@SuppressWarnings("all")
public class CouponController extends BaseController {

	protected static final Logger LOG = LoggerFactory.getLogger(CouponController.class);

	@Autowired
	private CouponService couponService;

	//优惠券列表(我的优惠券列表(2.22之前使用)，抢单时获取的优惠券列表都是用这个接口(2.22之后照常使用))
	@RequestMapping(value = "queryCouponByStatus", method = RequestMethod.POST)
	@ResponseBody
	public ResponseModel queryCouponByStatus(@RequestBody RequestCouponByUserId requestCouponByUserId,
			BindingResult bindingResult) {
		if (requestCouponByUserId.getUserId() == null || requestCouponByUserId.getUserId().isEmpty()) {
			return ResponseModel.fail("查询失败");
		}
		// 根据userId查询该用户的优惠券
		List<CouponInfo> couponLists = couponService.queryCouponByStatus(requestCouponByUserId);

		// 查询有多少人点击优惠券菜单
		couponService.addClickAmount(requestCouponByUserId.getCouponStatus());

		if (couponLists.size() > 0 && couponLists != null) {
			return ResponseModel.success(couponLists);
		}

		return new ResponseModel(0, "暂无数据", null);

	}
	//我的优惠券和历史优惠券(2.22之后使用)
	@RequestMapping(value = "queryMyCoupon", method = RequestMethod.POST)
	@ResponseBody
	public ResponseModel queryMyCoupon(@RequestBody RequestCouponByUserId requestCouponByUserId,
											 BindingResult bindingResult) {
		if (requestCouponByUserId.getUserId() == null || requestCouponByUserId.getUserId().isEmpty()) {
			return ResponseModel.fail("查询失败");
		}
		// 根据userId查询该用户的优惠券
		List<CouponInfo> couponLists = couponService.queryMyCoupon(requestCouponByUserId);

		// 查询有多少人点击优惠券菜单
		couponService.addClickAmount(requestCouponByUserId.getCouponStatus());

		if (couponLists.size() > 0 && couponLists != null) {
			return ResponseModel.success(couponLists);
		}

		return new ResponseModel(0, "暂无数据", null);

	}
	//查询该用户当前可使用的优惠券张数(2.3之后使用)
    @RequestMapping(value="getUseAbleCouponCounts")
	@ResponseBody
	public ResponseModel getUseAbleCouponCounts(@RequestParam(required = true) String userId,@RequestParam(required = false) Integer couponType){
		try {
			if(couponType!=null) {
				return ResponseModel.success(couponService.getUseAbleCouponCounts(userId, couponType));
			}else{
				Integer couponType1=couponService.getUseAbleCouponCounts(userId, 1);
				Integer couponType2=couponService.getUseAbleCouponCounts(userId, 2);
				List<Integer> couponTypeCount=new ArrayList<>();
				couponTypeCount.add(couponType1);
				couponTypeCount.add(couponType2);
				return ResponseModel.success(couponTypeCount);
			}
		}catch(Exception e){
			e.printStackTrace();
			return ResponseModel.fail();
		}
	}
	// 查看该用户当前可使用的优惠券张数,2.1版本之前用的接口
	@RequestMapping(value = "getCouponCounts", method = RequestMethod.GET)
	@ResponseBody
	public ResponseModel getCouponCounts(@RequestParam String userId) {
		return couponService.getCouponCounts(userId);

	}

	// 查看该用户当前可使用的优惠券张数，2.1版本开始用的接口
	@RequestMapping(value = "getAllCouponCounts", method = RequestMethod.POST)
	@ResponseBody
	public ResponseModel getAllCouponCounts(@RequestBody RequestAllIncome requestAllIncome) {
		return couponService.getAllCouponCounts(requestAllIncome);

	}

	// 获取领券中心列表里的券
	@RequestMapping(value = "getCouponCenterList", method = RequestMethod.POST)
	@ResponseBody
	public ResponseModel getCouponCenterList(@RequestBody RequestCouponByUserId requestCouponByUserId) {
		List<ResponseGetCoupon> allCouponList = couponService.getAllCouponList(requestCouponByUserId); // 所有优惠券列表
		//对领券中心优惠券进行排序
		Collections.sort(allCouponList,(coupon1, Coupon2)->{
					return coupon1.compareTo(Coupon2);
				}
		);
		if (allCouponList.size() > 0 && allCouponList != null) {
			return ResponseModel.success(allCouponList);
		}

		return ResponseModel.success();

	}

	// 用户点领券时，调用的操作
	@Log("用户在领券中心领券")
	@RequestMapping(value = "getCouponAutomaticly", method = RequestMethod.POST)
	@ResponseBody
	public ResponseModel getCouponAutomaticly(@RequestBody RequestCouponByUserId requestCouponByUserId) {

		String couponCode = requestCouponByUserId.getCouponCode();
		synchronized (POOL.intern(couponCode)) {
			// 根据该券的标识码查询此种券的剩余数量
			int count = couponService.getCouponCountByCode(couponCode);
			if (count > 0) {
				// 用户在领券中心，点击领取优惠券时进行的操作
				int result = couponService.getCouponAutomaticly(requestCouponByUserId);
				if (result == 1) {
					couponService.setCouponCount(couponCode);
					return ResponseModel.success();
				} else if (result == 2) {
					return ResponseModel.fail("对不起，抵扣券已经被抢光了，请明天再来");
				}

			} else {
				return ResponseModel.fail("对不起，抵扣券已经被抢光了，请明天再来");
			}
		}
		LOG.error(requestCouponByUserId.getUserId()+"--领券中心--领取失败" );
		return ResponseModel.fail("领取失败");

	}

	// 查询该用户充值时可使用的优惠券
	@RequestMapping(value = "queryCouponForRecharge", method = RequestMethod.GET)
	@ResponseBody
	public ResponseModel queryCouponForRecharge(@RequestParam String userId) {
		if (StringUtils.isBlank(userId)) {
			return ResponseModel.fail("查询失败");
		}
		// 根据userId查询该用户的优惠券
		List<CouponInfo> couponLists = couponService.queryCouponForRecharge(userId);
		if (couponLists.size() > 0 && couponLists != null) {
			return ResponseModel.success(couponLists);
		}

		return new ResponseModel(0, "暂无数据", null);

	}

	//用户充值对应金额时，返回对应金额的可以使用的券
	@RequestMapping(value = "queryCouponForGivingMoney", method = RequestMethod.POST)
	@ResponseBody
	public ResponseModel queryCouponForGivingMoney(@RequestBody RequestCouponByRechargingMoney requestCouponByRechargingMoney) {

		// 根据userId 以及传过来的充值金额查询该用户的优惠券
		List<CouponInfo> couponLists = couponService.queryCouponForGivingMoney(requestCouponByRechargingMoney);
		if (couponLists.size() > 0 && couponLists != null) {
			return ResponseModel.success(couponLists);
		}

		return new ResponseModel(0, "暂无数据", null);

	}





}
