package com.db.ess.synthesis.vo;

import java.io.Serializable;

public class Exchange implements Serializable {
	
	private int exchId;
	private String exchCode;
	private String description;
	private String countryCode;
	public int getExchId() {
		return exchId;
	}
	public void setExchId(int exchId) {
		this.exchId = exchId;
	}
	public String getExchCode() {
		return exchCode;
	}
	public void setExchCode(String exchCode) {
		this.exchCode = exchCode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	

}
