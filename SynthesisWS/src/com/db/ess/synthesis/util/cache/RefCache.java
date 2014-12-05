package com.db.ess.synthesis.util.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dao.CacheDAO;
import com.db.ess.synthesis.dvo.CacheEntry;
import com.db.ess.synthesis.util.ESSLocation;
import com.db.ess.synthesis.vo.Exchange;

public class RefCache {

	private static Logger logger = Logger.getLogger(CacheDAO.class.getName());

	private static final Map<Integer, String> LOGICALCALMapLondon = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, String> LOGICALCALMapNewYork = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, String> SWAPTYPEDOMAINMapLondon = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, String> SWAPTYPEDOMAINMapNewYork = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, String> STRATEGYDOMAINMapLondon = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, String> STRATEGYDOMAINMapNewYork = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, String> SWAPSTATUSDOMAINMapLondon = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, String> SWAPSTATUSDOMAINMapNewYork = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, String> COLLATERALTYPEDOMAINMapLondon = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, String> COLLATERALTYPEDOMAINMapNewYork = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, String> COMMISSIONTYPEDOMAINMapLondon = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, String> COMMISSIONTYPEDOMAINMapNewYork = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, String> COMMISSIONPAYDOMAINMapLondon = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, String> COMMISSIONPAYDOMAINMapNewYork = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, String> RETURNTYPEDOMAINMapLondon = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, String> RETURNTYPEDOMAINMapNewYork = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, String> FITYPEDOMAINMapLondon = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, String> FITYPEDOMAINMapNewYork = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, String> COUNTRYMapLondon = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, String> COUNTRYMapNewYork = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, CachedCurrency> CURRENCYMapLondon = new ConcurrentHashMap<Integer, CachedCurrency>();
	private static final Map<Integer, CachedCurrency> CURRENCYMapNewYork = new ConcurrentHashMap<Integer, CachedCurrency>();
	private static final Map<String, CachedCurrency> CCYCodeMapLondon = new ConcurrentHashMap<String, CachedCurrency>();
	private static final Map<String, CachedCurrency> CCYCodeMapNewYork = new ConcurrentHashMap<String, CachedCurrency>();
	private static final Map<Integer, String> PAYDIVDOMAINMapLondon = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, String> PAYDIVDOMAINMapNewYork = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, CachedBasis> BASISDOMAINMapLondon = new ConcurrentHashMap<Integer, CachedBasis>();
	private static final Map<Integer, CachedBasis> BASISDOMAINMapNewYork = new ConcurrentHashMap<Integer, CachedBasis>();
	private static final Map<Integer, String> PERIODDOMAINMapLondon = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, String> PERIODDOMAINMapNewYork = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, String> FREQTYPEDOMAINMapLondon = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, String> FREQTYPEDOMAINMapNewYork = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, String> BUMPCONVENTIONDOMAINMapLondon = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, String> BUMPCONVENTIONDOMAINMapNewYork = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, String> LEGALENTITYMapLondon = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, String> LEGALENTITYMapNewYork = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, String> EXTTYPEMapLondon = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, String> EXTTYPEMapNewYork = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, String> INSTITUTIONMapLondon = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, String> INSTITUTIONMapNewYork = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, String> DESKMapLondon = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, String> DESKMapNewYork = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, CachedBook> BOOKMapLondon = new ConcurrentHashMap<Integer, CachedBook>();
	private static final Map<Integer, CachedBook> BOOKMapNewYork = new ConcurrentHashMap<Integer, CachedBook>();
	private static final Map<Integer, String> BASKETEVENTDOMAINMapLondon = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, String> BASKETEVENTDOMAINMapNewYork = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, String> REGIONMapLondon = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, String> REGIONMapNewYork = new ConcurrentHashMap<Integer, String>();
	private static final Map<String, Map<String, Double>> FXRATEMapLondon = new ConcurrentHashMap<String, Map<String, Double>>();
	private static final Map<String, Map<String, Double>> FXRATEMapNewYork = new ConcurrentHashMap<String, Map<String, Double>>();
	private static final Map<Integer, String> FISTATUSDOMAINMAPLondon = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, String> FISTATUSDOMAINMAPNewYork = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, String> PRICEFREQDOMAINMAPLondon = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, String> PRICEFREQDOMAINMAPNewYork = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, String> PRICETYPEDOMAINMAPLondon = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, String> PRICETYPEDOMAINMAPNewYork = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, String> USERPROFILEMAPLondon = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, String> USERPROFILEMAPNewYork = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, Exchange> EXCHANGEMAPLondon = new ConcurrentHashMap<Integer, Exchange>();
	private static final Map<Integer, Exchange> EXCHANGEMAPNewYork = new ConcurrentHashMap<Integer, Exchange>();
	private static final Map<Integer, String> ACCOUNTMAPLondon = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, String> ACCOUNTMAPNewYork = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, String> BUSINESSUNITMAPLondon = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, String> BUSINESSUNITMAPNewYork = new ConcurrentHashMap<Integer, String>();
	private static final Map<String, List<CacheEntry>> SECURITYMAPLondon = new ConcurrentHashMap<String, List<CacheEntry>>();
	private static final Map<String, List<CacheEntry>> SECURITYMAPNewYork = new ConcurrentHashMap<String, List<CacheEntry>>();
	private static final Map<Integer, String> BookSeriesMapLondon = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, String> BookSeriesMapNewYork = new ConcurrentHashMap<Integer, String>();
	// custom basket: populate user list
	private static final Map<Integer, String> SynUserListLondon = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, String> SynUserListNewYork = new ConcurrentHashMap<Integer, String>();
	// reason map for create exception
	private static final Map<Integer, String> REASONMAPLondon = new ConcurrentHashMap<Integer, String>();
	private static final Map<Integer, String> REASONMAPNewYork = new ConcurrentHashMap<Integer, String>();


	public static class SecurityType {
		public static final String TICKER = "Ticker";
		public static final String RIC = "RIC";
		public static final String CUSIP = "CUSIP";
		public static final String SEDOL = "SEDOL";
		public static final String ADPCODE = "ADPCode";
		public static final String WANG = "WANGCode";
		public static final String ISIN = "ISIN";
		public static final String VALOREN = "Valoren";
		public static final String ESPEAR = "Espear";
		public static final String ADPID = "ADPID";
		private static final List<String> LIST = new ArrayList<String>();
		static {
			LIST.add(TICKER); // This is indeed "RIC"
			// LIST.add(RIC);
			LIST.add(CUSIP);
			LIST.add(SEDOL);
			// LIST.add(ADPCODE);
			// LIST.add(WANG);
			LIST.add(ISIN);
			LIST.add(VALOREN);
			LIST.add(ESPEAR);
			// LIST.add(ADPID);
		}

		public static List<String> types() {
			return Collections.unmodifiableList(LIST);
		}
	}

	private static final String GET_LOGICALCAL = "select lc.calId, lc.calName from logicalCal lc";
	private static final String GET_SWAPTYPEDOMAIN = "select std.type, std.typeCode from SwapTypeDomain std";
	private static final String GET_STRATEGYDOMAIN = "select sd.strategy, sd.code from StrategyDomain sd";
	private static final String GET_SWAPSTATUSDOMAIN = "select ssd.type, ssd.description from SwapStatusDomain ssd";
	private static final String GET_COLLATERALTYPEDOMAIN = "select ctd.type, ctd.typeCode from CollateralTypeDomain ctd";
	private static final String GET_COMMISSIONTYPEDOMAIN = "select ctd.commissionType, ctd.commissionTypeCode from CommissionTypeDomain ctd";
	private static final String GET_COMMISSIONPAYDOMAIN = "select cpd.commissionPay, cpd.commissionPayCode from CommissionPayDomain cpd";
	private static final String GET_RETURNTYPEDOMAIN = "select rtd.type, rtd.typeCode from ReturnTypeDomain rtd";
	private static final String GET_FITYPEDOMAIN = "select ftd.fiType, ftd.fiTypeCode from FiTypeDomain ftd";
	private static final String GET_COUNTRY = "select c.countryId, c.description from Country c";
	private static final String GET_CURRENCY = "select c.ccyId, c.swiftCode, c.decPlaces, c.basis, c.swapBasis, "
			+ "c.spotDays, c.rounding, c.calId, c.rtFeedDivisor, c.display from Currency c";
	private static final String GET_PAYDIVDOMAIN = "select pdd.payDiv, pdd.payDivCode from PayDivDomain pdd";
	private static final String GET_BASISDOMAIN = "select bd.unitType, bd.unitTypeCode, bd.days, bd.month30days, "
			+ "bd.display from BasisDomain bd where bd.display = 1";
	private static final String GET_PERIODDOMAIN = "select pd.period, pd.periodCode from PeriodDomain pd";
	private static final String GET_FREQTYPEDOMAIN = "select ftd.freqType, ftd.freqTypeCode from FreqTypeDomain ftd where ftd.freqType >= 3";
	private static final String GET_BUMPCONVENTIONDOMAIN = "select bcd.conv, bcd.code from BumpConventionDomain bcd";
	private static final String GET_LEGALENTITY = "select legalEntityId, name from LegalEntity";
	private static final String GET_EXTTYPE = "select extIdType, extIdTypeCode from ExtIdTypeDomain";
	private static final String GET_INSTITUTION = "select institutionId, name from Institution";
	private static final String GET_DESK = "select deskId, name from Desk";
	private static final String GET_BOOK = "select b.bookId, b.name, b.adpAcctNum, a.name, d.name, bu.name "
			+ "from Book b join Account a on b.acctId = a.acctId join Desk d on a.deskId = d.deskId "
			+ "join BusinessUnit bu on d.businessUnitId = bu.businessUnitId";
	private static final String GET_BASKETEVENTDOMAIN = "select bed.event, bed.eventCode from BasketEventDomain bed";
	private static final String GET_REGION = "select r.id, r.description from Region r";
	private static final String GET_FXRATE = "select p.majorCcyId,p.minorCcyId,pp.todayRate,"
			+ "pp.creationDate from CurrencyPairPoint pp join CurrencyPair p on pp.ccyPairId = p.ccyPairId "
			+ "join Currency c on p.majorCcyId = c.ccyId join Currency c2 on p.minorCcyId = c2.ccyId "
			+ "where pp.creationDate = (select max(pp2.creationDate) from CurrencyPairPoint pp2) "
			+ "order by p.majorCcyId, p.minorCcyId";

	private static final String CHECK_FXRATECURRENT = "select max(p.creationDate) from CurrencyPairPoint p";

	private static final String GET_FISTATUS = "select fiStatus,fiStatusCode from FiStatusDomain";
	private static final String GET_PRICEFREQ = "select priceFreq,priceFreqCode from PriceFreqDomain";
	private static final String GET_PRICETYPE = "select priceType,priceTypeCode from PriceTypeDomain";
	private static final String GET_USERPROFILE = "select uid,uname from ETS_Entitlement..user_profile";
	private static final String GET_EXCHANGEMAP = "select exchId,exchCode,description,countryCode from Exchange";
	private static final String GET_ACCOUNTMAP = "select acctId,name,deskId,fullName,mnemonic,status,enterUserId, enterTime from Account";
	private static final String GET_BUSINESSUNITMAP = "select businessUnitId,name,legalEntityId,fullName,mnemonic,status,enterUserId, enterTime from BusinessUnit";
	private static final String GET_TICKER = "{ call dbo.SYN_GetSecurities (@Name = ?) }";
	private static final String GET_BookSeries = "SELECT bookSeriesId, description FROM BookSeriesDomain";
	private static final String GET_SynUsers = "select distinct u.userId, lower(rtrim(u.emailAddress)) from ETS_Entitlement..EM_User u where u.emailAddress like '%@db.com%'";
	private static final String GET_REASON = "SELECT reasonId, case when exceptionType=1 then (description + '_Spr') else (description + '_Div') end FROM ExceptionReasonDomain where activeFlag=1";

	public static void initCache() {
		loadLogicalCal();
		loadSwapTypeDomain();
		loadStrategyDomain();
		loadSwapStatusDomain();
		loadCollateralTypeDomain();
		loadCommissionTypeDomain();
		loadCommissionPayDomain();
		loadReturnTypeDomain();
		loadFITypeDomain();
		loadCountry();
		loadCurrency();
		loadPayDivDomain();
		loadBasisDomain();
		loadPeriodDomain();
		loadFreqTypeDomain();
		loadBumpConventionDomain();
		loadLegalEntity();
		loadExtType();
		loadInstitution();
		loadDesk();
		loadBook();
		loadBasketEventDomain();
		loadRegion();
		loadAccount();
		loadBusinessUnit();

		// Added for Instruments
		loadFiSatus();
		loadPriceFreq();
		loadPriceType();
		loadUserProfile();
		loadBookSeries();
		loadExchange();
		loadSecurities();

		// custom basket
		loadSynUsers();
		// reason maps
		loadReason();
		// TODO: refactor the whole thing as soon as possible....
		new CacheDAO().udpateCacheInfo();
	}

	private static <K, V> V getData(Map<K, V> map1, Map<K, V> map2,
			Loadable loadable, K key, int location) {
		
		if (map1.size() == 0 || map2.size() == 0) {
			loadable.load();
		}
		
		return location == 1 ? map1.get(key) : map2.get(key);
	}

	public static String getLogicalCal(int key, int location) {
		return getData(LOGICALCALMapLondon, LOGICALCALMapNewYork,
				new Loadable() {
					public void load() {
						loadLogicalCal();
					}
				}, key, location);
	}

	public static String getSwapTypeDomain(int key, int location) {
		return getData(SWAPTYPEDOMAINMapLondon, SWAPTYPEDOMAINMapNewYork,
				new Loadable() {
					public void load() {
						loadSwapTypeDomain();
					}
				}, key, location);
	}

	public static String getStrategyDomain(int key, int location) {
		return getData(STRATEGYDOMAINMapLondon, STRATEGYDOMAINMapNewYork,
				new Loadable() {
					public void load() {
						loadStrategyDomain();
					}
				}, key, location);
	}

	public static String getSwapStatusDomain(int key, int location) {
		return getData(SWAPSTATUSDOMAINMapLondon, SWAPSTATUSDOMAINMapNewYork,
				new Loadable() {
					public void load() {
						loadSwapStatusDomain();
					}
				}, key, location);
	}

	public static String getCollateralTypeDomain(int key, int location) {
		return getData(COLLATERALTYPEDOMAINMapLondon,
				COLLATERALTYPEDOMAINMapNewYork, new Loadable() {
					public void load() {
						loadCollateralTypeDomain();
					}
				}, key, location);
	}

	public static String getCommissionTypeDomain(int key, int location) {
		return getData(COMMISSIONTYPEDOMAINMapLondon,
				COMMISSIONTYPEDOMAINMapNewYork, new Loadable() {
					public void load() {
						loadCommissionTypeDomain();
					}
				}, key, location);
	}

	public static String getCommissionPayDomain(int key, int location) {
		return getData(COMMISSIONPAYDOMAINMapLondon,
				COMMISSIONPAYDOMAINMapNewYork, new Loadable() {
					public void load() {
						loadCommissionPayDomain();
					}
				}, key, location);
	}

	public static String getReturnTypeDomain(int key, int location) {
		return getData(RETURNTYPEDOMAINMapLondon, RETURNTYPEDOMAINMapNewYork,
				new Loadable() {
					public void load() {
						loadReturnTypeDomain();
					}
				}, key, location);
	}

	public static String getFITypeDomain(int key, int location) {
		return getData(FITYPEDOMAINMapLondon, FITYPEDOMAINMapNewYork,
				new Loadable() {
					public void load() {
						loadFITypeDomain();
					}
				}, key, location);
	}

	public static String getCountry(int key, int location) {
		return getData(COUNTRYMapLondon, COUNTRYMapNewYork, new Loadable() {
			public void load() {
				loadCountry();
			}
		}, key, location);
	}

	public static CachedCurrency getCurrency(int key, int location) {
		return getData(CURRENCYMapLondon, CURRENCYMapNewYork, new Loadable() {
			public void load() {
				loadCurrency();
			}
		}, key, location);
	}

	public static CachedCurrency getCurrency(String key, int location) {
		return getData(CCYCodeMapLondon,CCYCodeMapNewYork, new Loadable() {
			public void load() {
				loadCurrency();
			}
		}, key, location);
	}

	public static String getPayDivDomain(int key, int location) {
		return getData(PAYDIVDOMAINMapLondon, PAYDIVDOMAINMapNewYork,
				new Loadable() {
					public void load() {
						loadPayDivDomain();
					}
				}, key, location);
	}

	public static CachedBasis getBasisDomain(int key, int location) {
		return getData(BASISDOMAINMapLondon, BASISDOMAINMapNewYork,
				new Loadable() {
					public void load() {
						loadBasisDomain();
					}
				}, key, location);
	}

	public static String getPeriodDomain(int key, int location) {
		return getData(PERIODDOMAINMapLondon, PERIODDOMAINMapNewYork,
				new Loadable() {
					public void load() {
						loadPeriodDomain();
					}
				}, key, location);
	}

	public static String getFreqTypeDomain(int key, int location) {
		return getData(FREQTYPEDOMAINMapLondon, FREQTYPEDOMAINMapNewYork,
				new Loadable() {
					public void load() {
						loadFreqTypeDomain();
					}
				}, key, location);
	}

	public static String getBumpConventionDomain(int key, int location) {
		return getData(BUMPCONVENTIONDOMAINMapLondon,
				BUMPCONVENTIONDOMAINMapNewYork, new Loadable() {
					public void load() {
						loadBumpConventionDomain();
					}
				}, key, location);
	}

	public static String getLegalEntity(int key, int location) {
		return getData(LEGALENTITYMapLondon, LEGALENTITYMapNewYork,
				new Loadable() {
					public void load() {
						loadLegalEntity();
					}
				}, key, location);
	}

	public static String getExtType(int key, int location) {
		return getData(EXTTYPEMapLondon, EXTTYPEMapNewYork, new Loadable() {
			public void load() {
				loadExtType();
			}
		}, key, location);
	}

	public static String getInstitution(int key, int location) {
		return getData(INSTITUTIONMapLondon, INSTITUTIONMapNewYork,
				new Loadable() {
					public void load() {
						loadInstitution();
					}
				}, key, location);
	}

	public static String getDesk(int key, int location) {
		return getData(DESKMapLondon, DESKMapNewYork, new Loadable() {
			public void load() {
				loadDesk();
			}
		}, key, location);
	}

	public static CachedBook getBook(int key, int location) {
		return getData(BOOKMapLondon, BOOKMapNewYork, new Loadable() {
			public void load() {
				loadBook();
			}
		}, key, location);
	}

	public static String getBasketEventDomain(int key, int location) {
		return getData(BASKETEVENTDOMAINMapLondon, BASKETEVENTDOMAINMapNewYork,
				new Loadable() {
					public void load() {
						loadBasketEventDomain();
					}
				}, key, location);
	}

	public static String getRegion(int key, int location) {
		return getData(REGIONMapLondon, REGIONMapNewYork, new Loadable() {
			public void load() {
				loadRegion();
			}
		}, key, location);
	}

	public static String getFIStatusDomain(int key, int location) {
		return getData(REGIONMapLondon, REGIONMapNewYork, new Loadable() {
			public void load() {
				loadFiSatus();
			}
		}, key, location);
	}

	public static Exchange getExchange(int key, int location) {
		return getData(EXCHANGEMAPLondon, EXCHANGEMAPNewYork, new Loadable() {
			public void load() {
				loadExchange();
			}
		}, key, location);
	}

	public static String getPriceTypeDomain(int key, int location) {
		return getData(PRICEFREQDOMAINMAPLondon, PRICEFREQDOMAINMAPNewYork,
				new Loadable() {
					public void load() {
						loadPriceType();
					}
				}, key, location);
	}

	public static String getPriceFreqDomain(int key, int location) {
		return getData(PRICEFREQDOMAINMAPLondon, PRICEFREQDOMAINMAPNewYork,
				new Loadable() {
					public void load() {
						loadPriceFreq();
					}
				}, key, location);
	}

	public static String getUserProfile(int key, int location) {
		return getData(USERPROFILEMAPLondon, USERPROFILEMAPNewYork,
				new Loadable() {
					public void load() {
						loadUserProfile();
					}
				}, key, location);
	}

	public static String getBusinessUnit(int key, int location) {
		return getData(BUSINESSUNITMAPLondon, BUSINESSUNITMAPNewYork,
				new Loadable() {
					public void load() {
						loadBusinessUnit();
					}
				}, key, location);
	}

	public static String getAccount(int key, int location) {
		return getData(ACCOUNTMAPLondon, ACCOUNTMAPNewYork, new Loadable() {
			public void load() {
				loadAccount();
			}
		}, key, location);
	}

	public static String getBookSeries(int key, int location) {
		return getData(BookSeriesMapLondon, BookSeriesMapNewYork,
				new Loadable() {
					public void load() {
						loadBookSeries();
					}
				}, key, location);
	}
	
	public static String getReason(int key, int location) {
		return getData(REASONMAPLondon, REASONMAPNewYork,
				new Loadable() {
					public void load() {
						loadReason();
					}
				}, key, location);
	}

	private static long load(String sql, Map<Integer, String> map1,
			Map<Integer, String> map2) {
		try {
			new CacheDAO().loadMap(sql, map1, 1);
			new CacheDAO().loadMap(sql, map2, 2);
		} catch (Exception ex) {
			logger.error("Error loading cache cache: SQL = " + sql, ex);
		}
		return System.currentTimeMillis();
	}

	private static void loadLogicalCal() {
		load(GET_LOGICALCAL, LOGICALCALMapLondon, LOGICALCALMapNewYork);
		logger.info("logicalCal cache loaded:");
	}

	private static void loadSwapTypeDomain() {
		load(GET_SWAPTYPEDOMAIN, SWAPTYPEDOMAINMapLondon,
				SWAPTYPEDOMAINMapNewYork);
		logger.info("SwapTypeDomain cache loaded:");
	}

	private static void loadStrategyDomain() {
		load(GET_STRATEGYDOMAIN, STRATEGYDOMAINMapLondon,
				STRATEGYDOMAINMapNewYork);
		logger.info("StrategyDomain cache loaded:");
	}

	private static void loadSwapStatusDomain() {
		load(GET_SWAPSTATUSDOMAIN, SWAPSTATUSDOMAINMapLondon,
				SWAPSTATUSDOMAINMapNewYork);
		logger.info("StrategyDomain cache loaded: ");
	}

	private static void loadCollateralTypeDomain() {
		load(GET_COLLATERALTYPEDOMAIN, COLLATERALTYPEDOMAINMapLondon,
				COLLATERALTYPEDOMAINMapNewYork);
		logger.info("StrategyDomain cache loaded: ");
	}

	private static void loadCommissionTypeDomain() {
		load(GET_COMMISSIONTYPEDOMAIN, COMMISSIONTYPEDOMAINMapLondon,
				COMMISSIONTYPEDOMAINMapNewYork);
		logger.info("StrategyDomain cache loaded: ");
	}

	private static void loadCommissionPayDomain() {
		load(GET_COMMISSIONPAYDOMAIN, COMMISSIONPAYDOMAINMapLondon,
				COMMISSIONPAYDOMAINMapNewYork);
		logger.info("CommissionPayDomain cache loaded: ");
	}

	private static void loadReturnTypeDomain() {
		load(GET_RETURNTYPEDOMAIN, RETURNTYPEDOMAINMapLondon,
				RETURNTYPEDOMAINMapNewYork);
		logger.info("ReturnTypeDomain cache loaded:");
	}

	private static void loadFITypeDomain() {
		load(GET_FITYPEDOMAIN, FITYPEDOMAINMapLondon, FITYPEDOMAINMapNewYork);
		logger.info("FITypeDomain cache loaded:");
	}

	private static void loadCountry() {
		load(GET_COUNTRY, COUNTRYMapLondon, COUNTRYMapNewYork);
		logger.info("Country cache loaded: ");
	}

	private static void loadCurrency() {
		try {
			new CacheDAO().loadCurrencyMap(GET_CURRENCY, CURRENCYMapLondon, 1);
			new CacheDAO().loadCurrencyMap(GET_CURRENCY, CURRENCYMapNewYork, 2);

			for (CachedCurrency c : CURRENCYMapLondon.values()) {
				CCYCodeMapLondon.put(c.getSwiftCode(), c);
			}
			for (CachedCurrency c : CURRENCYMapNewYork.values()) {
				CCYCodeMapNewYork.put(c.getSwiftCode(), c);
			}

			logger.info("Currency cache loaded: ");
		} catch (Exception e) {
			logger.error("Error loading Currency cache: ", e);
		}
	}

	private static void loadPayDivDomain() {
		load(GET_PAYDIVDOMAIN, PAYDIVDOMAINMapLondon, PAYDIVDOMAINMapNewYork);
		logger.info("PayDivDomain cache loaded: ");
	}

	private static void loadBasisDomain() {
		try {
			new CacheDAO().loadBasisMap(GET_BASISDOMAIN, BASISDOMAINMapLondon,
					1);
			new CacheDAO().loadBasisMap(GET_BASISDOMAIN, BASISDOMAINMapNewYork,
					2);
			logger.info("BasisDomain cache loaded: ");
		} catch (Exception e) {
			logger.error("Error loading BasisDomain cache: ", e);
		}
	}

	private static void loadPeriodDomain() {
		load(GET_PERIODDOMAIN, PERIODDOMAINMapLondon, PERIODDOMAINMapNewYork);
		logger.info("PeriodDomain cache loaded: ");
	}

	private static void loadFreqTypeDomain() {
		load(GET_FREQTYPEDOMAIN, FREQTYPEDOMAINMapLondon,
				FREQTYPEDOMAINMapNewYork);
		logger.info("FreqTypeDomain cache loaded: ");
	}

	private static void loadBumpConventionDomain() {
		load(GET_BUMPCONVENTIONDOMAIN, BUMPCONVENTIONDOMAINMapLondon,
				BUMPCONVENTIONDOMAINMapNewYork);
		logger.info("BumpConventionDomain cache loaded: ");
	}

	private static void loadLegalEntity() {
		load(GET_LEGALENTITY, LEGALENTITYMapLondon, LEGALENTITYMapNewYork);
		logger.info("LegalEntity cache loaded: ");
	}

	private static void loadSynUsers() {
		load(GET_SynUsers, SynUserListLondon, SynUserListNewYork);
		logger.info("Synthesis Users cache loaded ");
	}

	private static void loadExtType() {
		load(GET_EXTTYPE, EXTTYPEMapLondon, EXTTYPEMapNewYork);
		logger.info("EXT Type cache loaded: ");
	}

	private static void loadInstitution() {
		load(GET_INSTITUTION, INSTITUTIONMapLondon, INSTITUTIONMapNewYork);
		logger.info("Institution cache loaded: ");
	}

	private static void loadDesk() {
		load(GET_DESK, DESKMapLondon, DESKMapNewYork);
		logger.info("Desk cache loaded: ");
	}

	private static void loadBook() {
		try {
			new CacheDAO().loadBookMap(GET_BOOK, BOOKMapLondon, 1);
			new CacheDAO().loadBookMap(GET_BOOK, BOOKMapNewYork, 2);
			logger.info("Book cache loaded: ");
		} catch (Exception e) {
			logger.error("Error loading Book cache: ", e);
		}
	}

	private static void loadBasketEventDomain() {
		load(GET_BASKETEVENTDOMAIN, BASKETEVENTDOMAINMapLondon,
				BASKETEVENTDOMAINMapNewYork);
		logger.info("Basket Event Domain  cache loaded: ");
	}

	private static void loadRegion() {
		load(GET_REGION, REGIONMapLondon, REGIONMapNewYork);
		logger.info("Region loaded: ");
	}

	private static void loadFiSatus() {
		load(GET_FISTATUS, FISTATUSDOMAINMAPLondon, FISTATUSDOMAINMAPNewYork);
		logger.info("FiSatus loaded: ");
	}

	private static void loadPriceFreq() {
		load(GET_PRICEFREQ, PRICEFREQDOMAINMAPLondon, PRICEFREQDOMAINMAPNewYork);
		logger.info("PriceFreq loaded: ");
	}

	private static void loadPriceType() {
		load(GET_PRICETYPE, PRICETYPEDOMAINMAPLondon, PRICETYPEDOMAINMAPNewYork);
		logger.info("PriceType loaded: ");
	}

	private static void loadUserProfile() {
		load(GET_USERPROFILE, USERPROFILEMAPLondon, USERPROFILEMAPNewYork);
		logger.info("UserProfile loaded: ");
	}

	private static void loadBookSeries() {
		load(GET_BookSeries, BookSeriesMapLondon, BookSeriesMapNewYork);
		logger.info("BookSeries loaded: ");
	}

	private static void loadExchange() {
		try {
			new CacheDAO().loadExchangeMap(GET_EXCHANGEMAP, EXCHANGEMAPLondon,
					1);
			new CacheDAO().loadExchangeMap(GET_EXCHANGEMAP, EXCHANGEMAPNewYork,
					2);
			logger.info("Exchange loaded: ");
		} catch (Exception e) {
			logger.error("Error Exchange cache: ", e);
		}
	}

	private static void loadSecurities() {
		try {
			SECURITYMAPLondon.putAll(new CacheDAO().loadSecurities(GET_TICKER,
					1));
			SECURITYMAPNewYork.putAll(new CacheDAO().loadSecurities(GET_TICKER,
					2));
			logger.info("Securities loaded: ");
		} catch (Exception e) {
			logger.error("Error Securities cache: ", e);
		}
	}

	private static void loadAccount() {
		load(GET_ACCOUNTMAP, ACCOUNTMAPLondon, ACCOUNTMAPNewYork);
		logger.info("Account loaded:");
	}

	private static void loadBusinessUnit() {
		load(GET_BUSINESSUNITMAP, BUSINESSUNITMAPLondon, BUSINESSUNITMAPNewYork);
		logger.info("BusinessUnit loaded:");
	}
	
	private static void loadReason() {
		load(GET_REASON, REASONMAPLondon, REASONMAPNewYork);
		logger.info("Reason Maps loaded:");
	}

	private static final <I, S> Map<I, S> populateMap(Map<I, S> londonMap,
			Map<I, S> nyMap, int location) {
		Map<I, S> map = null;
		switch (location) {
		case 1:
			map = new HashMap<I, S>(londonMap);
			break;
		case 2:
			map = new HashMap<I, S>(nyMap);
			break;
		case 3:
			map = new HashMap<I, S>(londonMap);
			map.putAll(nyMap);
			break;
		default:
			break;
		}
		return map;
	}

	public static final Map<Integer, String> getSynthesisUsers(int location) {

		return populateMap(SynUserListLondon, SynUserListNewYork, location);
	}

	public static final Map<Integer, String> getLogicalcalmap(int location) {
		return populateMap(LOGICALCALMapLondon, LOGICALCALMapNewYork, location);
	}

	public static final Map<Integer, String> getSwaptypedomainmap(int location) {
		return populateMap(SWAPTYPEDOMAINMapLondon, SWAPTYPEDOMAINMapNewYork,
				location);
	}

	public static final Map<Integer, String> getStrategydomainmap(int location) {
		return populateMap(STRATEGYDOMAINMapLondon, STRATEGYDOMAINMapNewYork,
				location);
	}

	public static final Map<Integer, String> getSwapstatusdomainmap(int location) {
		return populateMap(SWAPSTATUSDOMAINMapLondon,
				SWAPSTATUSDOMAINMapNewYork, location);
	}

	public static final Map<Integer, String> getCollateraltypedomainmap(
			int location) {
		return populateMap(COLLATERALTYPEDOMAINMapLondon,
				COLLATERALTYPEDOMAINMapNewYork, location);
	}

	public static final Map<Integer, String> getCommissiontypedomainmap(
			int location) {
		return populateMap(COMMISSIONTYPEDOMAINMapLondon,
				COMMISSIONTYPEDOMAINMapNewYork, location);
	}

	public static final Map<Integer, String> getCommissionpaydomainmap(
			int location) {
		return populateMap(COMMISSIONPAYDOMAINMapLondon,
				COMMISSIONPAYDOMAINMapNewYork, location);
	}

	public static final Map<Integer, String> getReturntypedomainmap(int location) {
		return populateMap(RETURNTYPEDOMAINMapLondon,
				RETURNTYPEDOMAINMapNewYork, location);
	}

	public static final Map<Integer, String> getFItypedomainmap(int location) {
		return populateMap(FITYPEDOMAINMapLondon, FITYPEDOMAINMapNewYork,
				location);
	}

	public static final Map<Integer, String> getCountrymap(int location) {
		return populateMap(COUNTRYMapLondon, COUNTRYMapNewYork, location);
	}

	public static final Map<Integer, CachedCurrency> getCurrencymap(int location) {
		return populateMap(CURRENCYMapLondon, CURRENCYMapNewYork, location);
	}

	public static final Map<Integer, String> getPaydivdomainmap(int location) {
		return populateMap(PAYDIVDOMAINMapLondon, PAYDIVDOMAINMapNewYork,
				location);
	}

	public static final Map<Integer, CachedBasis> getBasisdomainmap(int location) {
		return populateMap(BASISDOMAINMapLondon, BASISDOMAINMapNewYork,
				location);
	}

	public static final Map<Integer, String> getPerioddomainmap(int location) {
		return populateMap(PERIODDOMAINMapLondon, PERIODDOMAINMapNewYork,
				location);
	}

	public static final Map<Integer, String> getFreqtypedomainmap(int location) {
		return populateMap(FREQTYPEDOMAINMapLondon, FREQTYPEDOMAINMapNewYork,
				location);
	}

	public static final Map<Integer, String> getBumpconventiondomainmap(
			int location) {
		return populateMap(BUMPCONVENTIONDOMAINMapLondon,
				BUMPCONVENTIONDOMAINMapNewYork, location);
	}

	public static final Map<Integer, String> getLegalentitymap(int location) {
		return populateMap(LEGALENTITYMapLondon, LEGALENTITYMapNewYork,
				location);
	}

	public static final Map<Integer, String> getExttypemap(int location) {
		return populateMap(EXTTYPEMapLondon, EXTTYPEMapNewYork, location);
	}

	public static final List<CacheEntry> getSecurityList(int location,
			String securityType) {
		switch (location) {
		case 1:
			return SECURITYMAPLondon.get(securityType);
		case 2:
			return SECURITYMAPNewYork.get(securityType);
		case 3:
			List<CacheEntry> list = new ArrayList<CacheEntry>(
					SECURITYMAPLondon.get(securityType));
			list.addAll(SECURITYMAPNewYork.get(securityType));
			return list;
		default:
			break;
		}
		return new ArrayList<CacheEntry>();
	}

	public static final Map<Integer, String> getInstitutionMap(int location) {
		return populateMap(INSTITUTIONMapLondon, INSTITUTIONMapNewYork,
				location);
	}

	public static final Map<Integer, String> getDeskmap(int location) {
		return populateMap(DESKMapLondon, DESKMapNewYork, location);
	}

	public static final Map<Integer, CachedBook> getBookmap(int location) {
		return populateMap(BOOKMapLondon, BOOKMapNewYork, location);
	}

	public static final Map<Integer, String> getBasketEventDomainmap(
			int location) {
		return populateMap(BASKETEVENTDOMAINMapLondon,
				BASKETEVENTDOMAINMapNewYork, location);
	}

	public static final Map<Integer, String> getRegionmap(int location) {
		return populateMap(REGIONMapLondon, REGIONMapNewYork, location);
	}

	public static final Map<String, Map<String, Double>> getFXRatemap(
			int location) {
		return populateMap(FXRATEMapLondon, FXRATEMapNewYork, location);
	}

	public static Map<Integer, String> getFistatusdomainmap(int location) {
		return populateMap(FISTATUSDOMAINMAPLondon, FISTATUSDOMAINMAPNewYork,
				location);
	}

	public static Map<Integer, String> getPricefreqdomainmap(int location) {
		return populateMap(PRICEFREQDOMAINMAPLondon, PRICEFREQDOMAINMAPNewYork,
				location);
	}

	public static Map<Integer, String> getPricetypedomainmap(int location) {
		return populateMap(PRICETYPEDOMAINMAPLondon, PRICETYPEDOMAINMAPNewYork,
				location);
	}

	public static Map<Integer, String> getUserprofilemap(int location) {
		return populateMap(USERPROFILEMAPLondon, USERPROFILEMAPNewYork,
				location);
	}

	public static Map<Integer, Exchange> getExchangemap(int location) {
		return populateMap(EXCHANGEMAPLondon, EXCHANGEMAPNewYork, location);
	}

	public static Map<Integer, String> getAccountmap(int location) {
		return populateMap(ACCOUNTMAPLondon, ACCOUNTMAPNewYork, location);
	}

	public static Map<Integer, String> getBusinessUintmap(int location) {
		return populateMap(BUSINESSUNITMAPLondon, BUSINESSUNITMAPNewYork,
				location);
	}

	public static Map<Integer, String> getBookSeries(int location) {
		return populateMap(BookSeriesMapLondon, BookSeriesMapNewYork, location);
	}
	
	public static Map<Integer, String> getReason(int location) {
		return populateMap(REASONMAPLondon, REASONMAPNewYork, location);
	}
}

interface Loadable {
	public void load();
}
