package com.stardai.manage.mapper;

import java.util.HashMap;
import java.util.List;

import com.stardai.manage.pojo.UserPush;
import com.stardai.manage.request.RequestPersonVerifyInfo;
import com.stardai.manage.request.RequestPhoneAndVerifyCode;
import com.stardai.manage.request.RequestUserLoginLocation;
import com.stardai.manage.response.*;
import org.apache.ibatis.annotations.Param;
import com.stardai.manage.pojo.PayWallet;
import com.stardai.manage.pojo.User;
import com.stardai.manage.pojo.UserSystemMessage;
import com.stardai.manage.request.RequestFeedBack;

/**
 * @author jdw
 * @date 2017/10/16
 */
public interface UserMapper {

	/*
	 * // 修改用户信息 public Integer updateUser(User user);
	 */

	// 获取用户信息
	User getUserInfo(@Param("userId") String userId);

	// 根据手机号校验用户是否存在
	User checkUser(@Param("mobileNumber") String mobileNumber);

	// 注册页面添加用户信息
	Integer addUserInfo(@Param("userId") String userId, @Param("password") String password,
                        @Param("mobileNumber") String mobileNumber);

	// 注册页面添加用户和邀请人userId信息
	Integer addUserInfoAndSharedId(@Param("userId") String userId, @Param("password") String password,
                                   @Param("mobileNumber") String mobileNumber, @Param("sharedId") String sharedId);

	// 根据userId查询用户密码
	String getPasswordByUserId(@Param("userId") String userId);

	// 根据用户id查询手机号
	String getPhoneNumberByUserId(@Param("userId") String userId);

	// 登录查询 是否存在用户
	ResponseUser getUser(@Param("mobileNumber") String mobileNumber, @Param("password") String password);
	/*
	 * // 图片修改 public Integer updateAvatar(@Param("userId") String
	 * userId, @Param("avatar") String avatar);
	 * 
	 * 
	 * 
	 * // 芝麻分添加 public Integer addZhiMaFen(@Param("name") String
	 * name, @Param("idCard") String idCard,
	 * 
	 * @Param("userId") String userId, @Param("openId") String
	 * openId, @Param("zmScore") String zmScore);
	 * 
	 * // 取消芝麻授权 public Integer deleteZhiMaShouQuan(@Param("userId") String
	 * userId);
	 * 
	 * // 根据userId查询该用户是否授权芝麻分 public Integer
	 * queryZhiMaShouQuan(@Param("userId") String userId);
	 * 
	 * // 保存分数 public Integer updateScore(@Param("userId") String
	 * userId, @Param("score") Integer score);
	 */

	// 修改头像
	Integer updateAvatar(@Param("userId") String userId, @Param("imgurl") String imgurl);

	// 实名认证添加数据
	Integer updateUserInfo(@Param("userId") String userId, @Param("userName") String name,
                           @Param("idCard") String idCard, @Param("sex") String sex, @Param("address") String address,
                           @Param("role") String role);

	// 反馈信息
	void submitFeedback(RequestFeedBack feedBack);

	// 查询姓名
	String queryNameByUserId(@Param("userId") String userId);

	// 企业认证
	Integer updateApproveCompany(@Param("companyType") String companyType,
                                 @Param("companyAddress") String companyAddress, @Param("companyBranch") String companyBranch,
                                 @Param("companyCity") String companyCity, @Param("companyName") String companyName,
                                 @Param("userId") String userId, @Param("images") String images);

	// 修改企业认证状态
	void updateApproveCompanyOnUser(@Param("userId") String userId);

	// 查询企业认证状态
	Integer queryApproveCompanyByUserId(@Param("userId") String userId);

	// 第一次认证添加
	Integer addApproveCompany(@Param("companyType") String companyType, @Param("companyAddress") String companyAddress,
                              @Param("companyBranch") String companyBranch, @Param("companyCity") String companyCity,
                              @Param("companyName") String companyName, @Param("userId") String userId, @Param("images") String images);

	// 修改密码
	public Integer updatePassWord(@Param("mobileNumber") String mobileNumber, @Param("password") String password);

	// 阅读消息
	Integer updatePersonalMessage(@Param("userId") String userId, @Param("id") Integer id);

	// 获得个人未读的消息
	List<ResponsePersonalMessage> queryPersonalMessage(@Param("page") int page, @Param("pageSize") int pageSize,
                                                       @Param("userId") String userId);

	// 根据userId查询此人有没有认证
	ResponseApprove queryApproveByUserId(@Param("userId") String userId);

	// 查询此人账户金额是否够此次消费
	PayWallet queryMoneyByUserId(@Param("userId") String userId);

	// 公告通知
	List<UserSystemMessage> querySystemMessage(@Param("page") int page, @Param("pageSize") int pageSize);

	// 根据UserID查询用户认证状态
	ResponseApprove queryApproveStatus(@Param("userId") String userId);

	// 根据userId查询是否曾通过认证
	Integer queryHasApproved(@Param("userId") String userId);

	//查询用户对于订单推送的相关设置
    UserPush queryUserPush(@Param("userId") String userId);

	//查询用户实名认证的公司所在城市,用户给推送订单的城市默认赋值
	String queryCompanyCity(@Param("userId") String userId);

	//新增用户接收订单推送数据记录
    Integer insertUserPush(@Param("userPush") UserPush userPush);

    //更新用户接收订单推送的设置
	Integer updateUserPush(@Param("userPush") UserPush userPush);

	//向用户信息扩展表中添加用户图片
    void insertUserApproveImage(RequestPersonVerifyInfo person);

	//更新用户个人认证上传的图片
	void updateUserApproveImage(RequestPersonVerifyInfo person);

    //查询该身份证号是否已经被用于认证
	Integer checkUserIdCard(@Param("idCard") String idCard);

	//查实名认证信息
    ResponseApproveData queryApproveData(@Param("userId") String userId);

    //添加用户的实名手机号
	void updateCfPhoneOnUser(@Param("userId") String userId, @Param("certificationPhone") String certificationPhone);
	
	/**
	 * @Author:Tina
	 * @Description:返回公司名称及对应的简称，首字母(1.4.0加热门公司)
	 * @Date:2018年5月3日下午1:02:11
	 * @Param:
	 * @Return:List<HashMap<String,Object>>
	 */
	List<HashMap<String, Object>> getAllCompanyNames();

	//查询用户未读消息数量
    Integer queryPersonalMessageAmount(String userId);

	Integer setAllMessageRead(String userId);

	//验证码登录，根据手机号查询用户的具体信息
	ResponseUser getUserInfoByPhone(String mobileNumber);

    void updateUserToken(@Param("userId")String userId, @Param("token")String token);

    String getTokenByUserId(String userId);

    String getUserIdByMobileNumber(String mobileNumber);

	Integer updatePhoneByUserId(@Param("userId")String userId, @Param("mobileNumber")String mobileNumber);

	void addChangePhoneRecord(RequestPhoneAndVerifyCode requestPhoneAndVerifyCode);

	String getInitialPhoneByNowPhone(String mobileNumber);

    ResponseUserCardAndName getUserCardAndNameByUserId(String userId);

	Integer getUserExistFromExtended(@Param("userId")String userId);


	//更新库里用户更新手机号上传的人脸图片
	void updateUserFaceImage(RequestPersonVerifyInfo person);

	//插入用户更新手机号上传的人脸图片
	void insertUserFaceImage(RequestPersonVerifyInfo person);

	//更新用户的惩罚状态和登录时间
	void updateUserLoginStatus(String userId);

	//根据userId 查询是否对该用户有处罚措施
	HashMap<String,Integer> getPunishCodeByUserId(String userId);

	//查询此人之前有没有企业认证过
	String queryIsFirstSubmitApproveCompany(String userId);

    ResponseUser getRoleAndPhoneByUserId(String userId);

    //添加用户登陆的ip或者定位城市
    Integer addUserLoginLocation(RequestUserLoginLocation rull);

    //查看是否对此用户抢单进行限制
	Long isGrabDisabledByUserId(@Param("userId")String userId,@Param("currentTime")long currentTime);

	//查询限制用户抢单的一些参数
	ResponseGrabLimit queryGrabLimitPram();

	//限制用户一段时间内不能抢单
	Integer limitUserGrabOrder(@Param("userId")String userId, @Param("reason")String reason, @Param("beginTime")long beginTime, @Param("endTime")long endTime,@Param("refundFailTime") long refundFailTime);

	//查看用户之前是否添加过相同的ip
    Integer getIsAddSameIp(RequestUserLoginLocation rull);

    //更新用户ip和所在位置
    Integer updateUserLoginLocation(RequestUserLoginLocation rull);

    //查询用户个人认证状态
	Integer queryApprovePerson(String userId);
}
