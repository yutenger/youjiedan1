package com.stardai.manage.controller;

import com.stardai.manage.bean.ResponseModel;
import com.stardai.manage.request.RequestLoanUserInfo;
import com.stardai.manage.response.ResponseLoanOrder;
import com.stardai.manage.service.LoanUserService;
import com.stardai.manage.constants.Constants;
import com.stardai.manage.utils.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jokery
 * @date 2017/12/13
 */
@Controller
@RequestMapping(value = "MZloanUserNew")
@SuppressWarnings("all")
public class LoanUserNewController extends BaseController {

	@Autowired
	private LoanUserService loanUserService;


	// 小星借款APP添加借款信息
	@RequestMapping(value = "insertLoanInfo", method = RequestMethod.POST)
	@ResponseBody
	public ResponseModel insertLoanInfo(@RequestBody HashMap<String, Object> loanUserInfo) {
		try {
			// 先将用户提交的借款申请中的所在城市到数据库中的城市名统一表中查一下转换成统一的城市名叫法
			String unifiedLocation = loanUserService.unifyLocations((String) (loanUserInfo.get("location")));
			loanUserInfo.put("location", unifiedLocation);
			long orderNo = loanUserService.insertLoanUserInfo(loanUserInfo);
			return ResponseModel.success(orderNo);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseModel.fail(e.getMessage());
		}
	}

	// 小星借款APP添加借款信息
	@RequestMapping(value = "insertLoanInfo2", method = RequestMethod.POST)
	@ResponseBody
	public ResponseModel insertLoanInfo2(@RequestBody RequestLoanUserInfo loanUserInfo) {
		try {

			long orderNo = loanUserService.insertLoanInfo(loanUserInfo);
			return ResponseModel.success(orderNo);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseModel.fail(e.getMessage());
		}
	}

	// 小星借款APP查询他的申请贷款的信息
	@RequestMapping(value = "queryOrder", method = RequestMethod.GET)
	@ResponseBody
	public ResponseModel queryOrder(@RequestParam("orderNo") String order, HttpServletRequest request) {
		long orderNo = Long.parseLong(order);
		// 直接到贷款经理order表中查询是否存在
		ResponseLoanOrder loanOrder = loanUserService.queryOrder(orderNo);
		if (loanOrder != null) {
			return ResponseModel.success(loanOrder);
		} else {
			return ResponseModel.fail();
		}

	}

	// 查询此单有没有被抢
	@RequestMapping(value = "queryStatusByOrderNo", method = RequestMethod.GET)
	@ResponseBody
	public ResponseModel queryStatusByOrderNo(@RequestParam("orderNo") long orderNo) {
		if (orderNo == 0) {
			return ResponseModel.fail();
		}
		// 根据订单号查询订单状态
		Integer status = loanUserService.queryStatusByOrderNo(orderNo);
		if (status == null) {
			return ResponseModel.fail();
		} else {
			Map loanuserInfo = new HashMap();
			loanuserInfo.put("status", status);
			return ResponseModel.success(loanuserInfo);
		}
	}

	// 设置此单已经被抢，使用key值验证
	@RequestMapping(value = "updateLoanUserStatus", method = RequestMethod.POST)
	@ResponseBody
	public ResponseModel updateLoanUserStatus(@RequestBody HashMap<String, Object> loanUserInfo) {
		long orderNo = (long) loanUserInfo.get("orderNo");
		if (orderNo == 0) {
			return ResponseModel.fail();
		}
		String key = (String) loanUserInfo.get("key");
		String localKey = Utils.getMd5(orderNo + Constants.UPDATE_STATUS_KEY);
		if (localKey.equals(key)) {// 校验成功才能进行后续操作
			// 根据订单号查询订单状态
			Integer status = loanUserService.queryStatusByOrderNo(orderNo);
			if (status == null) {
				return ResponseModel.fail("查不到此订单");
			} else {
				// 设置此订单已经被抢
				Integer isGrabbed = loanUserService.updateLoanUserStatus(orderNo);
				if (isGrabbed != null && isGrabbed == 1) {
					return ResponseModel.success();
				}

			}
		}

		return ResponseModel.fail("网络异常，请稍后再试");
	}

}
