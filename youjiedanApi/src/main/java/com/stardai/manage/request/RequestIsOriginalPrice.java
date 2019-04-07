package com.stardai.manage.request;

/**
 * 
 * @author Tina
 * 2018年6月6日
 */
public class RequestIsOriginalPrice {

	private int isOriginalPrice; //是否是原价单，1表示原价单不打折，0表示非原价单，2表示原价单打折

	private boolean priceResult; //查询单子价格是否正确

	

	public int getIsOriginalPrice() {
		return isOriginalPrice;
	}



	public void setIsOriginalPrice(int isOriginalPrice) {
		this.isOriginalPrice = isOriginalPrice;
	}



	public boolean getPriceResult() {
		return priceResult;
	}



	public void setPriceResult(boolean priceResult) {
		this.priceResult = priceResult;
	}



	@Override
	public String toString() {
		return "RequestIsOriginalPrice [isOriginalPrice=" + isOriginalPrice + ", priceResult=" + priceResult + "]";
	}

	

	
}
