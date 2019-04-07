package com.stardai.manage.response;

public class ResponseOrderRefund {
 private long orderNo;//要退款的订单号
 private double orderPrice;//订单价格
 private Integer isDone;//退单状态
 private String reason;//退款原因
 private String description;//问题描述
 private long mobileNumber;//借贷经理手机号
 private long loanPhone;//借款人的手机号
 private String imageUrl;//图片url链接
 private String createTime;//创建时间
@Override
public String toString() {
	return "ResponseOrderRefund [orderNo=" + orderNo + ", orderPrice=" + orderPrice + ", isDone=" + isDone + ", reason="
			+ reason + ", description=" + description + ", mobileNumber=" + mobileNumber + ", loanPhone=" + loanPhone
			+ ", imageUrl=" + imageUrl + ", createTime=" + createTime + "]";
}
public long getOrderNo() {
	return orderNo;
}
public void setOrderNo(long orderNo) {
	this.orderNo = orderNo;
}
public double getOrderPrice() {
	return orderPrice;
}
public void setOrderPrice(double orderPrice) {
	this.orderPrice = orderPrice;
}
public Integer getIsDone() {
	return isDone;
}
public void setIsDone(Integer isDone) {
	this.isDone = isDone;
}
public String getReason() {
	return reason;
}
public void setReason(String reason) {
	this.reason = reason;
}
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}
public long getMobileNumber() {
	return mobileNumber;
}
public void setMobileNumber(long mobileNumber) {
	this.mobileNumber = mobileNumber;
}
public long getLoanPhone() {
	return loanPhone;
}
public void setLoanPhone(long loanPhone) {
	this.loanPhone = loanPhone;
}
public String getImageUrl() {
	return imageUrl;
}
public void setImageUrl(String imageUrl) {
	this.imageUrl = imageUrl;
}
public String getCreateTime() {
	return createTime;
}
public void setCreateTime(String createTime) {
	this.createTime = createTime;
}

}
