package com.stardai.manage.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.stardai.manage.bean.ResponseModel;
import com.stardai.manage.pojo.UserPush;
import com.stardai.manage.request.*;
import com.stardai.manage.response.*;
import com.stardai.manage.utils.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.stardai.manage.mapper.UserMapper;
import com.stardai.manage.pojo.PayWallet;
import com.stardai.manage.pojo.User;
import com.stardai.manage.pojo.UserSystemMessage;

/**
 * @author jdw
 * @date 2017/10/16
 */
@Service
@SuppressWarnings("all")
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private Base64Image base;

    @Autowired
    private PersonUtils personUtils;

    @Autowired
    private RedisCacheManager redisCacheManager;

    private static SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private Des des;

    // 根据手机号查询是否存在用户
    public boolean checkUser(String mobileNumber) {
        User user = userMapper.checkUser(mobileNumber);
        if (user != null) {
            return true;
        }
        return false;
    }

    // 根据手机号查询是否存在用户
    public User checkUserForShare(String mobileNumber) {
        User user = userMapper.checkUser(mobileNumber);
        return user;
    }

    // 添加用户
    public String addUserInfo(RequestRegister userInfo) throws Exception {
        String userId = Utils.getUUID();
        // 密码密文
        String encodepassword = userInfo.getPassword();
        // 密文解密
        // String password = des.decodePassword(encodepassword);
        String mobileNumber = userInfo.getMobileNumber();
        userMapper.addUserInfo(userId, encodepassword, mobileNumber);
        return userId;
    }

    // 获取用户信息
    public User getUserInfo(String userId) {
        User user = userMapper.getUserInfo(userId);
        return user;
    }

    // 根据用户id查询密码
    public String getPasswordByUserId(String userId) {
        return userMapper.getPasswordByUserId(userId);
    }

    // 根据用户id查询手机号
    public String getPhoneNumberByUserId(String userId) {
        return userMapper.getPhoneNumberByUserId(userId);
    }

    // 登录查询用户是否存在
    public ResponseUser getUser(String mobileNumber, String password) {
        ResponseUser user = userMapper.getUser(mobileNumber, password);
        return user;
    }

    // 上传头像
    public String updateAvatar(String userId, String avatar, HttpServletRequest request) {
        String imgurl = base.GenerateImage(avatar, request, OssClientConstants.AVTARFOLDER);
        Integer updataAvatar = userMapper.updateAvatar(userId, imgurl);
        if (updataAvatar == 1) {
            return imgurl;
        }
        return null;
    }

    /**
     * @Author : jokery
     * @Description : 实名认证-身份认证
     * @Date : 2018/3/5 17:38
     * @Param :person (封装了userId,userName,idCard,mobileNumber)
     * @Return : ResponseUserInfo
     */

    public ResponseUserInfo approvePerson(RequestPersonVerifyInfo person) {
        ResponseUserInfo userInfoResult = new ResponseUserInfo();
        // 先到数据库查询身份证号有没有被认证过
        String idCard = person.getIdCardNumber();
        Integer isUsed = this.userMapper.checkUserIdCard(idCard);
        if (isUsed != 0) {
            userInfoResult.setApprovePerson(3);
            return userInfoResult;
        }
        //用户个人认证时人脸识别
        boolean getResult = FaceRecognition.personverify(person);
        if (!getResult) {
            //人脸识别失败
            userInfoResult.setApprovePerson(2);
            return userInfoResult;
        } else {
            String userName = person.getName();
            String role = userName.substring(0, 1) + "经理";
            Integer result = userMapper.updateUserInfo(person.getUserId(), userName, idCard,
                    person.getSex(), person.getAddress(), role);
            if (result != 0) {
                // 接下来调用身份证,手机,
                // 先用userId去查手机号
                String mobilenumber = this.userMapper.getPhoneNumberByUserId(person.getUserId());
                HashMap<String, String> res = personUtils.approvePersonByMobileNumber(userName, idCard, mobilenumber);
                try {
                    if (res != null) {
                        person.setRes(res.get("res"));
                        person.setResmsg(res.get("resmsg"));
                        // 向用户信息扩展表中添加一条数据记录三要素认证的结果
                        this.userMapper.insertMobileApproveResult(person);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                userInfoResult.setApprovePerson(1);
                userInfoResult.setRole(role);
                return userInfoResult;
            } else {
                return null;
            }
        }

    }

    // 反馈信息
    public void submitFeedback(RequestFeedBack feedBack) {
        userMapper.submitFeedback(feedBack);
    }

    // 根据userId查询姓名
    public String queryNameByUserId(String userId) {
        String name = userMapper.queryNameByUserId(userId);
        return name;
    }

    /*
     * // 企业认证 public String GenerateImage(String image, HttpServletRequest
     * request, String userId) { // 查询姓名 String name =
     * userMapper.queryNameByUserId(userId); String imgurl =
     * base.GenerateImageForCompany(image, request); return imgurl; }
     */

    // 企业认证
    public String GenerateImage(RequestCompany company, HttpServletRequest request, String name) {
        StringBuffer sb = new StringBuffer();
        sb.append(OssClientConstants.POSTBARURL + OssClientConstants.FOLDER + "/" + company.getUserId() + name + "/");
        ArrayList<String> companyImages = company.getCompanyImages();
        for (String image : companyImages) {
            String imgurl = base.GenerateImageForCompany(image, request,
                    OssClientConstants.FOLDER + "/" + company.getUserId() + name);
            sb.append(imgurl).append(",");
        }
        String images = sb.substring(0, sb.length() - 1);
        return images;
    }

    // 第一次认证添加
    public boolean addApproveCompany(RequestCompany company, String images) {
        Integer result = userMapper.addApproveCompany(company.getCompanyType(), company.getCompanyAddress(),
                company.getCompanyBranch(), company.getCompanyCity(), company.getCompanyName(), company.getUserId(),
                images);
        userMapper.updateApproveCompanyOnUser(company.getUserId());

        if (company.getCertificationPhone() != null && !company.getCertificationPhone().isEmpty()) {
            userMapper.updateCfPhoneOnUser(company.getUserId(), company.getCertificationPhone());
        }

        if (result != 0) {
            return true;
        }
        return false;
    }

    // 修改企业认证
    public boolean updateApproveCompany(RequestCompany company, String images) {
        Integer result = userMapper.updateApproveCompany(company.getCompanyType(), company.getCompanyAddress(),
                company.getCompanyBranch(), company.getCompanyCity(), company.getCompanyName(), company.getUserId(),
                images);
        userMapper.updateApproveCompanyOnUser(company.getUserId());

        if (company.getCertificationPhone() != null && !company.getCertificationPhone().isEmpty()) {
            userMapper.updateCfPhoneOnUser(company.getUserId(), company.getCertificationPhone());
        }

        if (result != 0) {
            return true;
        }
        return false;
    }

    // 查询企业认证状态
    public Integer queryApproveCompanyByUserId(String userId) {
        Integer result = userMapper.queryApproveCompanyByUserId(userId);
        return result;
    }

    // 修改密码与找回密码
    public Integer updatePassWord(String mobileNumber, String newPassword) {
        Integer updatePassWord = userMapper.updatePassWord(mobileNumber, newPassword);
        return updatePassWord;
    }

    // 阅读消息
    public boolean updatePersonalMessage(String userId, Integer id) {
        Integer result = userMapper.updatePersonalMessage(userId, id);
        if (result != 0) {
            return true;
        }
        return false;
    }

    // 获得个人未读的消息
    public List<ResponsePersonalMessage> queryPersonalMessage(RequestMessage requestMessage) {
        int pageSize = requestMessage.getPageSize();
        int page = requestMessage.getPage() * pageSize;
        String userId = requestMessage.getUserId();
        List<ResponsePersonalMessage> responsePersonalMessage = userMapper.queryPersonalMessage(page, pageSize, userId);
        return responsePersonalMessage;
    }

    // 根据userId查询此人有没有认证
    public boolean queryApproveByUserId(String userId) {
        ResponseApprove result = userMapper.queryApproveByUserId(userId);

        if (result.getApproveCompany() == 1 && result.getApprovePerson() == 1) {
            return true;

        } else {
            return false;
        }

    }

    // 查询此人账户金额是否够此次消费
    public boolean queryMoneyByUserId(double price, String userId) {
        PayWallet result = userMapper.queryMoneyByUserId(userId);
        double allMoney = result.getmPresent() + result.getmRecharge();
        if (allMoney < price) {
            return false;
        }
        return true;
    }

    // 公告通知
    public List<UserSystemMessage> querySystemMessage(RequestSystemMessage requestSystemMessage) {
        int pageSize = requestSystemMessage.getPageSize();
        int page = requestSystemMessage.getPage() * pageSize;
        List<UserSystemMessage> systemMessage = userMapper.querySystemMessage(page, pageSize);
        return systemMessage;
    }

    // 根据UserID查询企业认证
    public Integer queryApproveCompany(String userId) {
        Integer result = userMapper.queryApproveCompany(userId);
        return result;
    }

    // 根据userId查询是否曾通过认证
    public Integer queryHasApproved(String userId) {
        Integer result = userMapper.queryHasApproved(userId);
        return result;
    }

    // 查询用户对于订单推送的相关设置
    public UserPush queryUserPush(String userId) {
        return userMapper.queryUserPush(userId);
    }

    // 查询用户实名认证的公司所在城市,用户给推送订单的城市默认赋值
    public String queryCompanyCity(String userId) {
        return userMapper.queryCompanyCity(userId);
    }

    // 新增用户接收订单推送数据记录
    public Integer insertUserPush(UserPush userPush) {
        return userMapper.insertUserPush(userPush);
    }

    // 更新用户接收订单推送的设置
    public Integer updateUserPush(UserPush userPush) {
        return userMapper.updateUserPush(userPush);
    }

    /**
     * @Author : jokery
     * @Description : 查信贷经理的实名认证信息
     * @Date : 2018/3/12 14:19
     * @Param :userId
     * @Return :
     */
    public ResponseApproveData queryApproveData(String userId) {
        ResponseApproveData approveData = this.userMapper.queryApproveData(userId);
        // 如果根据id查不到,说明没有进行过实名认证
        if (approveData == null) {
            return null;
        } else {
            // 查到的时间是有时分秒的,前端需要的是年月日
            String applyTime = approveData.getCreateTime().substring(0, 10);
            String[] split = applyTime.split("-");
            if (split[1].startsWith("0")) {
                split[1] = split[1].substring(1, 2);
            }
            if (split[2].startsWith("0")) {
                split[2] = split[2].substring(1, 2);
            }
            applyTime = split[0] + "年" + split[1] + "月" + split[2] + "日";
            approveData.setCreateTime(applyTime);
            return approveData;
        }
    }

    /**
     * @Author:Tina
     * @Description:TODO
     * @Date:2018年5月3日下午1:01:38
     * @Param:
     * @Return:List<HashMap<String,Object>>
     */
    public List<HashMap<String, Object>> getAllCompanyNames() {
        return userMapper.getAllCompanyNames();
    }

    /**
     * @Author:Tina
     * @Description:已认证用户更改手机号时人脸识别
     * @Date:2018年6月20日下午5:28:48
     * @Param:
     * @Return:String
     */
    public int getfaceRecognizeByChangePhone(RequestPersonVerifyInfo requestPersonVerifyInfo) {
        boolean getResult = false;
        String userId = requestPersonVerifyInfo.getUserId();
        //更改手机号时，用户人脸识别，需要从后台获取身份证号和姓名
        ResponseUserCardAndName userInfo = userMapper.getUserCardAndNameByUserId(userId);
        if(userInfo != null){
            requestPersonVerifyInfo.setIdCardNumber(userInfo.getIdCard());
            requestPersonVerifyInfo.setName(userInfo.getUserName());
            getResult = FaceRecognition.personverify(requestPersonVerifyInfo);
            //如果人脸识别没有通过，要计算失败次数，更换手机号人脸识别三次失败就只能第二天再修改
            if (!getResult) {
                long dayMis = 0;
                // 获取当前时间与当天最后时间相差的毫秒值
                try {
                    dayMis = sdfDay.parse(DateUtil.getDay()).getTime() + 1000 * 60 * 60 * 24L - 1
                            - System.currentTimeMillis();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String reKey = "face_Recognize_" + userId;
                int revalue = 1;
                // 查看今天更换手机号人脸识别有没有失败的
                if (redisCacheManager.hasKey(reKey)) { // 如果输错过
                    revalue = (int) redisCacheManager.get(reKey);
                    revalue += 1;
                    redisCacheManager.set(reKey, revalue, redisCacheManager.getExpire(reKey));
                } else { // 今天更换手机号人脸识别没有失败过
                    redisCacheManager.set(reKey, revalue, dayMis);

                }
                return 2;
            }else{//验证成功

               //用户扩展表里是否存在数据
                int isExist = userMapper.getUserExistFromExtended(userId);
                if(isExist != 0){
                    userMapper.updateUserFaceImage(requestPersonVerifyInfo);
                }else{
                   userMapper.insertUserFaceImage(requestPersonVerifyInfo);
                }
                return 1;
            }
        }

        return 3;
    }

    public Integer queryPersonalMessageAmount(String userId) {
        return userMapper.queryPersonalMessageAmount(userId);
    }

    public Integer setAllMessageRead(String userId) {
        return userMapper.setAllMessageRead(userId);
    }

    //验证码登录，根据手机号查询用户的具体信息
    public ResponseUser getUserInfoByPhone(String mobileNumber) {
        return userMapper.getUserInfoByPhone(mobileNumber);
    }

    public void updateUserToken(String userId, String token) {
        userMapper.updateUserToken(userId, token);
    }

    public String getTokenByUserId(String userId) {
        return userMapper.getTokenByUserId(userId);
    }

    public Integer updatePhoneByUserId(String userId, String mobileNumber) {
        return userMapper.updatePhoneByUserId(userId, mobileNumber);
    }

    public void addChangePhoneRecord(RequestPhoneAndVerifyCode requestPhoneAndVerifyCode) {
        userMapper.addChangePhoneRecord(requestPhoneAndVerifyCode);
    }

    //根据用户当前登陆的手机号码，查找他最初注册app的手机号
    public String getInitialPhoneByNowPhone(String mobileNumber) {
        return userMapper.getInitialPhoneByNowPhone(mobileNumber);
    }

    //更新用户的惩罚状态和登录时间
    public void updateUserLoginStatus(String userId) {
        userMapper.updateUserLoginStatus(userId);
    }

    //根据用户userId 查看是否对该用户有处罚措施
    public HashMap<String,Integer> getPunishCodeByUserId(String userId) {
        return userMapper.getPunishCodeByUserId(userId);
    }
}
