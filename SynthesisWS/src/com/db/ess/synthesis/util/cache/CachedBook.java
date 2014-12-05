package com.db.ess.synthesis.util.cache;

public class CachedBook {
	
	private int bookId;
	private String name;
	private String adpAcctNum;
	private String accountname;
	private String deskname;
	private String businessUnitName;

	public final int getBookId() {
		return bookId;
	}
	public final void setBookId(int bookId) {
		this.bookId = bookId;
	}
	public final String getName() {
		return name;
	}
	public final void setName(String name) {
		this.name = name;
	}
	public final String getAdpAcctNum() {
		return adpAcctNum;
	}
	public final void setAdpAcctNum(String adpAcctNum) {
		this.adpAcctNum = adpAcctNum;
	}
	public final String getAccountname() {
		return accountname;
	}
	public final void setAccountname(String accountname) {
		this.accountname = accountname;
	}
	public final String getDeskname() {
		return deskname;
	}
	public final void setDeskname(String deskname) {
		this.deskname = deskname;
	}
	public final String getBusinessUnitName() {
		return businessUnitName;
	}
	public final void setBusinessUnitName(String businessUnitName) {
		this.businessUnitName = businessUnitName;
	}
}
