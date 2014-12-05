package com.db.ess.synthesis.dao;

import org.apache.log4j.Logger;

public class DividendAltDivCcyDAO extends SynthesisBaseDAO {
	
	private static final Logger logger = Logger.getLogger(DividendAltDivCcyDAO.class.getName());
	
	/* SQL statements for AltDivCcy - DivOptionalPayCcy table */
	
	
	public static final String INSERT_ALT_DIV_CCYS = 
		"INSERT INTO DivOptionalPayCcy (dividendId,optionCcyId,optionAmount,optionGross) VALUES (?,?,?,?)";
	public static final String DELETE_ALT_DIV_CCYS = 
		"DELETE DivOptionalPayCcy WHERE dividendId = ?";
	
	/* SQL statements for Impacted Swaps - DivPayCcyException table */
	public static final String GET_IMPACTED_SWAPS = "{ call dbo.SYN_GetImpactedSwaps (@DivId=?) }";
	public static final String INSERT_CCY_EXCEPTION =
		"INSERT INTO DivPayCcyException (swapId,dividendId,payCcyId) values (?, ?, ?)";
	public static final String DELETE_CCY_EXCEPTION = 
		"DELETE DivPayCcyException WHERE swapId=? AND dividendId=?";
	
	
}

