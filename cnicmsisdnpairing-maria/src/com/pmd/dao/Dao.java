package com.pmd.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.pmd.util.LoadProperties;

public class Dao {
	protected Logger logger = Logger.getLogger(getClass());
	private  LoadProperties loadProperties;
	private String dbSchema;
	protected Connection dbConn;
	
	public String getDbSchema() {
		return dbSchema;
	}
	private void setDbSchema(String dbSchema) {
		this.dbSchema = dbSchema;
	}

	
	public Dao(String dbSchema) throws IOException{
		setDbSchema(dbSchema);
		
		loadProperties = new LoadProperties(dbSchema);
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (Exception e) {
			logger.info("Oracle drivers loading Fail:" + e);
			throw new IOException("Oracle drivers loading Fail:" + e);
		}
		
	
	}

	protected void setDbConnection() throws SQLException{
		try {
			String conn= loadProperties.getDbConnString();
			String user=loadProperties.getUser();
			String pwd =loadProperties.getPwd();
			this.dbConn=DriverManager.getConnection(conn, user, pwd);
		} catch (Exception e) {
			logger.error("!!!!!!!"+getDbSchema()+"DB Connection Fail!!!!!!" +e);
			throw new SQLException("!!!!!!!"+getDbSchema()+"DB Connection Fail!!!!!!" +e);
		}
	}
	
	public String getAppStatus (){
		return loadProperties.getServicestatus();
	}
	public Boolean isDbConnOpen(){
		if (dbConn!=null){
			return true;
		}
		return false;
	}
	public Boolean closeDbConn(){
		if (dbConn!=null){
			try {
				dbConn.close();
				return true;
			} catch (Exception e) {
				logger.info("Fail to Close Connection", e);
			}
		}
		return false;
	}
}
