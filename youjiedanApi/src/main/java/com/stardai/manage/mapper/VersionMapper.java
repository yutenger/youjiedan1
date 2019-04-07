package com.stardai.manage.mapper;

import com.stardai.manage.pojo.SysVersionCheck;
import com.stardai.manage.pojo.SysVersionCheckForIOS;

/**
 * @author jokery
 * @date 创建时间：2017年12月4日 下午1:03:24
 * @类说明
 */
public interface VersionMapper {

	// 查询当前最新版本(安卓)
	SysVersionCheck checkVersionAndroid();
	
	//查询iOS不同版本的状态，即是否通过审核
	SysVersionCheckForIOS checkStatusForIOSVersion(String iosVersion);

}
