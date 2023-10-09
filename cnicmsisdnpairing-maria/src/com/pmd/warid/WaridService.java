package com.pmd.warid;

import com.pmd.cmpa.imp.TargetCMOService;
import com.pmd.util.StaticConstant;
import com.pmd.warid.NadraInfoVerifyWSImplServiceStub.GetCNICInfo;
import com.pmd.warid.NadraInfoVerifyWSImplServiceStub.GetCNICInfoE;
import com.pmd.warid.NadraInfoVerifyWSImplServiceStub.GetCNICInfoResponse;
import com.pmd.warid.NadraInfoVerifyWSImplServiceStub.GetCNICInfoResponseE;


public class WaridService extends TargetCMOService {

	public WaridService() {
		super();
	}
	@Override
	public String callService(String pmdId, String msisdn,String cnic,String transactionID,
			String param1,String param2,String param3,String param4)throws Exception{
		String sql = null;
		try {
			NadraInfoVerifyWSImplServiceStub secpstub = new NadraInfoVerifyWSImplServiceStub(StaticConstant.JAZZWURL);
			logger.info("Default Connection Time OUT: "+secpstub._getServiceClient().getOptions().getTimeOutInMilliSeconds());
			secpstub._getServiceClient().getOptions().setTimeOutInMilliSeconds(StaticConstant.TIMEOUT);

			GetCNICInfoE getCNICInfo0 = new GetCNICInfoE();
			GetCNICInfo cnicInfo = new GetCNICInfo();
			cnicInfo.setUserName(StaticConstant.JAZZWUSER);
			cnicInfo.setPassword(StaticConstant.JAZZWPASSWORD);
			cnicInfo.setCnic(cnic);
			cnicInfo.setSubno("92".concat(msisdn));
			getCNICInfo0.setGetCNICInfo(cnicInfo);
			GetCNICInfoResponseE res = secpstub.getCNICInfo(getCNICInfo0);
			GetCNICInfoResponse result = res.getGetCNICInfoResponse();
			String returnstr = result.get_return();
			
			String code = "55";
			if (returnstr.equalsIgnoreCase("TRUE")){
				code = "01";
				setMessage("Yes");
			}
			else if (returnstr.equalsIgnoreCase("FALSE")){
				code = "02";
				setMessage("No");
			}
			setCode(code);
			logger.info("Warid Response:" + result.get_return());
			sql = "insert into WARIDRESPONSE(PMDID,WRETURN,RESPONSETIME)" +
			"values ('"+pmdId+"','"+result.get_return()+
			"',systimestamp)";
			
		} catch (Exception e) {
			logger.error(getClass()+" not reachable" +e);
			throw new Exception( getClass()+" not reachable" +e);
		}
		return sql;
	}
//	public static void main(String[] args) {
//		WaridService ws = new WaridService();
//		try {
//			String sql = ws.callService("2324","3215560394", "3520015596903", "", "", "", "", "");
//			System.out.print(sql);
//			
//		} catch (Exception e) {
//			// TODO: handle exception
//			System.out.println("Fail");
//		}
//	}

}
