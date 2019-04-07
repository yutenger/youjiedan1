package com.stardai.manage.service;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

import com.stardai.manage.response.*;
import com.stardai.manage.utils.DateUtil;
import com.stardai.manage.utils.RedisCacheManager;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.stardai.manage.mapper.CouponMapper;
import com.stardai.manage.mapper.CreditMapper;
import com.stardai.manage.mapper.LoanUserMapper;
import com.stardai.manage.mapper.OrderMapper;
import com.stardai.manage.mapper.PayMoneyMapper;
import com.stardai.manage.mapper.UserMapper;
import com.stardai.manage.pojo.PayWallet;
import com.stardai.manage.request.RequestAllIncome;
import com.stardai.manage.request.RequestCouponInfo;
import com.stardai.manage.request.RequestCreditDetail;
import com.stardai.manage.request.RequestGrabOrder;
import com.stardai.manage.request.RequestGrabOrderByCoupon;
import com.stardai.manage.request.RequestOutIncome;

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

	private static SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");

	private static String ENCODING = "UTF-8";

	// 根据手机号与用户id查询是否一致
	public Integer queryUser(String mobileNumber, String userId) {
		Integer result = payMoneyMapper.queryUser(mobileNumber, userId);
		return result;
	}

	// 设置支付密码
	public Integer addPayPd(String pd, String userId) {
		Integer result = payMoneyMapper.addPayPd(pd, userId);
		return result;
	}

	// 修改支付密码
	public Integer updatePayPd(String pd, String userId) {
		Integer result = payMoneyMapper.updatePayPd(pd, userId);
		return result;
	}

	// 注册赠送星币 向统计收入表添加数据
	public void addPayPresentMessage(String userId) {
		double money = 15;
		String pathWay = "注册";
		// 向收入表中存数据
		payMoneyMapper.addPayPresentMessage(userId, "注册赠送", pathWay, money, 0);
		// payMoneyMapper.addPayPresentMessage(userId,money, pathWay);
		// 向金额表中添加数据
		payMoneyMapper.addPresentMoney(userId, money);
	}

	/*
	 * // 向充值表中添加数据 public void addRechargeMoney(String userId, String orderNo,
	 * String channel, Double amountYuan) {
	 * payMoneyMapper.addRechargeMoney(userId, orderNo, channel, amountYuan); }
	 */

	// 向统计收入表添加数据
	public void addPayRechargeMessageAndUpdateRechargeMoney(String userId, String incomeNo, String channel,
			double amountYuan) {
		// 由于时间延迟webhooks通知会在未接收到返回200的状态吗之后会再次调用方法
		// 先去查询是否已经添加过这条数据
		String name = userService.queryNameByUserId(userId);
		String role = name.subSequence(0, 1) + "经理";
		Integer result = payMoneyMapper.queryIncomeMessage(userId, incomeNo);
		if (result == 0) {
			// 获取本次充值金额
			int amount = (int) amountYuan;
			// 根据充值的金额去查询赠送的金额
			int addMoney = payMoneyMapper.queryAddMoney(amount);
			// 加上赠送金额
			payMoneyMapper.updatePresentMoney(userId, addMoney);
			// 向充值记录表中添加充值记录
			if (addMoney != 0) {
				payMoneyMapper.addPayPresentMessage(userId, "充值赠送", "充值", Double.valueOf(addMoney + ""), 0);
			}

			// 查询当前时间是否有充值加赠活动
			Integer extraMoney = payMoneyMapper.queryExtraMoney(amount, System.currentTimeMillis());
			if (extraMoney != null && extraMoney > 0) {
				// 加上赠送金额
				payMoneyMapper.updatePresentMoney(userId, extraMoney);
				payMoneyMapper.addPayPresentMessage(userId, "活动奖励", "活动奖励", Double.valueOf(extraMoney + ""), 0);
			}

			payMoneyMapper.addPayPresentMessage(userId, incomeNo, channel, amountYuan, 1);
			// 向个人信息中添加充值的通知信息
			payMoneyMapper.addMessage(userId, 1, "充值成功！",
					"亲爱的" + role + "：您的星币" + amountYuan + "已经到账，请到优接单首页开始您的抢单之旅吧！");
			// 根据userId查询当前该充值人的余额(不含赠送的)
			int chargeMoney = payMoneyMapper.queryRechargeMoney(userId);
			amountYuan = amountYuan + chargeMoney;
			// 修改金额
			payMoneyMapper.updateRechargeMoney(userId, amountYuan);
		}
	}

	// 查询余额
	public ResponseMoney queryAllMoney(String userId) {
		PayWallet allMoney = payMoneyMapper.queryAllMoney(userId);
		if (allMoney == null) {
			return null;
		} else {
			ResponseMoney result = new ResponseMoney();
			result.setAllMoney(allMoney.getmPresent() + allMoney.getmRecharge());
			result.setMoneyPresent(allMoney.getmPresent());
			result.setMoneyRecharge(allMoney.getmRecharge());
			return result;
		}
	}

	/*
	 * // 修改金额 public void updateRechargeMoney(String userId, Double amountYuan)
	 * { // 根据userId查询金额 int chargeMoney =
	 * payMoneyMapper.queryRechargeMoney(userId); amountYuan = amountYuan +
	 * chargeMoney; // 修改金额 payMoneyMapper.updateRechargeMoney(userId,
	 * amountYuan); }
	 */

	// 个人消息添加到数据库
	public void addMessage(String userId, Integer type, String title, String message) {
		payMoneyMapper.addMessage(userId, type, title, message);
	}

	// 抢单减去价格并且将这个订单添加到个人的数据库并添加消费消息
	public void grabOrder(RequestGrabOrder orderNo) {
		String name = userService.queryNameByUserId(orderNo.getUserId());
		String role = name.subSequence(0, 1) + "经理";
		String userId = orderNo.getUserId();
		String message = "亲爱的" + role + "：您已经成功获得订单为" + orderNo.getOrderNo() + "的联系方式，能不能牵手成功，小优只能帮您到这里了~~~~祝您旗开得胜！";
		long time = new Date().getTime();

		// 查看此订单的channel
		ResponseConfirmPrice priceResult = orderMapper.queryOrderPrice(orderNo.getOrderNo());
		String channel = priceResult.getChannel();

		// 查询用户钱包金额,先消费充值的,不够才消费赠送的
		PayWallet result = userMapper.queryMoneyByUserId(userId);
		if (result.getmRecharge() > orderNo.getPrice()) {
			// 余额
			double balance = result.getmRecharge() - orderNo.getPrice();
			payMoneyMapper.updateRechargeMoney(userId, balance);
			// 插入不同渠道消费的星币来源,该订单是用充值金额支付
			payMoneyMapper.addMoneySource(orderNo.getOrderNo(), orderNo.getPrice(), 0.0, channel, userId);

		} else {
			// 先扣除充值的金额，在扣除赠送的金额
			double result2 = result.getmPresent() - (orderNo.getPrice() - result.getmRecharge());
			// 修改数据库金额
			payMoneyMapper.updatePresentUserMoney(userId, result2);
			// 插入不同渠道消费的星币来源,该订单是用充值加赠送的一起支付的
			payMoneyMapper.addMoneySource(orderNo.getOrderNo(), result.getmRecharge(),
					orderNo.getPrice() - result.getmRecharge(), channel, userId);
		}
		// 将订单储存到抢单人的数据中
		orderMapper.addOrderByUserId(orderNo.getPrice(), orderNo.getUserId(), orderNo.getOrderNo(), time);
		// 设置此订单已经被抢
		loanUserMapper.updateLoanUserStatus(orderNo.getOrderNo());
		// 添加消费信息
		// payMoneyMapper.addPayPresentMessage(userId, "-" + orderNo.getPrice()
		// + ".00", "抢单");
		// 订单号随机生成的uuid+时间戳 WX的长度不能 超过32位
		String outNo = System.currentTimeMillis() + new SimpleDateFormat("yyyyMMdd").format(new Date());
		payMoneyMapper.addOutComeMoney(userId, orderNo.getOrderNo(), outNo, "抢单", (double) orderNo.getPrice());
		// 在抢单人的信息中添加已经抢到单的信息
		payMoneyMapper.addMessage(orderNo.getUserId(), 1, "抢单成功！", message);
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

	// 抢单减去价格并且将这个订单添加到个人的数据库并添加消费消息(统计细分到channelBranch)
	public ResponseFeedBackToChannels grabOrder2(RequestGrabOrder orderNo) {
		String userId = orderNo.getUserId();
		Long orderNumber = orderNo.getOrderNo();
		String role = userService.queryNameByUserId(userId).subSequence(0, 1) + "经理";
		// 再次查询此单有没有被抢
		Integer status = loanUserService.queryStatusByOrderNo(orderNumber);
		if (status != null && status == 0) {
			// 设置此订单已经被抢
			Integer isGrabbed = loanUserMapper.updateLoanUserStatus(orderNumber);
			if (isGrabbed != null && isGrabbed == 0) {
				return null;
			}
			Double orderPrice = orderNo.getPrice();
			ResponseFeedBackToChannels back = this.payForOrder(role, orderNumber, orderPrice, userId);
			return back;
		} else {
			this.addMessage(userId, 1, "抢单失败",
					"亲爱的" + role + "：很遗憾，您和" + orderNumber + "的配对未成功，请把您的理想型（信用分，借款金额，借款日期）告诉小优，我们会第一时间为你推送合适的客户。");
			return null;
		}
	}

	// 抢单减去价格并且将这个订单添加到个人的数据库并添加消费消息(统计细分到channelBranch,使用优惠券版本)
	public ResponseFeedBackToChannels grabOrderByCoupon(RequestGrabOrderByCoupon order) {
		String userId = order.getUserId();
		Long orderNumber = order.getOrderNo();
		String role = userService.queryNameByUserId(userId).subSequence(0, 1) + "经理";
		// 再次查询此单有没有被抢
		Integer status = loanUserService.queryStatusByOrderNo(orderNumber);
		if (status != null && status == 0) {
			// 设置此订单已经被抢
			Integer isGrabbed = loanUserMapper.updateLoanUserStatus(orderNumber);
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

			ResponseFeedBackToChannels back = this.payForOrderByCoupon(role, orderNumber, orderPrice, couponPrice,
					userId);
			return back;
		} else {
			this.addMessage(userId, 1, "抢单失败",
					"亲爱的" + role + "：很遗憾，您和" + orderNumber + "的配对未成功，请把您的理想型（信用分，借款金额，借款日期）告诉小优，我们会第一时间为你推送合适的客户。");
			return null;
		}
	}

	// 抢单送积分
	public ResponseFeedBackToChannels grabOrderByCouponGivingCredit(RequestGrabOrderByCoupon order) {
		String userId = order.getUserId();
		Long orderNumber = order.getOrderNo();
		String userName = userService.queryNameByUserId(userId);
		String role = userName.subSequence(0, 1) + "经理";

		// 再次查询此单有没有被抢
		Integer status = loanUserService.queryStatusByOrderNo(orderNumber);
		if (status != null && status == 0) {
			// 设置此订单已经被抢
			Integer isGrabbed = loanUserMapper.updateLoanUserStatus(orderNumber);
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
				//8月6日-8月24日，每天抢3单最新发布的单子即可赠送1张10元无门槛抢单优惠券
				this.sendCouponForGrabOrder(order);
			}
			// 用户抢单获得等值消耗的星币积分，每日上限10000积分。（只限原价单，包括打折）
			if ((order.getIsOriginalPrice() == 1 || order.getIsOriginalPrice() == 2) && couponPrice > 0) {
				// 使用redis来实现
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

			// 对用户抢单进行计数，如果1小时抢了50次，就对内部人员发短信
			String countKey = "userGrabCount_" + userId;
			int countValue = 1;
			if (redisCacheManager.hasKey(countKey)) {
				countValue = (int) redisCacheManager.get(countKey);
				if (countValue < 50) {
					redisCacheManager.set(countKey, countValue + 1, redisCacheManager.getExpire(countKey));
				} else {
					String appName = "优接单";
					String phone = userService.getPhoneNumberByUserId(userId);
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
						params.put("mobile", "18375337360,18551220708,18896561077,15950026664,18351615340,15651518503");

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
					redisCacheManager.set(countKey, 0, 60 * 60);// 有效时间为 1 小时
				}

			} else {
				redisCacheManager.set(countKey, countValue, 60 * 60);// 有效时间为 1
																		// 小时
			}

			ResponseFeedBackToChannels back = this.payForOrderByCoupon(role, orderNumber, orderPrice, couponPrice,
					userId);
			return back;
		} else {
			this.addMessage(userId, 1, "抢单失败",
					"亲爱的" + role + "：很遗憾，您和" + orderNumber + "的配对未成功，请把您的理想型（信用分，借款金额，借款日期）告诉小优，我们会第一时间为你推送合适的客户。");
			return null;
		}
	}

	//8月6日-8月24日，每天抢3单最新发布的单子即可赠送1张10元无门槛抢单优惠券
	private void sendCouponForGrabOrder(RequestGrabOrderByCoupon order) {
		String userId = order.getUserId();
		Double orderPrice = order.getPrice();
		Double couponPrice = order.getCouponPrice();
		long time = System.currentTimeMillis();
		if(time >= 1533484800000L && time <= 1535126399000L){
			//判断是不是工作日
			Date today = new Date();
			Calendar c=Calendar.getInstance();
			c.setTime(today);
			int weekday=c.get(Calendar.DAY_OF_WEEK);
			if(weekday > 1 && weekday < 7){//如果当天是工作日
				if(orderPrice.equals(couponPrice) ){
					//用户抢的原价单，且未使用优惠券
					String reKey = "GrabOrderGetCoupon_" + userId + "_" + DateUtil.getDay();
					int revalue = 0;
					long dayMis = 0;
					/*try {

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
					}*/

				}

			}

		}

	}

	public ResponseFeedBackToChannels payForOrder(String role, Long orderNumber, Double orderPrice, String userId) {
		String message = "亲爱的" + role + "：您已经成功获得订单为" + orderNumber + "的联系方式，能不能牵手成功，小优只能帮您到这里了~~~~祝您旗开得胜！";
		long time = new Date().getTime();
		// 查看此订单的channel和channelBranch
		ResponseConfirmPrice priceResult = orderMapper.queryOrderPrice(orderNumber);
		String channel = priceResult.getChannel();
		String channelBranch = priceResult.getChannelBranch();
		// 抢单减去价格并且将这个订单添加到个人的订单表
		// 先用充值的,再用赠送的
		PayWallet result = userMapper.queryMoneyByUserId(userId);
		Double mrecharge = result.getmRecharge();
		// 消费掉的充值的星币
		Double trueMoney = null;
		if (mrecharge > orderPrice) {
			// 余额
			double balance = mrecharge - orderPrice;
			payMoneyMapper.updateRechargeMoney(userId, balance);
			// 插入不同渠道消费的星币来源,该订单是用充值金额支付
			payMoneyMapper.addMoneySource2(orderNumber, orderPrice, 0.0, channel, channelBranch, userId);
			trueMoney = orderPrice;
		} else {
			// 先扣除充值的金额，在扣除赠送的金额
			double balance2 = result.getmPresent() - (orderPrice - mrecharge);
			// 修改数据库金额
			payMoneyMapper.updatePresentUserMoney(userId, balance2);
			// 插入不同渠道消费的星币来源,该订单是用充值加赠送的一起支付的
			payMoneyMapper.addMoneySource2(orderNumber, mrecharge, orderPrice - mrecharge, channel, channelBranch,
					userId);
			trueMoney = mrecharge;
		}
		// 将订单储存到抢单人的数据中
		orderMapper.addOrderByUserId(orderPrice, userId, orderNumber, time);
		// 订单号随机生成的uuid+时间戳 WX的长度不能 超过32位
		String outNo = System.currentTimeMillis() + new SimpleDateFormat("yyyyMMdd").format(new Date());
		payMoneyMapper.addOutComeMoney(userId, orderNumber, outNo, "抢单", (double) orderPrice);
		// 在抢单人的信息中添加已经抢到单的信息
		payMoneyMapper.addMessage(userId, 1, "抢单成功！", message);
		Date date = new Date();
		date.setTime(time);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = simpleDateFormat.format(date);
		ResponseFeedBackToChannels back = new ResponseFeedBackToChannels("", now, trueMoney);
		return back;
	}

	public ResponseFeedBackToChannels payForOrderByCoupon(String role, Long orderNumber, Double orderPrice,
			Double couponPrice, String userId) {
		String message = "亲爱的" + role + "：您已经成功获得订单为" + orderNumber + "的联系方式，能不能牵手成功，小优只能帮您到这里了~~~~祝您旗开得胜！";
		long time = new Date().getTime();
		// 查看此订单的channel和channelBranch
		ResponseConfirmPrice priceResult = orderMapper.queryOrderPrice(orderNumber);
		String channel = priceResult.getChannel();
		String channelBranch = priceResult.getChannelBranch();
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
			// 插入不同渠道消费的星币来源,该订单是用充值金额支付
			payMoneyMapper.addMoneySource2(orderNumber, couponPrice, 0.0, channel, channelBranch, userId);
			trueMoney = orderPrice;
		} else {
			// 先扣除充值的金额，在扣除赠送的金额
			double balance2 = result.getmPresent() - (couponPrice - mrecharge);
			// 修改数据库金额
			payMoneyMapper.updatePresentUserMoney(userId, balance2);
			// 插入不同渠道消费的星币来源,该订单是用充值加赠送的一起支付的
			payMoneyMapper.addMoneySource2(orderNumber, mrecharge, couponPrice - mrecharge, channel, channelBranch,
					userId);
			trueMoney = mrecharge;
		}
		// 将订单储存到抢单人的数据中
		orderMapper.addOrderByUserId(orderPrice, userId, orderNumber, time);
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
		ResponseFeedBackToChannels back = new ResponseFeedBackToChannels("", now, trueMoney);
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
