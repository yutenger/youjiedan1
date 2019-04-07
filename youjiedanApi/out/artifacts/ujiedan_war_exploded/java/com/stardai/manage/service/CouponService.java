/**
 * 
 */
package com.stardai.manage.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.stardai.manage.bean.ResponseModel;
import com.stardai.manage.mapper.CouponMapper;
import com.stardai.manage.pojo.CouponInfo;
import com.stardai.manage.request.RequestCouponByUserId;
import com.stardai.manage.request.RequestCouponForNewUser;
import com.stardai.manage.request.RequestCouponInfo;
import com.stardai.manage.response.ResponseGetCoupon;
import com.stardai.manage.utils.DateUtil;
import com.stardai.manage.utils.IdWorker;

/**
 * @author Administrator 2018年4月10日
 */
@Service
@SuppressWarnings("all")

public class CouponService {

	@Autowired
	private CouponMapper couponMapper;

	private final static SimpleDateFormat sdfTimeHour = new SimpleDateFormat("yyyy-MM-dd HH:00:00");

	private IdWorker idWorker = new IdWorker(0, 8);

	/**
	 * 
	 * @Author:Tina
	 * @Description:根据前端传过来的状态获取优惠券列表
	 * @Date:2018年4月12日下午4:04:16
	 * @Param:
	 * @Return:List<CouponInfo>
	 */

	public List<CouponInfo> queryCouponByStatus(RequestCouponByUserId requestCouponByUserId) {

		int couponType;
		int couponForm;
		int couponStatus = requestCouponByUserId.getCouponStatus();
		String effectiveTime = "";
		String expirationTime = "";

		int pageSize = requestCouponByUserId.getPageSize();
		int page = requestCouponByUserId.getPage() * pageSize;
		requestCouponByUserId.setPage(page);

		List<CouponInfo> conpons = new ArrayList<CouponInfo>();

		// 根据前端传过来的状态获取优惠券列表
		if (requestCouponByUserId.getCouponStatus() == 2) {
			conpons = couponMapper.queryCouponUsedByStatus(requestCouponByUserId);
		} else {
			conpons = couponMapper.queryCouponByStatus(requestCouponByUserId);
		}

		for (CouponInfo coupon : conpons) {
			// 获取优惠券值（20星币或者8折）
			couponForm = coupon.getCouponForm();
			// 打折券
			if (couponForm == 1) {
				coupon.setCouponValue(coupon.getCouponDiscount() + "折");
				coupon.setCouponFormName("打折券");

			} else {
				// 满减券
				if (couponForm == 2) {
					coupon.setCouponFormName("满" + coupon.getCouponFullreduction() + "星币使用");

				}
				// 无门槛使用
				else {
					coupon.setCouponFormName("无门槛使用");

				}
				coupon.setCouponValue((coupon.getCouponAmount() + "").replace(".0", "") + "星币");
			}
			// 获取优惠券的有效期
			effectiveTime = (coupon.getEffectiveTime() + "").substring(0, 16).replace("-", ".");
			expirationTime = (coupon.getExpirationTime() + "").substring(0, 16).replace("-", ".");
			coupon.setPeriodTime(effectiveTime + "-" + expirationTime);
			// 获取优惠券类型
			couponType = coupon.getCouponType();
			if (couponType == 1) {
				coupon.setCouponTypeName("充值抵扣券");
			} else {
				coupon.setCouponTypeName("抢单抵扣券");
			}
			coupon.setCouponStatus(couponStatus);

		}
		return conpons;
	}

	/**
	 * 
	 * @Author:Tina
	 * @Description:为新注册用户赠送优惠券
	 * @Date:2018年4月12日下午4:04:32
	 * @Param:
	 * @Return:void
	 */
	public void addCouponForNewUser(String userId) {
		// 查看优惠券的有效时间,面额种类
		HashMap<String, Object> couponProperty = couponMapper.getValidTimeForNewUser();
		String amount = (String) couponProperty.get("amount");
		int validTime = (int) couponProperty.get("validTime");// 优惠券有效时间
		String[] amounts = amount.split(",");// 优惠券面额种类

		Date now = new Date();
		Calendar calendar = new GregorianCalendar();
		// 将Date设置到Calendar中
		calendar.setTime(now);
		// 获得当前时间之后一周时间点
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + validTime);
		String effective = sdfTimeHour.format(now);
		String expiration = sdfTimeHour.format(calendar.getTime());

		// 创建优惠券列表
		List<RequestCouponForNewUser> coupons = new ArrayList<RequestCouponForNewUser>();

		long orderNo;
		for (int i = 0; i < 5; i++) {
			RequestCouponForNewUser requestCouponForNewUser = new RequestCouponForNewUser();
			requestCouponForNewUser.setCouponId("QW" + idWorker.nextId());
			requestCouponForNewUser.setUserId(userId);
			requestCouponForNewUser.setCouponAmount(Integer.parseInt(amounts[i]));
			requestCouponForNewUser.setEffectiveTime(effective);
			requestCouponForNewUser.setExpirationTime(expiration);
			requestCouponForNewUser.setCouponChannel("注册赠送");
			coupons.add(requestCouponForNewUser);

		}
		try {

			couponMapper.addCouponForNewUser(coupons);
		} catch (Exception e) {
			// 如果插入发生异常，就记录在一张表里
			couponMapper.addUnusualRecords(userId);
		}

	}

	/**
	 * @Author:Tina
	 * @Description:查看该用户当前可使用的优惠券张数
	 * @Date:2018年4月12日下午2:52:19
	 * @Param:
	 * @Return:ResponseModel
	 */
	public ResponseModel getCouponCounts(String userId) {

		HashMap<String, Object> data = couponMapper.getCouponCounts(userId);
		if (!data.isEmpty() && data.size() > 0) {
			return ResponseModel.success(data);
		}
		return ResponseModel.success();
	}

	/**
	 * @Author:Tina
	 * @Description:TODO
	 * @Date:2018年4月13日上午10:43:54
	 * @Param:
	 * @Return:boolean
	 */
	public boolean queryCouponPrice(String couponId, double price, double couponPrice) {
		RequestCouponInfo requestCouponInfo = couponMapper.queryCouponInfo(couponId);
		if (requestCouponInfo == null) {
			return false;
		}
		double couponAmount = requestCouponInfo.getCouponAmount();
		if (price <= couponPrice + couponAmount) {
			return true;
		}
		return false;
	}

	/**
	 * @Author:Tina
	 * @Description:优惠券使用之后，把该优惠券从当前表删除，插入到已使用的表里
	 * @Date:2018年4月13日下午2:45:01
	 * @Param:
	 * @Return:void
	 */
	public boolean changeCouponUsed(String couponId) {
		RequestCouponInfo requestCouponInfo = couponMapper.queryCouponInfo(couponId);

		return false;

	}

	/**
	 * @Author:Tina
	 * @Description:TODO
	 * @Date:2018年4月17日下午2:07:31
	 * @Param:
	 * @Return:void
	 */
	public void addClickAmount(int couponStatus) {
		String day = DateUtil.getDay();

		// 计算用户点击每个状态的次数
		Integer amount = couponMapper.getAmountByDateAndStatus(couponStatus, day);
		if (amount != null) {
			couponMapper.updateClickAmount(couponStatus, day);
		} else {
			couponMapper.addClickAmount(couponStatus, day);
		}

	}

	/**
	 * @Author:Tina
	 * @Description:获取在展示时间内领券中心里的优惠券
	 * @Date:2018年5月25日下午3:10:24
	 * @Param:
	 * @Return:List<ResponseGetCoupon>
	 */
	public List<ResponseGetCoupon> getAllCouponList(RequestCouponByUserId requestCouponByUserId) {
		/*int pageSize = requestCouponByUserId.getPageSize();
		int page = requestCouponByUserId.getPage() * pageSize;
		requestCouponByUserId.setPage(page);*/

		String nowDate = DateUtil.getDay(); // 获取当天日期
		String nowTime = DateUtil.getTime();// 获取当前时间
		String userId = requestCouponByUserId.getUserId();

		requestCouponByUserId.setNowTime(nowTime);
		// 进行中的优惠券列表
		List<ResponseGetCoupon> processingCouponList = couponMapper.getProcessingCouponList(requestCouponByUserId);
		// 未开始的优惠券列表
		List<ResponseGetCoupon> berforeTimeCouponList = couponMapper.getBerforeTimeCouponList(requestCouponByUserId);
		// 已结束的优惠券列表
		List<ResponseGetCoupon> endCouponList = couponMapper.getEndCouponList(requestCouponByUserId);

		// 对进行中的优惠券进行遍历修改
		for (ResponseGetCoupon rgc : processingCouponList) {
			String couponCode = rgc.getCouponCode();
			// 查询该用户当天有没有领过此类型的券
			String isExist = couponMapper.queryIsGetCouponToday(userId, couponCode);
			// 如果没有领取过，couponGetStatus设为2
			if (StringUtils.isBlank(isExist)) {
				if(rgc.getCouponInitialCount() > 0){
					double leftPercent = (rgc.getCouponInitialCount()-rgc.getCouponCount())*1.0/rgc.getCouponInitialCount()*100;
					rgc.setLeftPercent((int)leftPercent);
				}
				
				
				rgc.setCouponGetStatus(2);
			} else { // 领取过此券，couponGetStatus设为1
				rgc.setCouponGetStatus(1);
			}
			if(rgc.getCouponType() ==2){
				rgc.setCouponTypeName("抢单抵扣券");
			}
			if(rgc.getCouponForm() ==3){
				rgc.setCouponFormName("无门槛使用");
			}

		}
		// 对未开始的优惠券进行遍历修改
		for (ResponseGetCoupon rgc : berforeTimeCouponList) {
			String getBeginTime = rgc.getGetBeginTime();// 获取领券的开始时间
			if (nowDate.equals(getBeginTime.substring(0, 10))) { // 如果该券是当天未开始的券,显示9点开始
				rgc.setBeginTime(getBeginTime.substring(11, 13).replaceFirst("0", "") + "点开始");
			} else { // 如果不是当天未开始的，显示13日9点开始
				rgc.setBeginTime(getBeginTime.substring(8, 10).replaceFirst("0", "") + "日"
						+ getBeginTime.substring(11, 13).replaceFirst("0", "") + "点开始");
			}
			rgc.setCouponGetStatus(0);
			if(rgc.getCouponType() ==2){
				rgc.setCouponTypeName("抢单抵扣券");
			}
			if(rgc.getCouponForm() ==3){
				rgc.setCouponFormName("无门槛使用");
			}
		}
		// 对已结束的优惠券列表遍历修改
		for (ResponseGetCoupon rgc : endCouponList) {
			String couponCode = rgc.getCouponCode();
			// 查询该用户当天有没有领过此类型的券
			String isExist = couponMapper.queryIsGetCouponToday(userId, couponCode);
			// 如果没有领取过，couponGetStatus设为3，已结束状态
			if (StringUtils.isBlank(isExist)) {
				rgc.setCouponGetStatus(3);
			} else { // 领取过已经结束的券，couponGetStatus设为1
				rgc.setCouponGetStatus(1);
			}
			if(rgc.getCouponType() ==2){
				rgc.setCouponTypeName("抢单抵扣券");
			}
			if(rgc.getCouponForm() ==3){
				rgc.setCouponFormName("无门槛使用");
			}
		}
		processingCouponList.addAll(berforeTimeCouponList);
		processingCouponList.addAll(endCouponList);

		return processingCouponList;
	}

	/**
	 * @Author:Tina
	 * @Description:用户在领券中心，点击领取优惠券时进行的操作
	 * @Date:2018年5月26日下午7:10:53
	 * @Param:
	 * @Return:int
	 */
	public int getCouponAutomaticly(RequestCouponByUserId requestCouponByUserId) {

		// 根据该券的标识码查询此种券的面额，生效时间，过期时间等
		RequestCouponForNewUser rcfn = couponMapper.getCouponInfoByCode(requestCouponByUserId.getCouponCode());
		if (rcfn != null) {

			// 剩余券的数量大于0，用户才可以领
			if (rcfn.getCouponCount() > 0) {
				String couponId = "QW" + idWorker.nextId();
				requestCouponByUserId.setCouponId(couponId);
				requestCouponByUserId.setGetType(1);//标记是从领券中心获取的
				String isGetBefore = couponMapper.queryIsGetCouponBefore(requestCouponByUserId);//先看之前有没有领过这种券
				if(isGetBefore != null){
					return 0;
				}
				
				// 用户领过券之后，把领券信息插入到数据库表里，用作记录
				Integer result = couponMapper.addCouponForUserAutomaticly(requestCouponByUserId);
				if (result != null && result == 1) {
					RequestCouponForNewUser requestCouponForNewUser = new RequestCouponForNewUser();
					requestCouponForNewUser.setCouponId(couponId);
					requestCouponForNewUser.setUserId(requestCouponByUserId.getUserId());
					requestCouponForNewUser.setCouponAmount(rcfn.getCouponAmount());
					// 如果优惠券是发放开始计时，直接从后台取优惠券的有效期
					if (rcfn.getTimingType() == 1) {
						requestCouponForNewUser.setEffectiveTime(rcfn.getEffectiveTime());
						requestCouponForNewUser.setExpirationTime(rcfn.getExpirationTime());
					} else {// 优惠券是领取开始计时，计算优惠券的有效期
						Integer couponValidity = rcfn.getCouponValidity();// 获取优惠券的有效时长
						long currentTime = System.currentTimeMillis();
						Date begindate = new Date(currentTime);
						Date enddate = new Date(currentTime + couponValidity * 60 * 60 * 1000l);
						requestCouponForNewUser.setEffectiveTime(sdfTimeHour.format(begindate));
						requestCouponForNewUser.setExpirationTime(sdfTimeHour.format(enddate));

					}

					requestCouponForNewUser.setCouponChannel("领券中心");
					//把用户领取的优惠券插入到优惠券表里
					Integer addResult = couponMapper.addCouponForGettingUser(requestCouponForNewUser);
					if (addResult != null && addResult == 1) {
						
						// 正常返回
						return 1;
					}
				}
			} else {
				// 优惠券领完了
				return 2;
			}
		}
		//异常返回
		return 0;

	}


	/**
	 * @Author:Tina
	 * @Description:用户每领一张券，券的数量减1
	 * @Date:2018年5月28日下午3:46:49
	 * @Param:
	 * @Return:void
	 */
	public void setCouponCount(String couponCode) {
		couponMapper.setCouponCount(couponCode);
		
	}

	/**
	 * @Author:Tina
	 * @Description:根据优惠券码获取优惠券的剩余数量
	 * @Date:2018年5月28日下午3:48:33
	 * @Param:
	 * @Return:int
	 */
	public int getCouponCountByCode(String couponCode) {
		return couponMapper.getCouponCountByCode(couponCode);
	}

}
