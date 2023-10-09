package com.pmd.telenor;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;
import com.pmd.cmpa.imp.TargetCMOService;

import com.pmd.util.StaticConstant;
import com.pmd.cmpa.telenor.model.*;

public class TelenorService extends TargetCMOService {
	private AuthResponse authResponse;
	private InfoError infoError;
	private ResponseTelenor responseTelenor;

	public AuthResponse getAuthResponse() {
		return authResponse;
	}

	public InfoError getInfoError() {
		return infoError;
	}

	public ResponseTelenor getUserInfo() {
		return responseTelenor;
	}

	public TelenorService() {
		super();
	}

	@Override
	public String callService(String pmdId, String msisdn, String cnic, String transactionID, String param1,
			String param2, String param3, String param4) throws Exception {
		String sql = null;
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

			//////////////////////////// Rest Service Call///////////////////////////
			String url = StaticConstant.TELENORURL;
			URL obj = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			logger.info("Telenor TOKEN: " + StaticConstant.TELENORPASSWORD);
			if (StaticConstant.TELENORPASSWORD.equalsIgnoreCase("") || StaticConstant.TELENORPASSWORD == null) {
				StaticConstant.ISTELENORTOKENREFRESHING = true;
				getTelenorAccessToken();
			}

			while (StaticConstant.ISTELENORTOKENREFRESHING) {
				Thread.sleep(2000);
			}

			// add reuqest header
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setRequestMethod("POST");
			con.setRequestProperty("Authorization", "Bearer " + StaticConstant.TELENORPASSWORD);
			con.setRequestProperty("Accept", "application/json");
			con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

			String urlParameters = "{\"MSISDN\": \"" + "0" + msisdn + "\", \"CNIC\": \"" + cnic
					+ "\", \"RequestID\": \"" + pmdId + "\" }";
			// Send post request
			// System.out.println(urlParameters);
			logger.debug(urlParameters);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			InputStream _is;
			if (con.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
				_is = con.getInputStream();
				responseTelenor = new Gson().fromJson(getResponse(_is), ResponseTelenor.class);
				// SuccessResponse sResp = new SuccessResponse();
				// sResp.setIsSuccessful(true);
//				sResp.setPmdID(pmdId);
//				sResp.setSmscID("TELENOR667");
//				sResp.setMsisdn("0"+msisdn);
//				sResp.setName(responseTelenor.getName().trim());
//				sResp.setCnic(responseTelenor.getCNIC().trim().replace("-", ""));

				setStatus("OK");
				setMessage(responseTelenor.getStatus().trim());
				setCode(responseTelenor.getResponseCode());
				setCodeDisc(responseTelenor.getRequestID());
				String code = "55";
				if (getMessage().equalsIgnoreCase("Yes")) {
					code = "01";
					setMessage("Yes");
				} else if (getMessage().equalsIgnoreCase("No")) {
					code = "02";
					setMessage("No");
				}
				setCode(code);

				sql = "insert into TELENORRESPONSE(PMDID,STATUS,MESSAGE,RESPONSECODE,RESPONSETIME)" + "values ('"
						+ pmdId + "','" + getStatus() + "','" + getMessage() + "','" + getCode() + "',now())";
				logger.info("Telenor Status: " + getStatus() + " Message: " + getMessage() + " Response Code: "
						+ getCode());

//				String imsiTelenor = responseTelenor.getIMSI().trim();
//				logger.error("Telenor Imsi: " + imsiTelenor);
//				if (imsiTelenor.length()>1){
//					sResp.setImsi(imsiTelenor.substring(3, imsiTelenor.length()));
//				}
//				else {
//					sResp.setImsi(imsiTelenor.trim());
//				}
//				sResp.setActivationDate(responseTelenor.getActivationDate());
//				sResp.setConnectionType(responseTelenor.getConnectionType());
//				sResp.setStatusCode(responseTelenor.getRequestID());
//				sResp.setDescription(responseTelenor.getMessage());
//
//				cmoResponse = sResp;
				// System.out.println(userInfo.getConnectionType());
				// sql = "create SQL method";

			} else {
				// MsisdnCnicVerifyServiceServiceagentStub telStub = new
				// MsisdnCnicVerifyServiceServiceagentStub(StaticConstant.TARGETCMOURL);
				// logger.info("Default Connection Time OUT:
				// "+telStub._getServiceClient().getOptions().getTimeOutInMilliSeconds());
				// telStub._getServiceClient().getOptions().setTimeOutInMilliSeconds(StaticConstant.TIMEOUT);
				// MsisdnCnicVerifyRequest msisdnCnicVerifyRequest0 = new
				// MsisdnCnicVerifyRequest();
				_is = con.getErrorStream();
				InfoError infoError = new Gson().fromJson(getResponse(_is), InfoError.class);
				if (infoError.getErrorCode().equalsIgnoreCase("401.027.006")) {
					StaticConstant.ISTELENORTOKENREFRESHING = true;
					getTelenorAccessToken();
					sql = callService(pmdId, msisdn, cnic, transactionID, "", "", "", "");
					return sql;
				}
				if (infoError.getErrorCode().equalsIgnoreCase("400.027.009")) {
					// { "requestId":"2825-1484060-1", "errorCode": "400.019.009", "errorMessage":
					// "Bad Request - Invalid SubscriberNo"
					setStatus(infoError.getRequestId());
					setMessage(infoError.getErrorMessage().trim());
					setCode("55");
					setCodeDisc(infoError.getErrorCode());
				}
//				else {
//				ErrorResponse eResp = new ErrorResponse();
//					eResp.setIsSuccessful(false);
//					eResp.setPmdID(pmdId);
//					eResp.setSmscID("TELENOR667");
//					eResp.setMsisdn("0"+msisdn);
//					eResp.setRequestId(infoError.getRequestId());
//					eResp.setErrorCode(infoError.getErrorCode());
//					eResp.setErrorMessage(infoError.getErrorMessage());
//					cmoResponse = eResp;
//				}
				// System.out.println("Response Error : " + infoErr.getErrorMessage());
				// return response;

				sql = "insert into TELENORRESPONSE(PMDID,STATUS,MESSAGE,RESPONSECODE,RESPONSETIME)" + "values ('"
						+ pmdId + "','" + getStatus() + "','" + getMessage() + "','" + getCode() + "',now())";
				logger.info("Telenor Status: " + getStatus() + " Message: " + getMessage() + " Response Code Internal: "
						+ getCode() + " Response error Code: " + getCodeDisc());

			}

			//////////////////////////// End of Rest Service Call////////////////////
//			RequestHeaderType requestHeaderType = new RequestHeaderType();
//			requestHeaderType.setCorrelationID("1");
//			requestHeaderType.setRequestID(pmdId);
//			requestHeaderType.setSourceSystem("PMD");
//			requestHeaderType.setTimestamp(dateFormat.format(StaticConstant.CALENDAR.getTime()));
//			SecurityType securityType = new SecurityType();
//			securityType.setUser(StaticConstant.TARGETCMOUSER);
//			securityType.setPassword(StaticConstant.TARGETCMOPASSWORD);
//			requestHeaderType.setSecurity(securityType);
//			msisdnCnicVerifyRequest0.setRequestHeader(requestHeaderType);
//			RequestBody_type0 param = new RequestBody_type0();
//			param.setCNIC(cnic);
//			param.setMSISDN("0".concat(msisdn));
//			
//			msisdnCnicVerifyRequest0.setRequestBody(param);
//		
//			MsisdnCnicVerifyResponse res = telStub.msisdnCnicVerifyOperation(msisdnCnicVerifyRequest0);

			//////////
//			ResultBody_type0 result =  res.getResultBody();
//			String returnCode = result.getResponseCode().trim();
//			String code = "55";
//			if (returnCode.equalsIgnoreCase("01"))
//				code = "01";
//			else if (returnCode.equalsIgnoreCase("02"))
//				code = "02";
//				setCode(code);
//			setStatus(result.getStatus().trim());
//			setMessage(result.getMessage().trim());
//			setCode(code);
//			setCodeDisc("");
//			sql = "insert into TELENORRESPONSE(PMDID,STATUS,MESSAGE,RESPONSECODE,RESPONSETIME)" +
//			"values ('"+pmdId+"','"+result.getStatus()+"','"+result.getMessage()+"','"+result.getResponseCode()+
//			"',systimestamp)";
//			logger.info("Telenor Status: " + result.getStatus() +
//								" Message: "+ result.getMessage() +
//								" Response Code: "+ result.getResponseCode());

		} catch (Exception e) {
			logger.error(getClass() + " not reachable" + e);
			throw new Exception(getClass() + " not reachable" + e);
		}
		return sql;
	}

	private String getResponse(InputStream _is) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(_is));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// print result
		// System.out.println(response.toString());
		return response.toString();
	}

	private boolean getTelenorAccessToken() throws Exception {
		boolean token = false;

		String url = "https://apis.telenor.com.pk/oauthtoken/v1/generate?grant_type=client_credentials";
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		// add request header
		// con.setRequestMethod("POST");
		// add reuqest header
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setRequestMethod("POST");
		con.setRequestProperty("Authorization", "Basic " + StaticConstant.TELENORUSER);
		// con.setRequestProperty("Authorization","Bearer "+
		// StaticConstant.TELENORPASSWORD);
		con.setRequestProperty("Accept", "application/json");
		con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes("");
		wr.flush();
		wr.close();
		// StaticConstant.TELENORUSER
		// ="anl4UVY1NmtrdXBSRFJVdnp4UTJOckFBOVlaanl6d0U6cFVMdUh6M0RlWGFna0FDOQ==";
		// String basic = "Basic "+StaticConstant.TELENORUSER;
		// con.setRequestProperty("Accept", "application/json");
		// con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		// con.setRequestProperty("User-Agent", "PostmanRuntime/7.13.0");
		// con.setRequestProperty("Authorization","Basic "+StaticConstant.TELENORUSER);
		// con.setRequestProperty("Content-Length", "113");
		// con.addRequestProperty(arg0, arg1)
		// int responseCode = con.getResponseCode();

		// BufferedReader in = new BufferedReader(
		// new InputStreamReader(con.getInputStream()));
		// String inputLine;
		// StringBuffer response = new StringBuffer();

//		while ((inputLine = in.readLine()) != null) {
//			response.append(inputLine);
//		}
//		in.close();

		// print result
		InputStream _is;
		if (con.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
			_is = con.getInputStream();
			authResponse = new Gson().fromJson(getResponse(_is), AuthResponse.class);
			StaticConstant.TELENORPASSWORD = authResponse.getAccess_token();
			StaticConstant.ISTELENORTOKENREFRESHING = false;
			// System.out.println("Token Received"+authResponse.getAccess_token());
			token = true;

		} else {

			_is = con.getErrorStream();
			infoError = new Gson().fromJson(getResponse(_is), InfoError.class);
			// System.out.println("Response Error : " + infoError.getErrorMessage());
			token = false;

		}

		return token;
	}

	public static void main(String[] args) throws Exception {
		// TelenorService ts = new TelenorService();
		// ts.callService("PMD1232", "3008507410", "1234567891025", "abc", "a", "b",
		// "c","d");
		// ts.callService("PMD123", "3448569910", "3520015596903", "abc", "a", "b", "c",
		// "d");
		// ts.getTelenorAccessToken();
	}

}
