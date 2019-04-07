package com.stardai.manage.controller;

import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.stardai.manage.config.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.stardai.manage.bean.ResponseModel;
import com.stardai.manage.pojo.OrderCityList;
import com.stardai.manage.request.RequestCitiesByUserId;
import com.stardai.manage.service.HotCityService;

/**
 * @author jdw
 * @date 2017/10/16
 */
@Controller
@RequestMapping(value = "MZcity")
@SuppressWarnings("all")
public class HotCityController {

	@Autowired
	private HotCityService hotCityService;

	protected static final Logger LOG = LoggerFactory.getLogger(HotCityController.class);

	// 修改关注城市
	@RequestMapping(value = "updateCitiesByUserId", method = RequestMethod.POST)
	@ResponseBody
	public ResponseModel updateCitiesByUserId(@RequestBody RequestCitiesByUserId cities) {
		// 修改他的关注的城市
		List<HashMap<String,Object>> result = hotCityService.updateCitiesByUserId(cities);
		if (result != null) {
			return ResponseModel.success(JSON.toJSONString(result));
		} else {
			return ResponseModel.fail("关注城市失败");
		}

	}

	// 获取城市列表
	@ResponseBody
	@RequestMapping(value = "queryCityList", method = RequestMethod.GET)
	public ResponseModel queryCityList() {

		List<OrderCityList> orderCityList = hotCityService.queryCityList();
		if (orderCityList != null && orderCityList.size() > 0) {
			return ResponseModel.success(orderCityList);
		} else {
			return ResponseModel.fail("查询失败");
		}
	}



}
