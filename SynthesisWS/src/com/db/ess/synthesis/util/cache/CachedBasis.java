package com.db.ess.synthesis.util.cache;

public class CachedBasis {

    private int unitType;
    private String unitTypeCode;
    private int days;
    private short month30days;
    private short display;

    public final int getUnitType() {
		return unitType;
	}
	public final void setUnitType(int unitType) {
		this.unitType = unitType;
	}
	public final String getUnitTypeCode() {
		return unitTypeCode;
	}
	public final void setUnitTypeCode(String unitTypeCode) {
		this.unitTypeCode = unitTypeCode;
	}
	public final int getDays() {
		return days;
	}
	public final void setDays(int days) {
		this.days = days;
	}
	public final short getMonth30days() {
		return month30days;
	}
	public final void setMonth30days(short month30days) {
		this.month30days = month30days;
	}
	public final short getDisplay() {
		return display;
	}
	public final void setDisplay(short display) {
		this.display = display;
	}
}
