package com.db.ess.synthesis.dao.tasks.adjustment;

import com.db.ess.synthesis.dvo.Adjustment;
import com.db.ess.synthesis.validation.AdjustmentSwapValidator;
import com.db.ess.synthesis.validation.AdjustmentTickerValidator;
import com.db.ess.synthesis.validation.EventIdValidator;

public class AdjustmentsValidator 
{

	//private static final Logger logger = Logger.getLogger(AdjustmentsValidator.class.getName());
	private Adjustment ad;
	private int location ;
	private boolean hasError = false;
	private boolean hasTraderAccess = false;
	
	public AdjustmentsValidator(Adjustment ad,int location,boolean hasTraderAccess)
	{
		this.ad = ad;
		this.location = location;
		this.hasTraderAccess = hasTraderAccess;
	}
	
	public void performValidation()
	{
		new EventIdValidator(ad, location).validate();
		new AdjustmentTickerValidator(location, ad).validate();
		new AdjustmentSwapValidator(location, ad).validate();
		
		hasError = ad.getEventIdHasError() || ad.getSwapNumberHasError() || ad.getTickerHasError();
	}
		
	
	public Adjustment getAdjustmentObject()
	{
		return this.ad;
	}
	
	public boolean adjustmentHasError()
	{
		return hasError;
	}
}
