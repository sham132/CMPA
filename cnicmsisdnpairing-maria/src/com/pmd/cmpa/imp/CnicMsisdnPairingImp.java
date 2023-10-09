package com.pmd.cmpa.imp;

import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import org.apache.axis2.context.MessageContext;
import org.apache.log4j.Logger;

import com.pmd.dao.DaoCmpa;

import java.util.Calendar;
import com.pmd.dao.DaoCrm;
import com.pmd.mobilink.MobilinkService;
import com.pmd.object.Request;
import com.pmd.object.Response;
import com.pmd.telenor.TelenorService;
import com.pmd.ufone.UfoneService;
import com.pmd.util.StaticConstant;
import com.pmd.zong.ZongService;

import PortedNumber.PortedNumber;

public class CnicMsisdnPairingImp {

	private Logger logger = Logger.getLogger(getClass());
	private DaoCmpa cmpadb = null;
	private DaoCrm crmdb = null;

	public CnicMsisdnPairingImp() {
		// super();
		// TODO Auto-generated constructor stub
	}

	public Response verify(String userName, String password, Request request) {

		// PropertyConfigurator.configure(StaticConstant.LOGPROPERTYFILE);
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		StaticConstant.CALENDAR = Calendar.getInstance();
		logger.info(
				"Request Received from::" + userName + " at " + (dateFormat.format(StaticConstant.CALENDAR.getTime())));

		String msisdn = request.getMsisdn().trim();
		String cnic = request.getCnic().trim();
		String transactionID = request.getTransactionID().trim();
		String param1 = "";// request.getParam1().trim();
		String param2 = "";// request.getParam2().trim();
		String param3 = "";// request.getParam3().trim();
		String param4 = "";// request.getParam4().trim();

		logger.info("MSISDN:" + msisdn + " CNIC:" + cnic + " TransectionID:" + transactionID);
		// logger.info("PARAM1:"+param1+" PARAM2:"+param2+ " PARAM3:"+param3+"
		// PARAM4:"+param4);
		String status = "00";
		String message = "";
		String responseCode = "";
		String resp1 = "";
		String resp2 = "";
		String resp3 = "";
		String resp4 = "";

		//////////////////////

		logger.info(getClientIP());
		// request.getHeader("url");
		// String ipAddress = request.getHeader("X-FORWARDED-FOR");
		// if (ipAddress == null) {
		// ipAddress = request.getRemoteAddr();
		// }

		////////////////////

		Response res = null;
		TargetCMOService targetCMOService = null;
		DaoCmpa cmpadb = null;
		try {
			cmpadb = new DaoCmpa();
			crmdb = new DaoCrm();

			String operator = null;
			if (userName == null || password == null || cnic == null || cnic.length() != 13 || !cnic.matches("^[0-9]+$")
					|| cnic.length() != 13 || msisdn.length() != 10 || !msisdn.matches("^[0-9]+$")
					|| !userName.matches("^[a-zA-Z0-9]{3,50}$")) {
				// cmpadb.cmpaHitDbLog(userName, password, msisdn, cnic, transactionID, param1,
				// param2, param3, param4, "FError");
				logger.error("Some mandatory information is missing/incorrect");
				responseCode = "06";
				message = "Some mandatory information is missing/incorrect";
				res = setResponse(status, message, responseCode, resp1, resp2, resp3, resp4);
				return res;
			} else if (cmpadb.verifyUser(userName, password)) {
				logger.info("Legitimate User");

				boolean isAccountOpen = crmdb.checkAccountStatus(userName);
				if (isAccountOpen == false) {
					logger.info("***** ACCOUNT IS DISABALED  *****");
					message = "Service Not Reachable";
					status = "02";
					res = setResponse(status, message, responseCode, resp1, resp2, resp3, resp4);

					return res;
				}
				/*
				 * operator = checkNumber(msisdn); if (operator==null){ message =
				 * "Invalid Number"; responseCode = "07"; res = setResponse(status, message,
				 * responseCode, resp1, resp2, resp3, resp4); return res; }
				 */

				try {

					Calendar calender = Calendar.getInstance();

					calender.add(Calendar.HOUR_OF_DAY, -1);

					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

					String lastHourString = sdf.format(calender.getTime());

					String checkRedisData = cmpadb.CheckRedisFunction(userName, msisdn, cnic, lastHourString);

					if (checkRedisData != null) {
						String[] RedisData = checkRedisData.split("-");

						if (RedisData[0].equalsIgnoreCase("yes") || RedisData[0].equalsIgnoreCase("no")) {
							System.out.println(status);
							System.out.println(RedisData[0]);
							System.out.println(RedisData[0]);
							System.out.println(responseCode);

							cmpadb.cmpaHitDbLog(userName, password, msisdn, cnic, transactionID, operator, param2,
									param3, param4, "REJECT");

							res = setResponse("09", "Duplicate Transaction", "99", resp1, resp2, resp3, resp4);

							return res;
						}

					}

				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				String id = null;

				try {

					String cmo = PortedNumber.callService(msisdn);
					operator = cmo == null ? operator : cmo;
					if (operator != null && operator.equalsIgnoreCase("PAKTEL")) {
						operator = "ZONG";
					}
					if (operator != null && operator.equalsIgnoreCase("JAZZM")) {
						operator = "MOBILINK";
					}
					if (operator != null && operator.equalsIgnoreCase("JAZZW")) {
						operator = "WARID";
					}

					if (cmo == null) {
						operator = checkNumber(msisdn);
						if (operator == null) {
							message = "Invalid Number";
							responseCode = "07";
							res = setResponse(status, message, responseCode, resp1, resp2, resp3, resp4);
							return res;
						}
					}

				} catch (Exception e) {
					cmpadb.cmpaHitDbLog(userName, password, msisdn, cnic, transactionID, param1, param2, param3, param4,
							"NPCError");
					logger.info("Ported Number Script Issue Maybe not Running");
					throw new Exception("NPC Not Accessible::" + "::" + e);
				}
				id = cmpadb.cmpaHitDuplicateCheck(userName, password, msisdn, cnic, transactionID, operator, param2,
						param3, param4, "FCORRECT");

				if (id == null) {
					message = "Duplicate Transaction ID";
					responseCode = "08";
					res = setResponse(status, message, responseCode, resp1, resp2, resp3, resp4);
					return res;
				}
				if (operator.equalsIgnoreCase("Mobilink") || operator.equalsIgnoreCase("JAZZW")
						|| operator.equalsIgnoreCase("JAZZM")) {
					System.out.println("***** Going to Call JAZZ  *****");
					targetCMOService = new MobilinkService();
				} else if (operator.equalsIgnoreCase("Zong")) {
					System.out.println("***** Going to Call ZONG  *****");
					targetCMOService = new ZongService();
				} else if (operator.equalsIgnoreCase("Warid")) {
					System.out.println("***** Going to Call WARID  *****");
					targetCMOService = new MobilinkService();
				} else if (operator.equalsIgnoreCase("Ufone")) {
					System.out.println("***** Going to Call Ufone  *****");
					targetCMOService = new UfoneService();
				} else if (operator.equalsIgnoreCase("Telenor")) {
					System.out.println("***** Going to Call Telenor  *****");
					targetCMOService = new TelenorService();

				}
				operator = operator.toUpperCase();
				String pmdId = getPMDId(id);

				try {
					if (cmpadb.getAppStatus().equalsIgnoreCase("Production") && (crmdb.allowtoHit(userName, operator))
							&& isAccountOpen) {
						cmpadb.loadCMOsCredentionals();
						cmpadb.cmpaInsertTans(userName, pmdId, msisdn, cnic, transactionID, operator, param2, param3,
								param4);
						logger.info("Going to Call ** " + operator + " Service::"
								+ (dateFormat.format(StaticConstant.CALENDAR.getTime())));
						// logger.info("\nURL:"+StaticConstant.TARGETCMOURL);
						String sql = targetCMOService.callService(pmdId, msisdn, cnic, transactionID, param1, param2,
								param3, param4);
						logger.info("Response Received from **" + operator + " Service::"
								+ (dateFormat.format(StaticConstant.CALENDAR.getTime())));
						String targetCMOMessage = targetCMOService.getMessage();

						if (targetCMOMessage.length() != 0)
							targetCMOMessage = ":".concat(targetCMOMessage);
						message = message + targetCMOMessage;
						responseCode = targetCMOService.getCode();
						message = targetCMOService.getMessage();
//						cmpadb.cmpaUpdateTans(pmdId, status, message, responseCode, operator, resp2, resp3, resp4,
//								"YES");

						if (sql != null) {
							cmpadb.cmpaInsertCMOResponse(sql);

							if (operator.equalsIgnoreCase("zong")) {
								if (message.equalsIgnoreCase("YES") || message.equalsIgnoreCase("NO")) {

									Boolean statuss = crmdb.updateBalance(userName);

									logger.info("Update Balance for the " + userName + ": " + statuss);

									if (statuss == true) {

										Boolean transSattus = cmpadb.cmpaUpdateTans(pmdId, status, message,
												responseCode, operator, resp2, resp3, resp4, "YES");

										if (transSattus == true) {
											cmpadb.cmpaHitDbLog(userName, password, msisdn, cnic, transactionID,
													operator, param2, param3, param4, "FCORRECT");
										}

									}

								} else {
									logger.info("Zong MSISDN is not Active");
								}
							} else if (operator.equalsIgnoreCase("Ufone")) {
								if (message.equalsIgnoreCase("YES") || message.equalsIgnoreCase("NO")) {

									Boolean statuss = crmdb.updateBalance(userName);

									logger.info("Update Balance for the " + userName + ": " + statuss);

									if (statuss == true) {

										Boolean transSattus = cmpadb.cmpaUpdateTans(pmdId, status, message,
												responseCode, operator, resp2, resp3, resp4, "YES");

										if (transSattus == true) {
											cmpadb.cmpaHitDbLog(userName, password, msisdn, cnic, transactionID,
													operator, param2, param3, param4, "FCORRECT");
										}

									}

								} else {
									logger.info("Ufone MSISDN is not Active");
								}

							} else if (operator.equalsIgnoreCase("Telenor")) {
								if (message.equalsIgnoreCase("YES") || message.equalsIgnoreCase("NO")) {

									Boolean statuss = crmdb.updateBalance(userName);

									logger.info("Update Balance for the " + userName + ": " + statuss);

									if (statuss == true) {

										Boolean transSattus = cmpadb.cmpaUpdateTans(pmdId, status, message,
												responseCode, operator, resp2, resp3, resp4, "YES");

										if (transSattus == true) {
											cmpadb.cmpaHitDbLog(userName, password, msisdn, cnic, transactionID,
													operator, param2, param3, param4, "FCORRECT");
										}

									}

								} else {
									logger.info("Telenor MSISDN is not Active");
								}
							} else if (operator.equalsIgnoreCase("Mobilink") || operator.equalsIgnoreCase("JAZZ")
									|| operator.equalsIgnoreCase("JAZZM") || operator.equalsIgnoreCase("JAZZW")
									|| operator.equalsIgnoreCase("WARID")) {

								if (message.equalsIgnoreCase("YES") || message.equalsIgnoreCase("NO")) {

									Boolean statuss = crmdb.updateBalance(userName);

									logger.info("Update Balance for the " + userName + ": " + statuss);

									if (statuss == true) {

										Boolean transSattus = cmpadb.cmpaUpdateTans(pmdId, status, message,
												responseCode, operator, resp2, resp3, resp4, "YES");

										if (transSattus == true) {
											cmpadb.cmpaHitDbLog(userName, password, msisdn, cnic, transactionID,
													operator, param2, param3, param4, "FCORRECT");
										}

									}

								} else {
									logger.info("Mobilink MSISDN is not Active");
								}
							} else {
								logger.info("Porting Status didn;t point");
							}

						}
					} else if (cmpadb.getAppStatus().equalsIgnoreCase("development")) {
						logger.info(
								"*******Development Application******* 3504567890123--3452734454 or 5104567890123 -- 3215560394");
						logger.info("PMD ID : " + pmdId + " ,MSISDN Received : " + msisdn + ",CNIC Received : " + cnic
								+ "Transaction ID Received" + transactionID);
						if (cnic.equalsIgnoreCase("3660128315969") && msisdn.equalsIgnoreCase("3333323466")) {
							message = "Yes";
							responseCode = "01";
						} else {
							message = "No";
							responseCode = "02";

						}
					} else if (cmpadb.getAppStatus().isEmpty()) {
						logger.info("please set the servicestatus=development/staging/production");
					}

					else {
						logger.info("***** OUT OF BALANCE *****");
						message = "Out of Balance";
						responseCode = "66";
						res = setResponse(status, message, responseCode, resp1, resp2, resp3, resp4);
					}

				} catch (Exception e) {
					message = "Service failed Target CMO is not reachable";
					responseCode = "04";
					cmpadb.cmpaUpdateTans(pmdId, status, message, responseCode, operator, resp2, resp3, resp4, "ERROR");
					logger.error("*********" + message + "--" + responseCode + "*********");
				}

			} // end Password Correct
			else {
				message = "User Name OR Password incorrect";
				responseCode = "03";
			}
		} catch (Exception e) {
			System.out.println("EXCEPTION " + e.getMessage());
			logger.error("Connection Fail." + e);
			status = "99";
			message = "PMD Internal Issue";
		} finally {
			if (cmpadb != null) {
				cmpadb.closeDbConn();
			}
			logger.info("PMD Response Returned :" + (dateFormat.format(StaticConstant.CALENDAR.getTime())));
			res = setResponse(status, message, responseCode, resp1, resp2, resp3, resp4);
		}
		return res;

	}

	public String getPMDId(String id) throws InterruptedException, UnknownHostException {

		// Thread.sleep(200);

		Random rand = new Random();

		int rand_int1 = 100 + rand.nextInt(99);

		SimpleDateFormat formatter = new SimpleDateFormat("yyMMddhhmmssSSS");
		String dateString = formatter.format(new java.util.Date());

		logger.info(StaticConstant.AppNumber + rand_int1 + dateString);

		return StaticConstant.AppNumber + rand_int1 + dateString;
	}

	private String checkNumber(String msisdn) {
		String message = null;
		String firstThreeChar = msisdn.charAt(0) + "" + msisdn.charAt(1);
		if (firstThreeChar.equalsIgnoreCase("30"))
			message = "Mobilink";
		else if (firstThreeChar.equalsIgnoreCase("31"))
			message = "Zong";
		else if (firstThreeChar.equalsIgnoreCase("32"))
			message = "Warid";
		else if (firstThreeChar.equalsIgnoreCase("33"))
			message = "Ufone";
		else if (firstThreeChar.equalsIgnoreCase("34"))
			message = "Telenor";
		return message;
	}

	private Response setResponse(String status, String message, String responseCode, String resp1, String resp2,
			String resp3, String resp4) {
		Response res = new Response();// (status,message,responseCode,resp1,resp2,resp3,resp4);
		res.setStatus(status);
		res.setMessage(message);
		res.setResponseCode(responseCode);
		logger.info("Status :" + res.getStatus() + " ResponseCode :" + res.getResponseCode() + " Message :"
				+ res.getMessage());
		// logger.info("Resp1 :" + res.getResp1()+" Resp2 :" + res.getResp2()+" Resp3 :"
		// + res.getResp3()+" Resp4 :" + res.getResp4());
		res.setStatus(status);
		res.setMessage(message);
		res.setResponseCode(responseCode);
		return res;
	}

	private String getClientIP() {

//		request.getHeader("url");
		// String ipAddress = request.getHeader("X-FORWARDED-FOR");
		// if (ipAddress == null) {
		// ipAddress = request.getRemoteAddr();
		// }
		return (String) (MessageContext.getCurrentMessageContext()).getProperty(MessageContext.REMOTE_ADDR);

	}

//	public static void main (String arg[]) {

//	CnicMsisdnPairingImp cmpImp = new CnicMsisdnPairingImp();
//	Request request = new Request("3057510868", "3630255436169", "98763", "", "", "", "");//Zong
//	//Request request = new Request("3458265912", "4210192524789", "98739", "", "", "", "");//Telenor
//	//Request request = new Request("3345553336", "3520015596903", "98749", "", "", "", "");//Ufone
//	Response res = cmpImp.verify("MOBILINK", "MB0u3287", request);
//	String message = res.getMessage()+"---"+res.getResponseCode()+"---"+res.getStatus()
//	+"---"+res.getResp1()+"---"+res.getResp2()+"---"+res.getResp3()+"---"+res.getResp4();
//	System.out.println(message);
//
//}

}// End of Class
