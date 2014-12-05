package com.db.ess.synthesis.dao.tasks.securityId;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.db.ess.synthesis.util.PropertiesUtil;



public class TaskHelper {

	private static final String DRIVER_NAME = "oracle.jdbc.driver.OracleDriver";
	private static final String DATABASE = PropertiesUtil.getInstance().getProperty("DBI.database");
	private static final String USER = PropertiesUtil.getInstance().getProperty("DBI.user");
	private static final String PASSWORD = PropertiesUtil.getInstance().getProperty("DBI.password");

	public static Connection getConnection() throws SQLException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		Class.forName(DRIVER_NAME).newInstance();
		return DriverManager.getConnection(DATABASE, USER, PASSWORD);
	}
	
	
	

}
