package com.stardai.manage.controller;



import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.stardai.manage.bean.ResponseModel;
import com.stardai.manage.response.ResponseLoanOrder;
import com.stardai.manage.service.LoanUserService;

/**
 * @author jdw
 * @date 2017/10/16
 */
@Controller
@RequestMapping(value = "MZloanUser")
@SuppressWarnings("all")
public class LoanUserController extends BaseController {

	@Autowired
	private LoanUserService loanUserService;




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
}


