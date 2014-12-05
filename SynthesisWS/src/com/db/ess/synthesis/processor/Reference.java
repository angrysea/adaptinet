package com.db.ess.synthesis.processor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.adaptinet.sdk.processoragent.Processor;
import org.apache.log4j.Logger;

import com.db.ess.synthesis.dao.ReferenceDAO;
import com.db.ess.synthesis.dvo.CacheEntry;
import com.db.ess.synthesis.dvo.GetCountriesRequest;
import com.db.ess.synthesis.dvo.GetCountriesResponse;
import com.db.ess.synthesis.dvo.GetCurrenciesRequest;
import com.db.ess.synthesis.dvo.GetCurrenciesResponse;
import com.db.ess.synthesis.dvo.GetNamedCacheRequest;
import com.db.ess.synthesis.dvo.GetNamedCacheResponse;
import com.db.ess.synthesis.dvo.NamedCache;
import com.db.ess.synthesis.dvo.ReloadCacheRequest;
import com.db.ess.synthesis.dvo.ReturnResponse;
import com.db.ess.synthesis.util.cache.CachedCurrency;
import com.db.ess.synthesis.util.cache.RefCache;

public class Reference extends Processor {

	private static final Logger logger = Logger.getLogger(Swap.class
			.getName());

	public GetCurrenciesResponse GetCurrencies(GetCurrenciesRequest request) {

		GetCurrenciesResponse response = null;
		ReturnResponse returnResponse = new ReturnResponse();

		try {
			response = new ReferenceDAO().getCurrencies(request);
			returnResponse.setreturnCode(200);
			response.setreturnResponse(returnResponse);
		} catch (Exception e) {
			response = new GetCurrenciesResponse();
			returnResponse.setreturnCode(404);
			returnResponse.setmessage(e.getMessage());
			response.setreturnResponse(returnResponse);
		}
		return response;
	}

	public GetCountriesResponse GetCountries(GetCountriesRequest request) {

		GetCountriesResponse response = null;
		ReturnResponse returnResponse = new ReturnResponse();

		try {
			response = new ReferenceDAO().getCountries(request);
			returnResponse.setreturnCode(200);
			response.setreturnResponse(returnResponse);
		} catch (Exception e) {
			response = new GetCountriesResponse();
			returnResponse.setreturnCode(404);
			returnResponse.setmessage(e.getMessage());
			response.setreturnResponse(returnResponse);
		}
		return response;
	}

	/*
	public GetDesksResponse GetDesks(GetDesksRequest request) {

		GetDesksResponse response = null;
		ReturnResponse returnResponse = new ReturnResponse();

		try {
			response = new ReferenceDAO().getDesks(request);
			returnResponse.setreturnCode(200);
			response.setreturnResponse(returnResponse);
		} catch (Exception e) {
			response = new GetDesksResponse();
			returnResponse.setreturnCode(404);
			returnResponse.setmessage(e.getMessage());
			response.setreturnResponse(returnResponse);
		}
		return response;
	}
	 */
	public GetNamedCacheResponse GetNamedCache(GetNamedCacheRequest request) {
		GetNamedCacheResponse response = new GetNamedCacheResponse();
	    int location = request.getlocation();
		
	    try {
	    	if (request.gettype() == null || request.gettype().isEmpty())
	    	{
	    		response.setnamedCache(loadMapGlobal(1, location, "Strategy"));
	    		response.setnamedCache(loadMapGlobal(2, location, "Swap Type"));
	    		response.setnamedCache(loadMapGlobal(3, location, "Status"));
	    		response.setnamedCache(loadMapGlobal(4, location, "Ticker Type"));
	    		//response.setnamedCache(loadCurrency(RefCache.getCurrencymap(location)
	    		//		.entrySet().iterator(), "Currency Code"));
	    		response.setnamedCache(loadMapGlobal(5, location, "Country Code"));
	    		response.setnamedCache(loadMapGlobal(6, location, "Legal Entity"));
	    		response.setnamedCache(loadMapGlobal(7, location, "Institution"));
	    		response.setnamedCache(loadMapGlobal(8, location, "ExId"));
	    		response.setnamedCache(loadMapGlobal(9, location, "Desk"));
	    		response.setnamedCache(loadMapGlobal(10, location, "Account"));
	    		response.setnamedCache(loadMapGlobal(11, location, "Business Unit"));
	    		response.setnamedCache(loadMapGlobal(12, location, "Book Series"));
	    		response.setnamedCache(loadMapGlobal(13, location, "SynthesisUsers"));
	    		response.setnamedCache(loadMapGlobal(14, location, "BasketEventDomain"));
	    		response.setnamedCache(loadMapGlobal(15, location, "Reason"));
	    	}
	    	else if( request.gettype().equalsIgnoreCase("Currency"))
	    	{
	    		response.setnamedCache(loadMapCurrency(location, "Currency Code"));
	    	}
	    	else {
	    		response.setnamedCache(loadMapSecurity(location, request.gettype())); 
	    	}
	    } catch (Exception ex) {
	    	logger.error(">>> failed to load named cached..", ex);
	    }
		return response;
	}
	
	private Map<Integer, String> getRefMap(int refIndicator, int loc)
	{
		Map<Integer, String> returnMap = new HashMap<Integer, String>();
		switch(refIndicator)
		{
			case 1	: returnMap = RefCache.getStrategydomainmap(loc);
						break;
			case 2	: returnMap = RefCache.getSwaptypedomainmap(loc);
						break;
			case 3	: returnMap = RefCache.getSwapstatusdomainmap(loc);
						break;
			case 4	: returnMap = RefCache.getExttypemap(loc);
						break;
			case 5	: returnMap = RefCache.getCountrymap(loc);
						break;
			case 6	: returnMap = RefCache.getLegalentitymap(loc);
						break;
			case 7	: returnMap = RefCache.getInstitutionMap(loc);
						break;
			case 8	: returnMap = RefCache.getExttypemap(loc);
						break;
			case 9	: returnMap = RefCache.getDeskmap(loc);
						break;	
			case 10	: returnMap = RefCache.getAccountmap(loc);
					 	break;
			case 11	: returnMap = RefCache.getBusinessUintmap(loc);
						break;
			case 12	: returnMap = RefCache.getBookSeries(loc);
						break;		
			case 13	: returnMap = RefCache.getSynthesisUsers(loc);
						break;
			case 14	: returnMap = RefCache.getBasketEventDomainmap(loc);
						break;
			case 15	: returnMap = RefCache.getReason(loc);
						break;
			default : returnMap = null;
						break;
		}
		return returnMap;
	}
	
	private NamedCache loadMapGlobal(int refIndex, int location, String name)
	{
		Iterator<Entry<Integer, String>> refIterate;
		NamedCache namedCache = new NamedCache();
        namedCache.setname(name);
        if(location == 1 || location == 2)
        {
        	refIterate = getRefMap(refIndex, location).entrySet().iterator();
       
	        while (refIterate.hasNext()) {
	              Entry<Integer, String> e = refIterate.next();
	              CacheEntry entry = new CacheEntry();
	              entry.setkey(e.getKey().intValue());
	              entry.setvalue(e.getValue());
	              entry.setdescription(e.getValue());
	              entry.setlocation(location);
	              namedCache.setentry(entry);
	        }
        }
        else
        {	
        	refIterate = getRefMap(refIndex, 1).entrySet().iterator();
        	while (refIterate.hasNext()) {
        		Entry<Integer, String> e = refIterate.next();
        		CacheEntry entry = new CacheEntry();
        		entry.setkey(e.getKey().intValue());
	            entry.setvalue(e.getValue());
	            entry.setdescription(e.getValue());
	            entry.setlocation(1);
	            namedCache.setentry(entry);
        	}
        	
        	refIterate = getRefMap(refIndex, 2).entrySet().iterator();
        	while (refIterate.hasNext()) {
        		Entry<Integer, String> e = refIterate.next();
        		CacheEntry entry = new CacheEntry();
        		entry.setkey(e.getKey().intValue());
	            entry.setvalue(e.getValue());
	            entry.setdescription(e.getValue());
	            entry.setlocation(2);
	            namedCache.setentry(entry);
        	}
        }
        return namedCache;
	}

	public ReturnResponse ReloadCache(ReloadCacheRequest request) {
		ReturnResponse returnResponse = new ReturnResponse();

		try {
			RefCache.initCache();
			returnResponse.setreturnCode(200);
		} catch (Exception e) {
			returnResponse.setreturnCode(404);
			returnResponse.setmessage(e.getMessage());
		}
		return returnResponse;
	}

	/*private NamedCache loadMap(Iterator<Entry<Integer, String>> it, String name) {
        NamedCache namedCache = new NamedCache();
        namedCache.setname(name);
        while (it.hasNext()) {
              Entry<Integer, String> e = it.next();
              CacheEntry entry = new CacheEntry();
              entry.setkey(e.getKey().intValue());
              entry.setvalue(e.getValue());
              entry.setdescription(e.getValue());
              namedCache.setentry(entry);
        }
        return namedCache;
  } 

	private NamedCache loadCurrency(
			Iterator<Entry<Integer, CachedCurrency>> it, String name) {
		NamedCache namedCache = new NamedCache();
		namedCache.setname(name);
		while (it.hasNext()) {
			Entry<Integer, CachedCurrency> e = it.next();
			CacheEntry entry = new CacheEntry();
			entry.setkey(e.getKey().intValue());
			entry.setvalue(e.getValue().getSwiftCode());
			namedCache.setentry(entry);
		}
		return namedCache;
	}
	
	private NamedCache loadSecurities(List<CacheEntry> list, String name) {
		NamedCache namedCache = new NamedCache();
		namedCache.setname(name);
		for(CacheEntry e : list){
			namedCache.setentry(e);
		}
		return namedCache;
	}*/
	
	private NamedCache loadCurrency(Iterator<Entry<Integer, CachedCurrency>> it, NamedCache namedCache, int location) {
		
		while (it.hasNext()) {
			Entry<Integer, CachedCurrency> e = it.next();
			CacheEntry entry = new CacheEntry();
			entry.setkey(e.getKey().intValue());
			entry.setvalue(e.getValue().getSwiftCode());
			entry.setdescription(e.getValue().getSwiftCode());
			entry.setlocation(location);
			namedCache.setentry(entry);
		}
		return namedCache;
	}
	
	private NamedCache loadSecurities(List<CacheEntry> list, NamedCache namedCache, int location) {
		for(CacheEntry e : list){
			e.setlocation(location);
			namedCache.setentry(e);
		}
		return namedCache;
	}
	
	private NamedCache loadMapCurrency(int location, String name)
	{
		Iterator<Entry<Integer, CachedCurrency>> refIterate;
		NamedCache namedCacheFinal = new NamedCache();
		namedCacheFinal.setname(name);
        if(location == 1 || location == 2)
        {
        	refIterate = RefCache.getCurrencymap(location).entrySet().iterator();
        	namedCacheFinal = loadCurrency(refIterate, namedCacheFinal, location);
        }
        else
        {	
        	refIterate = RefCache.getCurrencymap(1).entrySet().iterator();
        	namedCacheFinal = loadCurrency(refIterate, namedCacheFinal, 1);
        	
        	refIterate = RefCache.getCurrencymap(2).entrySet().iterator();
        	namedCacheFinal = loadCurrency(refIterate, namedCacheFinal, 2);
        }
        return namedCacheFinal;
	}
	
	private NamedCache loadMapSecurity(int location, String type)
	{
		List<CacheEntry> refList;
		NamedCache namedCacheFinal = new NamedCache();
		namedCacheFinal.setname(type);
        if(location == 1 || location == 2)
        {
        	refList = RefCache.getSecurityList(location, type);
        	namedCacheFinal = loadSecurities(refList, namedCacheFinal, location);
        }
        else
        {	
        	refList = RefCache.getSecurityList(1, type);
        	namedCacheFinal = loadSecurities(refList, namedCacheFinal, 1);
        	
        	refList = RefCache.getSecurityList(2, type);
        	namedCacheFinal = loadSecurities(refList, namedCacheFinal, 2);
        }
        return namedCacheFinal;
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
