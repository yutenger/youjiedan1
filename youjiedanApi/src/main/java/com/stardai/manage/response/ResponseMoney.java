package com.stardai.manage.response;

public class ResponseMoney {

	private double allMoney;

	private double moneyRecharge;

	private double moneyPresent;

	private int totalCredit;

	public double getAllMoney() {
		return allMoney;
	}

	public void setAllMoney(double allMoney) {
		this.allMoney = allMoney;
	}

	public double getMoneyRecharge() {
		return moneyRecharge;
	}

	public void setMoneyRecharge(double moneyRecharge) {
		this.moneyRecharge = moneyRecharge;
	}

	public double getMoneyPresent() {
		return moneyPresent;
	}

	public void setMoneyPresent(double moneyPresent) {
		this.moneyPresent = moneyPresent;
	}

	public int getTotalCredit() {
		return totalCredit;
	}

	public void setTotalCredit(int totalCredit) {
		this.totalCredit = totalCredit;
	}

	@Override
	public String toString() {
		return "ResponseMoney{" +
				"allMoney=" + allMoney +
				", moneyRecharge=" + moneyRecharge +
				", moneyPresent=" + moneyPresent +
				", totalCredit=" + totalCredit +
				'}';
	}
}
