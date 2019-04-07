package com.stardai.manage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.stardai.manage.bean.ResponseModel;
import com.stardai.manage.pojo.SysVersionCheck;
import com.stardai.manage.pojo.SysVersionCheckForIOS;
import com.stardai.manage.service.VersionService;

/**
 * @author jokery
 * @date 创建时间：2017年12月4日 上午11:17:15
 * @类说明
 */
@Controller
@RequestMapping(value = "MZversion")
@SuppressWarnings("all")
public class VersionController extends BaseController {

	@Autowired
	private VersionService versionCheckService;

	// 获取当前最新版本(安卓)
	@RequestMapping(value = "versionCheckAndroid", method = RequestMethod.GET)
	@ResponseBody
	public ResponseModel versionCheckAndroid() {
		SysVersionCheck sysVersionCheck = null;
		try {
			sysVersionCheck = versionCheckService.checkVersionAndroid();
			if (sysVersionCheck == null) {
				return ResponseModel.error("未查询到当前系统下的最新版本号");
			}
			return ResponseModel.success(sysVersionCheck);
		} catch (Exception e1) {
			return ResponseModel.fail("网络异常,请稍后再试");
		}
	}

	// 查询iOS不同版本的状态，即是否通过审核（1.4.0版本启用）
	@RequestMapping(value = "checkStatusForIOSVersion", method = RequestMethod.GET)
	@ResponseBody
	public ResponseModel checkStatusForIOSVersion(@RequestParam("iosVersion") String iosVersion) {
		SysVersionCheckForIOS sysVersionCheckForIOS = null;
		try {
			sysVersionCheckForIOS = versionCheckService.checkStatusForIOSVersion(iosVersion);
			if (sysVersionCheckForIOS == null) {
				return ResponseModel.error("未查询到当前版本的状态");
			}
			return ResponseModel.success(sysVersionCheckForIOS);
		} catch (Exception e1) {
			return ResponseModel.fail("网络异常,请稍后再试");
		}
	}

}
