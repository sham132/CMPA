package com.pmd.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Logger;

public class LoadProperties {

	static private Properties Configuration = new Properties();
	private Logger logger = Logger.getLogger(getClass());
	private String dbConnString = null, user = null, pwd = null, servicestatus = null, appNumber = null;

	public LoadProperties(String dbSchema) throws IOException {
		try {
			Configuration.load(new FileInputStream(new File(StaticConstant.DBCONFIGURATIONFILE)));
			if (dbSchema.contains("cmpa")) {
				setDbConnString(getValue(dbSchema + "dbMaria", true));
				setUser(getValue(dbSchema + "userMaria", true));
				setPwd(getValue(dbSchema + "pwdMaria", true));
				setServicestatus(getValue("servicestatus", true));
				setAppNumber(getValue("appNumber", true));
			} else {
				setDbConnString(getValue(dbSchema + "db", true));
				setUser(getValue(dbSchema + "user", true));
				setPwd(getValue(dbSchema + "pwd", true));
				setServicestatus(getValue("servicestatus", true));
				setAppNumber(getValue("appNumber", true));
			}

			logger.info("Configuration File loaded.....");
			if (logger.isDebugEnabled())
				logger.debug("Configuration File loaded.....");
			if (dbConnString.isEmpty() || user.isEmpty() || pwd.isEmpty()) {
				if (logger.isDebugEnabled())
					logger.debug("One or More Parameter is missing in file. \n ParmList(1--" + dbSchema + "dbString 2--"
							+ dbSchema + "user 3--" + dbSchema + "pwd");
				// System.out.println("Conf File Loaded");
				throw new IOException("Required Parameter(s) Not Found");
			}
		} catch (Exception e) {
			logger.error("!!!Exception in Loading Configuration File or Paramter Missing!!!" + e);
			// System.out.println("Exception in Loading Conf File "+e);
			throw new IOException("Exception in Loading Conf File " + e);

		} finally {
			Configuration.clear();
		}
	}
	
	
	

	public String getDbConnString() {
		return dbConnString;
	}

	private void setDbConnString(String dbConnString) {
		this.dbConnString = dbConnString;
	}

	public String getUser() {
		return user;
	}

	private void setUser(String user) {
		this.user = user;
	}

	public String getPwd() {
		return pwd;
	}

	private void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getAppNumber() {
		return appNumber;
	}

	private void setAppNumber(String appNumber) {
		this.appNumber = appNumber;
	}

	public String getServicestatus() {
		return servicestatus;
	}

	private void setServicestatus(String servicestatus) {
		this.servicestatus = servicestatus;
	}

	private String getValue(String Key, boolean flag) throws IOException {
		String value = "";
		if (logger.isDebugEnabled())
			logger.debug("Reading file: " + Key);
		// System.out.println("Reading File: " +Key);
		value = Configuration.getProperty(Key);
		if (value == null)
			value = null;
		value = value.trim();
		if (logger.isDebugEnabled())
			logger.debug("Reading file: " + Key + "=" + value);
		return value;
	}

}
