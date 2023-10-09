package com.pmd.dao;

public class DaoBilling extends DaoMaria {

	public DaoBilling() throws Exception {
		super("billing");
		setDbConnection();
	}
	public Boolean allowtoHit(String userID, String operator){
		Boolean isAllowtoHit = false;
		
		return isAllowtoHit;
	}
	private int currentBalance(String userID){
		int balance = 0;
		String sql = "";
		return balance;
	}
	private Boolean updateBalance(String userID){
		return false;
	}
}
