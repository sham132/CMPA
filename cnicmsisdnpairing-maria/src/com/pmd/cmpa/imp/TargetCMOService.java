package com.pmd.cmpa.imp;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.pmd.util.StaticConstant;

public class TargetCMOService {
	private String status;
	private String message;
	private String code;
	private String codeDisc;
	protected Logger logger = Logger.getLogger(getClass());
	
	public TargetCMOService(){
		PropertyConfigurator.configure(StaticConstant.LOGPROPERTYFILE);
		this.status = "";
		this.message = "";
		this.code = "";
		this.codeDisc = "";
	}
	public String callService(String pmdID, String msisdn,String cnic,String transactionID,
			String param1,String param2,String param3,String param4)throws Exception{
		setMessage("Service failed Target CMO is not reachable");
		return null;
	}
	public String getStatus() {
		return status;
	}

	protected void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	protected void setMessage(String message) {
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	protected void setCode(String code) {
		this.code = code;
	}

	public String getCodeDisc() {
		return codeDisc;
	}

	protected void setCodeDisc(String codeDisc) {
		this.codeDisc = codeDisc;
	}
}
