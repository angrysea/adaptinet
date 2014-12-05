package com.db.ess.synthesis.processor;

import org.adaptinet.sdk.processoragent.Processor;
import org.apache.log4j.Logger;

import com.db.ess.synthesis.dvo.CreateBasketRequest;
import com.db.ess.synthesis.dvo.GetBasketConstituentResponse;
//import com.db.ess.synthesis.mercury.utils.IndexBasketCalculator;

public class CustomBasketMercuryValidation extends Processor {
	private static final Logger logger = Logger
			.getLogger(CustomBasketMercuryValidation.class.getName());

	public GetBasketConstituentResponse ValidateCustomBasketMercury(
			CreateBasketRequest request) {
		long time = System.currentTimeMillis();
		GetBasketConstituentResponse response = null;
		/*
		IndexBasketCalculator calculator = new IndexBasketCalculator();
		response = calculator.processRequest(request);
		*/
		logger.info("Ellapsed time: " + (System.currentTimeMillis() - time));
		return response;

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub
		
	}

}
