package com.stardai.manage.pojo;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author jdw
 * @date 2017/10/16
 */
// 反馈
@Table(name = "yjd_user_feedback")
public class UserFeedBack {

	@Id
	private Long id;

	// 手机号码
	private String phoneNumber;

	private String userId;
	// 内容
	private String content;

	// 创建时间
	private long createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "FeedBack [id=" + id + ", phoneNumber=" + phoneNumber + ", userId=" + userId + ", content=" + content
				+ ", createTime=" + createTime + "]";
	}

}
