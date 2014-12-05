// Title:        LegalEntity
// Author:       Generate code
// Company:      
// Description:  This class was generated by the XML-Broker Developer Console
// Schema name:  /home/anthony/workspace/SynthesisWS/schema/Synthesis.xsd
// Java SDK:     

package com.db.ess.synthesis.dvo;

import java.util.Date;
import java.io.IOException;

import org.adaptinet.sdk.adaptinetex.FastCacheException;
import org.adaptinet.sdk.fastcache.CacheServer;
import org.adaptinet.sdk.fastcache.DataArray;
import org.adaptinet.sdk.fastcache.DataItem;
import org.adaptinet.sdk.fastcache.FastCacheDVOBase;

public class LegalEntity implements FastCacheDVOBase  {
	public Date getagreementDate() { 
		return _agreementDate;
	}
	public void setagreementDate(Date newValue) { 
		_agreementDate = newValue;
	}
	public int getlegalEntityId() {
		return _legalEntityId;
	}
	public void setlegalEntityId(int newValue) {
		_legalEntityId = newValue;
	}
	public int getinstitutionId() {
		return _institutionId;
	}
	public void setinstitutionId(int newValue) {
		_institutionId = newValue;
	}
	public String getlegalEntityname() {
		if(_legalEntityname!=null) {
			return new String(_legalEntityname);
		}
		else {
			return null;
		}
	}
	public void setlegalEntityname(String newValue) {
		_legalEntityname = newValue!=null ? newValue.toCharArray() : null;
	}
	public String getlegalEntityFullName() {
		if(_legalEntityFullName!=null) {
			return new String(_legalEntityFullName);
		}
		else {
			return null;
		}
	}
	public void setlegalEntityFullName(String newValue) {
		_legalEntityFullName = newValue!=null ? newValue.toCharArray() : null;
	}
	public String getmnemonic() {
		if(_mnemonic!=null) {
			return new String(_mnemonic);
		}
		else {
			return null;
		}
	}
	public void setmnemonic(String newValue) {
		_mnemonic = newValue!=null ? newValue.toCharArray() : null;
	}
	public String getpbgoLegalEntityId() {
		if(_pbgoLegalEntityId!=null) {
			return new String(_pbgoLegalEntityId);
		}
		else {
			return null;
		}
	}
	public void setpbgoLegalEntityId(String newValue) {
		_pbgoLegalEntityId = newValue!=null ? newValue.toCharArray() : null;
	}
	public String getaddress() {
		if(_address!=null) {
			return new String(_address);
		}
		else {
			return null;
		}
	}
	public void setaddress(String newValue) {
		_address = newValue!=null ? newValue.toCharArray() : null;
	}
	public String getaddress2() {
		if(_address2!=null) {
			return new String(_address2);
		}
		else {
			return null;
		}
	}
	public void setaddress2(String newValue) {
		_address2 = newValue!=null ? newValue.toCharArray() : null;
	}
	public String getcity() {
		if(_city!=null) {
			return new String(_city);
		}
		else {
			return null;
		}
	}
	public void setcity(String newValue) {
		_city = newValue!=null ? newValue.toCharArray() : null;
	}
	public String getstate() {
		if(_state!=null) {
			return new String(_state);
		}
		else {
			return null;
		}
	}
	public void setstate(String newValue) {
		_state = newValue!=null ? newValue.toCharArray() : null;
	}
	public String getpostalCode() {
		if(_postalCode!=null) {
			return new String(_postalCode);
		}
		else {
			return null;
		}
	}
	public void setpostalCode(String newValue) {
		_postalCode = newValue!=null ? newValue.toCharArray() : null;
	}
	public String getcountry() {
		if(_country!=null) {
			return new String(_country);
		}
		else {
			return null;
		}
	}
	public void setcountry(String newValue) {
		_country = newValue!=null ? newValue.toCharArray() : null;
	}
	public String getcportLegalEntityId() {
		if(_cportLegalEntityId!=null) {
			return new String(_cportLegalEntityId);
		}
		else {
			return null;
		}
	}
	public void setcportLegalEntityId(String newValue) {
		_cportLegalEntityId = newValue!=null ? newValue.toCharArray() : null;
	}
	public int getparagonId() {
		return _paragonId;
	}
	public void setparagonId(int newValue) {
		_paragonId = newValue;
	}
	public String getcontactPerson() {
		if(_contactPerson!=null) {
			return new String(_contactPerson);
		}
		else {
			return null;
		}
	}
	public void setcontactPerson(String newValue) {
		_contactPerson = newValue!=null ? newValue.toCharArray() : null;
	}
	public String getemailAddress() {
		if(_emailAddress!=null) {
			return new String(_emailAddress);
		}
		else {
			return null;
		}
	}
	public void setemailAddress(String newValue) {
		_emailAddress = newValue!=null ? newValue.toCharArray() : null;
	}
	public String gettelephone() {
		if(_telephone!=null) {
			return new String(_telephone);
		}
		else {
			return null;
		}
	}
	public void settelephone(String newValue) {
		_telephone = newValue!=null ? newValue.toCharArray() : null;
	}
	public String getfax() {
		if(_fax!=null) {
			return new String(_fax);
		}
		else {
			return null;
		}
	}
	public void setfax(String newValue) {
		_fax = newValue!=null ? newValue.toCharArray() : null;
	}
	public String gettelex() {
		if(_telex!=null) {
			return new String(_telex);
		}
		else {
			return null;
		}
	}
	public void settelex(String newValue) {
		_telex = newValue!=null ? newValue.toCharArray() : null;
	}
	public String getagreementText() {
		if(_agreementText!=null) {
			return new String(_agreementText);
		}
		else {
			return null;
		}
	}
	public void setagreementText(String newValue) {
		_agreementText = newValue!=null ? newValue.toCharArray() : null;
	}
	public String getsalesMan() {
		if(_salesMan!=null) {
			return new String(_salesMan);
		}
		else {
			return null;
		}
	}
	public void setsalesMan(String newValue) {
		_salesMan = newValue!=null ? newValue.toCharArray() : null;
	}
	public String gettierCode() {
		if(_tierCode!=null) {
			return new String(_tierCode);
		}
		else {
			return null;
		}
	}
	public void settierCode(String newValue) {
		_tierCode = newValue!=null ? newValue.toCharArray() : null;
	}
	public int getdbClientTradingRef() {
		return _dbClientTradingRef;
	}
	public void setdbClientTradingRef(int newValue) {
		_dbClientTradingRef = newValue;
	}
	public String getcisNumber() {
		if(_cisNumber!=null) {
			return new String(_cisNumber);
		}
		else {
			return null;
		}
	}
	public void setcisNumber(String newValue) {
		_cisNumber = newValue!=null ? newValue.toCharArray() : null;
	}
	public String getcomment() {
		if(_comment!=null) {
			return new String(_comment);
		}
		else {
			return null;
		}
	}
	public void setcomment(String newValue) {
		_comment = newValue!=null ? newValue.toCharArray() : null;
	}
	public boolean getdbagEntityFlag() {
		return _dbagEntityFlag;
	}
	public void setdbagEntityFlag(boolean newValue) {
		_dbagEntityFlag = newValue;
	}
	public boolean getpbgoLegalEntityFlag() {
		return _pbgoLegalEntityFlag;
	}
	public void setpbgoLegalEntityFlag(boolean newValue) {
		_pbgoLegalEntityFlag = newValue;
	}
	public int getessTradePartyFlag() {
		return _essTradePartyFlag;
	}
	public void setessTradePartyFlag(int newValue) {
		_essTradePartyFlag = newValue;
	}
	public String getPrimeBrokerageFlag() {
		if(_PrimeBrokerageFlag!=null) {
			return new String(_PrimeBrokerageFlag);
		}
		else {
			return null;
		}
	}
	public void setPrimeBrokerageFlag(String newValue) {
		_PrimeBrokerageFlag = newValue!=null ? newValue.toCharArray() : null;
	}
	public String getinstituionName() {
		if(_instituionName!=null) {
			return new String(_instituionName);
		}
		else {
			return null;
		}
	}
	public void setinstituionName(String newValue) {
		_instituionName = newValue!=null ? newValue.toCharArray() : null;
	}
	public boolean getbrokerDealerFlag() {
		return _brokerDealerFlag;
	}
	public void setbrokerDealerFlag(boolean newValue) {
		_brokerDealerFlag = newValue;
	}
	public boolean getprimaryDealerFlag() {
		return _primaryDealerFlag;
	}
	public void setprimaryDealerFlag(boolean newValue) {
		_primaryDealerFlag = newValue;
	}
	public int getpriceDigits() {
		return _priceDigits;
	}
	public void setpriceDigits(int newValue) {
		_priceDigits = newValue;
	}
	public int gettaxWithHoldingMethod() {
		return _taxWithHoldingMethod;
	}
	public void settaxWithHoldingMethod(int newValue) {
		_taxWithHoldingMethod = newValue;
	}
	public int gettotalReturnSwapClientFlag() {
		return _totalReturnSwapClientFlag;
	}
	public void settotalReturnSwapClientFlag(int newValue) {
		_totalReturnSwapClientFlag = newValue;
	}
	public String getessLocation() {
		if(_essLocation!=null) {
			return new String(_essLocation);
		}
		else {
			return null;
		}
	}
	public void setessLocation(String newValue) {
		_essLocation = newValue!=null ? newValue.toCharArray() : null;
	}
	public void putToCache(String key, CacheServer server) throws FastCacheException {
		DataArray da = writeCache();
		item.putDataArray(da);
		server.putValue(key, item);
		item.DataItemClear();
	}
	public void getFromCache(String key, CacheServer server) throws FastCacheException {
		DataItem item = server.getValue(key);
		DataArray da = new DataArray();
		item.getDataArray(da);
		readCache(da);
		item.DataItemClear();
	}
	public DataArray writeCache() throws FastCacheException {
		int sa_idx = 0;
		DataArray da = new DataArray(DataItem.DATAITEMDATAITEM, 37);
		item.putDate(_agreementDate);
		da.setDataItem(sa_idx++, item);
		item.putInt(_legalEntityId);
		da.setDataItem(sa_idx++, item);
		item.putInt(_institutionId);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_legalEntityname);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_legalEntityFullName);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_mnemonic);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_pbgoLegalEntityId);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_address);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_address2);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_city);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_state);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_postalCode);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_country);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_cportLegalEntityId);
		da.setDataItem(sa_idx++, item);
		item.putInt(_paragonId);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_contactPerson);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_emailAddress);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_telephone);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_fax);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_telex);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_agreementText);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_salesMan);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_tierCode);
		da.setDataItem(sa_idx++, item);
		item.putInt(_dbClientTradingRef);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_cisNumber);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_comment);
		da.setDataItem(sa_idx++, item);
		item.putBoolean(_dbagEntityFlag);
		da.setDataItem(sa_idx++, item);
		item.putBoolean(_pbgoLegalEntityFlag);
		da.setDataItem(sa_idx++, item);
		item.putInt(_essTradePartyFlag);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_PrimeBrokerageFlag);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_instituionName);
		da.setDataItem(sa_idx++, item);
		item.putBoolean(_brokerDealerFlag);
		da.setDataItem(sa_idx++, item);
		item.putBoolean(_primaryDealerFlag);
		da.setDataItem(sa_idx++, item);
		item.putInt(_priceDigits);
		da.setDataItem(sa_idx++, item);
		item.putInt(_taxWithHoldingMethod);
		da.setDataItem(sa_idx++, item);
		item.putInt(_totalReturnSwapClientFlag);
		da.setDataItem(sa_idx++, item);
		item.putCharArray(_essLocation);
		da.setDataItem(sa_idx++, item);
		item.DataItemClear();
		return da;
	}
	public void readCache(DataArray in) throws FastCacheException {
		int sa_idx = 0;
		in.getDataItem(sa_idx++, item);
		_agreementDate = item.getDate();
		in.getDataItem(sa_idx++, item);
		_legalEntityId = item.getInt();
		in.getDataItem(sa_idx++, item);
		_institutionId = item.getInt();
		in.getDataItem(sa_idx++, item);
		_legalEntityname = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_legalEntityFullName = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_mnemonic = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_pbgoLegalEntityId = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_address = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_address2 = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_city = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_state = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_postalCode = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_country = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_cportLegalEntityId = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_paragonId = item.getInt();
		in.getDataItem(sa_idx++, item);
		_contactPerson = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_emailAddress = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_telephone = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_fax = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_telex = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_agreementText = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_salesMan = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_tierCode = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_dbClientTradingRef = item.getInt();
		in.getDataItem(sa_idx++, item);
		_cisNumber = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_comment = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_dbagEntityFlag = item.getBoolean();
		in.getDataItem(sa_idx++, item);
		_pbgoLegalEntityFlag = item.getBoolean();
		in.getDataItem(sa_idx++, item);
		_essTradePartyFlag = item.getInt();
		in.getDataItem(sa_idx++, item);
		_PrimeBrokerageFlag = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_instituionName = item.getCharArray();
		in.getDataItem(sa_idx++, item);
		_brokerDealerFlag = item.getBoolean();
		in.getDataItem(sa_idx++, item);
		_primaryDealerFlag = item.getBoolean();
		in.getDataItem(sa_idx++, item);
		_priceDigits = item.getInt();
		in.getDataItem(sa_idx++, item);
		_taxWithHoldingMethod = item.getInt();
		in.getDataItem(sa_idx++, item);
		_totalReturnSwapClientFlag = item.getInt();
		in.getDataItem(sa_idx++, item);
		_essLocation = item.getCharArray();
	}
	public void writeObject(byte [] data) throws FastCacheException {
		item.writeObject(data);
		DataArray in = new DataArray();
		item.getDataArray(in);
		readCache(in);
	}
	public byte [] readObject() throws FastCacheException, IOException {
		DataArray requestDA = writeCache();
		item.putDataArray(requestDA);
		return item.readObject();
	}
	private Date _agreementDate =  new Date();
	private int _legalEntityId;
	private int _institutionId;
	private char[] _legalEntityname;
	private char[] _legalEntityFullName;
	private char[] _mnemonic;
	private char[] _pbgoLegalEntityId;
	private char[] _address;
	private char[] _address2;
	private char[] _city;
	private char[] _state;
	private char[] _postalCode;
	private char[] _country;
	private char[] _cportLegalEntityId;
	private int _paragonId;
	private char[] _contactPerson;
	private char[] _emailAddress;
	private char[] _telephone;
	private char[] _fax;
	private char[] _telex;
	private char[] _agreementText;
	private char[] _salesMan;
	private char[] _tierCode;
	private int _dbClientTradingRef;
	private char[] _cisNumber;
	private char[] _comment;
	private boolean _dbagEntityFlag;
	private boolean _pbgoLegalEntityFlag;
	private int _essTradePartyFlag;
	private char[] _PrimeBrokerageFlag;
	private char[] _instituionName;
	private boolean _brokerDealerFlag;
	private boolean _primaryDealerFlag;
	private int _priceDigits;
	private int _taxWithHoldingMethod;
	private int _totalReturnSwapClientFlag;
	private char[] _essLocation;
	private DataItem item = new DataItem();
}

