package com.pmd.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DaoNpc extends Dao {

	public DaoNpc() throws Exception {
		super("npc");
		setDbConnection();
	}
	public String checkPorted(String msisdn) throws Exception{
		String operator = null;
		Statement stmt=null;
		ResultSet rs=null;
		try{
			stmt = dbConn.createStatement();
			//String sql = "Select CURRENT_OWNER from npcdb.xnp_ported_numbers where trim(NUMBER_FROM) = '"+msisdn+"'";
			String sql = "Select CURRENT_OWNER from npcdb.xnp_ported_numbers where NUMBER_FROM = '"+msisdn+"'";
			rs = stmt.executeQuery(sql);
			if (rs.next()){
				operator = rs.getString("CURRENT_OWNER");
				return operator;
			}
		}catch (Exception e) {
			logger.error("" +e);
			throw new SQLException("" +e);
		}
		finally{
			if (rs!=null)
				rs.close();
			if (stmt!=null)	
				stmt.close();
			closeDbConn();		
		}
		return operator;
	}
}
