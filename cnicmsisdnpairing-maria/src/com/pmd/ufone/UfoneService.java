package com.pmd.ufone;

import com.pmd.cmpa.imp.TargetCMOService;
import com.pmd.ufone.NadraCnicVerificationStub.SecurityParamsType;
import com.pmd.ufone.NadraCnicVerificationStub.Verification;
import com.pmd.ufone.NadraCnicVerificationStub.VerificationResponse;
import com.pmd.util.StaticConstant;

public class UfoneService extends TargetCMOService {
	public UfoneService() {
		super();
	}

	@Override
	public String callService(String pmdId, String msisdn, String cnic, String transactionID, String param1,
			String param2, String param3, String param4) throws Exception {
		String sql = null;
		try {
			String id = "763".concat(pmdId.substring(3));
			NadraCnicVerificationStub ufoneStub = new NadraCnicVerificationStub(StaticConstant.UFONEURL);
			logger.info("Default Connection Time OUT: "
					+ ufoneStub._getServiceClient().getOptions().getTimeOutInMilliSeconds());
			ufoneStub._getServiceClient().getOptions().setTimeOutInMilliSeconds(StaticConstant.TIMEOUT);
			Verification verification2 = new Verification();
			verification2.setCNIC(cnic);
			verification2.setMSISDN("92".concat(msisdn));
			verification2.setRequestID(id);
			SecurityParamsType userDetails = new SecurityParamsType();
			userDetails.setUsername(StaticConstant.UFONEUSER);
			userDetails.setPassword(StaticConstant.UFONEPASSWORD);
			verification2.setSecurityParams(userDetails);

			VerificationResponse result = ufoneStub.verification(verification2);
			setStatus(result.getTransactionID());
			setMessage(result.getReturnDesc());
			String returnCode = result.getReturnCode().trim();
			String code = "55";
			if (returnCode.equalsIgnoreCase("0")) {
				code = "01";
				setMessage("Yes");
			} else if (returnCode.equalsIgnoreCase("1")) {
				code = "02";
				setMessage("No");
			}
			setCode(code);
			setCodeDisc(result.getRequestID());
			sql = "insert into UFONERESPONSE(PMDID,RequestID,TransactionID_STATUS,ReturnCode,ReturnDesc,RESPONSETIME)"
					+ "values ('" + pmdId + "','" + result.getRequestID() + "','" + result.getTransactionID() + "','"
					+ result.getReturnCode() + "','" + result.getReturnDesc() + "',now())";
			logger.info("Ufone Request ID: " + result.getRequestID() + " Transaction ID: " + result.getTransactionID()
					+ " Code: " + result.getReturnCode() + " Description: " + result.getReturnDesc());
		} catch (Exception e) {
			logger.error(getClass() + " not reachable" + e);
			throw new Exception(getClass() + " not reachable" + e);
		}
		return sql;
	}

}
