package com.stardai.manage.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.stardai.manage.constants.Constants;
import com.stardai.manage.pojo.UserPush;
import com.stardai.manage.request.*;
import com.stardai.manage.response.*;
import com.stardai.manage.service.CouponService;
import com.stardai.manage.service.CreditService;
import com.stardai.manage.service.HotCityService;
import com.stardai.manage.service.LoanUserService;
import com.stardai.manage.utils.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.stardai.manage.bean.ResponseModel;
import com.stardai.manage.pojo.UserSmsCode;
import com.stardai.manage.pojo.UserSystemMessage;
import com.stardai.manage.service.PayMoneyService;
import com.stardai.manage.service.UserService;
import com.stardai.manage.service.UserSmsCodeService;

/**
 * @author jdw
 * @date 2017/10/16
 */
@Controller
@RequestMapping(value = "MZuser")
@SuppressWarnings("all")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserSmsCodeService userSmsCodeService;

    @Autowired
    private PayMoneyService payMoneyService;

    @Autowired
    private LoanUserService loanUserService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private CreditService creditService;

    @Autowired
    private HotCityService hotCityService;

    @Autowired
    private RedisCacheManager redisCacheManager;

    @Autowired
    HttpServletRequest request;

    protected static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    // 用户信息注册
    @RequestMapping(value = "registerForNewEdition", method = RequestMethod.POST)
    @ResponseBody
    public ResponseModel registerForNewEdition(@RequestBody RequestRegister userInfo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseModel.error(bindingResult);
        }
        String mobileNumber = userInfo.getMobileNumber();
        String password;
        try {
            synchronized (POOL.intern(mobileNumber)) {
                password = Des.decodePassword(userInfo.getPassword());
                Integer isRightCode = this.queryIsRightVerifyCode(mobileNumber, userInfo.getVerifyCode());
                if (isRightCode == 2) {
                    return ResponseModel.error("验证码已失效，请重新获取验证码！");
                } else if (isRightCode == 3) {
                    return ResponseModel.error("验证码错误，请重新验证！");
                } else if (isRightCode == 4) {
                    return ResponseModel.error("验证码错误次数太多，请重新获取验证码！");
                } else {
                    boolean existence = userService.checkUser(mobileNumber);
                    if (existence) {
                        return ResponseModel.error("手机号已注册");
                    } else {
                        String geencryptedPassword = Utils.generate(password, userInfo.getMobileNumber());
                        userInfo.setPassword(geencryptedPassword);
                        String userId = userService.addUserInfo(userInfo);

                        // 用户注册成功，送5张优惠券
                        couponService.addCouponForNewUser(userId);
                        // 用户注册，送优惠券的同时，在用户余额表里插入一条记录，方便后面充值，更新不会报错
                        payMoneyService.addPayRecord(userId);

                        // 将注册成功的消息添加到数据库
                        payMoneyService.addMessage(userId, 1, "恭喜您注册成功", "小优在此恭候多时了，送您5张无门槛抢单券作为见面礼，快去首页撩撩海量客户吧！");

                        return ResponseModel.success(userId);
                    }
                }

            }
        } catch (Exception e1) {
            return ResponseModel.error("注册失败");
        }
    }

    // 用户登录
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseModel login(@RequestBody RequestLogin loginRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseModel.error(bindingResult);
        }
        String mobileNumber = loginRequest.getMobileNumber();
        try {
            synchronized (POOL.intern(mobileNumber)) {

                ResponseUser loginedUser = new ResponseUser();
                loginedUser = userService.getUserInfoByPhone(mobileNumber);
				/*if(loginedUser == null){
					return ResponseModel.fail("该手机号未注册");
				}*/
                int type = loginRequest.getType();
                String encodepassword = "";
                String token = "";
                //用户选择的密码登陆
                if (type == 0) {
                    String initialPhone = userService.getInitialPhoneByNowPhone(mobileNumber);
                    System.out.println(loginRequest.getPassword() + "-----");
                    String decodePassword = Des.decodePassword(loginRequest.getPassword());

                    // 密码使用MD5+salt加密，密码是根据用户注册的手机号生成的
                    encodepassword = Utils.generate(decodePassword, initialPhone);
                    System.out.println(encodepassword);
                    loginedUser = userService.getUser(mobileNumber, encodepassword);
                    if (loginedUser != null) {
                        if (loginedUser.getPunishCode().equals(Constants.PutInBlackForVerifyCodeLogin)) {
                            return ResponseModel.fail("您的账号存在异常，请通过短信验证码登录！");
                        }
                    } else {
                        return ResponseModel.fail("手机号或密码错误");
                    }

                } else if (type == 1) {//验证码登陆，检查验证码是否正确
                    Integer isRightCode = this.queryIsRightVerifyCode(mobileNumber, loginRequest.getVertifyCode());
                    if (isRightCode == 2) {
                        return ResponseModel.fail("验证码已失效，请重新获取验证码！");
                    } else if (isRightCode == 3) {
                        return ResponseModel.fail("验证码错误，请重新验证！");
                    } else if (isRightCode == 4) {
                        return ResponseModel.fail("验证码错误次数太多，请重新获取验证码！");
                    } else {
                        loginedUser = userService.getUserInfoByPhone(mobileNumber);
                        encodepassword = loginedUser.getPwd();
                    }

                } else if (type == 2) {//token登陆
                    token = loginRequest.getToken();
                    loginedUser = userService.getUserInfoByPhone(mobileNumber);

                    if (!token.equals(loginedUser.getToken())) {
                        return new ResponseModel(3, "您的账号在其它设备上登录，如非本人操作，请及时修改密码！", null);
                    }

                }
                if (loginedUser != null) {
                    if (loginedUser.getPunishCode().equals(Constants.PutInBlackForForbidLogin)) {
                        return ResponseModel.fail("系统监测到您的账号存在违反优接单注册及使用协议的行为，该账号已被禁止登录。如有疑问，请与客服取得联系。");
                    }
                    String userId = loginedUser.getUserId();
                    if (type != 2) {
                        // 将密码加密的MD5作为token
                        token = Utils.generate(encodepassword, "mianze" + System.currentTimeMillis());

                        loginedUser.setToken(token);
                        //用户每次登陆更新用户的token
                        userService.updateUserToken(userId, token);
                    } else {
                        userService.updateUserLoginStatus(userId);
                    }
                    ResponseMoney queryAllMoney = payMoneyService.queryAllMoney(userId);
                    double userAmount = 0.0;
                    if (queryAllMoney != null) {
                        userAmount = queryAllMoney.getAllMoney();
                    }
                    loginedUser.setAmount(userAmount);

                    // 查询用户对于接收订单推送的相关设置
                    UserPush userPush = userService.queryUserPush(userId);
                    // 将相关设置放入响应模型返回前台
                    if (userPush != null) {

                        ArrayList pushCityList = new ArrayList();
                        pushCityList.add(userPush.getFollowedCityOne());
                        // 把查出推送城市名称及对应的编码，传到前端展示
                        loginedUser.setPushCity(JSON.toJSONString(hotCityService.getCityCodeAndNameByCityCode(pushCityList)));
                        loginedUser.setPushAble(userPush.getPushAble());
                        loginedUser.setPushTime(userPush.getPushTime());
                        if (StringUtils.isNotBlank(userPush.getPushAmount())) {
                            loginedUser.setPushAmount(userPush.getPushAmount());
                        }
                        if (StringUtils.isNotBlank(userPush.getPushWebank())) {
                            loginedUser.setPushWebank(userPush.getPushWebank());
                        }
                        if (StringUtils.isNotBlank(userPush.getPushIsNative())) {
                            loginedUser.setPushIsNative(userPush.getPushIsNative());
                        }
                    } else {
                        loginedUser.setPushAble(1);
                    }
                    //查询用户关注的城市，返回城市加编码的list
                    String city = loginedUser.getCity();
                    if (StringUtils.isNotBlank(city)) {
                        List<String> list = Arrays.asList(city.split(","));
                        ArrayList<String> arrayList = new ArrayList<String>(list);
                        List<HashMap<String, Object>> cityNameAndCode = hotCityService
                                .getCityCodeAndNameByCityCode(arrayList);
                        loginedUser.setCityNameAndCode(JSON.toJSONString(cityNameAndCode));
                    }
                    return ResponseModel.success(loginedUser);
                } else {
                    return ResponseModel.fail("该手机号未注册");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseModel.fail("登录失败");
    }


    // 验证码发送或更新原有的验证码
    @RequestMapping(value = "send", method = RequestMethod.POST)
    @ResponseBody
    public ResponseModel smsCode(@RequestBody RequestSmsCode smsCodeRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseModel.error(bindingResult);
        }
        try {
            synchronized (POOL.intern(smsCodeRequest.getMobileNumber())) {
                String mobileNumber = smsCodeRequest.getMobileNumber();
                //1 是注册用户 2 是找回密码 3 验证码登陆 4 更换手机，使用旧手机号发送验证码时 5 更换手机，使用新手机号发送验证码时  6 使用验证码修改密码
                Integer type = smsCodeRequest.getType();
                // 极光没有一小时只能发几次验证码的限制，所以我们使用redis来实现 1小时内同一手机号只能发送3次验证码
                String reKey = "SMS_" + mobileNumber + "_" + DateUtil.getDay();
                int revalue = 1;
                // 查看最近一小时有没有给此人发送过验证码
                if (redisCacheManager.hasKey(reKey)) { // 如果发送过
                    revalue = (int) redisCacheManager.get(reKey);
                    revalue += 1;
                    redisCacheManager.set(reKey, revalue, redisCacheManager.getExpire(reKey));
                    if (revalue > 3) {
                        return ResponseModel.fail("验证码获取次数已达上限，请稍后再试！");
                    }

                } else { // 最近一小时没有发送过验证码

                    redisCacheManager.set(reKey, revalue, 60 * 60L);
                }
                boolean existence = userService.checkUser(mobileNumber);
                if (type == 1) {//注册用户
                    if (existence) {
                        return ResponseModel.fail("手机号已注册");
                    }
                } else if (type == 2) {//找回密码
                    if (!existence) {
                        return ResponseModel.fail("手机号不存在");
                    }
                } else if (type == 3) {//验证码登陆
                    if (!existence) {
                        return ResponseModel.fail("该手机号未注册,请先注册!");
                    }
                } else if (type == 4) {//更换手机，使用旧手机号发送验证码时
                    if (!existence) {
                        return ResponseModel.fail("手机号不存在");
                    }
                } else if (type == 5) {//更换手机，使用新手机号发送验证码时
                    if (existence) {
                        return ResponseModel.fail("该手机号已被绑定，请更换手机号码!");
                    }
                } else if (type == 6) {//使用验证码修改密码
                    if (!existence) {
                        return ResponseModel.fail("手机号不存在");
                    }
                }
                String ip = "getip:" + IpUtil.getIpAddr(request);
                synchronized (POOL.intern(ip)) {
                    int ipCount = 0;
                    if (redisCacheManager.hasKey(ip)) {
                        ipCount = (int) redisCacheManager.get(ip);
                        ipCount += 1;
                        redisCacheManager.set(ip, ipCount, redisCacheManager.getExpire(ip));
                        if (ipCount > 5) {
                            return ResponseModel.success("验证码发送成功");
                        }
                    } else {
                        redisCacheManager.set(ip, ipCount, 60 * 60L);
                    }
                }

                //发送验证码，先使用极光发短信，极光不能发的话，就使用云片发
                Integer sendSmsCode = userSmsCodeService.sendSmsCode(mobileNumber, smsCodeRequest.getAppName());
                if (sendSmsCode == 1) {
                    return ResponseModel.success("发送成功");
                } else if (sendSmsCode == 2) {
                    return ResponseModel.fail("验证码获取次数已达上限，请稍后再试！");
                }
            }
        } catch (Exception e) {
            return ResponseModel.fail("发送失败");
        }
        return ResponseModel.fail("发送失败");
    }


    //手机验证码修改密码
    @RequestMapping(value = "updatePasswordByCode", method = RequestMethod.POST)
    @ResponseBody
    public ResponseModel updatePasswordByCode(@RequestBody RequestUpdatePassword updateRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseModel.error(bindingResult);
        }
        try {

            Integer isRightCode = this.queryIsRightVerifyCode(updateRequest.getMobileNumber(), updateRequest.getVerifyCode());

            if (isRightCode == 2) {
                return ResponseModel.fail("验证码已失效，请重新获取验证码！");
            } else if (isRightCode == 3) {
                return ResponseModel.fail("验证码错误，请重新验证！");
            } else if (isRightCode == 4) {
                return ResponseModel.fail("验证码错误次数太多，请重新获取验证码！");
            } else {
                String newDecodePassword = Des.decodePassword(updateRequest.getNewPassword());
                ResponseUser loginedUser = userService.getUserInfoByPhone(updateRequest.getMobileNumber());
                if (loginedUser != null) {
                    // 密码加密
                    Integer updatePassWord = userService.updatePassWord(updateRequest.getMobileNumber(),
                            Utils.generate(newDecodePassword, updateRequest.getMobileNumber()));
                    if (updatePassWord != 0) {
                        payMoneyService.addMessage(updateRequest.getUserId(), 1, "修改密码",
                                "密码已修改成功，请妥善保管哦~~~~快去抢单页面瞅瞅，促销活动正在进行中哦！");
                        LOG.info("用户使用验证码修改密码成功!!!!!!!!!!");
                        return ResponseModel.success("修改成功！");
                    } else {
                        LOG.info("用户使用验证码修改密码失败!!!!!!!!!!");
                        return ResponseModel.success("修改失败！");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        LOG.info("用户使用验证码修改密码失败!!!!!!!!!!");
        return ResponseModel.error("修改失败！");
    }


    //校验验证码是否正确
    private Integer queryIsRightVerifyCode(String mobileNumber, String verifyCode) {
        Integer result = 0;
        synchronized (POOL.intern(mobileNumber)) {
            UserSmsCode smsCode = userSmsCodeService.querySmsCode(mobileNumber);
            if (smsCode != null && smsCode.getVerifyCode() != null
                    && smsCode.getVerifyCode().equals(verifyCode)) {
                //验证码有效期三分钟
                if (System.currentTimeMillis() > smsCode.getCreateTime() + 180000L) {
                    //验证码过期
                    return 2;
                } else {//验证正确
                    return 1;
                }
            } else {//验证码错误
                //60s内校验到达3次提示只能获取新的验证码验证
                String reKey = "SMS_CHECK_" + mobileNumber;
                int revalue = 1;
                // 查看60s内，验证码是否输错过
                if (redisCacheManager.hasKey(reKey)) { // 如果输错过
                    revalue = (int) redisCacheManager.get(reKey);
                    revalue += 1;
                    redisCacheManager.set(reKey, revalue, redisCacheManager.getExpire(reKey));
                    if (revalue >= 3) {
                        return 4;
                    }
                } else { // 最近60s内没有输错过验证码
                    redisCacheManager.set(reKey, revalue, 60L);

                }
                return 3;
            }
        }
    }

    // 用户头像修改上传
    @ResponseBody
    @RequestMapping(value = "avatar", method = RequestMethod.POST)
    public ResponseModel avatar(@RequestBody RequestAvatar avatar, HttpServletRequest request) {
        if (avatar.getAvatar() == null || avatar.getAvatar().isEmpty()) {
            return ResponseModel.error("头像不能为空");
        }
        String url = userService.updateAvatar(avatar.getUserId(), avatar.getAvatar(), request);
        if (url != null) {
            return ResponseModel.success(url);
        }
        return ResponseModel.fail("头像上传失败");
    }

    // 个人认证或者更换手机号时的人脸识别
    @RequestMapping(value = "faceRecognize")
    @ResponseBody
    public ResponseModel faceRecognize(@RequestBody RequestPersonVerifyInfo requestPersonVerifyInfo,
                                       BindingResult bindingResult) {
        int type = requestPersonVerifyInfo.getType();
        //个人认证时的人脸识别
        if (type == 0) {
            String idCard = requestPersonVerifyInfo.getIdCardNumber();
            if (StringUtils.isBlank(idCard)) {
                return ResponseModel.fail("网络异常，认证失败！");
            }
            ResponseUserInfo user = userService.approvePerson(requestPersonVerifyInfo);
            if (user != null) {
                Integer approveResult = user.getApprovePerson();
                if (approveResult == 1) {
                    // 个人认证成功获取100积分
                    RequestCreditDetail requestCreditDetail = new RequestCreditDetail();
                    requestCreditDetail.setUserId(requestPersonVerifyInfo.getUserId());
                    requestCreditDetail.setType(2);
                    requestCreditDetail.setCreditValue(100);
                    requestCreditDetail.setCreditPathway("个人认证");
                    requestCreditDetail.setCreditDetail("个人认证");
                    creditService.addCreditDetail(requestCreditDetail);
                    creditService.updateCreditWallet(requestPersonVerifyInfo.getUserId(), 100);
                    return ResponseModel.success(user);
                } else if (approveResult == 2) {
                    return ResponseModel.error("人脸验证失败！");
                } else if (approveResult == 3) {
                    return ResponseModel.error("该身份证已被他人认证过！");
                }
            }
            return ResponseModel.fail("网络异常，认证失败！");
        } else {
            //已认证用户更改手机号时人脸识别
            Integer result = userService.getfaceRecognizeByChangePhone(requestPersonVerifyInfo);
            if (result == 1) {
                return ResponseModel.success();
            } else if (result == 2) {
                return ResponseModel.fail("人脸识别匹配失败，请联系客服进行人工审核更改！");
            } else if (result == 3) {
                return ResponseModel.fail("请联系客服！");
            } else {
                return ResponseModel.fail("网络异常，认证失败！");
            }

        }
    }

    // 企业认证
    @ResponseBody
    @RequestMapping(value = "approveCompany", method = RequestMethod.POST)
    public ResponseModel approveCompany(@RequestBody RequestCompany company, HttpServletRequest request,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseModel.error(bindingResult);
        }
        if (company.getCompanyAddress() == null || company.getCompanyAddress().isEmpty()
                || company.getCompanyBranch() == null || company.getCompanyBranch().isEmpty()
                || company.getCompanyCity() == null || company.getCompanyCity().isEmpty()
                || null == company.getCompanyImages() || company.getCompanyImages().isEmpty()
                || company.getCompanyImages().size() < 1 || company.getCompanyName() == null
                || company.getCompanyName().isEmpty()) {
            return ResponseModel.fail("信息填写不完整");
        }
        String userId = company.getUserId();
        // company.setCompanyCity(loanUserService.unifyLocations(company.getCompanyCity()));
        String images = null;
        String name = userService.queryNameByUserId(userId);
        //查询个人认证状态
        Integer personStatus = userService.queryApprovePerson(userId);
        if (personStatus == null || (personStatus != null && personStatus == 0)) {
            return new ResponseModel(4, "账号异常，请返回登录界面重新登录！", null);
        } else {
            String role = name.subSequence(0, 1) + "经理";
            //把定位取得的区域编码转成对应的城市编码
            String companyCity = hotCityService.setAreaCodeToCityCode(company.getCompanyCity());
            company.setCompanyCity(companyCity);
            // 查询企业认证是第一次认证还是修改
            String isExist = userService.queryIsFirstSubmitApproveCompany(userId);

            if (StringUtils.isBlank(isExist)) {
                images = userService.GenerateImage(company, request, name);
                boolean approvePerson = userService.addApproveCompany(company, images);
                if (approvePerson) {
                    payMoneyService.addMessage(userId, 1, "审核中",
                            "亲爱的" + role + "：您的资料已经成功提交，审核猿会在3个工作日内进行审核，请耐心等待，您可以到我的钱包->充值页面预存星币，抢单快人一步！");
                    return ResponseModel.success("审核中");
                }
                return ResponseModel.fail("认证失败");
            } else {
                images = userService.GenerateImage(company, request, name);
                boolean approvePerson = userService.updateApproveCompany(company, images);
                if (approvePerson) {
                    return ResponseModel.success("审核中");
                }
                return ResponseModel.fail("认证失败");
            }
        }
    }

    // 根据UserID查询企业认证
    @ResponseBody
    @RequestMapping(value = "queryApproveCompanyV2", method = RequestMethod.GET)
    public ResponseModel queryApproveCompanyV2(@RequestParam("userId") String userId) {
        ResponseApprove responseApprove = userService.queryApproveStatus(userId);
        if (responseApprove == null) {
            responseApprove = new ResponseApprove(0, 0, 0);
            return ResponseModel.success(responseApprove);
        } else {
            return ResponseModel.success(responseApprove);
        }

    }

    // 根据旧密码修改密码
    @RequestMapping(value = "updatePassword", method = RequestMethod.POST)
    @ResponseBody
    public ResponseModel updatePassword(@RequestBody RequestUpdatePassword updateRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseModel.error(bindingResult);
        }
        try {
            String userId = updateRequest.getUserId();

            String oldDecodePassword = Des.decodePassword(updateRequest.getOldPassword());
            String newDecodePassword = Des.decodePassword(updateRequest.getNewPassword());

            ResponseUser loginedUser = userService.getUser(updateRequest.getMobileNumber(),
                    Utils.generate(oldDecodePassword, updateRequest.getMobileNumber()));
            if (loginedUser != null) {
                // 密码加密
                Integer updatePassWord = userService.updatePassWord(updateRequest.getMobileNumber(),
                        Utils.generate(newDecodePassword, updateRequest.getMobileNumber()));
                if (updatePassWord != 0) {
                    payMoneyService.addMessage(updateRequest.getUserId(), 1, "修改密码",
                            "密码已修改成功，请妥善保管哦~~~~快去抢单页面瞅瞅，促销活动正在进行中哦！");
                    return ResponseModel.success("修改成功！");
                }
                return ResponseModel.success("修改失败！");
            } else {
                return ResponseModel.error("原密码错误！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseModel.error("修改失败！");
    }

    // 验证验证码是否正确
    @RequestMapping(value = "forgetPassword", method = RequestMethod.POST)
    @ResponseBody
    public ResponseModel forgetPassword(@RequestBody RequestPhoneAndVerifyCode forgetPassword,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseModel.error(bindingResult);
        }
        if (forgetPassword.getMobileNumber() == null || forgetPassword.getMobileNumber().isEmpty()
                || forgetPassword.getVerifyCode() == null || forgetPassword.getVerifyCode().isEmpty()) {
            return ResponseModel.error("请填写正确信息");
        }
        String mobileNumber = forgetPassword.getMobileNumber();
        String verifyCode = forgetPassword.getVerifyCode();
        Integer result = this.queryIsRightVerifyCode(mobileNumber, verifyCode);
        if (result == 2) {
            return ResponseModel.error("验证码已失效，请重新获取验证码！");
        } else if (result == 3) {
            return ResponseModel.error("验证码错误，请重新验证！");
        } else if (result == 4) {
            return ResponseModel.error("验证码错误次数太多，请重新获取验证码！");
        } else {
            return ResponseModel.success();
        }

    }

    // 找回密码--后修改密码
    @RequestMapping(value = "findPd", method = RequestMethod.POST)
    @ResponseBody
    public ResponseModel retrievePassword(@RequestBody RequestRetrievePassword retrievequest,
                                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseModel.error(bindingResult);
        }
        if (retrievequest.getMobileNumber() == null || retrievequest.getMobileNumber().isEmpty()
                || retrievequest.getPassword() == null || retrievequest.getPassword().isEmpty()) {
            return ResponseModel.error("请填写正确信息");
        }
        try {
            String mobileNumber = retrievequest.getMobileNumber();
            synchronized (POOL.intern(mobileNumber)) {
                boolean existence = userService.checkUser(mobileNumber);
                if (!existence) {
                    return ResponseModel.error("手机号不存在");
                }
                String newDecodePassword = Des.decodePassword(retrievequest.getPassword());
                // 密码加密
                Integer updatePassWord = userService.updatePassWord(retrievequest.getMobileNumber(),
                        Utils.generate(newDecodePassword, retrievequest.getMobileNumber()));
                if (updatePassWord != 0) {
                    return ResponseModel.success("修改成功！");
                }
                return ResponseModel.success("修改失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseModel.fail("修改失败");
    }

    // 反馈
    @RequestMapping(value = "feedback", method = RequestMethod.POST)
    @ResponseBody
    public ResponseModel feedback(@RequestBody RequestFeedBack feedBack, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseModel.error(bindingResult);
        }
        if (StringUtils.isBlank(feedBack.getContent())) {
            return ResponseModel.fail("请填写反馈内容");
        }
        if (feedBack.getContent() == null || feedBack.getPhoneNumber() == null || feedBack.getPhoneNumber().isEmpty()) {
            return ResponseModel.fail("反馈失败");
        }
        userService.submitFeedback(feedBack);
        return ResponseModel.success("提交反馈成功！");
    }

    // 阅读消息
    @RequestMapping(value = "updateMessage", method = RequestMethod.POST)
    @ResponseBody
    public ResponseModel updatePersonalMessage(@RequestBody RequestPersonalMessage personalMessage,
                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseModel.error(bindingResult);
        }
        boolean result = userService.updatePersonalMessage(personalMessage.getUserId(), personalMessage.getId());
        if (result) {
            return ResponseModel.success();
        }
        return ResponseModel.fail();
    }

    // 获得个人消息
    @RequestMapping(value = "queryMessage", method = RequestMethod.POST)
    @ResponseBody
    public ResponseModel queryPersonalMessage(@RequestBody RequestMessage requestMessage, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseModel.error(bindingResult);
        }
        List<ResponsePersonalMessage> responsePersonalMessage = userService.queryPersonalMessage(requestMessage);
        if (responsePersonalMessage != null && responsePersonalMessage.size() > 0) {
            return ResponseModel.success(responsePersonalMessage);
        }
        return ResponseModel.success(new ArrayList());
    }

    // 公告通知
    @RequestMapping(value = "querySystemMessage", method = RequestMethod.POST)
    @ResponseBody
    public ResponseModel querySystemMessage(@RequestBody RequestSystemMessage requestSystemMessage,
                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseModel.error(bindingResult);
        }
        List<UserSystemMessage> systemMessage = userService.querySystemMessage(requestSystemMessage);
        if (systemMessage != null && systemMessage.size() > 0) {
            return ResponseModel.success(systemMessage);
        }
        return ResponseModel.success(new ArrayList());
    }

    // 获取用户消息列表的未读消息
    @RequestMapping(value = "queryPersonalMessageAmount", method = RequestMethod.GET)
    @ResponseBody
    public ResponseModel queryPersonalMessageAmount(@RequestParam(value = "userId") String userId) {
        try {
            if (StringUtils.isNotBlank(userId)) {
                Integer messageAmount = userService.queryPersonalMessageAmount(userId);
                HashMap<String, Object> result = new HashMap<String, Object>();
                result.put("messageAmount", messageAmount);
                return ResponseModel.success(result);
            }
            return ResponseModel.fail("网络异常,请稍后再试");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseModel.fail("网络异常,请稍后再试");
        }
    }

    // 一键已读所有的未读消息
    @RequestMapping(value = "setAllMessageRead", method = RequestMethod.GET)
    @ResponseBody
    public ResponseModel setAllMessageRead(@RequestParam(value = "userId") String userId) {
        try {
            if (StringUtils.isNotBlank(userId)) {
                Integer result = userService.setAllMessageRead(userId);
                if (result != null && result >= 0) {
                    return ResponseModel.success();
                }
            }
            return ResponseModel.fail("网络异常,请稍后再试");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseModel.fail("网络异常,请稍后再试");
        }
    }


    // 设置是否接收订单推送
    @RequestMapping(value = "setPushAble", method = RequestMethod.GET)
    @ResponseBody
    public ResponseModel setPushAble(@RequestParam(value = "userId") String userId,
                                     @RequestParam(value = "pushAble") Integer pushAble) {
        try {
            if (StringUtils.isNotBlank(userId) && pushAble != null) {
                UserPush userPush = userService.queryUserPush(userId);
                Integer successed = null;
                if (userPush == null) {
                    userPush = new UserPush();
                    userPush.setUserId(userId);
                    userPush.setPushAble(pushAble);
                    successed = userService.insertUserPush(userPush);
                } else {
                    userPush.setPushAble(pushAble);
                    successed = userService.updateUserPush(userPush);
                }
                if (successed == 1) {
                    return ResponseModel.success(pushAble.toString());
                }
            }
            return ResponseModel.fail("网络异常,请稍后再试");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseModel.fail("网络异常,请稍后再试");
        }
    }

    // 设置接收订单推送的时间
    @RequestMapping(value = "setPushTime", method = RequestMethod.POST)
    @ResponseBody
    public ResponseModel setPushTime(@RequestBody RequestPushTime setPushTime, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseModel.error(bindingResult);
        }
        try {
            String pushTime = setPushTime.getPushTime();
            UserPush userPush = userService.queryUserPush(setPushTime.getUserId());
            Integer successed = null;
            if (userPush == null) {
                userPush = new UserPush();
                userPush.setUserId(setPushTime.getUserId());
                userPush.setPushAble(1);
                userPush.setPushTime(pushTime);
                successed = userService.insertUserPush(userPush);
            } else {
                userPush.setPushTime(pushTime);
                successed = userService.updateUserPush(userPush);
            }
            if (successed == 1) {
                return ResponseModel.success(pushTime);
            }
            return ResponseModel.fail("网络异常,请稍后再试");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseModel.fail("网络异常,请稍后再试");
        }
    }

    // 设置接收哪些城市的订单推送
    @RequestMapping(value = "setPushCities", method = RequestMethod.GET)
    @ResponseBody
    public ResponseModel setPushCities(@RequestParam(value = "userId") String userId, HttpServletRequest request) {
        try {
            String pushCity = request.getParameter("pushCity");
            //把定位取得的区域编码转成对应的城市编码
            pushCity = hotCityService.setAreaCodeToCityCode(pushCity);

            if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(pushCity)) {
                // pushCity = loanUserService.unifyLocations(pushCity);
                UserPush userPush = userService.queryUserPush(userId);
                Integer successed = null;
                if (userPush == null) {
                    userPush = new UserPush();
                    userPush.setUserId(userId);
                    userPush.setFollowedCityOne(pushCity);
                    userPush.setPushAble(1);
                    successed = userService.insertUserPush(userPush);
                } else {
                    userPush.setFollowedCityOne(pushCity);
                    successed = userService.updateUserPush(userPush);
                }
                if (successed == 1) {
                    ArrayList pushCityList = new ArrayList();
                    pushCityList.add(pushCity);
                    // 根据城市编码查询城市编码及对应的名称
                    List<HashMap<String, Object>> cityNameAndCode = hotCityService
                            .getCityCodeAndNameByCityCode(pushCityList);
                    return ResponseModel.success(JSON.toJSONString(cityNameAndCode));
                }
            }
            return ResponseModel.fail("网络异常,请稍后再试");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseModel.fail("网络异常,请稍后再试");
        }
    }


    // 设置接收订单推送的贷款金额区间,微粒贷，本地户籍
    @RequestMapping(value = "setPushParameter", method = RequestMethod.POST)
    @ResponseBody
    public ResponseModel setPushParameter(@RequestBody RequestPushParameter requestPushParameter, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseModel.error(bindingResult);
        }
        try {
            int successed = 0;
            UserPush userPush = userService.queryUserPush(requestPushParameter.getUserId());
            int pushType = requestPushParameter.getPushType();
            if (pushType == 1) {
                //贷款金额区间
                if (userPush == null) {
                    userPush = new UserPush();
                    userPush.setUserId(requestPushParameter.getUserId());
                    userPush.setPushAble(1);
                    userPush.setPushAmount(requestPushParameter.getPushAmount());
                    successed = userService.insertUserPush(userPush);
                } else {
                    userPush.setPushAmount(requestPushParameter.getPushAmount());
                    successed = userService.updateUserPush(userPush);
                }
                if (successed == 1) {
                    return ResponseModel.success(requestPushParameter.getPushAmount());
                }

            } else if (pushType == 2) {
                //微粒贷
                if (userPush == null) {
                    userPush = new UserPush();
                    userPush.setUserId(requestPushParameter.getUserId());
                    userPush.setPushAble(1);
                    userPush.setPushWebank(requestPushParameter.getPushWebank());
                    successed = userService.insertUserPush(userPush);
                } else {
                    userPush.setPushWebank(requestPushParameter.getPushWebank());
                    successed = userService.updateUserPush(userPush);
                }
                if (successed == 1) {
                    return ResponseModel.success(requestPushParameter.getPushWebank());
                }

            } else if (pushType == 3) {
                //本地户籍
                if (userPush == null) {
                    userPush = new UserPush();
                    userPush.setUserId(requestPushParameter.getUserId());
                    userPush.setPushAble(1);
                    userPush.setPushIsNative(requestPushParameter.getPushIsNative());
                    successed = userService.insertUserPush(userPush);
                } else {
                    userPush.setPushIsNative(requestPushParameter.getPushIsNative());
                    successed = userService.updateUserPush(userPush);
                }
                if (successed == 1) {
                    return ResponseModel.success(requestPushParameter.getPushIsNative());
                }

            }
            return ResponseModel.fail("网络异常,请稍后再试");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseModel.fail("网络异常,请稍后再试");
        }
    }


    /**
     * @Author : jokery
     * @Description : 进入实名认证页面时,先查询该信贷经理是否曾经认证过,如果认证过,返回上次提交的数据用于填充
     * @Date : 2018/3/12 11:21
     * @Param :userId
     * @Return :
     */
    @RequestMapping(value = "queryApproveData", method = RequestMethod.GET)
    @ResponseBody
    public ResponseModel queryApproveData(@RequestParam(value = "userId") String userId) {
        try {
            if (StringUtils.isBlank(userId)) {
                return ResponseModel.error("用户ID不存在");
            }

            ResponseApproveData data = this.userService.queryApproveData(userId);
            if (data != null) {
                String city = data.getCompanyCity();

                ArrayList cityList = new ArrayList();
                cityList.add(city);
                // 根据城市编码查询城市编码及对应的名称
                List<HashMap<String, Object>> cityNameAndCode = hotCityService.getCityCodeAndNameByCityCode(cityList);
                data.setCompanyCityAndCode(JSON.toJSONString(cityNameAndCode));
                HashMap<String, Integer> punishCodeAndStatus = userService.getPunishCodeByUserId(userId);
                Integer punishCode = punishCodeAndStatus.get("punishCode");
                data.setPunishCode(punishCode);
                return ResponseModel.success(data);
            }
            return ResponseModel.fail("该用户未提交过实名认证资料");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseModel.fail("网络异常请稍后再试");
        }
    }

    /**
     * @Author : Tina
     * @Description : 返回公司名称及对应的简称，首字母（1.4.0添加了热门公司）
     * @Date : 2018/5/3 9:53
     * @Return :
     */
    @RequestMapping(value = "getAllCompanyNames", method = RequestMethod.GET)
    @ResponseBody
    public ResponseModel getAllCompanyNames() {
        try {

            List<HashMap<String, Object>> data = userService.getAllCompanyNames();

            if (data != null) {
                return ResponseModel.success(data);
            }
            return ResponseModel.fail("网络异常请稍后再试");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseModel.fail("网络异常请稍后再试");
        }
    }

    //用户在优接单app更换手机号码时
    @RequestMapping(value = "changeMobileNumber")
    @ResponseBody
    public ResponseModel changeMobileNumber(@RequestBody RequestPhoneAndVerifyCode requestPhoneAndVerifyCode,
                                            BindingResult bindingResult) {
        String userId = requestPhoneAndVerifyCode.getUserId();
        String mobileNumber = requestPhoneAndVerifyCode.getMobileNumber();
        boolean existence = userService.checkUser(mobileNumber);
        Integer result = 0;

        if (existence) {
            return ResponseModel.fail("该手机号已被绑定，请更换手机号码");
        }
        Integer isRightCode = this.queryIsRightVerifyCode(mobileNumber, requestPhoneAndVerifyCode.getVerifyCode());
        if (isRightCode == 2) {
            return ResponseModel.fail("验证码已失效，请重新获取验证码！");
        } else if (isRightCode == 3) {
            return ResponseModel.fail("验证码错误，请重新验证！");
        } else if (isRightCode == 4) {
            return ResponseModel.fail("验证码错误次数太多，请重新获取验证码！");
        } else {
            String oldNumber = requestPhoneAndVerifyCode.getOldNumber();
            String rightOldNumber = userService.getPhoneNumberByUserId(userId);
            if (!rightOldNumber.equals(oldNumber)) {
                return ResponseModel.fail("网络异常，请稍后再试！");
            }
            result = userService.updatePhoneByUserId(requestPhoneAndVerifyCode.getUserId(), mobileNumber);
            if (result == 1) {

                requestPhoneAndVerifyCode.setOldNumber(oldNumber);
                userService.addChangePhoneRecord(requestPhoneAndVerifyCode);

                return ResponseModel.success();
            } else {
                return ResponseModel.fail("更换失败");
            }
        }


    }


    // 人脸识别次数是否超限
    @RequestMapping(value = "isAllowfaceRecognize")
    @ResponseBody
    public ResponseModel isAllowfaceRecognize(@RequestParam(value = "userId") String userId) {
        String reKey = "face_Recognize_" + userId;
        int revalue = 1;
        // 查看今天更换手机号人脸识别有没有失败的
        if (redisCacheManager.hasKey(reKey)) { // 如果输错过
            revalue = (int) redisCacheManager.get(reKey);
            if (revalue > 3) {
                return ResponseModel.fail("当日手机更换验证次数已达上限");
            }

        }
        return ResponseModel.success();
    }

    @RequestMapping(value = "getOssToken")
    @ResponseBody
    public ResponseModel getOssToken() {
        return ResponseModel.success(HttpUtilOkHttpClient.get("https://localhost:7080"));
    }

    // h5退单用户登录
    @RequestMapping(value = "loginH5")
    @ResponseBody
    public ResponseModel loginH5(@RequestBody RequestLogin loginRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseModel.error(bindingResult);
        }
        try {
            String passWord = Aes.aesDecrypt(loginRequest.getPassword(), Aes.KEY);
            String encodepassword = Utils.generate(passWord, loginRequest.getMobileNumber());
            ResponseUser loginedUser = userService.getUser(loginRequest.getMobileNumber(), encodepassword);
            if (loginedUser != null) {
                String userId = loginedUser.getUserId();
                return ResponseModel.success(loginedUser);
            } else {
                return ResponseModel.error("手机号或密码错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseModel.error("登录失败");
    }

    // h5退单用户忘记密码发送短信
    @RequestMapping(value = "sendForPassword")
    @ResponseBody
    public ResponseModel sendForPassWord(@RequestBody RequestSmsCode smsCodeRequest) {
        try {
            String mobileNumber = smsCodeRequest.getMobileNumber();
            synchronized (POOL.intern(mobileNumber)) {
                String ip = "getip:" + IpUtil.getIpAddr(request);
                synchronized (POOL.intern(ip)) {
                    int ipCount = 0;
                    if (redisCacheManager.hasKey(ip)) {
                        ipCount = (int) redisCacheManager.get(ip);
                        ipCount += 1;
                        redisCacheManager.set(ip, ipCount, redisCacheManager.getExpire(ip));
                        if (ipCount > 5) {
                            return ResponseModel.success("验证码发送成功");
                        }
                    } else {
                        redisCacheManager.set(ip, ipCount, 60 * 60L);
                    }
                }

                Integer sendSmsCode = userSmsCodeService.sendSmsCode(mobileNumber, "优接单");
                if (sendSmsCode == 1) {
                    return new ResponseModel(0, "发送成功", null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseModel.error("发送失败");
        }
        return ResponseModel.error("发送失败");
    }

    // h5退单用户忘记密码,修改密码
    @RequestMapping(value = "reset")
    @ResponseBody
    public ResponseModel resetPassword(@RequestBody RequestRetrievePassword retrievequest,
                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseModel.error(bindingResult);
        }
        try {
            String mobileNumber = retrievequest.getMobileNumber();
            synchronized (POOL.intern(mobileNumber)) {
                boolean existence = userService.checkUser(mobileNumber);
                if (!existence) {
                    return ResponseModel.error("手机号不存在");
                }
                UserSmsCode smsCode = userSmsCodeService.querySmsCode(mobileNumber);
                if (smsCode != null && smsCode.getVerifyCode() != null
                        && smsCode.getVerifyCode().equals(retrievequest.getVerifyCode())) {
                    String password = retrievequest.getPassword();
                    String encodepassword = Utils.generate(password, mobileNumber);
                    userService.updatePassWord(mobileNumber, encodepassword);
                    return new ResponseModel(0, "修改成功", null);

                } else {
                    return ResponseModel.fail("修改失败!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseModel.fail("修改失败");
        }

    }

    //获取用户的真实定位或者ip
    @RequestMapping(value = "getUserLoginLocation")
    @ResponseBody
    public ResponseModel getUserLoginLocation(@RequestBody RequestUserLoginLocation rull) {
        if ((StringUtils.isBlank(rull.getIp()) && StringUtils.isBlank(rull.getCityPositioning()))
                || StringUtils.isBlank(rull.getUserId())) {
            return ResponseModel.fail("网络异常，请稍后再试！");
        }
        rull.setIp(IpUtil.getIpAddr(request));
        Integer result = 0;
        String cityPositioning = rull.getCityPositioning();
        Integer isExist = userService.getIsAddSameIp(rull);
        if (StringUtils.isBlank(cityPositioning)) {
            if (isExist == 0) {
                result = userService.addUserLoginLocation(rull);
            } else {
                result = userService.updateUserLoginLocation(rull);
            }
        } else {
            if (isExist == 0) {
                result = userService.addUserLoginLocation(rull);
            } else {
                result = userService.updateUserLoginLocation(rull);
            }
        }

        if (result != null && result >= 1) {
            return ResponseModel.success();
        }

        return ResponseModel.error("添加失败");
    }

    //获取oss直传的token
    @RequestMapping(value = "ossToken", method = RequestMethod.GET)
    @ResponseBody
    public String ossToken() {
        return HttpUtilOkHttpClient.get("http://localhost:7080");
    }


}
