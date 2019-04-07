package com.stardai.manage.mapper;

import org.apache.ibatis.annotations.Param;
import com.stardai.manage.pojo.UserSmsCode;

/**
 * @author jdw
 * @date 2017/10/16
 */
public interface UserSmsCodeMapper {

	// 根据手机号查找验证码
	UserSmsCode querySmsCode(String mobileNumber);

	// 添加手机号与验证码
	void addSmsCodeBymoblie(@Param("mobileNumber") String mobileNumber, @Param("verifyCode") String verifyCode,
                            @Param("createTime") Long createTime);

	// 将旧的验证码覆盖
	Integer updateSmsCode(@Param("mobileNumber") String mobileNumber, @Param("verifyCode") String verifyCode,
                          @Param("createTime") Long createTime);

	/**
	 * @Author:Tina
	 * @Description:极光发送短信失败时，使用云片发送，并记录使用云片发送短信的时间
	 * @Date:2018年5月22日下午5:46:30
	 * @Param:
	 * @Return:void
	 */
	void addUnNaturalMessage(@Param("mobileNumber") String mobileNumber);

	/**
	 * @Author:Tina
	 * @Description极光发送短信失败时，使用云片发送，记录之前查下之前有没有记录过
	 * @Date:2018年5月29日下午5:24:18
	 * @Param:
	 * @Return:String
	 */
	String isOnceSaved(String mobileNumber);

}
