package com.stardai.manage.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pingplusplus.Pingpp;
import com.pingplusplus.exception.PingppException;
import com.pingplusplus.model.Charge;
import com.stardai.manage.bean.ResponseModel;
import com.stardai.manage.request.RequestAllIncome;
import com.stardai.manage.request.RequestOutIncome;
import com.stardai.manage.request.RequestPayRecharge;
import com.stardai.manage.response.ResponseIncome;
import com.stardai.manage.response.ResponseMoney;
import com.stardai.manage.response.ResponseOut;
import com.stardai.manage.service.CreditService;
import com.stardai.manage.service.PayMoneyService;
import com.stardai.manage.service.UserService;
import com.stardai.manage.service.UserSmsCodeService;
import com.stardai.manage.utils.Des;
import com.stardai.manage.utils.PingppConstants;

/**
 * @author jdw
 * @date 2017/10/16
 */
@Controller
@RequestMapping(value = "MZpay")
@SuppressWarnings("all")
public class PayMoneyController extends BaseController {

	// ping++支付 ApiKey
	private static String APIKEY;

	// ping++ 私钥
	private static String PRIVATEKEY;

	// ping++ appid
	public static String APPID;

	// 初始化属性
	static {
		APIKEY = PingppConstants.APIKEY;
		PRIVATEKEY = PingppConstants.PRIVATEKEY;
		APPID = PingppConstants.APPID;
	}

	@Autowired
	private PayMoneyService payMoneyService;

	@Autowired
	private UserSmsCodeService userSmsCodeService;

	@Autowired
	private UserService userService;

	@Autowired
	private Des des;
	
	@Autowired
	private CreditService creditService;
	
	/*
	 * @Autowired private PayPassWordUtils payPassWordUtils;
	 */

	// 查询余额
	@RequestMapping(value = "queryAllMoney", method = RequestMethod.GET)
	@ResponseBody
	public ResponseModel queryAllMoney(@RequestParam("userId") String userId) {
		ResponseMoney result = payMoneyService.queryAllMoney(userId);
		if (null == result) {
			return ResponseModel.success(new ResponseMoney());
		}
		return ResponseModel.success(result);
	}

	// 充值
	@RequestMapping(value = "payRecharge", method = RequestMethod.POST)
	@ResponseBody
	public String payRecharge(@RequestBody RequestPayRecharge responsePayRecharge, HttpServletRequest request) {
		if (responsePayRecharge.getPathway() == null || responsePayRecharge.getPathway().isEmpty()
				|| responsePayRecharge.getUserId() == null || responsePayRecharge.getUserId().isEmpty()) {
			return null;
		}
		Pingpp.apiKey = APIKEY;
		Pingpp.privateKey = PRIVATEKEY;
		// 将元转换成分
		int amount = responsePayRecharge.getAmount() * 100;
		// 充值标题
		String subject = "星币充值";
		// 充值内容
		String body = "星币充值" + responsePayRecharge.getAmount() + "元";
		// appIp地址
		String clientIp = responsePayRecharge.getClientIp();
		// 订单号随机生成的uuid+时间戳 WX的长度不能 超过32位
		String incomeNo = new SimpleDateFormat("yyyyMMdd").format(new Date()) + System.currentTimeMillis();
		// userId
		String userId = responsePayRecharge.getUserId();
		// 支付方式
		String channel = responsePayRecharge.getPathway();
		Map<String, Object> chargeMap = new HashMap<String, Object>();
		// 某些渠道需要添加extra参数，具体参数详见接口文档
		chargeMap.put("amount", amount);
		chargeMap.put("currency", "cny");
		chargeMap.put("subject", subject);
		chargeMap.put("body", body);
		chargeMap.put("order_no", incomeNo);
		chargeMap.put("channel", channel);
		chargeMap.put("client_ip", clientIp);
		chargeMap.put("description", userId);
		Calendar cal = Calendar.getInstance();
		// 15分钟失效
		cal.add(Calendar.MINUTE, 15);
		long timestamp = cal.getTimeInMillis() / 1000L;
		chargeMap.put("time_expire", timestamp);
		Map<String, String> app = new HashMap<String, String>();
		app.put("id", APPID);
		chargeMap.put("app", app);
		String chargeString = null;
		try {
			// 发起交易请求
			Charge charge = Charge.create(chargeMap);
			chargeString = charge.toString();
		} catch (PingppException e) {
			e.printStackTrace();
		}
		return chargeString;
	}

	// 监听并接收 Webhooks 通知
	@RequestMapping(value = "webHooks", method = RequestMethod.POST)
	@ResponseBody
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF8");
		// 获取头部所有信息
		Enumeration headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = (String) headerNames.nextElement();
			String value = request.getHeader(key);
		}
		// 获得 http body 内容
		BufferedReader reader = request.getReader();
		StringBuffer eventJson = new StringBuffer();
		String string;
		while ((string = reader.readLine()) != null) {
			eventJson.append(string);
		}
		reader.close();
		// 解析异步通知数据
		JSONObject event = JSON.parseObject(eventJson.toString());
		// 这里舍去验证签名
		if ("charge.succeeded".equals(event.get("type"))) {
			JSONObject data = JSON.parseObject(event.get("data").toString());
			JSONObject object = JSON.parseObject(data.get("object").toString());
			// 自己设置的订单号
			String incomeNo = (String) object.get("order_no");
			// 支付渠道
			String channel = (String) object.get("channel");
			// 前面传对象的时候将userId传过来
			String userId = (String) object.get("description");
			// ping++扣款,精确到分，而数据库精确到元
			int amountFen = (int) object.get("amount");
			Double amountYuan = amountFen * 1.0 / 100;
			if (channel.equals("alipay")) {
				channel = "支付宝购买星币";
			} else if (channel.equals("wx")) {
				channel = "微信购买星币";
			} else {
				channel = "其他";
			}
			// 向统计收入表添加数据 修改金额 先查询金额然后再加
			payMoneyService.addPayRechargeMessageAndUpdateRechargeMoney(userId, incomeNo, channel, amountYuan);
			
			
			
			response.setStatus(200);
		} else {
			response.setStatus(500);
		}
	}

	// 查询个人的充值记录
	@RequestMapping(value = "queryAllIncome", method = RequestMethod.POST)
	@ResponseBody
	public ResponseModel queryAllIncome(@RequestBody RequestAllIncome requestAllIncome, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResponseModel.error(bindingResult);
		}
		List<ResponseIncome> allIncome = payMoneyService.queryAllIncome(requestAllIncome);
		if (allIncome != null && allIncome.size() > 0) {
			return ResponseModel.success(allIncome);
		}
		return ResponseModel.success(new ArrayList());
	}

	// 查询支出记录及详情
	@RequestMapping(value = "queryAllOut", method = RequestMethod.POST)
	@ResponseBody
	public ResponseModel queryAllOutcome(@RequestBody RequestOutIncome requestOutIncome, BindingResult bindingResult) {
		List<ResponseOut> allOut = payMoneyService.queryAllOutcome(requestOutIncome);
		if (allOut != null && allOut.size() > 0) {
			return ResponseModel.success(allOut);
		}
		return ResponseModel.success(new ArrayList());
	}

}
