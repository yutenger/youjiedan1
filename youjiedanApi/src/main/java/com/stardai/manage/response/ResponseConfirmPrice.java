package com.stardai.manage.response;

/**
 * @author jdw
 * @date 2017/10/16
 */
public class ResponseConfirmPrice {

	private Integer orderAmount;

	private long orderTime;
	
	public String  channel;

	public Integer getDiscountedState() {
		return discountedState;
	}

	public void setDiscountedState(Integer discountedState) {
		this.discountedState = discountedState;
	}

	public String channelBranch;

	//判断此单卖出去是否结算，0：结算，1：不结算
	private  Integer valid;
	//打折状态 针对API设置
    private Integer discountedState;
	@Override
	public String toString() {
		return "ResponseConfirmPrice{" +
				"orderAmount=" + orderAmount +
				", orderTime=" + orderTime +
				", channel='" + channel + '\'' +
				", channelBranch='" + channelBranch + '\'' +
				", valid=" + valid +
				'}';
	}

	public String getChannelBranch() {
		return channelBranch;
	}

	public void setChannelBranch(String channelBranch) {
		this.channelBranch = channelBranch;
	}

	public Integer getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(Integer orderAmount) {
		this.orderAmount = orderAmount;
	}

	public long getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(long orderTime) {
		this.orderTime = orderTime;
	}

	public Integer getValid() {
		return valid;
	}

	public void setValid(Integer valid) {
		this.valid = valid;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

}
