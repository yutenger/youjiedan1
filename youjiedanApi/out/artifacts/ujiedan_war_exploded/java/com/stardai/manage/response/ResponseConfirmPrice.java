package com.stardai.manage.response;

/**
 * @author jdw
 * @date 2017/10/16
 */
public class ResponseConfirmPrice {

	private Integer orderAmount;

	private long orderTime;
	
	public String  channel;
	
	public String channelBranch;

	public String getChannelBranch() {
		return channelBranch;
	}

	public void setChannelBranch(String channelBranch) {
		this.channelBranch = channelBranch;
	}

	@Override
	public String toString() {
		return "ResponseConfirmPrice{" +
				"orderAmount=" + orderAmount +
				", orderTime=" + orderTime +
				", channel='" + channel + '\'' +
				", channelBranch='" + channelBranch + '\'' +
				'}';
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
	
	

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

}
