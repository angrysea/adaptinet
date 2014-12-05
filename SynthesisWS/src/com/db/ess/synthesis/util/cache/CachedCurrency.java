package com.db.ess.synthesis.util.cache;

public class CachedCurrency {

	private int ccyId;
	private String swiftCode;
    private int decPlaces;
    private int basis;
    private int swapBasis;
    private short spotDays;
    private short rounding;
    private int calId;
    private int rtFeedDivisor;
    private int display;

    public final int getCcyId() {
		return ccyId;
	}
	public final void setCcyId(int ccyId) {
		this.ccyId = ccyId;
	}
	public final String getSwiftCode() {
		return swiftCode;
	}
	public final void setSwiftCode(String swiftCode) {
		this.swiftCode = swiftCode;
	}
	public final int getDecPlaces() {
		return decPlaces;
	}
	public final void setDecPlaces(int decPlaces) {
		this.decPlaces = decPlaces;
	}
	public final int getBasis() {
		return basis;
	}
	public final void setBasis(int basis) {
		this.basis = basis;
	}
	public final int getSwapBasis() {
		return swapBasis;
	}
	public final void setSwapBasis(int swapBasis) {
		this.swapBasis = swapBasis;
	}
	public final short getSpotDays() {
		return spotDays;
	}
	public final void setSpotDays(short spotDays) {
		this.spotDays = spotDays;
	}
	public final short getRounding() {
		return rounding;
	}
	public final void setRounding(short rounding) {
		this.rounding = rounding;
	}
	public final int getCalId() {
		return calId;
	}
	public final void setCalId(int calId) {
		this.calId = calId;
	}
	public final int getRtFeedDivisor() {
		return rtFeedDivisor;
	}
	public final void setRtFeedDivisor(int rtFeedDivisor) {
		this.rtFeedDivisor = rtFeedDivisor;
	}
	public final int getDisplay() {
		return display;
	}
	public final void setDisplay(int display) {
		this.display = display;
	}
}
