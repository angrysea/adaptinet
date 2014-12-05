package com.db.ess.synthesis.util;

import org.apache.log4j.Logger;

public class ResourceMonitor {
	
	private static final Logger logger = Logger.getLogger(ResourceMonitor.class.getName());
	
	public static void memoryUsage() {
		
		long max = Runtime.getRuntime().maxMemory();
		long allocated = Runtime.getRuntime().totalMemory();
		long free = Runtime.getRuntime().freeMemory();
		
		logger.info("===================================================");
		logger.info("Max memory: " + max);
		logger.info(" Allocated: " + allocated);
		logger.info("      Free: " + (free + (max - allocated)));
		logger.info("===================================================");
	}
	
}
