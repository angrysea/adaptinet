package com.db.ess.synthesis.util;

public class ESSLocation {

	public static final int UNKNOWN = 0;
	public static final int LONDON = UNKNOWN + 1;
	public static final int NEWYORK = UNKNOWN + 2;
	public static final int GLOBAL = UNKNOWN + 3;
	
	private static final String london = "ESS London";
	private static final String newyork = "ESS New York";
	private static final String global = "ESS Global";
	
	static public int getLocation(String name) {
		int location = UNKNOWN;
		
		if(name.equalsIgnoreCase(london)) {
			location = LONDON;
		}
		else if(name.equalsIgnoreCase(newyork)) {
			location = NEWYORK;
		}
		else if(name.equalsIgnoreCase(global)) {
			location = GLOBAL;
		}
		return location;
	}
}
