package com.stardai.manage.response;

/**
 * @author jdw
 * @date 2017/10/16
 */
public class ResponsePersonalMessage {

	private long id;

	private String messageTitle;

	private String message;

	private String createTime;
	
	private Integer status;
	
	private Integer type;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMessageTitle() {
		return messageTitle;
	}

	public void setMessageTitle(String messageTitle) {
		this.messageTitle = messageTitle;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "ResponsePersonalMessage [id=" + id + ", messageTitle=" + messageTitle + ", message=" + message
				+ ", createTime=" + createTime + ", status=" + status + ", type=" + type + "]";
	}

}
