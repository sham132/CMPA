
/**
 * MsisdnCnicVerifyResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package com.pmd.telenor;

public class MsisdnCnicVerifyResponse extends java.lang.Exception{

    private static final long serialVersionUID = 1476859756402L;
    
    private com.pmd.telenor.MsisdnCnicVerifyServiceServiceagentStub.MsisdnCnicVerifyResponse faultMessage;

    
        public MsisdnCnicVerifyResponse() {
            super("MsisdnCnicVerifyResponse");
        }

        public MsisdnCnicVerifyResponse(java.lang.String s) {
           super(s);
        }

        public MsisdnCnicVerifyResponse(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public MsisdnCnicVerifyResponse(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(com.pmd.telenor.MsisdnCnicVerifyServiceServiceagentStub.MsisdnCnicVerifyResponse msg){
       faultMessage = msg;
    }
    
    public com.pmd.telenor.MsisdnCnicVerifyServiceServiceagentStub.MsisdnCnicVerifyResponse getFaultMessage(){
       return faultMessage;
    }
}
    