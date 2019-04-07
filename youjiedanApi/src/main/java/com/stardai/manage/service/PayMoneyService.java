package com.stardai.manage.service;

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import cn.jpush.api.push.PushResult;
import com.stardai.manage.mapper.*;
import com.stardai.manage.request.*;
import com.stardai.manage.response.*;
import com.stardai.manage.utils.*;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.stardai.manage.pojo.PayWallet;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jdw
 * @date 2017/10/16
 */
@Service
@SuppressWarnings("all")
public class PayMoneyService {

	@Autowired
	private LoanUserService loanUserService;

	@Autowired
	private PayMoneyMapper payMoneyMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private LoanUserMapper loanUserMapper;

	@Autowired
	private UserService userService;

	@Autowired
	private CouponMapper couponMapper;

	@Autowired
	private CreditMapper creditMapper;

	@Autowired
	private RedisCacheManager redisCacheManager;

	@Autowired
	HttpServletRequest request;
	@Autowired
	private EventMapper eventMapper;
	@Autowired
	private PayMoneyService payMoneyService;

	@Autowired
	private CouponService couponService;
	private static String ENCODING = "UTF-8";

	protected static final Logger LOG = LoggerFactory.getLogger(PayMoneyService.class);

	private static SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");



	private IdWorker idWorker = new IdWorker(0, 8);

	// 根据手机号与用户id查询是否一致
	public Integer queryUser(String mobileNumber, String userId) {
		Integer result = payMoneyMapper.queryUser(mobileNumber, userId);
		return result;
	}

	// 向统计收入表添加数据
	public void addPayRechargeMessageAndUpdateRechargeMoney(String userId, String incomeNo, String channel,
			double amountYuan,String couponId,int type) {
		// 由于时间延迟webhooks通知会在未接收到返回200的状态吗之后会再次调用方法
		// 先去查询是否已经添加过这条数据
		String name = userService.queryNameByUserId(userId);
		String role = "";
		if(StringUtils.isNotBlank(name)){
			role = name.subSequence(0, 1) + "经理";
		}

		Integer result = payMoneyMapper.queryIncomeMessage(userId, incomeNo);
		if (result == 0) {
			// 获取本次充值金额,amountYuan 是充值实际付的钱，amount 是充值到账的钱
			int amount = (int) amountYuan;
			if(StringUtils.isNotBlank(couponId)){
				RequestCouponInfo couponInfo = couponMapper.queryCouponInfo(couponId);
				if(couponInfo != null){
					double couponAmount = couponInfo.getCouponAmount();
					amount += (new Double(couponAmount)).intValue();
					//把已使用的优惠券挪到另一张表里
					Integer isUsed = couponMapper.addCouponUsed(couponInfo);
					if (isUsed != null && isUsed == 1) {
						couponMapper.removeUsedCoupon(couponId);
					}
					if(amount - amountYuan > 0 ){
						payMoneyMapper.updatePresentMoney(userId, amount - amountYuan);
					}
				}
			}
			//2.1 之前的版本，充值送星币，2.1之后充值可以使用满减券，就不送星币
			if(type == 0){
				// 根据充值的金额去查询赠送的金额
				Integer addMoney = payMoneyMapper.queryAddMoney(amount);
				// 向充值记录表中添加充值记录
				if (addMoney != null &&addMoney != 0) {
					// 加上赠送金额
					payMoneyMapper.updatePresentMoney(userId, addMoney);
					payMoneyMapper.addPayPresentMessage(userId, "充值赠送", "充值", Double.valueOf(addMoney + ""),
							Double.valueOf(0 + ""),0);
				}
			}else{
				//中秋国庆，活动期间，抢单送星币和优惠券
				//看充值的档位当前时间是否有活动,并获取充值赠送的优惠券金额和数量
				HashMap<String,Integer> couponAmountAndCount = payMoneyMapper.queryExtraMoney(amount, System.currentTimeMillis());
				if(couponAmountAndCount != null && couponAmountAndCount.size() > 0){
					Integer couponAmount = couponAmountAndCount.get("couponAmount");
					Integer couponCount = couponAmountAndCount.get("couponCount");
					if(couponAmount != null && couponCount!= null){

						// 创建优惠券列表
						List<RequestCouponForNewUser> coupons = new ArrayList<RequestCouponForNewUser>();
						//充值送 N 张无门槛抢单抵扣券
						for (int i = 0; i < couponCount; i++) {
							RequestCouponForNewUser requestCouponForNewUser = new RequestCouponForNewUser();
							requestCouponForNewUser.setCouponId("QW" + idWorker.nextId());
							requestCouponForNewUser.setUserId(userId);
							requestCouponForNewUser.setCouponAmount(couponAmount);
							requestCouponForNewUser.setEffectiveTime(DateUtil.getTime());
							requestCouponForNewUser.setExpirationTime(DateUtil.getAfterDayDate("3"));
							requestCouponForNewUser.setCouponForm(3);
							requestCouponForNewUser.setCouponType(2);
							requestCouponForNewUser.setCouponChannel("充值赠送抢单抵扣券");
							coupons.add(requestCouponForNewUser);

						}
						String message = "您的充值奖励"+couponAmount+"元*"+couponCount+"张抢单优惠券已到账，请尽快使用！";
						try {
							//发放优惠券
							couponMapper.addCouponForNewUser(coupons);
							//发送系统消息
							payMoneyMapper.addMessage(userId, 1, "奖励通知", message);
							//发推送
							PushResult pushResult = JiguangPush.push(userId,message);
							if (pushResult != null && pushResult.isResultOK()) {
								LOG.info("针对" + userId + "的订单信息推送成功！");
							} else {
								LOG.info("针对" + userId + "的订单信息推送失败！");
							}

						} catch (Exception e) {

						}

					}
				}


			}

			payMoneyMapper.addPayPresentMessage(userId, incomeNo, channel, (double)amount,amountYuan, 1);
			// 向个人信息中添加充值的通知信息
			payMoneyMapper.addMessage(userId, 1, "充值成功！",
					"亲爱的" + role + "：您的星币" + amount + "已经到账，请到优接单首页开始您的抢单之旅吧！");
			// 根据userId查询当前该充值人的余额(不含赠送的)
			int chargeMoney = payMoneyMapper.queryRechargeMoney(userId);
			amountYuan = amountYuan + chargeMoney;
			// 修改金额
			payMoneyMapper.updateRechargeMoney(userId, amountYuan);
			LOG.info(couponId);
			String couponCodeOld=couponMapper.queryCouponCodeBycouponId(couponId);
			LOG.info(couponCodeOld);
			Map activityInfo = eventMapper.getActivityInfoById(4);
			String startTime = VeDate.dateToStr( (java.sql.Timestamp)activityInfo.get("start_time"),"yyyy-MM-dd HH:mm:ss");
			String endTime = VeDate.dateToStr((java.sql.Timestamp)activityInfo.get("end_time"),"yyyy-MM-dd HH:mm:ss");
			int count=eventMapper.isRegistrationDuringTheEvent(userId,startTime,endTime);
			if(VeDate.getStringDate().compareTo(startTime)>0&&VeDate.getStringDate().compareTo(endTime)<0) {
				RequestCouponByUserId requestCouponByUserId = new RequestCouponByUserId();
				requestCouponByUserId.setUserId(userId);
				requestCouponByUserId.setGetType(1);
				String couponCode = "rechargeFeedbackNewGrabSheet";
				requestCouponByUserId.setCouponCode(couponCode);
				couponCode = couponMapper.queryIsGetCouponBefore(requestCouponByUserId);
				if (count>0&&couponCode == null) {
					int couponAmount = 0;
					if (amount == 500) {
						couponAmount = 5;
						couponService.insertBatchUserCoupon(10, userId, couponAmount, 0, DateUtil.getTime(), DateUtil.getAfterDayDate("2"), "充值回馈", 2, 3, 0);
						payMoneyService.addMessage(userId, 1, "奖励通知", role + "，充值回馈活动奖励的5星币无门槛抢单券*10张已发放到账户，请查收！");
						PushResult pushResult = JiguangPush.push(userId, "充值礼包赠送的优惠券已到账，请尽快使用！");
						if (pushResult != null && pushResult.isResultOK()) {
							LOG.info("针对" + userId + "的优惠券通知推送成功！");
						} else {
							LOG.info("针对" + userId + "的优惠券通知推送失败！");
						}
					}
					if (amount == 1000 || amount == 2000) {
						couponAmount = 10;
						couponService.insertBatchUserCoupon(5, userId, couponAmount, 0, DateUtil.getTime(), DateUtil.getAfterDayDate("2"), "充值回馈", 2, 3, 0);
						payMoneyService.addMessage(userId, 1, "奖励通知", role + "，充值回馈活动奖励的10星币无门槛抢单券*5张已发放到账户，请查收！");
						PushResult pushResult = JiguangPush.push(userId, "充值礼包赠送的优惠券已到账，请尽快使用！");
						if (pushResult != null && pushResult.isResultOK()) {
							LOG.info("针对" + userId + "的优惠券通知推送成功！");
						} else {
							LOG.info("针对" + userId + "的优惠券通知推送失败！");
						}
					}
					if (amount == 5000) {
						couponAmount = 20;
						couponService.insertBatchUserCoupon(5, userId, couponAmount, 0, DateUtil.getTime(), DateUtil.getAfterDayDate("2"), "充值回馈", 2, 3, 0);
						payMoneyService.addMessage(userId, 1, "奖励通知", role + "，充值回馈活动奖励的20星币无门槛抢单券*5张已发放到账户，请查收！");
						PushResult pushResult = JiguangPush.push(userId, "充值礼包赠送的优惠券已到账，请尽快使用！");
						if (pushResult != null && pushResult.isResultOK()) {
							LOG.info("针对" + userId + "的优惠券通知推送成功！");
						} else {
							LOG.info("针对" + userId + "的优惠券通知推送失败！");
						}
					}
					if (amount == 10000) {
						couponAmount = 20;
						couponService.insertBatchUserCoupon(10, userId, couponAmount, 0, DateUtil.getTime(), DateUtil.getAfterDayDate("2"), "充值回馈", 2, 3, 0);
						payMoneyService.addMessage(userId, 1, "奖励通知", role + "，充值回馈活动奖励的20星币无门槛抢单券*10张已发放到账户，请查收！");
						PushResult pushResult = JiguangPush.push(userId, "充值礼包赠送的优惠券已到账，请尽快使用！");
						if (pushResult != null && pushResult.isResultOK()) {
							LOG.info("针对" + userId + "的优惠券通知推送成功！");
						} else {
							LOG.info("针对" + userId + "的优惠券通知推送失败！");
						}
					}
				} else if (couponId == null || couponCodeOld == null || (couponCodeOld != null && !couponCodeOld.contains("rechargeFeedbackNew"))) {
					if (amount == 500) {
						couponService.insertBatchUserCoupon(3, userId, 10, 0, DateUtil.getTime(), DateUtil.getAfterDayDate("2"), "充值回馈", 2, 2, 30);
						couponService.insertBatchUserCoupon(3, userId, 10, 0, DateUtil.getTime(), DateUtil.getAfterDayDate("2"), "充值回馈", 2, 3, 0);
						payMoneyService.addMessage(userId, 1, "奖励通知", role + "，充值回馈活动奖励的满30减10星币抢单券*3张+10星币无门槛抢单券*3张已发放到账户，请查收！");
						PushResult pushResult = JiguangPush.push(userId, "充值礼包赠送的优惠券已到账，请尽快使用！");
						if (pushResult != null && pushResult.isResultOK()) {
							LOG.info("针对" + userId + "的优惠券通知推送成功！");
						} else {
							LOG.info("针对" + userId + "的优惠券通知推送失败！");
						}
					}
					if (amount == 1000) {
						couponService.insertBatchUserCoupon(5, userId, 10, 0, DateUtil.getTime(), DateUtil.getAfterDayDate("2"), "充值回馈", 2, 2, 30);
						couponService.insertBatchUserCoupon(3, userId, 10, 0, DateUtil.getTime(), DateUtil.getAfterDayDate("2"), "充值回馈", 2, 3, 0);
						payMoneyService.addMessage(userId, 1, "奖励通知", role + "，充值回馈活动奖励的满30减10星币抢单券*5张+10星币无门槛抢单券*3张已发放到账户，请查收！");
						PushResult pushResult = JiguangPush.push(userId, "充值礼包赠送的优惠券已到账，请尽快使用！");
						if (pushResult != null && pushResult.isResultOK()) {
							LOG.info("针对" + userId + "的优惠券通知推送成功！");
						} else {
							LOG.info("针对" + userId + "的优惠券通知推送失败！");
						}
					}
					if (amount == 2000) {
						couponService.insertBatchUserCoupon(6, userId, 10, 0, DateUtil.getTime(), DateUtil.getAfterDayDate("2"), "充值回馈", 2, 2, 30);
						couponService.insertBatchUserCoupon(3, userId, 10, 0, DateUtil.getTime(), DateUtil.getAfterDayDate("2"), "充值回馈", 2, 3, 0);
						payMoneyService.addMessage(userId, 1, "奖励通知", role + "，充值回馈活动奖励的满30减10星币抢单券*6张+10星币无门槛抢单券*3张已发放到账户，请查收！");
						PushResult pushResult = JiguangPush.push(userId, "充值礼包赠送的优惠券已到账，请尽快使用！");
						if (pushResult != null && pushResult.isResultOK()) {
							LOG.info("针对" + userId + "的优惠券通知推送成功！");
						} else {
							LOG.info("针对" + userId + "的优惠券通知推送失败！");
						}
					}
					if (amount == 5000) {
						couponService.insertBatchUserCoupon(6, userId, 20, 0, DateUtil.getTime(), DateUtil.getAfterDayDate("2"), "充值回馈", 2, 3, 0);
						payMoneyService.addMessage(userId, 1, "奖励通知", role + "，充值回馈活动奖励的20星币无门槛抢单券*6张已发放到账户，请查收！");
						PushResult pushResult = JiguangPush.push(userId, "充值礼包赠送的优惠券已到账，请尽快使用！");
						if (pushResult != null && pushResult.isResultOK()) {
							LOG.info("针对" + userId + "的优惠券通知推送成功！");
						} else {
							LOG.info("针对" + userId + "的优惠券通知推送失败！");
						}
					}
					if (amount == 10000) {
						couponService.insertBatchUserCoupon(15, userId, 20, 0, DateUtil.getTime(), DateUtil.getAfterDayDate("2"), "充值回馈", 2, 3, 0);
						payMoneyService.addMessage(userId, 1, "奖励通知", role + "，充值回馈活动奖励的20星币无门槛抢单券*15张已发放到账户，请查收！");
						PushResult pushResult = JiguangPush.push(userId, "充值礼包赠送的优惠券已到账，请尽快使用！");
						if (pushResult != null && pushResult.isResultOK()) {
							LOG.info("针对" + userId + "的优惠券通知推送成功！");
						} else {
							LOG.info("针对" + userId + "的优惠券通知推送失败！");
						}
					}
				}
			}
		}
	}

	// 查询余额
	public ResponseMoney queryAllMoney(String userId) {
		// 获取此用户当前剩余的积分
		Integer totalCredit = creditMapper.queryTotalCredit(userId);

		//获取用户的余额
		PayWallet allMoney = payMoneyMapper.queryAllMoney(userId);
		if (allMoney == null) {
			if(totalCredit != null){
				ResponseMoney result = new ResponseMoney();
				result.setTotalCredit(totalCredit);
				return result;
			}
			return null;
		} else {
			ResponseMoney result = new ResponseMoney();
			result.setAllMoney(allMoney.getmPresent() + allMoney.getmRecharge());
			result.setMoneyPresent(allMoney.getmPresent());
			result.setMoneyRecharge(allMoney.getmRecharge());
			if(totalCredit != null){
				result.setTotalCredit(totalCredit);
			}

			return result;
		}
	}

	// 个人消息添加到数据库
	public void addMessage(String userId, Integer type, String title, String message) {
		payMoneyMapper.addMessage(userId, type, title, message);
	}

	// 查询个人的充值记录
	public List<ResponseIncome> queryAllIncome(RequestAllIncome requestAllIncome) {
		int pageSize = requestAllIncome.getPageSize();
		int page = requestAllIncome.getPage() * pageSize;
		String userId = requestAllIncome.getUserId();
		List<ResponseIncome> allIncome = payMoneyMapper.queryAllIncome(page, pageSize, userId);
		return allIncome;
	}

	// 查询支出记录
	public List<ResponseOut> queryAllOutcome(RequestOutIncome requestOutIncome) {
		int pageSize = requestOutIncome.getPageSize();
		int page = requestOutIncome.getPage() * pageSize;
		String userId = requestOutIncome.getUserId();
		List<ResponseOut> out = payMoneyMapper.queryAllOutcome(page, pageSize, userId);
		return out;
	}

	// 抢单
	public ResponseFeedBackToChannels grabOrderByCouponGivingCredit(RequestGrabOrderByCoupon order) {
		String userId = order.getUserId();
		Long orderNumber = order.getOrderNo();
		String userName = userService.queryNameByUserId(userId);
		String role = userName.subSequence(0, 1) + "经理";

		// 再次查询此单有没有被抢
		Integer status = loanUserService.queryStatusByOrderNo(orderNumber);
		if (status != null && status == 0) {
			// 设置此订单已经被抢
			Integer isGrabbed = loanUserMapper.updateLoanUserStatus(orderNumber,1);
			if (isGrabbed != null && isGrabbed == 0) {
				return null;
			}

			Double orderPrice = order.getPrice();
			Double couponPrice = order.getCouponPrice();
			// 如果使用优惠券的话，把优惠券挪到已使用的表里
			String couponId = order.getCouponId();
			if (!StringUtils.isBlank(couponId)) {
				RequestCouponInfo requestCouponInfo = couponMapper.queryCouponInfo(couponId);
				Integer isUsed = couponMapper.addCouponUsed(requestCouponInfo);
				if (isUsed != null && isUsed == 0) {
					return null;
				}
				couponMapper.removeUsedCoupon(couponId);
			}
			//用户抢单送积分
			this.sendCreditForGrabOrder(order,role);
			ResponseFeedBackToChannels back = this.payForOrderByCoupon(role, orderNumber, orderPrice, couponPrice,
					userId);
			return back;
		} else {
			this.addMessage(userId, 1, "抢单失败",
					"亲爱的" + role + "：很遗憾，您和" + orderNumber + "的配对未成功，请把您的理想型（信用分，借款金额，借款日期）告诉小优，我们会第一时间为你推送合适的客户。");
			return null;
		}
	}
    //抢单限制计数+1
    public void grabLimitCounter(RequestGrabOrderByCoupon order){
		String userId = order.getUserId();
		long orderNo = order.getOrderNo();
		String userName = userService.queryNameByUserId(userId);
		long currentTime = System.currentTimeMillis();
		ResponseGrabLimit responseGrabLimit = userMapper.queryGrabLimitPram();
		int flag = 0;
		String reason = "";
		// 对用户抢单进行计数，如果1小时抢了N 次,限制用户不能抢单
		String countKey = "userGrabCount_" + userId;
		int countValue = 1;
		if (!redisCacheManager.hasKey(countKey)) {
			redisCacheManager.set(countKey, countValue, 60 * 60);// 有效时间为 1 小时
		} else {
			countValue = (int) redisCacheManager.get(countKey)+1;
			if (countValue < responseGrabLimit.getLimitHourCount()) {
				redisCacheManager.set(countKey, countValue , redisCacheManager.getExpire(countKey));
			} else {
				flag = 1;
				reason = "1小时抢" + responseGrabLimit.getLimitHourCount() + "单";
				redisCacheManager.set(countKey, 0, redisCacheManager.getExpire(countKey));// 有效时间为 1 小时
			}
		}

		//或者，抢单间隔连续小于等于x秒且大于等于m单
		if(flag == 0){
			//查看上次抢单的时间
			//Long lastGrabTime = orderMapper.getLastGrabTime(userId);
			//查看单子的生成时间
			Long orderTime = orderMapper.getOrderTimeByOrderNo(orderNo);
			if(orderTime != null ){
				String timeIntervalKey = "userGrabTimeInterval_"+userId;
				//如果订单生成时间小于指定的值，就继续计数，否则重新计数
				if(currentTime - orderTime <= responseGrabLimit.getLimitSecond()*1000){

					if(!redisCacheManager.hasKey(timeIntervalKey)){
						//如果上次抢单间隔没有在指定值内
						redisCacheManager.set(timeIntervalKey, 1, 60 * 60);
					}else{
						int timeIntervalCount = (int) redisCacheManager.get(timeIntervalKey)+1;
						if(timeIntervalCount < responseGrabLimit.getLimitSecondCount()){
							redisCacheManager.set(timeIntervalKey, timeIntervalCount, 60 * 60);
						}else{
							flag = 1;
							reason = "连续" + responseGrabLimit.getLimitSecondCount() + "单抢单间隔小于"+responseGrabLimit.getLimitSecond()+"秒";
							redisCacheManager.set(timeIntervalKey, 0, 60 * 60);
						}
					}
				}else{
					redisCacheManager.set(timeIntervalKey, 0, 60 * 60);
				}

			}
		}

		//或者 当日内，抢不同城市的单子，当抢第6个城市的单子时触发
		if(flag == 0){
			String city = loanUserMapper.getLoanCityByOrderNo(orderNo);
			String today=DateUtil.getDay();
			if(StringUtils.isNotBlank(city)){
				// 获取当前时间与当天最后时间相差的毫秒值
				long dayMis = 0;
				try {
					dayMis = sdfDay.parse(DateUtil.getDay()).getTime() + 1000 * 60 * 60 * 24 - 1
							- System.currentTimeMillis();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				HashSet<String> cityList = new HashSet<String>();
				String grabOtherCityKey = "grabOtherCity_"+userId+"_"+today;
				if(!redisCacheManager.hasKey(grabOtherCityKey)){
					cityList.add(city);
					redisCacheManager.set(grabOtherCityKey,cityList,dayMis/1000);
				}else{
					cityList = (HashSet<String>) redisCacheManager.get(grabOtherCityKey);
					cityList.add(city);
					if(cityList.size()>=responseGrabLimit.getLimitCity()){
						redisCacheManager.set(grabOtherCityKey,new HashSet<String>(),redisCacheManager.getExpire(grabOtherCityKey));
						flag = 1;
						reason = "当日抢第"+responseGrabLimit.getLimitCity()+"个城市的单子";
					}else{
						redisCacheManager.set(grabOtherCityKey,cityList,redisCacheManager.getExpire(grabOtherCityKey));
					}

				}

			}
		}


		if(flag == 1){
			long endTime = System.currentTimeMillis()+responseGrabLimit.getLimitHour()*60L*60*1000;
			long refundFailTime= System.currentTimeMillis()+responseGrabLimit.getLimitRefund()*60L*60*1000;
			//限制此用户抢单
			Integer limitResult = userMapper.limitUserGrabOrder(userId,reason,System.currentTimeMillis(),endTime,refundFailTime);
			if(limitResult != null && limitResult == 1){
				//给内部人员发短信
				String appName = "优接单";
				String phone = userMapper.getPhoneNumberByUserId(userId);
				long tpl_id = 2385218;
				String tpl_value = "";
				try {
					tpl_value = URLEncoder.encode("#name#", ENCODING) + "=" + URLEncoder.encode(userName, ENCODING)
							+ "&" + URLEncoder.encode("#phone#", ENCODING) + "="
							+ URLEncoder.encode(phone, ENCODING);

					Map<String, String> params = new HashMap<String, String>();
					params.put("apikey", "4e2bc3e7d93e529e99a25cf43f8a5a90");
					params.put("tpl_id", String.valueOf(tpl_id));
					params.put("tpl_value", tpl_value);
					// 分别对应 开发，老板，运营的手机号
					params.put("mobile", "18551220708,18896561077,15950026664,18351615340,15651518503");

					CloseableHttpClient client = HttpClients.createDefault();
					String responseText = "";
					CloseableHttpResponse response = null;
					HttpPost method = new HttpPost("https://sms.yunpian.com/v2/sms/tpl_batch_send.json");
					if (params != null) {
						List<NameValuePair> paramList = new ArrayList<NameValuePair>();
						for (Map.Entry<String, String> param : params.entrySet()) {
							NameValuePair pair = new BasicNameValuePair(param.getKey(), param.getValue());
							paramList.add(pair);
						}
						method.setEntity(new UrlEncodedFormEntity(paramList, ENCODING));
					}
					response = client.execute(method);
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						responseText = EntityUtils.toString(entity, ENCODING);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
	}
	//抢单送积分
	private void sendCreditForGrabOrder(RequestGrabOrderByCoupon order, String role) {
		String userId = order.getUserId();
		Double orderPrice = order.getPrice();
		Double couponPrice = order.getCouponPrice();
		// 如果用户抢的是原价单，抢满5单子原价单，送300积分，每日上限300积分
		if (order.getIsOriginalPrice() == 1) {
			Integer originalCount = creditMapper.getOriginalCountByUserId(userId);// 查看之前有没有抢到过原价单
			if (originalCount != null) {
				if (originalCount == 4) {// 抢满5单原价单，加300积分
					RequestCreditDetail requestCreditDetail = new RequestCreditDetail();
					requestCreditDetail.setUserId(userId);
					requestCreditDetail.setType(2);
					requestCreditDetail.setCreditValue(300);
					requestCreditDetail.setCreditPathway("抢单加成");
					requestCreditDetail.setCreditDetail("抢单加成");
					creditMapper.addCreditDetail(requestCreditDetail);// 添加一条积分明细
					creditMapper.updateCreditWallet(userId, 300);// 更新总积分
				}
				creditMapper.updateOriginalCount(userId);

			} else {
				creditMapper.insertOriginalCount(userId);
			}
			//活动期间内，每天抢N单最新发布的单子即可赠送X张元无门槛抢单优惠券
			this.sendCouponForGrabOrder(order,role);
		}

		// 用户抢单获得等值消耗的星币积分，每日上限10000积分。（只限原价单，包括打折）
		if ((order.getIsOriginalPrice() == 1 || order.getIsOriginalPrice() == 2) && couponPrice > 0) {
			// 设置key，value
			String reKey = "userGrabOrder_" + userId + "_" + DateUtil.getDay();
			int revalue = 0;
			long dayMis = 0;
			try {
				int creditValue = (new Double(couponPrice)).intValue();
				RequestCreditDetail requestCreditDetail = new RequestCreditDetail();
				requestCreditDetail.setUserId(userId);
				requestCreditDetail.setType(2);
				requestCreditDetail.setCreditValue(creditValue);
				requestCreditDetail.setCreditPathway("抢单");
				requestCreditDetail.setCreditDetail("抢单");
				// 获取当前时间与当天最后时间相差的毫秒值
				dayMis = sdfDay.parse(DateUtil.getDay()).getTime() + 1000 * 60 * 60 * 24 - 1
						- System.currentTimeMillis();
				// 查看今天有没有此人是否抢过单子
				if (redisCacheManager.hasKey(reKey)) { // 如果抢过

					revalue = (int) redisCacheManager.get(reKey);
					if (revalue < 10000) {
						if (revalue + couponPrice > 10000) {
							requestCreditDetail.setCreditValue(10000 - revalue);
							creditMapper.updateCreditWallet(userId, 10000 - revalue);
							revalue = 10000;
						} else {
							// 更新用户积分
							creditMapper.updateCreditWallet(userId, creditValue);
							revalue += couponPrice;
						}
						creditMapper.addCreditDetail(requestCreditDetail);
						redisCacheManager.set(reKey, revalue, dayMis / 1000);
					}
				} else { // 当天没有抢过订单
					creditMapper.addCreditDetail(requestCreditDetail);
					creditMapper.updateCreditWallet(userId, creditValue);
					revalue += couponPrice;
					// 设置此人当天的key，value
					redisCacheManager.set(reKey, revalue, dayMis / 1000);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}


	}

	//活动期间内，每天抢N单最新发布的单子即可赠送1张X元无门槛抢单优惠券
	private void sendCouponForGrabOrder(RequestGrabOrderByCoupon order,String role) {
		String userId = order.getUserId();
		Double orderPrice = order.getPrice();
		Double couponPrice = order.getCouponPrice();
		long time = System.currentTimeMillis();
		//查询当前时间是否有优惠券赠送
		Integer couponAmount = orderMapper.queryAmountAccordingTime(time);
		if(couponAmount != null && couponAmount > 0){
			//判断是不是工作日
			/*Date today = new Date();
			Calendar c=Calendar.getInstance();
			c.setTime(today);
			int weekday=c.get(Calendar.DAY_OF_WEEK);*/
			//if(weekday > 1 && weekday < 7){//如果当天是工作日
				if(orderPrice.equals(couponPrice) ){
					//用户抢的原价单，且未使用优惠券
					String reKey = "GrabOrderGetCoupon_" + userId + "_" + DateUtil.getDay();
					int revalue = 1;
					long dayMis = 0;
					try {
						// 查看今天此人是否抢过单子
						if (redisCacheManager.hasKey(reKey)) { // 如果抢过
							revalue = (int) redisCacheManager.get(reKey) + 1;
							if (revalue < 5) {
								if(revalue == 3){
									//为用户发送优惠券
									RequestCouponForNewUser requestCouponForNewUser = new RequestCouponForNewUser();
									requestCouponForNewUser.setCouponId( "QM" + idWorker.nextId());
									requestCouponForNewUser.setUserId(userId);
									requestCouponForNewUser.setCouponAmount(15);
									requestCouponForNewUser.setCouponFullreduction(30);
									requestCouponForNewUser.setEffectiveTime(DateUtil.getTime());
									requestCouponForNewUser.setExpirationTime(DateUtil.getAfterDayDate("1"));
									requestCouponForNewUser.setCouponChannel("抢单送豪礼");
									requestCouponForNewUser.setCouponType(2);
									requestCouponForNewUser.setCouponForm(2);
									Integer result = couponMapper.addCouponForGettingUser(requestCouponForNewUser);
									if(result != null && result == 1){
										orderMapper.updateLimitCouponForUser(userId);
										payMoneyMapper.addMessage(userId, 1, "奖励通知", role+"，抢单送券活动奖励的1张30元满减优惠券已发放到账户，请查收！");
									}
									//发推送
									PushResult pushResult = JiguangPush.push(userId,"恭喜您获得一张30元满减优惠券，请尽快使用！");
									if (pushResult != null && pushResult.isResultOK()) {
										LOG.info("针对" + userId + "的优惠券通知推送成功！");
									} else {
										LOG.info("针对" + userId + "的优惠券通知推送失败！");
									}
								}
							}else{
								//为用户发送优惠券
								RequestCouponForNewUser requestCouponForNewUser = new RequestCouponForNewUser();
								requestCouponForNewUser.setCouponId( "QW" + idWorker.nextId());
								requestCouponForNewUser.setUserId(userId);
								requestCouponForNewUser.setCouponAmount(couponAmount);
								requestCouponForNewUser.setEffectiveTime(DateUtil.getTime());
								requestCouponForNewUser.setExpirationTime(DateUtil.getAfterDayDate("1"));
								requestCouponForNewUser.setCouponChannel("抢单送豪礼");
								requestCouponForNewUser.setCouponType(2);
								requestCouponForNewUser.setCouponForm(3);
								Integer result = couponMapper.addCouponForGettingUser(requestCouponForNewUser);
								if(result != null && result == 1){

									orderMapper.updateNoLimitCouponForUser(userId);

									//记录用户获得的50无门槛优惠券
									payMoneyMapper.addMessage(userId, 1, "奖励通知", role+"，抢单送券活动奖励的1张"+couponAmount+"元无门槛优惠券已发放到账户，请查收！");
									//发推送
									PushResult pushResult = JiguangPush.push(userId,"恭喜您获得一张50元无门槛优惠券，请尽快使用！");
									if (pushResult != null && pushResult.isResultOK()) {
										LOG.info("针对" + userId + "的优惠券通知推送成功！");
									} else {
										LOG.info("针对" + userId + "的优惠券通知推送失败！");
									}

								}
								revalue = 0;

							}
							orderMapper.updateOrderAmountForUser(userId);
							redisCacheManager.set(reKey, revalue, redisCacheManager.getExpire(reKey));
						} else { // 当天没有抢过最新订单

							// 获取当前时间与当天最后时间相差的毫秒值
							dayMis = sdfDay.parse(DateUtil.getDay()).getTime() + 1000 * 60 * 60 * 24 - 1
									- System.currentTimeMillis();
							redisCacheManager.set(reKey, revalue, dayMis / 1000);
							//查看用户是否获取过优惠券
							Integer count = orderMapper.querySendCoupons(userId);
							if(count == 0){
								orderMapper.addOrderAmountForUser(userId);
							}else{
								orderMapper.updateOrderAmountForUser(userId);
							}

						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}

			//}

		}


		//查看当前抢最新发布的单子是否获得抽奖机会（11月活动）
		Integer orderCount = orderMapper.getOrderCountForLottery(time);//查看抢多少单可以参与抽奖
		if((orderCount != null && orderCount > 0)&&orderPrice.equals(couponPrice)) {
			String today =  DateUtil.getDay();
           String exclusionDate=orderMapper.getOrderCountForLotteryExclusionDate(time);
			if(StringUtils.isEmpty(exclusionDate)||!exclusionDate.contains(today)) { //排除非活动日期
				String reKey = "GrabOrderGetLottery_" + userId + "_" + today;
				int revalue = 1;
				long dayMis = 0;
				try {
					// 查看今天此人是否抢过单子
					Integer count = orderMapper.isHaveLotteryChance(userId, today);
					//if (redisCacheManager.hasKey(reKey)) {
						if (count!=null) {
							if(redisCacheManager.hasKey(reKey)) {
								revalue = (int) redisCacheManager.get(reKey) + 1;
							}
						   if (revalue % orderCount == 0) {
							orderMapper.updateLotteryChance(userId, today);
						   }
						redisCacheManager.set(reKey, revalue, redisCacheManager.getExpire(reKey));
						} else {
						orderMapper.addLotteryChance(userId);
						dayMis = sdfDay.parse(DateUtil.getDay()).getTime() + 1000 * 60 * 60 * 24 - 1
								- System.currentTimeMillis();
						redisCacheManager.set(reKey, revalue, dayMis / 1000);

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}


	}

	public ResponseFeedBackToChannels payForOrderByCoupon(String role, Long orderNumber, Double orderPrice,
			Double couponPrice, String userId) {
		String message = "亲爱的" + role + "：您已经成功获得订单为" + orderNumber + "的联系方式，能不能牵手成功，小优只能帮您到这里了~~~~祝您旗开得胜！";
		long time = new Date().getTime();
		// 查看此订单的channel和channelBranch
		ResponseConfirmPrice priceResult = orderMapper.queryOrderPrice(orderNumber);
		String channel = priceResult.getChannel();
		String channelBranch = priceResult.getChannelBranch();
		Integer valid = priceResult.getValid();
		// 抢单减去价格并且将这个订单添加到个人的订单表
		// 先用充值的,再用赠送的
		PayWallet result = userMapper.queryMoneyByUserId(userId);
		Double mrecharge = result.getmRecharge();
		// 消费掉的充值的星币
		Double trueMoney = null;
		if (mrecharge > couponPrice) {
			// 余额
			double balance = mrecharge - couponPrice;
			payMoneyMapper.updateRechargeMoney(userId, balance);
			if(valid != null && valid == 0){
				// 插入不同渠道消费的星币来源,该订单是用充值金额支付
				payMoneyMapper.addMoneySource2(orderNumber, couponPrice, 0.0, channel, channelBranch, userId,0);
			}else{
				payMoneyMapper.addMoneySource2(orderNumber, couponPrice, 0.0, channel, channelBranch, userId,1);
			}



			trueMoney = couponPrice;
		} else {
			// 先扣除充值的金额，在扣除赠送的金额
			double balance2 = result.getmPresent() - (couponPrice - mrecharge);
			// 修改数据库金额
			payMoneyMapper.updatePresentUserMoney(userId, balance2);
			if(valid != null && valid == 0){
				// 插入不同渠道消费的星币来源,该订单是用充值加赠送的一起支付的
				payMoneyMapper.addMoneySource2(orderNumber, mrecharge, couponPrice - mrecharge, channel, channelBranch,
						userId,0);
			}else{
				payMoneyMapper.addMoneySource2(orderNumber, mrecharge, couponPrice - mrecharge, channel, channelBranch,
						userId,1);
			}

			trueMoney = mrecharge;
		}

		System.out.println("trueMoney:====:"+trueMoney);
		//5元单子不给安信花结算
		/*if(orderPrice <= 5){
			trueMoney = 0.0;
		}*/
		// 将订单储存到抢单人的数据中
		orderMapper.addOrderByUserId(orderPrice, userId, orderNumber, time,IpUtil.getIpAddr(request));
		// 订单号随机生成的uuid+时间戳 WX的长度不能 超过32位
		String outNo = System.currentTimeMillis() + new SimpleDateFormat("yyyyMMdd").format(new Date());

		// 获取优惠券面额
		double couponamount = orderPrice - couponPrice;
		// 向支出表插入支出明细
		payMoneyMapper.addOutComeMoneyForCoupon(userId, orderNumber, outNo, "抢单", (double) orderPrice, couponamount);

		// 在抢单人的信息中添加已经抢到单的信息
		payMoneyMapper.addMessage(userId, 1, "抢单成功！", message);
		Date date = new Date();
		date.setTime(time);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = simpleDateFormat.format(date);
		ResponseFeedBackToChannels back = new ResponseFeedBackToChannels("", now, trueMoney,valid);

		return back;
	}

	/**
	 * @Author:Tina
	 * @Description:用户注册，送优惠券的同时，在用户余额表里插入一条记录，方便后面充值，更新不会报错
	 * @Date:2018年4月23日下午5:55:40
	 * @Param:
	 * @Return:void
	 */
	public void addPayRecord(String userId) {
		payMoneyMapper.addPayRecord(userId);

	}

}
