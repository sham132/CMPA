package com.pmd.util;

import java.util.Calendar;

public class StaticConstant {
	//static public String LOGPROPERTYFILE = "C:/PMD/Development/eclipsMSFService/CnicMobileNumberPairing/WebContent/WEB-INF/conf/log4j-CnicMsisdnPairing.properties"; 
	//static public String DBCONFIGURATIONFILE = "C:/PMD/Development/eclipsMSFService/CnicMobileNumberPairing/WebContent/WEB-INF/conf/DBConnection.properties";

	static public String TARGETCMOURL="";
	static public String TARGETCMOUSER="";
	static public String TARGETCMOPASSWORD="";
	static public String AppNumber="02";
	//static public String LOGPROPERTYFILE = null; 
	//static public String DBCONFIGURATIONFILE = null;
	static public String DBCONNURL = null;
	static public String DBUSER = null;
	static public String DBPASSWORD = null;
	static public String DBSCHEMA = "pmddb1";
	
	 static public String LOGPROPERTYFILE ="D://workspace 2//cnicmsisdnpairing-maria/WebContent/WEB-INF/conf/log4j-CMPairing.properties"; 
	 static public String DBCONFIGURATIONFILE ="D://workspace 2//cnicmsisdnpairing-maria/WebContent/WEB-INF/conf/DBConnection.properties";
	 static public String CRMCONFIGURATIONFILE="D://workspace 2//cnicmsisdnpairing-maria/WebContent/WEB-INF/conf/CRM.properties";
	
//	static public String LOGPROPERTYFILE = "/opt/tomcat/webapps/CnicMobileNumberPairing/WEB-INF/conf/log4j-CMPairing.properties";
//	static public String DBCONFIGURATIONFILE = "/opt/tomcat/webapps/CnicMobileNumberPairing/WEB-INF/conf/DBConnection.properties";
//	static public String CRMCONFIGURATIONFILE = "/opt/tomcat/webapps/CnicMobileNumberPairing/WEB-INF/conf/CRM.properties";
	static public Long TIMEOUT = (long) 15000;
	static public String DATEIME = null;
	static public Calendar CALENDAR = null;
	static public String JAZZWURL="http://172.21.100.98:7002/NadraInfoVerifyWS/NadraInfoVerifyWSImplService";
	static public String JAZZWUSER="Nadra";
	static public String JAZZWPASSWORD="n@dr@";
	static public String JAZZMURL="";
	static public String JAZZMUSER="";
	static public String JAZZMPASSWORD="";
	static public String ZONGURL="http://172.20.51.82:28080/cmpa_inter/services/cmpa";
	static public String ZONGUSER="PMDCMPA";
	static public String ZONGPASSWORD="Cmpa_2019";
	static public String UFONEURL="";
	static public String UFONEUSER="";
	static public String UFONEPASSWORD="";
	static public String TELENORURL="https://apis.telenor.com.pk/pairing/v1/query";
	static public String TELENORUSER="anl4UVY1NmtrdXBSRFJVdnp4UTJOckFBOVlaanl6d0U6cFVMdUh6M0RlWGFna0FDOQ==";
	static public String TELENORPASSWORD="";
	static public Boolean ISTELENORTOKENREFRESHING=false;
	static public String PortedNumberURL="http://192.168.0.191:8001/api/v1/rest/InternalLookup";
	
	
}
