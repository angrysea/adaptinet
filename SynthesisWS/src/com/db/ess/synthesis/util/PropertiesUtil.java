package com.db.ess.synthesis.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertiesUtil {
	private Properties props = new Properties();
	private static final Logger logger = Logger.getLogger(PropertiesUtil.class.getName());
	private static final String SYNTHESIS_PROPERTY_FILE = "C:/projects/adaptinet/InstrumentCache/conf/synthesis.properties";
	private static PropertiesUtil propInstance = null; 
	
	private PropertiesUtil(){
		try {
			props.load(new FileInputStream(SYNTHESIS_PROPERTY_FILE));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static PropertiesUtil getInstance(){
		if(propInstance == null){
			propInstance = new PropertiesUtil();
		}
		return propInstance;
	}
	
	public String getProperty(String property){
		return props.getProperty(property);
	}
	
}
