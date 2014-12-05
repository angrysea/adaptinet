package com.db.ess.synthesis.util;

public class SynthesisConstants {
	
	private static final String MAX_RESULT_SET_SIZE_PROPERTY = "max.result.set.size";
	
	public static final int MAX_RESULT_SET_SIZE = PropertiesUtil.getInstance()
			.getProperty(MAX_RESULT_SET_SIZE_PROPERTY) == null ? 10000 : Integer
			.parseInt(PropertiesUtil.getInstance().getProperty(
					MAX_RESULT_SET_SIZE_PROPERTY));
	
	private static final String RESULT_SET_OVERFLOW_MSG_PROPERTY = "result.set.overflow.message";

	public static final String RESULT_SET_OVERFLOW_MSG = PropertiesUtil
			.getInstance().getProperty(RESULT_SET_OVERFLOW_MSG_PROPERTY);
	
	private static final String RESULT_SET_OVERFLOW_ERRORCODE_PROPERTY = "result.set.overflow.errorcode";

	public static final int RESULT_SET_OVERFLOW_ERRORCODE = PropertiesUtil
			.getInstance().getProperty(RESULT_SET_OVERFLOW_ERRORCODE_PROPERTY) == null ? 301
			: Integer.parseInt(PropertiesUtil.getInstance().getProperty(
					RESULT_SET_OVERFLOW_ERRORCODE_PROPERTY));
	
	// constants used by mercury component to calculate price. 
	public static final int CALC_LIVE_PRICE = 0;	
	public static final int CALC_DEFAULT_PRICE = 1;
	public static final int CALC_HISTORIC_PRICE = 2;
	
}
