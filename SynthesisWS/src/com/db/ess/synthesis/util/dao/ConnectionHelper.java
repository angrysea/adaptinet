package com.db.ess.synthesis.util.dao;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.Properties;

import javax.naming.Context;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.util.ESSLocation;

public class ConnectionHelper {
	private static String LNDDATASOURCE_NAME = "essDataSourceLdn";
	private static String NYDATASOURCE_NAME = "essDataSourceNy";
	private static String DEFAULTDATASOURCE_NAME = "essDataSourceLdn";
	public static String DATABASE_NAME = "ess_test";
	private static String url, user,
			pwd, driver;
	private static LinkedList<Connection> connectionPool;

	private static String providerUrl;
	private static Properties contextProperties;
	
	private static final Logger logger = Logger.getLogger(ConnectionHelper.class.getName());
	
	public static Connection getConnection() throws Exception {
		return getConnection(DEFAULTDATASOURCE_NAME, true);
	}

	public static Connection getConnection(int location) throws Exception {
		Connection con = null;
		switch(location) {
		case ESSLocation.GLOBAL:
			logger.error("GLOBAL is an invalid location for datasource...");
			throw new Exception("GLOBAL is an invalid location for datasource...");
			
		case ESSLocation.NEWYORK:
			con = getConnection(NYDATASOURCE_NAME, true);
			break;
		case ESSLocation.LONDON:
		default:
			con = getConnection(LNDDATASOURCE_NAME, true);
			break;
		}		
		return con;
	}

	public static void setDefaultDataSourceName(String newdds) {
		if (newdds != null)
			DEFAULTDATASOURCE_NAME = newdds;
	}

	public static String getProviderUrl() {
		return providerUrl;
	}

	public static Properties getContextProperties() {
		return contextProperties;
	}

	public static Connection getConnection(boolean autocommit)
			throws Exception {
		return getConnection(DEFAULTDATASOURCE_NAME, autocommit);
	}

	public static Connection getConnection(String dataSourceName,
			boolean autocommit) throws Exception {

		Connection conn = null;

		if (connectionPool != null) {
			conn = getConnectionFromPool();
		} else if (driver != null) {
			conn = getConnectionFromInMemParams();
		}
		
		return conn;
	}

	public static void releaseConnection(Connection conn) {
		if (connectionPool != null) {
			releaseConnectionToPool(conn);
		} else {
			try {
				if (conn != null)
					conn.close();
			} catch (Exception ex) {
      		  logger.error("Closing JDBC Connection ", ex); 
			}
		}
	}

	private static Connection getConnectionFromPool() throws Exception {
		synchronized (connectionPool) {
			try {
				while (connectionPool.isEmpty()) {
					connectionPool.wait();
				}
				return (Connection) connectionPool.removeFirst();
			} catch (InterruptedException iEx) {
				throw new Exception(iEx);
			}
		}
	}

	private static void releaseConnectionToPool(Connection conn) {
		synchronized (connectionPool) {
			if (conn != null)
				connectionPool.addLast(conn);
			connectionPool.notifyAll();
		}
	}

	private static Connection getConnectionFromInMemParams() throws Exception {
		Connection conn = null;
		try {
			Class.forName(driver);
			conn = java.sql.DriverManager.getConnection(url,
					user, pwd);
			conn.setAutoCommit(true);
			return conn;
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}

	public static void init(int poolCount, String driver, String url, String user, String pwd) throws Exception {

		ConnectionHelper.driver = driver;
		ConnectionHelper.url = url;
		ConnectionHelper.user = user;
		ConnectionHelper.pwd = pwd;

		if (poolCount > 20)
			poolCount = 20;
		if (poolCount < 3)
			poolCount = 3;

		if (poolCount > 0) {
			LinkedList<Connection> templl = new LinkedList<Connection>();
			for (int i = 0; i < poolCount; i++) {
				Connection conn = getConnectionFromInMemParams();
				templl.add(conn);
			}
			connectionPool = templl;
		}
	}

	public static void setConnectionParameters(	String dsName,
												String contextFactory, 
												String providerUrl) throws Exception {

		try {
			DEFAULTDATASOURCE_NAME = dsName;
			ConnectionHelper.providerUrl = providerUrl;
			Properties env = new Properties();
			if (contextFactory != null && contextFactory.length() > 0) {
				env.setProperty(Context.INITIAL_CONTEXT_FACTORY, contextFactory);
			}
			
			if (providerUrl != null && providerUrl.length() > 0) {
				env.setProperty(Context.PROVIDER_URL, providerUrl);
			}
	
			ConnectionHelper.contextProperties = env;
		}
		catch(Exception e)
		{
			 throw new Exception(e.getMessage());
		}
	}

	public static void setConnectionParameters(	String dsName,
												String contextFactory, 
												String providerUrl,
												Properties contextProperties) 
		throws Exception {

		try {
			DEFAULTDATASOURCE_NAME = dsName;
			ConnectionHelper.providerUrl = providerUrl;
			
			Properties env;
			if (contextProperties != null) {
				env = contextProperties;
			} else {
				env = new Properties();
			}
			
			if (contextFactory != null && contextFactory.length() > 0) {
				env.setProperty(Context.INITIAL_CONTEXT_FACTORY, contextFactory);
			}
			
			if (providerUrl != null && providerUrl.length() > 0) {
				env.setProperty(Context.PROVIDER_URL, providerUrl);
			}	
			ConnectionHelper.contextProperties = env;
		}
		catch(Exception e)
		{
			 throw new Exception(e.getMessage());
		}
	}
}
