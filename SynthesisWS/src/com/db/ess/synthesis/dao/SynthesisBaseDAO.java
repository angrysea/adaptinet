package com.db.ess.synthesis.dao;

import com.db.ess.synthesis.dao.tasks.TaskFactory;
import com.db.ess.synthesis.dvo.ReturnResponse;
import com.db.ess.synthesis.util.ESSLocation;
import com.db.ess.synthesis.util.SynthesisConstants;

public class SynthesisBaseDAO {

	public <Req, Res> Res getResponse (Req request, Class<Res> responseClass, int location) 
	throws Exception {

		Res response = responseClass.newInstance();
		try {
			if (location == ESSLocation.GLOBAL) {
				TaskFactory.create(request, ESSLocation.LONDON).run(response);
				TaskFactory.create(request, ESSLocation.NEWYORK).run(response);
			} else {
				TaskFactory.create(request, location).run(response);
			}
		} catch (Exception ex) {
			throw ex;
		}
		return response;
	}
	
	public <Req, Res> Res getResponse (Req request, Class<Res> responseClass) 
	throws Exception {

		Res response = responseClass.newInstance();
		try {
            TaskFactory.create(request).run(response);
		} catch (Exception ex) {
			throw ex;
		}
		return response;
	}
	
	protected ReturnResponse getResultSetOverFlowResponse() {

		ReturnResponse returnResponse = new ReturnResponse();
		returnResponse.setmessage(SynthesisConstants.RESULT_SET_OVERFLOW_MSG);
		returnResponse
				.setreturnCode(SynthesisConstants.RESULT_SET_OVERFLOW_ERRORCODE);
		return returnResponse;

	}

	protected String getWildCardSearchInt(String SearchFieldName,String DBFieldName) {
		String SearchFieldNameQuery  = "", SearchFieldNameList   = "";
		if (SearchFieldName.contains(",")) {
			String[] SearchFieldNames  = SearchFieldName.split(",");
			for (int k = 0; k < SearchFieldNames .length; k++) {
				if (SearchFieldNames [k].contains("%")) {
					if(SearchFieldNameQuery .length() > 0)
					{
						SearchFieldNameQuery  = SearchFieldNameQuery 
							+ " or convert(varchar,"+DBFieldName+") like '"
							+ SearchFieldNames [k] + "'";
					}
					else
					{
						SearchFieldNameQuery  = SearchFieldNameQuery 
							+ " convert(varchar,"+DBFieldName+") like '"
							+ SearchFieldNames [k] + "'";
					}
				} else {
					if (!SearchFieldNames [k].equals(""))
					{
						if(SearchFieldNameList .length() > 0)
							SearchFieldNameList  = SearchFieldNames [k] + "," + SearchFieldNameList ;
						else
							SearchFieldNameList  = SearchFieldNames [k];
					}
				}
			}
			if ((SearchFieldNameList .length() > 0) && (SearchFieldNameQuery .length() > 0)) {
				SearchFieldNameQuery  = " and ("+DBFieldName+" in (" + SearchFieldNameList  + ") or "
						+ SearchFieldNameQuery  + ")";
			} else if ((SearchFieldNameList .length() > 0)
					&& (SearchFieldNameQuery .length() == 0)) {
				SearchFieldNameQuery  = " and "+DBFieldName+" in (" + SearchFieldNameList  + ")";
			} else if ((SearchFieldNameList .length() == 0)
					&& (SearchFieldNameQuery .length() > 0)) {
				SearchFieldNameQuery  = " and ("+SearchFieldNameQuery  + ")";
			}

		} else {
			if (SearchFieldName.contains("%")) {
				SearchFieldNameQuery  = " and convert(varchar,"+DBFieldName+") like '"
						+ SearchFieldName + "'";
			} else {
				SearchFieldNameQuery  = " and "+DBFieldName+" =" + SearchFieldName;
			}
		}
		return SearchFieldNameQuery ;
	}
		
	protected String getWildCardSearchString(String SearchFieldNameAct,String DBFieldName) {
		String SearchFieldName = SearchFieldNameAct.toUpperCase();
		String SearchFieldNameQuery  = "", SearchFieldNameList  = "";
		if (SearchFieldName.contains(",")) {
			String[] SearchFieldNames  = SearchFieldName.split(",");
			for (int k = 0; k < SearchFieldNames .length; k++) {
				if (SearchFieldNames [k].contains("%")) {
					if(SearchFieldNameQuery .length() > 0)
					{
						SearchFieldNameQuery  = SearchFieldNameQuery 
							+ " or "+DBFieldName+" like '"
							+ SearchFieldNames [k] + "'";
					}
					else
					{
						SearchFieldNameQuery  = SearchFieldNameQuery 
							+ DBFieldName +" like '"
							+ SearchFieldNames [k] + "'";
					}
				} else {
					if (!SearchFieldNames [k].equals(""))
					{
						if(SearchFieldNameList .length() > 0)
							SearchFieldNameList  = "'"+SearchFieldNames [k]+"'" + "," + SearchFieldNameList ;
						else
							SearchFieldNameList  = "'"+SearchFieldNames [k]+"'";
					}
				}
			}
			if ((SearchFieldNameList .length() > 0) && (SearchFieldNameQuery .length() > 0)) {
				SearchFieldNameQuery  = " and ("+DBFieldName+" in (" + SearchFieldNameList  + ") or "
						+ SearchFieldNameQuery  + ")";
			} else if ((SearchFieldNameList .length() > 0)
					&& (SearchFieldNameQuery .length() == 0)) {
				SearchFieldNameQuery  = " and "+DBFieldName+" in (" + SearchFieldNameList  + ")";
			} else if ((SearchFieldNameList .length() == 0)
					&& (SearchFieldNameQuery .length() > 0)) {
				SearchFieldNameQuery  = " and ("+SearchFieldNameQuery  + ")";
			}

		} else {
			if (SearchFieldName.contains("%")) {
				SearchFieldNameQuery  = " and "+DBFieldName+" like '"
						+ SearchFieldName + "'";
			} else {
				SearchFieldNameQuery  = " and "+DBFieldName+" ='" + SearchFieldName+"'";
			}
		}
		return SearchFieldNameQuery ;
	}
	
}
