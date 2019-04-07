package com.stardai.manage.controller;

import com.stardai.manage.bean.ResponseModel;
import com.stardai.manage.config.Log;
import com.stardai.manage.request.RequestLoanUserInfo;
import com.stardai.manage.response.ResponseLoanOrder;
import com.stardai.manage.service.LoanUserService;
import com.stardai.manage.constants.Constants;
import com.stardai.manage.service.LoanUserServiceNew;
import com.stardai.manage.utils.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	@Autowired
	private LoanUserServiceNew loanUserServiceNew;

	protected static final Logger LOG = LoggerFactory.getLogger(LoanUserNewController.class);


	// 往loan_info 表里面插数据，2.0开始使用
	@RequestMapping(value = "insertLoanInfo", method = RequestMethod.POST)
	@ResponseBody
	public ResponseModel insertLoanInfo(@RequestBody RequestLoanUserInfo loanUserInfo) {
		try {
			System.out.println(loanUserInfo);
			long orderNo = loanUserService.insertLoanInfo(loanUserInfo);
			return ResponseModel.success(orderNo);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.info(e.getMessage());
			return ResponseModel.fail(e.getMessage());
		}
	}

	// 往loan_info 表里面插数据，新功能开始使用
	@RequestMapping(value = "insertLoanInfoNew", method = RequestMethod.POST)
	@ResponseBody
	public ResponseModel insertLoanInfoNew(@RequestBody RequestLoanUserInfo loanUserInfo) {
		try {
			System.out.println(loanUserInfo);
			LOG.info("调用新接口往loan_info 表里面插数据insertLoanInfoNew:" + loanUserInfo);
			long orderNo = loanUserServiceNew.insertLoanInfoNew(loanUserInfo);
			return ResponseModel.success(orderNo);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.info(e.getMessage());
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
		LOG.info("小星借款APP查询他的申请贷款的信息结果为:"+loanOrder);
		//System.out.println("查询结果:"+loanOrder);
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
	@Log("设置此单已经被抢，使用key值验证")
	public ResponseModel updateLoanUserStatus(@RequestBody HashMap<String, Object> loanUserInfo) {
		try {
			long orderNo = (long) loanUserInfo.get("orderNo");
			Integer discountedState = (Integer) loanUserInfo.get("discountedState");
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
					Integer isGrabbed = 0;
					if (discountedState == 1) {
						//API 设置该订单打折
						isGrabbed = loanUserService.updateLoanUserDiscountState(orderNo, discountedState);
					} else {
						if (status == 3) {
							// 设置此订单已经被抢
							isGrabbed = loanUserService.updateLoanUserStatus(orderNo, 6);
						} else {
							// 设置此订单已经被抢
							isGrabbed = loanUserService.updateLoanUserStatus(orderNo, 1);
						}
					}
					if (isGrabbed != null && isGrabbed == 1) {
						return ResponseModel.success();
					}

				}
			}
			return ResponseModel.fail("修改失败");
		}catch (Exception e) {
			e.printStackTrace();
			return ResponseModel.fail("网络异常，请稍后再试");
		}
	}

}
