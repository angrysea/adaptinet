package com.db.ess.synthesis.processor;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dao.tasks.exception.PendingExceptionCountService;
import com.db.ess.synthesis.dvo.PingRequest;
import com.db.ess.synthesis.dvo.PingResponse;
import com.db.ess.synthesis.util.cache.RefCache;

public class Ping extends SynthesisProcessor {
	
	private static final Logger logger = Logger.getLogger(DividendAltDivCcy.class.getName());
	
	public PingResponse ping(PingRequest request) {
		return this.getResponse(request, PingResponse.class); 
	}
		
	@Override
	public void init() {
		logger.info("...Loading Reference Data Cache...");
		RefCache.initCache();
		logger.info("...Done Loading Reference Data Cache...");
		logger.info("...Starting PendingExceptionCountService...");
		PendingExceptionCountService.instance().start();
	}
	
}
