package com.stardai.manage.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.stardai.manage.bean.ResponseModel;
import com.stardai.manage.service.FunctionCountService;

/**
 * @author Tina
 * @date 创建时间：2018年5月3日 上午9:41:15
 * @类说明
 */
@Controller
@RequestMapping(value = "MZFunctionCount")
@SuppressWarnings("all")
public class FunctionCountController extends BaseController {

	@Autowired
	private FunctionCountService functionCountService;

	// 统计app里每个菜单的点击次数
	@RequestMapping(value = "functionCountForApp", method = RequestMethod.GET)
	@ResponseBody
	public ResponseModel functionCount(String functionName) {
		if (!StringUtils.isBlank(functionName)) {
			functionCountService.addFunctionCountForApp(functionName);
			return ResponseModel.success();
		}
		return ResponseModel.fail("参数为空");

	}

	// 统计h5邀请好友按钮的点击次数
	@RequestMapping(value = "functionCountForH5", method = RequestMethod.GET)
	@ResponseBody
	public void functionCounth5(String functionName) {
		if (!StringUtils.isBlank(functionName)) {
			functionCountService.addFunctionCountForH5(functionName);
		}

	}

}
