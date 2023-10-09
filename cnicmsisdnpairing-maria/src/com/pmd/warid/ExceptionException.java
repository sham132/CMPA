
/**
 * ExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package com.pmd.warid;

public class ExceptionException extends java.lang.Exception{

    private static final long serialVersionUID = 1478168855237L;
    
    private com.pmd.warid.NadraInfoVerifyWSImplServiceStub.ExceptionE faultMessage;

    
        public ExceptionException() {
            super("ExceptionException");
        }

        public ExceptionException(java.lang.String s) {
           super(s);
        }

        public ExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public ExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(com.pmd.warid.NadraInfoVerifyWSImplServiceStub.ExceptionE msg){
       faultMessage = msg;
    }
    
    public com.pmd.warid.NadraInfoVerifyWSImplServiceStub.ExceptionE getFaultMessage(){
       return faultMessage;
    }
}
    