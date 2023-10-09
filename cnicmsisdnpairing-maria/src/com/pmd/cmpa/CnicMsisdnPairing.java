package com.pmd.cmpa;

import com.pmd.cmpa.imp.CnicMsisdnPairingImp;
import com.pmd.object.Request;
import com.pmd.object.Response;


public class CnicMsisdnPairing {
	
	public Response verify (String userName, 
							String passwd, 
							Request request) {

		CnicMsisdnPairingImp cmpImp = new CnicMsisdnPairingImp();
		return cmpImp.verify(userName, passwd, request);
		
	}
}