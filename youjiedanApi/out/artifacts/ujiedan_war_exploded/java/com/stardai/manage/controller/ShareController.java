package com.stardai.manage.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.stardai.manage.response.ResponseShareUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSON;
import com.stardai.manage.bean.ResponseModel;
import com.stardai.manage.pojo.User;
import com.stardai.manage.pojo.UserSmsCode;
import com.stardai.manage.response.ResponseShareUser;
import com.stardai.manage.service.PayMoneyService;
import com.stardai.manage.service.ShareService;
import com.stardai.manage.service.UserService;
import com.stardai.manage.service.UserSmsCodeService;
import com.stardai.manage.utils.Utils;

/**
 * @author jdw
 * @date 2017/10/16
 */
@Controller
@RequestMapping(value = "MZshare")
@SuppressWarnings("all")
public class ShareController extends BaseController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserSmsCodeService userSmsCodeService;

	@Autowired
	private PayMoneyService payMoneyService;

	@Autowired
	private ShareService shareService;

	// 验证码发送
	@RequestMapping(value = "shareSend", method = RequestMethod.GET)
	public void shareSmsCode(@RequestParam(value = "mobileNumber", required = true) String mobileNumber,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			response.setCharacterEncoding("UTF-8");
			String callback = request.getParameter("callback");
			// 先到后台查询该手机是否已经注册了用户
			User user = userService.checkUserForShare(mobileNumber);
			if (user != null) {
				// 该手机已经注册了,到share_user表查询他的分享链接,如果有就返回userId,没有就生成一条记录再返回
				// 主要目的是确保用户分项表中一定有他的数据记录
				String userId = user.getUserId();
				ResponseShareUser result = shareService.updateSharePerson(userId,request);
				String jsonString = JSON.toJSONString(ResponseModel.error(userId));
				response.getWriter().write(callback + "(" + jsonString + ")");
			} else {
				Integer sendSmsCode = userSmsCodeService.sendSmsCode(mobileNumber, "优接单");
				if (sendSmsCode == 1) {
					String jsonString = JSON.toJSONString(ResponseModel.success("发送成功"));
					response.getWriter().write(callback + "(" + jsonString + ")");
				}
			}
		} catch (Exception e) {
			try {
				response.setCharacterEncoding("UTF-8");
				String callback = request.getParameter("callback");
				String jsonString = JSON.toJSONString(ResponseModel.fail("发送失败"));
				response.getWriter().write(callback + "(" + jsonString + ")");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}


	// 用户信息注册
	@RequestMapping(value = "shareRegisterForCoupon", method = RequestMethod.GET)
	@ResponseBody
	public void shareRegisterForCoupon(@RequestParam(value = "mobileNumber", required = true) String mobileNumber,
			@RequestParam(value = "password", required = true) String password,
			@RequestParam(value = "verifyCode", required = true) String verifyCode,
			@RequestParam(value = "parameter", required = true) String userId, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			response.setCharacterEncoding("UTF-8");
			String callback = request.getParameter("callback");
			synchronized (POOL.intern(mobileNumber)) {
				UserSmsCode smsCode = userSmsCodeService.querySmsCode(mobileNumber);
				if (smsCode != null && smsCode.getVerifyCode() != null && smsCode.getVerifyCode().equals(verifyCode)) {
					if (System.currentTimeMillis() > smsCode.getCreateTime() + 180000L) {

						String jsonString = JSON.toJSONString(ResponseModel.error("验证码已过期"));
						response.getWriter().write(callback + "(" + jsonString + ")");

					} else {
						User user = userService.checkUserForShare(mobileNumber);
						if (user != null) {
							String jsonString = JSON.toJSONString(ResponseModel.fail(user.getUserId()));
							response.getWriter().write(callback + "(" + jsonString + ")");
						} else {
							String geencryptedPassword = Utils.generate(password, mobileNumber);
							// 添加数据
							String zhuCeUserId = shareService.addUserInfoForCoupon(mobileNumber, geencryptedPassword, userId,request);
							String jsonString = JSON.toJSONString(ResponseModel.success(zhuCeUserId));
							response.getWriter().write(callback + "(" + jsonString + ")");
						}
					}
				} else {
					String jsonString = JSON.toJSONString(ResponseModel.error("验证码不正确"));
					response.getWriter().write(callback + "(" + jsonString + ")");
				}
			}
		} catch (Exception e1) {
			try {
				response.setCharacterEncoding("UTF-8");
				String callback = request.getParameter("callback");
				String jsonString = JSON.toJSONString(ResponseModel.error("网络异常,请稍后再试"));
				response.getWriter().write(callback + "(" + jsonString + ")");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}	

	// 查询金额与邀请的人数
	@RequestMapping(value = "querySharePerson", method = RequestMethod.GET)
	@ResponseBody
	public void queryShare(@RequestParam("parameter") String userId, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ResponseShareUser result = shareService.updateSharePerson(userId,request);
		String jsonString = JSON.toJSONString(result);
		response.setCharacterEncoding("UTF-8");
		String callback = request.getParameter("callback");
		response.getWriter().write(callback + "(" + jsonString + ")");
	}


    // 查询分享链接,和分享出去的文案(1.2.0版本以后启用,代替原queryShareUrl接口)
    @RequestMapping(value = "queryShareUrlAndCopy", method = RequestMethod.GET)
    @ResponseBody
    public ResponseModel queryShareUrlAndCopy(@RequestParam("userId") String userId) {
        if (userId == null || userId.isEmpty()) {
            return ResponseModel.error("分享失败");
        }
	 	ResponseShareUrl shareUrl = shareService.queryShareUrlAndCopy();
        String url = shareUrl.getUrl() + "?parameter=" + userId;
        shareUrl.setUrl(url);
        return ResponseModel.success(shareUrl);
    }
}
