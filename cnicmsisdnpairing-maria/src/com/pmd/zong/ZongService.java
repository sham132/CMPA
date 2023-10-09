package com.pmd.zong;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.pmd.cmpa.imp.TargetCMOService;
import com.pmd.util.StaticConstant;
import com.pmd.zong.SecpStub.UserVerify;
import com.pmd.zong.SecpStub.UserVerifyResponse;

public class ZongService extends TargetCMOService{
	public ZongService(){
		super();
	}
	@Override
	public String callService(String pmdId, String msisdn,String cnic,String transactionID,
			String param1,String param2,String param3,String param4)throws Exception{
		String sql = null;
		try {
			
			  String soapRequest="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:secp=\"http://www.example.org/secp/\">\n" + 
			  		"   <soapenv:Header/>\n" + 
			  		"   <soapenv:Body>\n" + 
			  		"      <secp:UserVerify>\n" + 
			  		"         <Username>"+StaticConstant.ZONGUSER+"</Username>\n" + 
			  		"         <Password>"+StaticConstant.ZONGPASSWORD+"</Password>\n" + 
			  		"         <MSISDN>"+"0".concat(msisdn)+"</MSISDN>\n" + 
			  		"         <CNIC>"+cnic+"</CNIC>\n" + 
			  		"         <TransactionID>"+pmdId+"</TransactionID>\n" + 
			  		"      </secp:UserVerify>\n" + 
			  		"   </soapenv:Body>\n" + 
			  		"</soapenv:Envelope>";
			  
			  
		           // String responseArray[]=callSoapService(xml);
		          //  System.out.println(responseF);
			
//			SecpStub secpstub = new SecpStub(StaticConstant.ZONGURL);
//			logger.info("Default Connection Time OUT: "+secpstub._getServiceClient().getOptions().getTimeOutInMilliSeconds());
//			//secpstub._getServiceClient().getOptions().setTimeOutInMilliSeconds(StaticConstant.TIMEOUT);
//			UserVerify userVerify = new UserVerify();
//			userVerify.setUsername(StaticConstant.ZONGUSER);
//			userVerify.setPassword(StaticConstant.ZONGPASSWORD);
//			userVerify.setCNIC(cnic);
//			userVerify.setMSISDN("0".concat(msisdn));
//			userVerify.setTransactionID(pmdId);
//			UserVerifyResponse result = secpstub.userVerify(userVerify);
//			String returnCode = ""+result.getMATCH_STATUS();
//			setMessage(result.getResult_Description().trim());
//			setStatus(""+result.getResult_Code());
//			setCodeDisc(""+ result.getMSISDN_STATUS());

		         String url = StaticConstant.ZONGURL; // replace your URL here
		   	     URL obj = new URL(url);
		   	     HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		   	     
		   	     // change these values as per soapui request on top left of request, click on RAW, you will find all the headers
		   	     con.setRequestMethod("POST");
		   	     con.setRequestProperty("Content-Type","text/xml; charset=utf-8"); 
		   	     con.setDoOutput(true);
		   	     DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		   	     wr.writeBytes(soapRequest);
		   	     wr.flush();
		   	     wr.close();
		   	     String responseStatus = con.getResponseMessage();
		   	     System.out.println(responseStatus);
		   	     BufferedReader in = new BufferedReader(new InputStreamReader(
		   	     con.getInputStream()));
		   	     String inputLine;
		   	     StringBuffer response = new StringBuffer();
		   	     while ((inputLine = in.readLine()) != null) {
		   	         response.append(inputLine);
		   	     }
		   	     in.close();
		   	     
		   	     
		   	     UserVerifyResponse result = new UserVerifyResponse();

		   	  
		   	     // You can play with response which is available as string now:
		   	     String finalvalue= response.toString();
		   	     
		   	     String result_code_new= finalvalue.substring(finalvalue.indexOf("<Result_Code>")+13,finalvalue.indexOf("</Result_Code>"));
		  	     System.out.println("result_code_new"+result_code_new);
		  	     
		  	     String result_desc= finalvalue.substring(finalvalue.indexOf("<Result_Description>")+20,finalvalue.indexOf("</Result_Description>"));
		  	     System.out.println("result_desc"+result_desc);
		  	     
		  	     String result_msisdn= finalvalue.substring(finalvalue.indexOf("<MSISDN_STATUS>")+15,finalvalue.indexOf("</MSISDN_STATUS>"));
		  	     System.out.println("result_msisdn"+result_msisdn);
		  	     
		  	     
		  	     String result_match_status= finalvalue.substring(finalvalue.indexOf("<MATCH_STATUS>")+14,finalvalue.indexOf("</MATCH_STATUS>"));
		  	     System.out.println("result_match_status"+result_match_status);
		  	    
			
		   	     result.setMATCH_STATUS(Integer.parseInt(result_match_status));
		   	     result.setMSISDN_STATUS(Integer.parseInt(result_msisdn));
		   	     result.setResult_Code(Integer.parseInt(result_code_new));
		   	     result.setResult_Description(result_desc);
		   	     
		         
		        String returnCode = ""+result.getMATCH_STATUS();
				setMessage(result.getResult_Description().trim());
				setStatus(""+result.getResult_Code());
				setCodeDisc(""+ result.getMSISDN_STATUS());
		     
				
			String code = "55";
			if (result.getResult_Code()== 0 && result.getMSISDN_STATUS()== 0){
				if (returnCode.equalsIgnoreCase("0")){
					code = "01";
					setMessage("Yes");
				}
				else if (returnCode.equalsIgnoreCase("1")){
					code = "02";
					setMessage("No");
				}
			}else {
				setMessage("Unknown Message Received from CMO");
			}
			setCode(code);
			logger.info("Zong Status:" + result.getMATCH_STATUS() +
								" MSISDN STATUS: " + result.getMSISDN_STATUS() +
								" Message Code: " + result.getResult_Code() +
								" Message Description: " + result.getResult_Description()
								);
			sql = "insert into ZONGRESPONSE(PMDID,MATCH_STATUS,MSISDN_STATUS,Result_Code,Result_Description,RESPONSETIME)" +
			"values ('"+pmdId+"','"+result.getMATCH_STATUS()+"','"+result.getMSISDN_STATUS()+"','"+result.getResult_Code()+"','"+result.getResult_Description()+
			"',now())";
			
			
			
		} catch (Exception e) {
			logger.error(getClass()+" not reachable" +e);
			throw new Exception( getClass()+" not reachable" +e);
		}
		return sql;
	}
	
//	   String[] callSoapService(String soapRequest) throws Exception {
//		 String combinevalue [] = {};
//	    try {
//	     String url = "http://192.168.0.46:6000/api/v1/soap/CMPA"; // replace your URL here
//	     URL obj = new URL(url);
//	     HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//	     
//	     // change these values as per soapui request on top left of request, click on RAW, you will find all the headers
//	     con.setRequestMethod("POST");
//	     con.setRequestProperty("Content-Type","text/xml; charset=utf-8"); 
//	     con.setDoOutput(true);
//	     DataOutputStream wr = new DataOutputStream(con.getOutputStream());
//	     wr.writeBytes(soapRequest);
//	     wr.flush();
//	     wr.close();
//	     String responseStatus = con.getResponseMessage();
//	     System.out.println(responseStatus);
//	     BufferedReader in = new BufferedReader(new InputStreamReader(
//	     con.getInputStream()));
//	     String inputLine;
//	     StringBuffer response = new StringBuffer();
//	     while ((inputLine = in.readLine()) != null) {
//	         response.append(inputLine);
//	     }
//	     in.close();
//	     
//	     
//	    
//	     // You can play with response which is available as string now:
//	     String finalvalue= response.toString();
//	     
//	 	
//	     String result_code_new= finalvalue.substring(finalvalue.indexOf("<Result_Code>")+2,finalvalue.indexOf("</Result_Code>"));
//	     System.out.println("result_code_new"+result_code_new);
//	     combinevalue[0]=result_code_new;
//	     
//	     String result_desc= finalvalue.substring(finalvalue.indexOf("<Result_Description>")+30,finalvalue.indexOf("</Result_Description>"));
//	     System.out.println("result_desc"+result_desc);
//	     combinevalue[1]=result_desc;
//	     
//	     String result_msisdn= finalvalue.substring(finalvalue.indexOf("<MSISDN_STATUS>")+2,finalvalue.indexOf("</MSISDN_STATUS>"));
//	     System.out.println("result_msisdn"+result_msisdn);
//	     combinevalue[2]=result_msisdn;
//	     
//	     
//	     String result_match_status= finalvalue.substring(finalvalue.indexOf("<MATCH_STATUS>")+2,finalvalue.indexOf("</MATCH_STATUS>"));
//	     System.out.println("result_match_status"+result_match_status);
//	     combinevalue[3]=result_match_status;
//	
//	     
//	     return combinevalue;
//	     } 
//	    catch (Exception e) {
//	       // return  e.getMessage();
//	    	logger.error(getClass()+  e.getMessage();
//			throw new Exception( getClass()+" not reachable" + e.getMessage());
//		    // return combinevalue;
//	    }   
//	}
	
	
	public static void main(String[] args) {
		//ZongService ws = new ZongService();
	try {
		//String sql = ws.callService("23241","3008507410", "3520015596903", "", "", "", "", "");
	//	System.out.print(sql);
		
		String finalvalue="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" + 
				"   <soapenv:Body>\n" + 
				"      <ns1:UserVerifyResponse xmlns:ns1=\"http://www.example.org/secp\">\n" + 
				"         <Result_Code>000</Result_Code>\n" + 
				"         <Result_Description>MSISDN and CNIC Matched</Result_Description>\n" + 
				"         <MSISDN_STATUS>000</MSISDN_STATUS>\n" + 
				"         <MATCH_STATUS>0000</MATCH_STATUS>\n" + 
				"      </ns1:UserVerifyResponse>\n" + 
				"   </soapenv:Body>\n" + 
				"</soapenv:Envelope>";
		
		 String result_code_new= finalvalue.substring(finalvalue.indexOf("<Result_Code>")+13,finalvalue.indexOf("</Result_Code>"));
  	     System.out.println("result_code_new"+result_code_new);
  	     
  	     String result_desc= finalvalue.substring(finalvalue.indexOf("<Result_Description>")+20,finalvalue.indexOf("</Result_Description>"));
  	     System.out.println("result_desc"+result_desc);
  	     
  	     String result_msisdn= finalvalue.substring(finalvalue.indexOf("<MSISDN_STATUS>")+15,finalvalue.indexOf("</MSISDN_STATUS>"));
  	     System.out.println("result_msisdn"+result_msisdn);
  	     
  	     
  	     String result_match_status= finalvalue.substring(finalvalue.indexOf("<MATCH_STATUS>")+14,finalvalue.indexOf("</MATCH_STATUS>"));
  	     System.out.println("result_match_status"+result_match_status);
  	    
	
	} catch (Exception e) {
		// TODO: handle exception
		System.out.println("Fail"+e);
	}
}
}
