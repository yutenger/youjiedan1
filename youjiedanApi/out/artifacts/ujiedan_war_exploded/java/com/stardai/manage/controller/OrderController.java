package com.stardai.manage.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import com.stardai.manage.request.*;
import com.stardai.manage.response.*;
import com.stardai.manage.service.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.stardai.manage.bean.OssService;
import com.stardai.manage.bean.ResponseModel;
import com.stardai.manage.pojo.OrderTime;
import com.stardai.manage.pojo.PayMoney;
import com.stardai.manage.utils.Des;

import net.sf.json.JSONObject;

/**
 * @author jdw
 * @date 2017/10/16
 */
@Controller
@RequestMapping(value = "MZorder")
@SuppressWarnings("all")
public class OrderController extends BaseController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private UserService userService;

	@Autowired
	private PayMoneyService payMoneyService;

	@Autowired
	private LoanUserService loanUserService;

	@Autowired
	private FeedBackToChannelsService feedBackToChannelsService;

	@Autowired
	private Des des;

	@Autowired
	private CouponService couponService;

	@Autowired
	private OssService ossService;

	@Autowired
	private CreditService creditService;

	// 根据条件查询订单(1.5.0版本,筛选条件改变，订单筛选变多选，折扣专区单选，订单状态单选)
	@ResponseBody
	@RequestMapping(value = "queryOrdersByManyConditions", method = RequestMethod.POST)
	public ResponseModel queryOrdersByManyConditions(@RequestBody RequestOrdersByManyConditions conditions) {
		// 查询状态对应的时间
		List<OrderTime> orderTimeList = orderService.queryAllStatus();
		if (orderTimeList != null && orderTimeList.size() > 0) {
			List<ResponseLoanUserTransferList> loanUserList = orderService.queryOrdersByManyConditions(conditions,
					orderTimeList);
			if (loanUserList != null && loanUserList.size() > 0) {
				return ResponseModel.success(loanUserList);
			} else {
				return ResponseModel.success(new ArrayList());
			}
		} else {
			return ResponseModel.fail("查询失败");
		}
	}

	
	// 首页订单的查询订单详情
	@ResponseBody
	@RequestMapping(value = "queryOrderByNo2", method = RequestMethod.POST)
	public ResponseModel queryOrderByOrderNo(@RequestBody RequestOrderByUserId requestOrderByUserId) {
		if (requestOrderByUserId.getOrderNo() == 0) {
			return ResponseModel.success();
		}
		// 根据单子号查询订单的详细信息
		ResponseLoanUser loanUser = orderService.queryOrderByOrderNo(requestOrderByUserId);
		if (loanUser != null) {
			return ResponseModel.success(loanUser);
		}
		return ResponseModel.success();
	}

	// 抢单(加进程锁,使用优惠券，抢单送积分，1.5.0版本更新)
	@ResponseBody
	@RequestMapping(value = "grabOrderByCouponGivingCredit", method = RequestMethod.POST)
	public ResponseModel grabOrderByCouponGivingCredit(@RequestBody RequestGrabOrderByCoupon order,
			BindingResult bindingResult) {
		try {
			if (bindingResult.hasErrors()) {
				return ResponseModel.error(bindingResult);
			}
			String userId = order.getUserId();
			Long orderNum = order.getOrderNo();

			if (StringUtils.isBlank(userId) || StringUtils.isBlank(order.getDateTime())) {
				return ResponseModel.fail("网络异常,请稍后再试");
			}
			String name = userService.queryNameByUserId(userId);
			String role = name.subSequence(0, 1) + "经理";
			// 查询抢单的人是否已经认证
			boolean approveResult = userService.queryApproveByUserId(userId);
			if (!approveResult) {
				return ResponseModel.fail("未认证");
			}
			// 再次查询此单的原价是否正确
			RequestIsOriginalPrice requestIsOriginalPrice = orderService.queryOrderPrice2(order.getPrice(), orderNum);
			boolean priceResult = requestIsOriginalPrice.getPriceResult();
			order.setIsOriginalPrice(requestIsOriginalPrice.getIsOriginalPrice());

			// 查询此单使用优惠券后的价格是否正确
			boolean couponResult = true;
			String couponId = order.getCouponId();
			if (!StringUtils.isBlank(couponId)) {
				couponResult = couponService.queryCouponPrice(couponId, order.getPrice(), order.getCouponPrice());
			}

			if (!priceResult || !couponResult) {
				payMoneyService.addMessage(userId, 1, "抢单失败",
						"亲爱的" + role + "很遗憾，您和" + orderNum + "的配对未成功，请把您的理想型（信用分，借款金额，借款日期）告诉小优，我们会第一时间为你推送合适的客户。");
				return ResponseModel.fail("抢单失败");
			}

			// 查询此人账户金额是否够此次消费
			boolean amountResult = userService.queryMoneyByUserId(order.getCouponPrice(), userId);
			if (!amountResult) {
				return ResponseModel.fail("金额不足");
			}
			// 再查询单子有没有被抢然后完成操作
			ResponseFeedBackToChannels feedback = null;
			synchronized (POOL.intern(orderNum + "")) {
				feedback = payMoneyService.grabOrderByCouponGivingCredit(order);
			}
			if (feedback != null) {
				try {
					// 如果信贷经理是用充值的星币送的,就不返回数据
					if (feedback.getBuy_money() != 0.0) {
						// 查询单子的手机号,渠道,渠道细分并返回给来源渠道,即使失败了也不能影响信贷经理抢单
						HashMap<String, String> result = feedBackToChannelsService.queryMobile(orderNum);
						if (result != null) {
							String mobile = result.get("loan_phone");
							String channel = result.get("channel");
							String channelBranch = result.get("channel_branch");
							feedback.setMobile(mobile);
							// 先根据渠道名查询要不要给该渠道返回数据,如果要,则返回接口路径
							String url = feedBackToChannelsService.queryToggleSwitch(channel, channelBranch);
							if (StringUtils.isNotBlank(url)) {
								feedBackToChannelsService.feedback(feedback, url, channel, channelBranch);
							}
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				return ResponseModel.success("抢单成功");
			} else {
				return ResponseModel.fail("抱歉，单子刚刚被其他信贷经理抢走了，去看看别的~");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseModel.fail("网络异常,请稍后再试");
		}
	}

	//跟进中 // 已完成
	// 查询已抢的订单
	@ResponseBody
	@RequestMapping(value = "queryOrderListByUserId", method = RequestMethod.POST)
	public ResponseModel queryOrderListByUserId(@RequestBody RequestOrderListByUserId orderByUserId,
			BindingResult bindingResult) {
		if (orderByUserId.getStatus() == null || orderByUserId.getUserId() == null
				|| orderByUserId.getUserId().isEmpty()) {
			return ResponseModel.fail("查询失败");
		}
		// 根据userId查询所有订单
		List<ResponseOrderList> orderList = orderService.queryOrderListByUserId(orderByUserId);
		if (orderList != null && orderList.size() > 0) {
			return ResponseModel.success(orderList);
		}
		return ResponseModel.success();
	}

	// 我的单子的详情
	@ResponseBody
	@RequestMapping(value = "queryOrderByUserId", method = RequestMethod.POST)
	public ResponseModel queryOrderByUserId(@RequestBody RequestOrderByUserId orderByUserId,
			BindingResult bindingResult) {
		if (orderByUserId.getUserId().isEmpty() || orderByUserId.getUserId() == null) {
			return ResponseModel.fail("查询失败");
		}
		// 根据userId查询订单详情
		ResponseLoanUser order = orderService.queryOrderByUserId(orderByUserId);
		if (order != null) {
			return ResponseModel.success(order);
		}
		return ResponseModel.success();
	}

	// 跟单成功反馈，获得与该订单用户消耗的星币相同的积分
	@ResponseBody
	@RequestMapping(value = "feedBackSuccessForCredit", method = RequestMethod.POST)
	public ResponseModel feedBackSuccessForCredit(@RequestBody RequestFeedBackSuccess feedBackSuccess,
			BindingResult bindingResult) {

		if (feedBackSuccess.getOrderSuccessTerm() > 500 || feedBackSuccess.getOrderSuccesAmount() > 10000000) {
			return ResponseModel.fail("贷款金额超过1千万或者贷款期限超过500期");
		}
		if (feedBackSuccess.getOrderSuccessTime() == null || feedBackSuccess.getOrderSuccessTime().isEmpty()
				|| feedBackSuccess.getUserId() == null || feedBackSuccess.getUserId().isEmpty()
				|| feedBackSuccess.getOrderSuccessTerm() > 500 || feedBackSuccess.getOrderSuccesAmount() > 10000000) {
			return ResponseModel.fail("请填写正确信息");
		}
		Integer feedBackResult = orderService.feedBackSuccess(feedBackSuccess);
		if (feedBackResult == null || feedBackResult != 1) {
			return ResponseModel.fail("反馈失败");
		}
		// 根据订单号查询此单的花费的实际金额
		Double costMoney = orderService.getCostMoneyByOrderNo(feedBackSuccess.getOrderNo());

		if (costMoney != null && costMoney > 0) {
			int creditValue = (new Double(costMoney)).intValue();

			// 确认跟单成功，获得与该订单用户消耗的星币相同的积分。（只限原价单）
			RequestCreditDetail requestCreditDetail = new RequestCreditDetail();
			requestCreditDetail.setUserId(feedBackSuccess.getUserId());
			requestCreditDetail.setType(2);
			requestCreditDetail.setCreditValue(creditValue);
			requestCreditDetail.setCreditPathway("跟单成功");
			requestCreditDetail.setCreditDetail(feedBackSuccess.getOrderNo() + "");
			Integer isGet = creditService.isAddForFBSuccess(requestCreditDetail);// 先查询再添加，避免服务器延迟
			if (isGet == null) {
				creditService.addCreditDetail(requestCreditDetail);
				creditService.updateCreditWallet(feedBackSuccess.getUserId(), creditValue);
			}

		}

		return ResponseModel.success("提交跟单信息成功");
	}

	// 跟单失败反馈
	@ResponseBody
	@RequestMapping(value = "feedBackBad", method = RequestMethod.POST)
	public ResponseModel feedBackBad(@RequestBody RequestFeedBackBad feedBackBad, BindingResult bindingResult) {
		if (feedBackBad.getUserId() == null || feedBackBad.getUserId().isEmpty()
				|| feedBackBad.getOrderBadCause() == null || feedBackBad.getOrderBadCause().isEmpty()) {
			return ResponseModel.fail("请填写正确信息");
		}
		Integer feedBackResult = orderService.feedBackBad(feedBackBad);
		if (feedBackResult == null || feedBackResult != 1) {
			return ResponseModel.fail();
		}
		return ResponseModel.success("提交跟单信息成功");
	}

	// 充值金额的选择
	@ResponseBody
	@RequestMapping(value = "queryRechargeMoney", method = RequestMethod.GET)
	public ResponseModel queryRechargeMoney(HttpServletRequest request) {
		// 查询充值的金额与赠送的金额
		List<PayMoney> rechargeMoneyList = orderService.queryRechargeMoney();
		if (rechargeMoneyList != null && rechargeMoneyList.size() > 0) {
			return ResponseModel.success(rechargeMoneyList);
		} else {
			return ResponseModel.fail("查询失败");
		}
	}

	/**
	 * @Author : jokery
	 * @Description : 优接单信贷经理退单申请功能
	 * @Date : 2018/2/2 9:34
	 * @Param :refund
	 * @Return :
	 */
	@ResponseBody
	@RequestMapping(value = "refundApplication", method = RequestMethod.POST)
	public ResponseModel refundApplication(@RequestBody RequestRefundApplication refund, BindingResult bindingResult) {
		try {
			if (bindingResult.hasErrors()) {
				return ResponseModel.error(bindingResult);
			}
			Integer result = this.orderService.addRefundApplication(refund);
			if (result != null && result == 1) {
				return new ResponseModel(0, "退款申请提交成功", null);
			} else if (result == 2) {
				return ResponseModel.error("订单号不存在");
			} else if (result == 3) {
				return ResponseModel.error("不是原价单不能申请");
			} else if (result == 4) {
				return ResponseModel.error("该手机号不存在");
			}else if (result == 5) {
				return ResponseModel.error("不是原价单不能申请");
			}else {
				return ResponseModel.error("请勿重复提交退款申请");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseModel.fail("网络异常,请稍后再试");
		}
	}

	/**
	 * @Author : yax
	 * @Description : 获取OSS签名
	 * @Date : 2018/5/18
	 * @Param :refund
	 * @Return :
	 */
	@ResponseBody
	@RequestMapping(value = "getOssSignature")
	public ResponseModel getOssSignature() {
		JSONObject jobject = ossService.getOssSignature();
		if (jobject != null) {
			return ResponseModel.success(jobject);
		} else {
			return ResponseModel.fail("获取OSS签名失败");
		}
	}

	/**
	 * @Author : yax
	 * @Description :订单号检测
	 * @Date : 2018/5/18
	 * @Param :refund
	 * @Return :
	 */
	@ResponseBody
	@RequestMapping(value = "refundCheckOut")
	public ResponseModel refundCheckOut(String userId, String orderNo) {
		Integer status = -1;
		try {
			status = orderService.orderIdCheck(userId, orderNo);
			switch (status) {
			case 0:
				return ResponseModel.success();
			case 1:
				return new ResponseModel(status, "订单号不存在", null);
			case 2:
				return new ResponseModel(status, "退单已在申请中", null);
			case 3:
				return new ResponseModel(status, "已退单", null);
			case 4:
				return new ResponseModel(status, "退单被拒绝", null);
			default:
				return new ResponseModel(status, "服务器异常", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseModel(status, "服务器异常", null);
		}
	}

	/**
	 * @Author : yax
	 * @Description :根据订单号查询退单详情
	 * @Date : 2018/5/24
	 * @Param :orderNo
	 * @Return :
	 */
	@ResponseBody
	@RequestMapping(value = "getOrderRefundByOrderNo")
	public ResponseModel getOrderRefundByOrderNo(@RequestParam long orderNo) {
		try {
			ResponseOrderRefund response = orderService.queryOrderRefundByOrderNo(orderNo);
			return ResponseModel.success(response);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseModel.fail("加载失败");
		}
	}

	/**
	 * @Author : yax
	 * @Description :根据userId查询退单列表(分页)
	 * @Date : 2018/5/24
	 * @Param :
	 * @Return :
	 */
	@ResponseBody
	@RequestMapping(value = "getRefundUserOrderListByUserId")
	public ResponseModel getRefundUserOrderListByUserId(@RequestParam String userId, @RequestParam int pageSize,
			@RequestParam int page) {
		try {
			List<ResponseRefund> responseOrderList = orderService.queryUserOrderListByUserId(userId, pageSize, page);
			return ResponseModel.success(responseOrderList);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseModel.fail("加载失败");
		}
	}
}
