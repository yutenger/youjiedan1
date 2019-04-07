package com.stardai.manage.pojo;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author jokery
 * @date 创建时间：2017年12月4日 上午10:59:17
 * @类说明 用来给前端打开app时调用的检测最新版本接口封装数据
 */

// 最新版本信息表
@Table(name = "yjd_sys_version_check")
public class SysVersionCheck {
	@Id
	private Integer uuid;

	private String osType;

	private String id;

	private Integer page;

	private Integer rows;

	private Integer isForceUpdate;

	private Integer preBaselineCode;

	private String versionName;

	private String versionCode;

	private String downurl;

	private String updateLog;

	private Long size;

	private String hasAffectCodes;

	private Long createTime;

	private String iosVersion;

	private String verifyStatus;

	private Integer isDeleted;

	public SysVersionCheck() {
		super();
	}

	public SysVersionCheck(Integer uuid, String osType, String id, Integer page, Integer rows, Integer isForceUpdate,
			Integer preBaselineCode, String versionName, String versionCode, String downurl, String updateLog,
			Long size, String hasAffectCodes, Long createTime, String iosVersion, String verifyStatus,
			Integer isDeleted) {
		super();
		this.uuid = uuid;
		this.osType = osType;
		this.id = id;
		this.page = page;
		this.rows = rows;
		this.isForceUpdate = isForceUpdate;
		this.preBaselineCode = preBaselineCode;
		this.versionName = versionName;
		this.versionCode = versionCode;
		this.downurl = downurl;
		this.updateLog = updateLog;
		this.size = size;
		this.hasAffectCodes = hasAffectCodes;
		this.createTime = createTime;
		this.iosVersion = iosVersion;
		this.verifyStatus = verifyStatus;
		this.isDeleted = isDeleted;
	}

	public Integer getUuid() {
		return uuid;
	}

	public void setUuid(Integer uuid) {
		this.uuid = uuid;
	}

	public String getOsType() {
		return osType;
	}

	public void setOsType(String osType) {
		this.osType = osType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public Integer getIsForceUpdate() {
		return isForceUpdate;
	}

	public void setIsForceUpdate(Integer isForceUpdate) {
		this.isForceUpdate = isForceUpdate;
	}

	public Integer getPreBaselineCode() {
		return preBaselineCode;
	}

	public void setPreBaselineCode(Integer preBaselineCode) {
		this.preBaselineCode = preBaselineCode;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public String getDownurl() {
		return downurl;
	}

	public void setDownurl(String downurl) {
		this.downurl = downurl;
	}

	public String getUpdateLog() {
		return updateLog;
	}

	public void setUpdateLog(String updateLog) {
		this.updateLog = updateLog;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getHasAffectCodes() {
		return hasAffectCodes;
	}

	public void setHasAffectCodes(String hasAffectCodes) {
		this.hasAffectCodes = hasAffectCodes;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public String getIosVersion() {
		return iosVersion;
	}

	public void setIosVersion(String iosVersion) {
		this.iosVersion = iosVersion;
	}

	public String getVerifyStatus() {
		return verifyStatus;
	}

	public void setVerifyStatus(String verifyStatus) {
		this.verifyStatus = verifyStatus;
	}

	@Override
	public String toString() {
		return "SysVersionCheck [uuid=" + uuid + ", osType=" + osType + ", id=" + id + ", page=" + page + ", rows="
				+ rows + ", isForceUpdate=" + isForceUpdate + ", preBaselineCode=" + preBaselineCode + ", versionName="
				+ versionName + ", versionCode=" + versionCode + ", downurl=" + downurl + ", updateLog=" + updateLog
				+ ", size=" + size + ", hasAffectCodes=" + hasAffectCodes + ", createTime=" + createTime
				+ ", iosVersion=" + iosVersion + ", verifyStatus=" + verifyStatus + ", isDeleted=" + isDeleted + "]";
	}

}
