package com.db.ess.synthesis.dao.tasks;

import org.apache.log4j.Logger;

public class TaskHelper {
	
	private static final Logger logger = Logger.getLogger(TaskHelper.class.getName());
	
	public static String convert(int value, String dbField, boolean isNumber) {
		if (value > 0) {
			return convert(value+"", dbField, true);
		} else {
			return null;
		}
	}
	
	 public static String convertWithComma(String value, String dbField, boolean isNumber) {
			
		    if (value == null || dbField == null) {
		    	logger.warn("params should not be null.");
		    	return null;
		    }
		    
		    value=value.toUpperCase();   
	        String queryString = "";
	        String values = "";
	        String convertString = "";
	        String singleQuote = "'";
	        
	        if (isNumber) {
	        	convertString = "convert(varchar," + dbField + ") like '";
	        	singleQuote = ""; // numbers do not need single quote for IN clause
	        } else {
	        	convertString = dbField + " like '";
	        }
			
	        if (value.contains("%")) {      
	             if (queryString.length() > 0) {
	                        queryString = queryString 
	                                    + " or " + convertString 
	                                    + value + "'";
	                    } else {
	                        queryString = queryString 
	                                    + convertString
	                                    + value + "'";
	                    }     
	         } else {
	             if (!value.equals("")) {
	                        if (values.length() > 0) {
				                values = singleQuote + value + singleQuote + "," + values;
	                        } else {
	                            values = singleQuote + value + singleQuote;
	                        }
						}
	         }
							
	         if ((values.length() > 0) && (queryString.length() > 0)) {
	                queryString = " and (" + dbField + " = " + values  + " or "
	                            + queryString  + ") ";    
	         } else if ((values.length() > 0) && (queryString.length() == 0)) {
	                queryString = " and " + dbField + " = " + values;  
	         } else if ((values.length() == 0) && (queryString.length() > 0)) {
	                queryString = " and (" + queryString + ") ";
	         } else {
	        	 	if (value.contains("%")) {
	        	 		queryString = " and " + convertString + value + "' ";
	        	 	} else {
	                queryString = " and " + dbField + " = " + singleQuote + value + singleQuote;
	        	 	}
	        }
			
	        return queryString ;
		}
	
    public static String convert(String value, String dbField, boolean isNumber) {
	
	    if (value == null || dbField == null) {
	    	logger.warn("params should not be null.");
	    	return null;
	    }
	    
	    value=value.toUpperCase();   
        String queryString = "";
        String values = "";
        String convertString = "";
        String singleQuote = "'";
        
        if (isNumber) {
        	convertString = "convert(varchar," + dbField + ") like '";
        	singleQuote = ""; // numbers do not need single quote for IN clause
        } else {
        	convertString = dbField + " like '";
        }
		
        if (value.contains(",")) {
			
            String[] valueArray  = value.split(",");
     
            for (int i = 0; i < valueArray.length; i++) {
            	
                if (valueArray[i].contains("%")) {
                
                	if (queryString.length() > 0) {
                        queryString = queryString 
                                    + " or " + convertString 
                                    + valueArray[i] + "'";
                    } else {
                        queryString = queryString 
                                    + convertString
                                    + valueArray[i] + "'";
                    }
                
                } else {
                    if (!valueArray[i].equals("")) {
                        if (values.length() > 0) {
			                values = singleQuote + valueArray[i] + singleQuote + "," + values;
                        } else {
                            values = singleQuote + valueArray[i] + singleQuote;
                        }
					}
				}
			}
			
            if ((values.length() > 0) && (queryString.length() > 0)) {
                queryString = " and (" + dbField + " in (" + values  + ") or "
                            + queryString  + ") ";
            
            } else if ((values.length() > 0) && (queryString.length() == 0)) {
                queryString = " and " + dbField + " in (" + values + ") ";
            
            } else if ((values.length() == 0) && (queryString.length() > 0)) {
                queryString = " and (" + queryString + ") ";
            }

        } else {
            
        	if (value.contains("%")) {
                queryString = " and " + convertString + value + "' ";
            
        	} else {
                queryString = " and " + dbField + " = " + singleQuote + value + singleQuote;
            }
        }
		
        return queryString ;
	}
    
    /**
     * Creates where clause for security search (see stored procedures) 
     */
    public static String createWhere(String value, String securityType) {
    
    	if (value == null || securityType == null) {
    		logger.warn("params should not be null.");
	    	return null;
    	}
    
    	String type = securityType.toUpperCase();
    	
    	if (type.equals("ANY")) {
    	    return convert(value, "upper(i.ticker)", false) + " or " +
    	           dropAnd(convert(value, "upper(ex.extIdCode)", false));
    		
    	} else if (type.equals("RIC") || type.equals("TICKER")) {
    		return convert(value, "upper(i.ticker)", false);
    		
    	} else {
    		return convert(securityType, "upper(exd.extIdTypeCode)", false) + 
    		       convert(value, "upper(ex.extIdCode)", false);
    		
    	}
    }
    
    public static String dropAnd(String value) {
    	return value.replaceFirst("and", "").trim();
    }
    
    /**
     * Creates where join statements for security search (see stored procedures) 
     */
    public static String createJoin(String value, String securityType) {
    	
    	if (value == null || securityType == null) {
    		logger.warn("params should not be null.");
	    	return null;
    	}
    
    	String type = securityType.toUpperCase();
    	
    	if (type.equals("ANY")) {
    		return " join ExtId ex on i.fiId = ex.extId ";
    		
    	} else if (type.equals("RIC") || type.equals("TICKER")) {
    		return "";
    		
    	} else {
    		return " join ExtId ex on i.fiId = ex.extId " +
    		       " join ExtIdTypeDomain exd on ex.extIdType = exd.extIdType ";
    	}
    }
    
    /*
     Resolves the security code in simple way
     */
    public static String getSecCode(String reqSec)
	{
		StringBuilder finalSecCode = new StringBuilder("");
		
		if(reqSec.contains(","))
		{
			String[] strArr = reqSec.split(",");
			finalSecCode.append(" in (");
			 for (int i = 0; i < strArr.length; i++) {
				 finalSecCode.append("'");
				 finalSecCode.append(strArr[i].toUpperCase().trim());
				 finalSecCode.append("',");
			 }
			finalSecCode.deleteCharAt(finalSecCode.length() - 1);
			finalSecCode.append(")");
		}
		else if(reqSec.contains("%") && !reqSec.contains(","))
		{
			finalSecCode.append(" like '");
			finalSecCode.append(reqSec.toUpperCase().trim());
			finalSecCode.append("'");
		}
		else
		{
			finalSecCode.append(" = '");
			finalSecCode.append(reqSec.toUpperCase().trim());
			finalSecCode.append("'");
		}
			
			
		return finalSecCode.toString();
	}
}
