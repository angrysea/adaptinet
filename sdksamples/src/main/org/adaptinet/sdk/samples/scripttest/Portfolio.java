package org.adaptinet.sdk.samples.scripttest;

import java.util.HashMap;

public class Portfolio {
	private HashMap<String, Float> prices = new HashMap<String, Float>();
	private String symbols[] = new String[5];
 
	Portfolio() {
    	prices.put("IBM", new Float(110));
		prices.put("APL", new Float(320));
		prices.put("MSFT", new Float(42));
		prices.put("DB", new Float(60));
		prices.put("JPM", new Float(11));
		
		symbols[0] = "IBM";
		symbols[1] = "APL";
		symbols[2] = "MSFT";
		symbols[3] = "DB";
		symbols[4] = "JPM";
		
	}

	public String GetPrice(String symbol) {
		Float f = prices.get(symbol); 
		return f.toString();
	}

	public Float GetValue(String symbol) {
		 
		return prices.get(symbol);
	}
}
