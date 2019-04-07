package com.stardai.manage.response;

import javax.persistence.Id;

/**
 * 
 * @author Administrator
 * 2018年5月10日
 */

//弹屏返回的信息
public class ResponsePopupInfo {
	
    private int priority;//1 版本更新 ，2 h5活动，3 邀请同行，4 充值， 5 认证
    
    private int eventId; //如果是h5活动的话，就返回活动的id
    
    private String url; //弹屏指向的url
    
    private String popUrl; //弹屏图片的url
    
    //安卓版本更新需要的参数
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

	private Integer isDeleted;
	
	//iOS版本更新需要的参数
	private String iosVersion;

	private String verifyStatus;
	
	
	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPopUrl() {
		return popUrl;
	}

	public void setPopUrl(String popUrl) {
		this.popUrl = popUrl;
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

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Override
	public String toString() {
		return "ResponsePopupInfo [priority=" + priority + ", eventId=" + eventId + ", url=" + url + ", popUrl="
				+ popUrl + ", uuid=" + uuid + ", osType=" + osType + ", id=" + id + ", page=" + page + ", rows=" + rows
				+ ", isForceUpdate=" + isForceUpdate + ", preBaselineCode=" + preBaselineCode + ", versionName="
				+ versionName + ", versionCode=" + versionCode + ", downurl=" + downurl + ", updateLog=" + updateLog
				+ ", size=" + size + ", hasAffectCodes=" + hasAffectCodes + ", createTime=" + createTime
				+ ", iosVersion=" + iosVersion + ", verifyStatus=" + verifyStatus + ", isDeleted=" + isDeleted + "]";
	}
    
    
    
   
    
}
