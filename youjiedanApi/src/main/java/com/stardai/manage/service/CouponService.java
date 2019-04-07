/**
 * 
 */
package com.stardai.manage.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.stardai.manage.mapper.UserMapper;
import com.stardai.manage.request.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.stardai.manage.bean.ResponseModel;
import com.stardai.manage.mapper.CouponMapper;
import com.stardai.manage.pojo.CouponInfo;
import com.stardai.manage.response.ResponseGetCoupon;
import com.stardai.manage.utils.DateUtil;
import com.stardai.manage.utils.IdWorker;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Administrator 2018年4月10日
 */
@Service
@SuppressWarnings("all")

public class CouponService {

	@Autowired
	private CouponMapper couponMapper;

	@Autowired
	private CreditService creditService;

	@Autowired
	private UserMapper userMapper;


	private final static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private final static SimpleDateFormat sdfTimeHour = new SimpleDateFormat("yyyy-MM-dd HH:00:00");

	private IdWorker idWorker = new IdWorker(0, 8);

	/**
	 * 
	 * @Author:Tina
	 * @Description:优惠券列表(我的优惠券列表，抢单时获取的优惠券列表都是用这个接口)
	 * @Date:2018年4月12日下午4:04:16
	 * @Param:
	 * @Return:List<CouponInfo>
	 */

	public List<CouponInfo> queryCouponByStatus(RequestCouponByUserId requestCouponByUserId) {
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

			coupon = this.setcouponFormAndType(coupon);
			// 获取优惠券的有效期
			effectiveTime = (coupon.getEffectiveTime() + "").substring(0, 16).replace("-", ".");
			expirationTime = (coupon.getExpirationTime() + "").substring(0, 16).replace("-", ".");
			coupon.setPeriodTime(effectiveTime + "-" + expirationTime);
			coupon.setCouponStatus(couponStatus);
		}
		return conpons;
	}

	public List<CouponInfo> queryMyCoupon(RequestCouponByUserId requestCouponByUserId) {
		int couponStatus = requestCouponByUserId.getCouponStatus();
		String effectiveTime = "";
		String expirationTime = "";

		int pageSize = requestCouponByUserId.getPageSize();
		int page = requestCouponByUserId.getPage() * pageSize;
		requestCouponByUserId.setPage(page);

		List<CouponInfo> conpons = new ArrayList<CouponInfo>();
		// 根据前端传过来的状态获取优惠券列表
		if (requestCouponByUserId.getAvaliable() == 0) { //couponStatus =1 我的优惠券
			requestCouponByUserId.setCouponStatus(1);
			conpons = couponMapper.queryCouponByStatus(requestCouponByUserId);
			conpons.forEach((k)->{
				//设置优惠券状态为可用
				k.setCouponStatus(1);
					}
			);
			Collections.sort(conpons,(k,v)->{
				int result=k.getReceiveTime().compareTo(v.getReceiveTime());
				if(result>0){
					return -1;
				}
				if(result<0){
					return 1;
				}
				return 0;
					}
			);
		} else {                                         //couponStatus=3 or 2 历史优惠券
			requestCouponByUserId.setCouponType(null);
			requestCouponByUserId.setCouponStatus(3);
			conpons = couponMapper.queryCouponByStatus(requestCouponByUserId);
			conpons.forEach((k)->{
						//设置优惠券状态为已过期
						k.setCouponStatus(3);
					}
			);
			List<CouponInfo> temp=couponMapper.queryCouponUsedByStatus(requestCouponByUserId);
			temp.forEach((k)->{
				//设置优惠券状态为已使用
				k.setCouponStatus(2);
			});
			conpons.addAll(temp);
			Collections.sort(conpons,(k,v)->{
				int result=k.getSortTime().compareTo(v.getSortTime());
				if(result>0){
					return -1;
				}
				if(result<0){
					return 1;
				}
				return 0;
					}
			);
		}

		for (CouponInfo coupon : conpons) {

			coupon = this.setcouponFormAndType(coupon);
			// 获取优惠券的有效期
			effectiveTime = (coupon.getEffectiveTime() + "").substring(0, 16).replace("-", ".");
			expirationTime = (coupon.getExpirationTime() + "").substring(0, 16).replace("-", ".");
			coupon.setPeriodTime(effectiveTime + "-" + expirationTime);
		}
		return conpons;
	}
	private CouponInfo setcouponFormAndType(CouponInfo coupon) {
		// 获取优惠券值（20星币或者8折）
		int couponForm = coupon.getCouponForm();
		// 获取优惠券类型
		int couponType = coupon.getCouponType();
		// 打折券
		if (couponForm == 1) {
			coupon.setCouponValue(coupon.getCouponDiscount() + "折");
			coupon.setCouponFormName("打折券");

		}else if (couponForm == 2) {// 满减券
				if(couponType == 1){
					coupon.setCouponFormName("充" + new Double(coupon.getCouponFullreduction()).intValue() + "可用");
					coupon.setCouponValue(new Double(coupon.getCouponAmount()).intValue() + "元");
				}else{
					coupon.setCouponFormName("满" + new Double(coupon.getCouponFullreduction()).intValue() + "可用");
					coupon.setCouponValue(new Double(coupon.getCouponAmount()).intValue() + "星币");
				}
			}else {// 无门槛使用
				coupon.setCouponFormName("无门槛使用");
			}
		if (couponType == 1) {
			coupon.setCouponTypeName("充值抵扣券");
		} else {

			coupon.setCouponTypeName("抢单抵扣券");
		}
		return coupon;

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
		// 获得当前时间之后一周时间点
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(now);
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + validTime);
		String effective = sdfTimeHour.format(now);
		String expiration = sdfTimeHour.format(calendar.getTime());

		//获取当前时间一月之后的时间点
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 30);
		String expirationForRecharge = sdfTimeHour.format(calendar.getTime());

		// 创建优惠券列表
		List<RequestCouponForNewUser> coupons = new ArrayList<RequestCouponForNewUser>();

		long orderNo;
		//注册送 5 张无门槛抢单抵扣券
		for (int i = 0; i < 5; i++) {
			RequestCouponForNewUser requestCouponForNewUser = new RequestCouponForNewUser();
			requestCouponForNewUser.setCouponId("QW" + idWorker.nextId());
			requestCouponForNewUser.setUserId(userId);
			requestCouponForNewUser.setCouponAmount(Integer.parseInt(amounts[i]));
			//设置优惠券生效时间
			requestCouponForNewUser.setEffectiveTime(effective);
			//设置优惠券过期时间
			requestCouponForNewUser.setExpirationTime(expiration);
			//设置优惠券花费形式：1 打折， 2 满减， 3 无门槛
			requestCouponForNewUser.setCouponForm(3);
			//设置优惠券类型：1 充值抵扣，2 抢单抵扣
			requestCouponForNewUser.setCouponType(2);
			requestCouponForNewUser.setCouponChannel("注册赠送");
			coupons.add(requestCouponForNewUser);

		}
		try {
			//为新注册用户各送3张充值满减券
			this.sendRechargeCouponForNewEditionUser(userId);
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
    public int getUseAbleCouponCounts(String userId,int couponType){
		return couponMapper.getUseAbleCouponCounts(userId,couponType);
	}
	public ResponseModel getAllCouponCounts(RequestAllIncome requestAllIncome) {
		Integer used = couponMapper.getAllCouponCountsUsed(requestAllIncome);
		Integer unused = couponMapper.getAllCouponCountsUnused(requestAllIncome);
		Integer overdue = couponMapper.getAllCouponCountsOverdue(requestAllIncome);
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("used",used);
		data.put("unused",unused);
		data.put("overdue",overdue);
		return ResponseModel.success(data);
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
		int couponForm = requestCouponInfo.getCouponForm();
		if(couponForm == 2 && requestCouponInfo.getCouponFullreduction() > price){
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

		String nowTime = DateUtil.getTime();// 获取当前时间
		String userId = requestCouponByUserId.getUserId();
		String companyCity = userMapper.queryCompanyCity(userId);


		requestCouponByUserId.setNowTime(nowTime);
		// 进行中的优惠券列表
		List<ResponseGetCoupon> processingCouponList = couponMapper.getProcessingCouponList(requestCouponByUserId);
		Iterator<ResponseGetCoupon> processingIterator = processingCouponList.iterator();
		// 未开始的优惠券列表
		List<ResponseGetCoupon> berforeTimeCouponList = couponMapper.getBerforeTimeCouponList(requestCouponByUserId);
		Iterator<ResponseGetCoupon> berforeTimeIterator = berforeTimeCouponList.iterator();
		// 已结束的优惠券列表
		List<ResponseGetCoupon> endCouponList = couponMapper.getEndCouponList(requestCouponByUserId);
		Iterator<ResponseGetCoupon> endIterator = endCouponList.iterator();

		String cities ="";
		// 对进行中的优惠券进行遍历修改
		while(processingIterator.hasNext()){
			ResponseGetCoupon rgc = processingIterator.next();
			cities = rgc.getCities();
			if(StringUtils.isNotBlank(cities) && !"000000".equals(cities)&& StringUtils.isNotBlank(companyCity) && !cities.contains(companyCity)){
				processingIterator.remove();
				continue;
			}
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
				rgc.setSortSign(0);
			} else { // 领取过此券，couponGetStatus设为1
				rgc.setCouponGetStatus(1);
				rgc.setSortSign(1);
			}
			//设置优惠券类型
			creditService.setCouponTypeAndForm(rgc);

		}
		//对未开始的优惠券遍历修改
		String getBeginTime = "";
		while(berforeTimeIterator.hasNext()) {
			ResponseGetCoupon rgc = berforeTimeIterator.next();
			cities = rgc.getCities();
			if (StringUtils.isNotBlank(cities) && !"000000".equals(cities)&& StringUtils.isNotBlank(companyCity) && !cities.contains(companyCity)) {
				berforeTimeIterator.remove();
				continue;
			}
			getBeginTime = rgc.getGetBeginTime();// 获取领券的开始时间
			try {

				Calendar beginTimeCal=Calendar.getInstance();
				Calendar todayCal=Calendar.getInstance();
				beginTimeCal.setTime(sdfTime.parse(getBeginTime));
				todayCal.setTime(new Date());

				//如果领券开始时间是当天
				if(beginTimeCal.get(Calendar.DAY_OF_MONTH) == todayCal.get(Calendar.DAY_OF_MONTH)){
					rgc.setBeginTime(beginTimeCal.get(Calendar.HOUR_OF_DAY) + "点开始");
				}else{
					rgc.setBeginTime(beginTimeCal.get(Calendar.DAY_OF_MONTH) + "日"
							+ beginTimeCal.get(Calendar.HOUR_OF_DAY)+ "点开始");
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}

			rgc.setCouponGetStatus(0);
			rgc.setSortSign(2);
			//设置优惠券类型
			creditService.setCouponTypeAndForm(rgc);

		}
		// 对已结束的优惠券列表遍历修改
		while(endIterator.hasNext()) {
			ResponseGetCoupon rgc = endIterator.next();
			cities = rgc.getCities();
			if (StringUtils.isNotBlank(cities) && !"000000".equals(cities) && StringUtils.isNotBlank(companyCity) && !cities.contains(companyCity)) {
				endIterator.remove();
				continue;
			}
			String couponCode = rgc.getCouponCode();
			// 查询该用户当天有没有领过此类型的券
			String isExist = couponMapper.queryIsGetCouponToday(userId, couponCode);
			// 如果没有领取过，couponGetStatus设为3，已结束状态
			if (StringUtils.isBlank(isExist)) {
				rgc.setCouponGetStatus(3);
				rgc.setSortSign(3);
			} else { // 领取过已经结束的券，couponGetStatus设为1
				rgc.setCouponGetStatus(1);
				rgc.setSortSign(1);
			}
			//设置优惠券类型
			creditService.setCouponTypeAndForm(rgc);
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
					requestCouponForNewUser.setCouponFullreduction(rcfn.getCouponFullreduction());
					requestCouponForNewUser.setCouponDiscount(rcfn.getCouponDiscount());
					requestCouponForNewUser.setCouponForm(rcfn.getCouponForm());
					requestCouponForNewUser.setCouponType(rcfn.getCouponType());
					// 如果优惠券是发放开始计时，直接从后台取优惠券的有效期
					if (rcfn.getTimingType() == 1) {
						requestCouponForNewUser.setEffectiveTime(rcfn.getEffectiveTime());
						requestCouponForNewUser.setExpirationTime(rcfn.getExpirationTime());
					} else {// 优惠券是领取开始计时，计算优惠券的有效期
						Integer couponValidity = rcfn.getCouponValidity();// 获取优惠券的有效时长
						long currentTime = System.currentTimeMillis();
						Date begindate = new Date(currentTime);
						Date enddate = new Date(currentTime + couponValidity * 60 * 60 * 1000L);
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

	/**
	 * 查询该用户充值时可使用的优惠券
	 * @param userId
	 * @return
	 */
	public List<CouponInfo> queryCouponForRecharge(String userId) {
		return couponMapper.queryCouponForRecharge(userId);
	}

	public List<CouponInfo> queryCouponForGivingMoney(RequestCouponByRechargingMoney requestCouponByRechargingMoney) {

		int pageSize = requestCouponByRechargingMoney.getPageSize();
		int page = requestCouponByRechargingMoney.getPage() * pageSize;
		requestCouponByRechargingMoney.setPage(page);
		String effectiveTime = "";
		String expirationTime = "";
		List<CouponInfo> couponList =  couponMapper.queryCouponForGivingMoney(requestCouponByRechargingMoney);
		for(CouponInfo couponInfo : couponList){
			couponInfo.setCouponFullreduction(new Double(couponInfo.getCouponFullreduction()).intValue());
			couponInfo.setCouponAmount(new Double(couponInfo.getCouponAmount()).intValue());
			couponInfo.setCouponTypeName("充值抵扣券");
			couponInfo.setCouponFormName("充"+ new Double(couponInfo.getCouponFullreduction()).intValue() + "可用");
			effectiveTime = (couponInfo.getEffectiveTime() + "").substring(0, 16).replace("-", ".");
			expirationTime = (couponInfo.getExpirationTime() + "").substring(0, 16).replace("-", ".");
			couponInfo.setPeriodTime(effectiveTime + "-" + expirationTime);

		}

		return couponList;
	}

	//根据优惠券id查询优惠券具体信息
	public RequestCouponInfo queryCouponInfo(String couponId) {
		return  couponMapper.queryCouponInfo(couponId);

	}

	//注册送充值满减券各 3 张
	public void sendRechargeCouponForNewEditionUser(String userId) {

		Date now = new Date();
		// 获得当前时间一月之后时间点
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(now);
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 30);
		String effective = sdfTimeHour.format(now);
		String expiration = sdfTimeHour.format(calendar.getTime());

		// 创建优惠券列表
		List<RequestCouponForNewUser> coupons = new ArrayList<RequestCouponForNewUser>();

		for (int i = 0; i < 3; i++) {
			RequestCouponForNewUser requestCouponForNewUser = new RequestCouponForNewUser();
			requestCouponForNewUser.setCouponId("CM" + idWorker.nextId());
			requestCouponForNewUser.setUserId(userId);
			requestCouponForNewUser.setCouponAmount(80);
			requestCouponForNewUser.setCouponFullreduction(500);
			requestCouponForNewUser.setEffectiveTime(effective);
			requestCouponForNewUser.setExpirationTime(expiration);
			requestCouponForNewUser.setCouponForm(2);
			requestCouponForNewUser.setCouponType(1);
			requestCouponForNewUser.setCouponChannel("注册赠送");
			coupons.add(requestCouponForNewUser);

		}
		for (int i = 0; i < 3; i++) {
			RequestCouponForNewUser requestCouponForNewUser = new RequestCouponForNewUser();
			requestCouponForNewUser.setCouponId("CM" + idWorker.nextId());
			requestCouponForNewUser.setUserId(userId);
			requestCouponForNewUser.setCouponAmount(200);
			requestCouponForNewUser.setCouponFullreduction(1000);
			requestCouponForNewUser.setEffectiveTime(effective);
			requestCouponForNewUser.setExpirationTime(expiration);
			requestCouponForNewUser.setCouponForm(2);
			requestCouponForNewUser.setCouponType(1);
			requestCouponForNewUser.setCouponChannel("注册赠送");
			coupons.add(requestCouponForNewUser);

		}
		for (int i = 0; i < 3; i++) {
			RequestCouponForNewUser requestCouponForNewUser = new RequestCouponForNewUser();
			requestCouponForNewUser.setCouponId("CM" + idWorker.nextId());
			requestCouponForNewUser.setUserId(userId);
			requestCouponForNewUser.setCouponAmount(450);
			requestCouponForNewUser.setCouponFullreduction(2000);
			requestCouponForNewUser.setEffectiveTime(effective);
			requestCouponForNewUser.setExpirationTime(expiration);
			requestCouponForNewUser.setCouponForm(2);
			requestCouponForNewUser.setCouponType(1);
			requestCouponForNewUser.setCouponChannel("注册赠送");
			coupons.add(requestCouponForNewUser);

		}
		for (int i = 0; i < 3; i++) {
			RequestCouponForNewUser requestCouponForNewUser = new RequestCouponForNewUser();
			requestCouponForNewUser.setCouponId("CM" + idWorker.nextId());
			requestCouponForNewUser.setUserId(userId);
			requestCouponForNewUser.setCouponAmount(1400);
			requestCouponForNewUser.setCouponFullreduction(5000);
			requestCouponForNewUser.setEffectiveTime(effective);
			requestCouponForNewUser.setExpirationTime(expiration);
			requestCouponForNewUser.setCouponForm(2);
			requestCouponForNewUser.setCouponType(1);
			requestCouponForNewUser.setCouponChannel("注册赠送");
			coupons.add(requestCouponForNewUser);

		}
		for (int i = 0; i < 3; i++) {
			RequestCouponForNewUser requestCouponForNewUser = new RequestCouponForNewUser();
			requestCouponForNewUser.setCouponId("CM" + idWorker.nextId());
			requestCouponForNewUser.setUserId(userId);
			requestCouponForNewUser.setCouponAmount(3000);
			requestCouponForNewUser.setCouponFullreduction(10000);
			requestCouponForNewUser.setEffectiveTime(effective);
			requestCouponForNewUser.setExpirationTime(expiration);
			requestCouponForNewUser.setCouponForm(2);
			requestCouponForNewUser.setCouponType(1);
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
	public String queryIsGetCouponBefore(RequestCouponByUserId requestCouponByUserId){
		return couponMapper.queryIsGetCouponBefore(requestCouponByUserId);
	}

	public int queryIsGetCouponBeforeCount(RequestCouponByUserId requestCouponByUserId){
		return couponMapper.queryIsGetCouponBeforeCount(requestCouponByUserId);
	}
	public int addCouponForUserAutomaticly(RequestCouponByUserId requestCouponByUserId){
		return couponMapper.addCouponForUserAutomaticly(requestCouponByUserId);
	}
	public void insertUserCoupon(String couponId,String userId,int couponAmount,double couponDiscount,String effectiveTime,String expirationTime,String couponChannel,Integer couponType,Integer couponForm,double couponFullreduction){
		RequestCouponForNewUser requestCouponForNewUser = new RequestCouponForNewUser();//为用户发送优惠券
		requestCouponForNewUser.setUserId(userId);
		requestCouponForNewUser.setCouponAmount(couponAmount);
		requestCouponForNewUser.setCouponDiscount(couponDiscount);
		requestCouponForNewUser.setEffectiveTime(effectiveTime);
		requestCouponForNewUser.setExpirationTime(expirationTime);
		requestCouponForNewUser.setCouponChannel(couponChannel);
		StringBuilder sb=new StringBuilder("");
		if(couponType==1){
			sb.append("C");   //充值抵扣
		}
		if(couponType==2){
			sb.append("Q");   //抢单抵扣
		}
		if(couponForm==1){
			sb.append("D");  //打折
		}
		if(couponForm==2){
			sb.append("M");  //满减
		}
		if(couponForm==3){
			sb.append("W");  //无门槛
		}
		requestCouponForNewUser.setCouponId( sb.toString()+couponId);
		requestCouponForNewUser.setCouponType(couponType);
		requestCouponForNewUser.setCouponForm(couponForm);
		requestCouponForNewUser.setCouponFullreduction(couponFullreduction);
		couponMapper.addCouponForGettingUser(requestCouponForNewUser);
	}
	public void insertBatchUserCoupon(int count,String userId,int couponAmount,double couponDiscount,String effectiveTime,String expirationTime,String couponChannel,Integer couponType,Integer couponForm,double couponFullreduction){
		RequestCouponForNewUser requestCouponForNewUser = new RequestCouponForNewUser();//为用户发送优惠券
		requestCouponForNewUser.setUserId(userId);
		requestCouponForNewUser.setCouponAmount(couponAmount);
		requestCouponForNewUser.setCouponDiscount(couponDiscount);
		requestCouponForNewUser.setEffectiveTime(effectiveTime);
		requestCouponForNewUser.setExpirationTime(expirationTime);
		requestCouponForNewUser.setCouponChannel(couponChannel);
		StringBuilder sb=new StringBuilder("");
		if(couponType==1){
			sb.append("C");   //充值抵扣
		}
		if(couponType==2){
			sb.append("Q");   //抢单抵扣
		}

		if(couponForm==1){
			sb.append("D");  //打折
		}
		if(couponForm==2){
			sb.append("M");  //满减
		}
		if(couponForm==3){
			sb.append("W");  //无门槛
		}
		requestCouponForNewUser.setCouponType(couponType);
		requestCouponForNewUser.setCouponForm(couponForm);
		requestCouponForNewUser.setCouponFullreduction(couponFullreduction);
		RequestCouponByUserId requestCouponByUserId = new RequestCouponByUserId();
		requestCouponByUserId.setUserId(userId);
		requestCouponByUserId.setGetType(1);
		String couponCode = "rechargeFeedbackNewGrabSheet";
		requestCouponByUserId.setCouponCode(couponCode);
		for(int i=0;i<count;i++) {
			String couponId = sb.toString()+idWorker.nextId();
			requestCouponForNewUser.setCouponId(couponId);
			requestCouponByUserId.setCouponId(couponId);
			couponMapper.addCouponForGettingUser(requestCouponForNewUser);
			couponMapper.addCouponForUserAutomaticly(requestCouponByUserId);
		}
	}
}
