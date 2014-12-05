package com.db.ess.synthesis.dao.tasks.exception;

import java.util.List;

import com.db.ess.synthesis.dvo.SynthesisException;
import com.db.ess.synthesis.validation.BookSeriesValidator;
import com.db.ess.synthesis.validation.CountryValidator;
import com.db.ess.synthesis.validation.CustomerIdValidator;
import com.db.ess.synthesis.validation.ExceptionIdValidator;
import com.db.ess.synthesis.validation.InstitutionIdValidator;
import com.db.ess.synthesis.validation.SwapNumValidator;
import com.db.ess.synthesis.validation.TickerValidator;
import com.db.ess.synthesis.validation.UserValidator;

public class ExceptionsValidator 
{

	private SynthesisException se;
	private int location ;
	private boolean hasError = false;
	private boolean hasTraderAccess = false;
	
	public ExceptionsValidator(SynthesisException se,int location,boolean hasTraderAccess)
	{
		this.se = se;
		this.location = location;
		this.hasTraderAccess = hasTraderAccess;
	}
	
	public void performValidation()
	{
		se.setcustIdHasError(new CustomerIdValidator(se.getcustId(), location).validate());
		se.setinstitutionIdHasError(new InstitutionIdValidator(se.getinstitutionId(), location).validate());	
		new TickerValidator(location,se).validate();
		new ExceptionIdValidator(se,location).validate();
		new SwapNumValidator(location,se).validate();
		new BookSeriesValidator(location,hasTraderAccess, se).validate();
		new UserValidator(location, se).validate();
		new CountryValidator(location, se).validate();
		
		hasError = se.getcustIdHasError() || se.getinstitutionIdHasError() || se.gettickerHasError()
					|| se.getexceptIdHasError() || se.getswapNumberHasError() || se.getbookSeriesHasError()
					|| se.getuserHasError() || se.getcountryCodeHasError();
	}
	
	public SynthesisException getExceptionObject()
	{
		return this.se;
	}
	
	public boolean exceptionHasError()
	{
		return hasError;
	}
}
