package com.stardai.manage.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 优接单退款申请
 *
 * @author jokery
 * @create 2018-02-02 9:25
 **/

public class RequestRefundApplication {

    @NotNull(message = "信贷经理手机号不能为空")
    @Size(min = 11, max = 11, message = "手机号为11位")
    private String mobileNumber;

    @NotNull(message = "借款人手机号不能为空")
    @Size(min = 11, max = 11, message = "手机号为11位")
    private String loanPhone;

    @NotNull(message = "要退款的订单号不能为空")
    private Long orderNo;

    @NotNull(message = "订单金额不能为空")
    private Double orderPrice;

    @NotNull(message = "退款原因不能为空")
    private String reason;

    private String userId;

    private String description;
    private String imageUrl;
    public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public String toString() {
		return "RequestRefundApplication [mobileNumber=" + mobileNumber + ", loanPhone=" + loanPhone + ", orderNo="
				+ orderNo + ", orderPrice=" + orderPrice + ", reason=" + reason + ", description=" + description
				+ ", imageUrl=" + imageUrl + "]";
	}

    public String getLoanPhone() {
        return loanPhone;
    }

    public void setLoanPhone(String loanPhone) {
        this.loanPhone = loanPhone;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public Double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(Double orderPrice) {
        this.orderPrice = orderPrice;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
