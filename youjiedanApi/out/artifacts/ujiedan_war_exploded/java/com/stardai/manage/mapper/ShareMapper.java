package com.stardai.manage.mapper;

import com.stardai.manage.response.ResponseShareUrl;
import org.apache.ibatis.annotations.Param;
import com.stardai.manage.response.ResponseShareUser;

public interface ShareMapper {

	// 查询金额与邀请的人数
	ResponseShareUser updateSharePerson(@Param("userId") String userId);

	//没有数据添加一条
	void addSharePerson(@Param("userId") String userId, @Param("shareUrl") String shareUrl, @Param("twoDimensionUrl") String twoDimensionUrl);

	// 查询分享链接
	String queryShareUrl(@Param("id") Integer id);

	//将推荐人的表中的次数+1和赠送金额增加
	void updateShareUserMoney(@Param("userId") String tuiJianRenUserId);

	//查询赠送的金额
	Integer queryShareGive(@Param("userId") String tuiJianRenUserId);

	//查询邀请链接和文案
    ResponseShareUrl queryShareUrlAndCopy(@Param("id") Integer id);

	/**
	 * @Author:Tina
	 * @Description:把二维码链接更新到数据库里
	 * @Date:2018年5月29日下午3:42:43
	 * @Param:
	 * @Return:void
	 */
	void updateTwoDimensionUrl(@Param("userId") String userId, @Param("twoDimensionUrl") String twoDimensionUrl);
}
