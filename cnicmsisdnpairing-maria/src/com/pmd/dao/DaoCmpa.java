package com.pmd.dao;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.pmd.dao.DaoCrm;

import oracle.jdbc.OracleTypes;

import com.pmd.util.StaticConstant;

public class DaoCmpa extends DaoMaria {

	public DaoCmpa() throws Exception {
		super("cmpa");
		setDbConnection();
		logger.info("Connection with CMPA Database Established!");
	}

	public Boolean verifyUser(String userName, String password) throws Exception {
		PreparedStatement stmt = null;
		// Statement stmt = null;
		ResultSet rs = null;
		try {
			// String sql = "select PASSWORD from USERS where USERNAME=? and status =?";
			String sql = "select PASSWORD from CMPA.USERS where USERNAME='" + userName.trim() + "'";
			// String sql = "select * from USERS";
			// stmt = dbConn.createStatement();
			stmt = dbConn.prepareStatement(sql);
			// stmt.setString(1, userName);
			// stmt.setInt(2, 1);

			// rs = stmt.executeQuery(sql);
			rs = stmt.executeQuery();

			logger.debug(sql);
			if (rs.next()) {
				String storePasswd = rs.getString("PASSWORD");
				if (password.equals(storePasswd))
					return true;
			}
			return false;
		} catch (Exception e) {
			logger.error(getClass() + "User Verification Fail" + e);
			throw new SQLException("User Verification Fail" + e);
		} finally {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
		}
	}

	public String cmpaHitDuplicateCheck(String userName, String password, String msisdn, String cnic,
			String transactionID, String param1, String param2, String param3, String param4, String format)
			throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		Boolean isDuplicate = false;
		String id = null;
		try {
			stmt = dbConn.createStatement();
			userName = userName.trim();
			String fTC = userName.charAt(0) + "" + userName.charAt(1) + "" + userName.charAt(2);
			transactionID = transactionID.trim();
			String sql = "SELECT TRANSACTIONID FROM CMPAHITS WHERE TRANSACTIONID = '" + fTC + transactionID + "'";
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				isDuplicate = true;
				format = "DUPTRANSID";
			}

			String queryMariaSelect = "SELECT AUTO_INCREMENT AS ID FROM  INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'CMPA' AND   TABLE_NAME   = 'CMPAHITS';";

			logger.debug(queryMariaSelect);

			try {

				rs1 = stmt.executeQuery(queryMariaSelect);

				// id = csMaria.getString(8);
				while (rs1.next()) {
					id = rs1.getString("ID");
				}
				logger.info("Auto ID from Database Maria:" + id);
			} catch (SQLException e) {
				id = null;
				logger.error("ERROR" + "CMPA HITS Failed");
			}

		} catch (Exception e) {
			logger.info(getClass() + "CMPA HITS Failed" + e);
			throw new SQLException(getClass() + "CMPA HITS Failed" + e);
		} finally {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
		}
		if (!isDuplicate)
			return id;
		else
			return null;
	}

	public void cmpaHitDbLog(String userName, String password, String msisdn, String cnic, String transactionID,
			String param1, String param2, String param3, String param4, String format) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = dbConn.createStatement();
			userName = userName.trim();
			String fTC = userName.charAt(0) + "" + userName.charAt(1) + "" + userName.charAt(2);
			transactionID = transactionID.trim();

			DaoCrm daoCrm = new DaoCrm();

			int getRHits = daoCrm.getRemainingHits(userName);

			logger.info("Remaining Hits for the Client of " + userName + "  is " + getRHits);

			String queryMaria = "INSERT INTO CMPA.CMPAHITS (USERNAME,MSISDN,CNIC,TRANSACTIONID,REMAINING_HITS,PARAM1,PARAM2,PARAM3,PARAM4,FORMAT)"
					+ " values ('" + userName + "','" + msisdn + "','" + cnic + "','" + fTC + transactionID + "','"
					+ getRHits + "','" + param1 + "','" + param2 + "','" + param3 + "','" + param4 + "','" + format
					+ "');";

			logger.debug(queryMaria);

			try {

				rs = stmt.executeQuery(queryMaria);

			} catch (SQLException e) {

				logger.error("ERROR" + "CMPA HITS Failed");
			}

		} catch (Exception e) {
			logger.info(getClass() + "CMPA HITS Failed" + e);
			throw new SQLException(getClass() + "CMPA HITS Failed" + e);
		} finally {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
		}

	}

	public Boolean cmpaInsertTans(String userName, String pmdId, String msisdn, String cnic, String transactionID,
			String param1, String param2, String param3, String param4) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		Boolean isInserted = false;
		try {
			stmt = dbConn.createStatement();
			String fTC = userName.charAt(0) + "" + userName.charAt(1) + "" + userName.charAt(2);
			DaoCrm daoCrm = new DaoCrm();

			String getPInfo = daoCrm.getPackageInfo(userName);

			transactionID = fTC + transactionID;

			String sql = "insert into CMPATRANSACTION(PMDID,USERNAME,MSISDN,CNIC,TRANSACTIONID,PACKAGE,PARAM1,PARAM2,PARAM3,PARAM4,REQUESTTIME)"
					+ "values ('" + pmdId + "','" + userName + "','" + msisdn + "','" + cnic + "','" + transactionID
					+ "','" + getPInfo + "','" + param1 + "','" + param2 + "','" + param3 + "','" + param4 + "',now())";

			logger.debug(sql);
			int rows = stmt.executeUpdate(sql);
			logger.info("CMPATRANSACTION Row(s) inserted :" + rows);
			isInserted = true;
		} catch (Exception e) {
			logger.error(getClass() + "CMPA Transaction Failed" + e);
			throw new SQLException(getClass() + "CMPA Transaction Failed" + e);
		} finally {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
		}
		return isInserted;
	}

	public Boolean cmpaInsertCMOResponse(String sql) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		Boolean isInserted = false;
		try {
			stmt = dbConn.createStatement();
			int rows = stmt.executeUpdate(sql);
			logger.info("Inserted CMO Response Row(s) inserted :" + rows);
			isInserted = true;
		} catch (Exception e) {
			logger.error(getClass() + "CMPA Insert CMO Response Failed" + e);
			throw new SQLException(getClass() + "CMPA Insert CMO Response Failed" + e);
		} finally {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
		}
		return isInserted;
	}

	public int checkZongMSISDN(String pmdId) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		int result = 1;
		try {
			stmt = dbConn.createStatement();

			String sql = "select MSISDN_STATUS from ZONGRESPONSE where PMDID='" + pmdId + "'";
			logger.debug(sql);
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				String valueOfMSISDN = rs.getString("MSISDN_STATUS");
				logger.info("Zong MSISDN STATUS:" + rs.getString("MSISDN_STATUS"));
				if (valueOfMSISDN != "0") {
					result = 0;
				}
			}
		} catch (Exception e) {
			logger.error(getClass() + "Zong MSISDN Fetch failed" + e);
			throw new SQLException(getClass() + "Zong MSISDN Fetch failed" + e);
		} finally {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
		}
		return result;
	}

	public String CheckRedisFunction(String username, String msisdn, String cnic, String requestTime) throws Exception {
		String operator = null;
		String rscode = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = dbConn.createStatement();
			// String sql = "Select CURRENT_OWNER from npcdb.xnp_ported_numbers where
			// trim(NUMBER_FROM) = '"+msisdn+"'";
			String sql = "Select MESSAGE,RESPONSECODE from CMPA.CMPATRANSACTION where username = '" + username
					+ "' AND msisdn = '" + msisdn + "' AND cnic = '" + cnic + "' AND requesttime> '" + requestTime
					+ "' LIMIT 1";
			System.out.println(sql);
			rs = stmt.executeQuery(sql);

			if (rs.next()) {
				operator = rs.getString("MESSAGE");
				rscode = rs.getString("RESPONSECODE");
				return operator + "-" + rscode;
			}
		} catch (Exception e) {
			logger.error("" + e);
			throw new SQLException("" + e);
		} finally {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();

		}
		return operator;
	}

	public Boolean cmpaUpdateTans(String pmdId, String status, String message, String responseCode, String resp1,
			String resp2, String resp3, String resp4, String completed) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		Boolean isInserted = false;
		try {
			stmt = dbConn.createStatement();

			String sql = "UPDATE CMPATRANSACTION set STATUS ='" + status + "',MESSAGE='" + message + "',RESPONSECODE='"
					+ responseCode + "',RESP1='" + resp1 + "',RESP2='" + resp2 + "',RESP3='" + resp3 + "',RESP4='"
					+ resp4 + "',COMPLETED='" + completed +
					// "',RESPONSETIME=systimestamp " +
					"',RESPONSETIME=now() " + "Where PMDID='" + pmdId + "'";
			logger.debug(sql);
			int rows = stmt.executeUpdate(sql);
			logger.info("CMPATRANSACTION Row(s) Updated :" + rows);
			isInserted = true;
		} catch (Exception e) {
			logger.error(getClass() + "CMPA Transaction Response Update Failed" + e);
			throw new SQLException(getClass() + "CMPA Transaction Response Update Failed" + e);
		} finally {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
		}
		return isInserted;
	}

	public Boolean loadCMOsCredentionals(String cmo) throws SQLException {
		Boolean completed = false;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = dbConn.createStatement();
			String sql = "SELECT CMO,URL,USERS,PASSWD FROM CONFCMPA where CMO='" + cmo.toUpperCase() + "'";
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				logger.info("\n Target CMO information Loaded (URL...,UserName....,Password...)");
				String CMO = rs.getString("CMO") == null ? "" : rs.getString("CMO").trim();
				if (CMO != null && CMO.equalsIgnoreCase(cmo)) {
					StaticConstant.TARGETCMOURL = rs.getString("URL") == null ? "" : rs.getString("URL").trim();
					StaticConstant.TARGETCMOUSER = rs.getString("USERS") == null ? "" : rs.getString("USERS").trim();
					StaticConstant.TARGETCMOPASSWORD = rs.getString("passwd") == null ? ""
							: rs.getString("passwd").trim();
					logger.debug("User Data:" + StaticConstant.TARGETCMOUSER + "--" + StaticConstant.TARGETCMOPASSWORD);
					completed = true;
				}
			}

		} catch (SQLException e) {
			logger.error("Unable to retrive Target CMO Information from CMPA DB........\n" + e);
			throw new SQLException(getClass() + "CMPA Target CMO Information FAIL" + e);
		} finally {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
		}
		return completed;
	}

	public Boolean loadCMOsCredentionals() throws SQLException {
		Boolean completed = false;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = dbConn.createStatement();
			String sql = "SELECT CMO,URL,USERS,PASSWD FROM CONFCMPA";
			logger.info(sql);
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				logger.info("\n Target CMOs information Loaded (URL...,UserName....,Password...)");
				String CMO = rs.getString("CMO") == null ? "" : rs.getString("CMO").trim();
				if (CMO != null && CMO.equalsIgnoreCase("JAZZM")) {
					StaticConstant.JAZZMURL = rs.getString("URL") == null ? "" : rs.getString("URL").trim();
					StaticConstant.JAZZMUSER = rs.getString("USERS") == null ? "" : rs.getString("USERS").trim();
					StaticConstant.JAZZMPASSWORD = rs.getString("passwd") == null ? "" : rs.getString("passwd").trim();
					logger.debug("User Data:" + StaticConstant.JAZZMUSER + "--" + StaticConstant.JAZZMPASSWORD);
					logger.info("URL : " + StaticConstant.JAZZMURL);
					completed = true;
				} else if (CMO != null && CMO.equalsIgnoreCase("JAZZW")) {
					/*
					 * StaticConstant.JAZZWURL = rs.getString("URL")==null ? "" :
					 * rs.getString("URL").trim(); StaticConstant.JAZZWUSER =
					 * rs.getString("USERS")==null ? "" : rs.getString("USERS").trim();
					 * StaticConstant.JAZZWPASSWORD = rs.getString("passwd")==null ? "" :
					 * rs.getString("passwd").trim();
					 * logger.debug("User Data:"+StaticConstant.JAZZWUSER+"--"+StaticConstant.
					 * JAZZWPASSWORD); logger.debug("URL : "+StaticConstant.JAZZWURL );
					 */
					StaticConstant.JAZZMURL = rs.getString("URL") == null ? "" : rs.getString("URL").trim();
					StaticConstant.JAZZMUSER = rs.getString("USERS") == null ? "" : rs.getString("USERS").trim();
					StaticConstant.JAZZMPASSWORD = rs.getString("passwd") == null ? "" : rs.getString("passwd").trim();
					logger.debug("User Data:" + StaticConstant.JAZZMUSER + "--" + StaticConstant.JAZZMPASSWORD);
					logger.info("URL : " + StaticConstant.JAZZMURL);
					completed = true;
				} else if (CMO != null && CMO.equalsIgnoreCase("TELENOR")) {
					StaticConstant.TELENORURL = rs.getString("URL") == null ? "" : rs.getString("URL").trim();
					StaticConstant.TELENORUSER = rs.getString("USERS") == null ? "" : rs.getString("USERS").trim();
					StaticConstant.TELENORPASSWORD = rs.getString("passwd") == null ? ""
							: rs.getString("passwd").trim();
					logger.debug("User Data:" + StaticConstant.TELENORUSER + "--" + StaticConstant.TELENORPASSWORD);
					logger.info("URL : " + StaticConstant.TELENORURL);
					completed = true;
				} else if (CMO != null && CMO.equalsIgnoreCase("ZONG")) {
					StaticConstant.ZONGURL = rs.getString("URL") == null ? "" : rs.getString("URL").trim();
					StaticConstant.ZONGUSER = rs.getString("USERS") == null ? "" : rs.getString("USERS").trim();
					StaticConstant.ZONGPASSWORD = rs.getString("passwd") == null ? "" : rs.getString("passwd").trim();
					logger.debug("User Data:" + StaticConstant.ZONGUSER + "--" + StaticConstant.ZONGPASSWORD);
					logger.info("URL : " + StaticConstant.ZONGURL);
					completed = true;
				} else if (CMO != null && CMO.equalsIgnoreCase("UFONE")) {
					StaticConstant.UFONEURL = rs.getString("URL") == null ? "" : rs.getString("URL").trim();
					StaticConstant.UFONEUSER = rs.getString("USERS") == null ? "" : rs.getString("USERS").trim();
					StaticConstant.UFONEPASSWORD = rs.getString("passwd") == null ? "" : rs.getString("passwd").trim();
					logger.debug("User Data:" + StaticConstant.UFONEUSER + "--" + StaticConstant.UFONEPASSWORD);
					logger.info("URL : " + StaticConstant.UFONEURL);
					completed = true;
				}
			}

		} catch (SQLException e) {
			logger.error("Unable to retrive Target CMO Information from SMS667 DB........\n" + e);
			throw new SQLException(getClass() + "SMS667 Target CMO Information FAIL" + e);
		} finally {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
		}
		return completed;
	}

}
