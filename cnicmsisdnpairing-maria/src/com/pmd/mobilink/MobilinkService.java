package com.pmd.mobilink;

import com.pmd.cmpa.imp.TargetCMOService;
import com.pmd.mobilink.MatchMSISDNAndCNICSOAPStub.CNIC_type1;
import com.pmd.mobilink.MatchMSISDNAndCNICSOAPStub.MSISDN_type1;
import com.pmd.mobilink.MatchMSISDNAndCNICSOAPStub.MatchMSISDNAndCNICIn;
import com.pmd.mobilink.MatchMSISDNAndCNICSOAPStub.MatchMSISDNAndCNICOut;
import com.pmd.mobilink.MatchMSISDNAndCNICSOAPStub.Message_type0;
import com.pmd.mobilink.MatchMSISDNAndCNICSOAPStub.Password_type1;
import com.pmd.mobilink.MatchMSISDNAndCNICSOAPStub.ResponseCode_type1;
import com.pmd.mobilink.MatchMSISDNAndCNICSOAPStub.Status_type0;
import com.pmd.mobilink.MatchMSISDNAndCNICSOAPStub.TransactionID_type1;
import com.pmd.mobilink.MatchMSISDNAndCNICSOAPStub.Username_type1;
import com.pmd.util.StaticConstant;

public class MobilinkService extends TargetCMOService {
	public MobilinkService() {
		super();
	}
	@Override
	public String callService(String pmdId, String msisdn,String cnic,String transactionID,
			String param1,String param2,String param3,String param4)throws Exception{
		String sql = null;
		try {
		MatchMSISDNAndCNICSOAPStub mobilinkStub = new MatchMSISDNAndCNICSOAPStub(StaticConstant.JAZZMURL);
		logger.info("Default Connection Time OUT: "+mobilinkStub._getServiceClient().getOptions().getTimeOutInMilliSeconds());
		mobilinkStub._getServiceClient().getOptions().setTimeOutInMilliSeconds(StaticConstant.TIMEOUT);
		MatchMSISDNAndCNICIn matchMSISDNAndCNICIn0 = new MatchMSISDNAndCNICIn();
		CNIC_type1 cnic1 = new CNIC_type1();
		cnic1.setCNIC_type1(cnic);
		matchMSISDNAndCNICIn0.setCNIC(cnic1);
		MSISDN_type1 msisdn1 = new MSISDN_type1();
		msisdn1.setMSISDN_type1("0".concat(msisdn));
		matchMSISDNAndCNICIn0.setMSISDN(msisdn1);
		TransactionID_type1 transID = new TransactionID_type1();
		transID.setTransactionID_type1(pmdId);
		matchMSISDNAndCNICIn0.setTransactionID(transID);
		Username_type1 userId = new Username_type1();
		userId.setUsername_type1(StaticConstant.JAZZMUSER);
		matchMSISDNAndCNICIn0.setUsername(userId);
		Password_type1 passwd = new Password_type1();
		passwd.setPassword_type1(StaticConstant.JAZZMPASSWORD);
		matchMSISDNAndCNICIn0.setPassword(passwd);
		MatchMSISDNAndCNICOut result = new MatchMSISDNAndCNICOut();
		
		result=mobilinkStub.matchMSISDNAndCNIC(matchMSISDNAndCNICIn0);
		Status_type0 status = result.getStatus();
		setStatus(status.getStatus_type0().trim());
		ResponseCode_type1 resCode = result.getResponseCode();
		String returnCode = resCode.getResponseCode_type1().trim();
		Message_type0 resMessage = result.getMessage();
		String mobMessage = "";
		if (resMessage != null){
			mobMessage = resMessage.getMessage_type0();
			setMessage(mobMessage);
		}
		
		String code = "55";
		if (returnCode.equalsIgnoreCase("01")){
			code = "01";
			setMessage("Yes");
		}
		else if (returnCode.equalsIgnoreCase("02")){
			code = "02";
			setMessage("No");
		}
			setCode(code);
		
		setCode(code);
		setCodeDisc("");
		sql = "insert into MOBILINKRESPONSE(PMDID,STATUS,MESSAGE,RESPONSECODE,RESPONSETIME)" +
		"values ('"+pmdId+"','"+status.getStatus_type0().trim()+"','"+mobMessage+"','"+resCode.getResponseCode_type1().trim()+
		"',now())";
		logger.info("Mobilink Status: " + status.getStatus_type0() +
							" Message: "+ mobMessage +
							" Response Code: "+ resCode.getResponseCode_type1());
	} catch (Exception e) {
		logger.error(getClass()+" not reachable" +e);
		throw new Exception( getClass()+" not reachable" +e);
	}
	return sql;
	}
}
