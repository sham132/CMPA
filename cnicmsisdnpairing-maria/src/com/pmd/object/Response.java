package com.pmd.object;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="Response")
@XmlType(propOrder={"message","status","responseCode"})//,"resp1","resp2","resp3","resp4"})
public class Response implements Serializable {
	private static final long serialVersionUID = -1129402159048345204L;
	private String status;
	private String message;
	private String responseCode;
//	private String resp1;
//	private String resp2;
//	private String resp3;
//	private String resp4;
	public Response (){	
		this.status = "";
		this.message = "";
		this.responseCode = "";
//		this.resp1 = "";
//		this.resp2 = "";
//		this.resp3 = "";
//		this.resp4 = "";
	}
	
	public Response (String status, String message, String responseCode,String resp1, String resp2, String resp3, String resp4){
		this.status = status;
		this.message = message;
		this.responseCode = responseCode;
//		this.resp1 = resp1;
//		this.resp2 = resp2;
//		this.resp3 = resp3;
//		this.resp4 = resp4;
	}
	@XmlElement(name="Status")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
//	public String getResp1() {
//		return resp1;
//	}
//	public void setResp1(String resp1) {
//		this.resp1 = resp1;
//	}
//	public String getResp2() {
//		return resp2;
//	}
//	public void setResp2(String resp2) {
//		this.resp2 = resp2;
//	}
//	public String getResp3() {
//		return resp3;
//	}
//	public void setResp3(String resp3) {
//		this.resp3 = resp3;
//	}
//	public String getResp4() {
//		return resp4;
//	}
//	public void setResp4(String resp4) {
//		this.resp4 = resp4;
//	}
		
}
