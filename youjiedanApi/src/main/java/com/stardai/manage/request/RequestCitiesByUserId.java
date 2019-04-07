package com.stardai.manage.request;

import java.util.ArrayList;

/**
 * @author jdw
 * @date 2017/10/16
 */
public class RequestCitiesByUserId {

	private String userId;
	
	private ArrayList<String> cities;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public ArrayList<String> getCities() {
		return cities;
	}

	public void setCities(ArrayList<String> cities) {
		this.cities = cities;
	}

	@Override
	public String toString() {
		return "RequestCitiesByUserId [userId=" + userId + "]";
	}

	

}
