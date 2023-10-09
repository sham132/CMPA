package com.pmd.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.pmd.util.LoadProperties;
import com.pmd.util.StaticConstant;

public class DaoCrm {

	public DaoCrm() throws Exception {

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (Exception e) {
			logger.info("Oracle drivers loading Fail:" + e);
			throw new IOException("Oracle drivers loading Fail:" + e);
		}

		setDbConnection();

	}

	public Connection crmConn = null;
	Logger logger = Logger.getLogger(getClass());

	public void setDbConnection() throws SQLException {
		try {
			Properties Configuration = new Properties();

			Configuration.load(new FileInputStream(new File(StaticConstant.CRMCONFIGURATIONFILE)));
			String conn = Configuration.getProperty("crmdb");
			logger.info(conn);
			String user = Configuration.getProperty("crmuser");
			String pwd = Configuration.getProperty("crmpwd");
			crmConn = DriverManager.getConnection(conn, user, pwd);
		} catch (Exception e) {
			logger.error("!!!!!!! CRM DB Connection Fail !!!!!!" + e);
			throw new SQLException("!!!!!!! CRM DB Connection Fail !!!!!!" + e);
		}
	}

	public Boolean allowtoHit(String userID, String operator) throws SQLException {
		Boolean isAllowtoHit = false;
		String sql = "select * From POC_SALES_SERVICE where REMAINING_HITS >0 and username ='" + userID + "'";
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = crmConn.createStatement();
			logger.info(sql);
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				isAllowtoHit = true;
			}

		} catch (SQLException e) {
			logger.error("allowtoHit Fails : " + e);
			throw new SQLException(getClass() + "allowtoHit Fails :" + e);
		} finally {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
		}
		return isAllowtoHit;
	}

	public Boolean checkAccountStatus(String userID) throws SQLException {
		Boolean isAccountEnabled = false;
		String sql = "select STATUS From POC_SALES_SERVICE WHERE username='" + userID + "'";
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = crmConn.createStatement();
			logger.info(sql);
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				if (rs.getString("STATUS") != null && rs.getString("STATUS").equalsIgnoreCase("ENABLE")) {
					isAccountEnabled = true;
				}
			}

		} catch (SQLException e) {
			logger.error("checkAccountStatus Fails : " + e);
			throw new SQLException(getClass() + "checkAccountStatus Fails :" + e);
		} finally {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
		}
		return isAccountEnabled;
	}

	public Boolean updateBalance(String userID) throws Exception {
		Boolean isUpdateBalance = false;
		String sql = "update POC_SALES_SERVICE set REMAINING_HITS=REMAINING_HITS -1 where username ='" + userID + "'";
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = crmConn.createStatement();
			int rows = stmt.executeUpdate(sql);
			logger.info("Package_Remaining_Hits decrement by 1 and rows return: " + rows);
			isUpdateBalance = true;
		} catch (Exception e) {
			logger.error(getClass() + "updateBalance :" + e);
			throw new SQLException(getClass() + "updateBalance :" + e);
		} finally {

			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
		}

		return isUpdateBalance;
	}

	public int getRemainingHits(String userID) throws SQLException {

		String sql = "select REMAINING_HITS From POC_SALES_SERVICE WHERE username='" + userID + "'";
		Statement stmt = null;
		ResultSet rs = null;
		int REMAINING_HITS = 0;
		try {
			stmt = crmConn.createStatement();
			logger.info(sql);
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				if (rs.getInt("REMAINING_HITS") > 0) {
					REMAINING_HITS = rs.getInt("REMAINING_HITS");
				}
			}

		} catch (SQLException e) {
			logger.error("REMAINING_HITS Fails : " + e);
			throw new SQLException(getClass() + "REMAINING_HITS Fails :" + e);
		} finally {
			if (rs != null)
				rs.close();
		}
		return REMAINING_HITS;
	}

	public String getPackageInfo(String userID) throws SQLException {

		String sql = "select POC_PACKAGE_NAME From POC_SALES_SERVICE WHERE username='" + userID + "'";
		Statement stmt = null;
		ResultSet rs = null;
		String POC_PACKAGE_NAME = null; // Initialize with null
		try {
			stmt = crmConn.createStatement();
			logger.info(sql);
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				POC_PACKAGE_NAME = rs.getString("POC_PACKAGE_NAME"); // Retrieve the string value
			}

		} catch (SQLException e) {
			logger.error("POC_PACKAGE_NAME Fails : " + e);
			throw new SQLException(getClass() + "POC_PACKAGE_NAME Fails :" + e);
		} finally {
			if (rs != null)
				rs.close();
		}
		return POC_PACKAGE_NAME;
	}

	

}
