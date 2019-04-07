package com.stardai.manage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.stardai.manage.mapper.VersionMapper;
import com.stardai.manage.pojo.SysVersionCheck;
import com.stardai.manage.pojo.SysVersionCheckForIOS;

/**
 * @author jokery
 * @date 创建时间：2017年12月4日 上午11:32:15
 * @类说明
 */

@Service
@SuppressWarnings("all")
public class VersionService {

	@Autowired
	private VersionMapper versionMapper;

	// 查询当前最新版本(安卓)
	public SysVersionCheck checkVersionAndroid() {
		SysVersionCheck sysVersionCheck = versionMapper.checkVersionAndroid();
		return sysVersionCheck;
	}
	
	// 查询iOS不同版本的状态，即是否通过审核
	public SysVersionCheckForIOS checkStatusForIOSVersion(String iosVersion) {
		SysVersionCheckForIOS sysVersionCheck = versionMapper.checkStatusForIOSVersion(iosVersion);
		return sysVersionCheck;
	}

}
