package com.pmd.object;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "Request")
@XmlType(propOrder = { "msisdn", "cnic", "transactionID" })
public class Request implements Serializable {
	private static final long serialVersionUID = -1129402159048345205L;
	private String msisdn;
	private String cnic;
	private String transactionID;

	public Request() {
		this.msisdn = "";
		this.cnic = "";
		this.transactionID = "";
	}

	public Request(final String msisdn, final String cnic, final String transactionID, final String param1,
			final String param2, final String param3, final String param4) {
		this.msisdn = msisdn;
		this.cnic = cnic;
		this.transactionID = transactionID;
	}

	@XmlElement(name = "Msisdn")
	public String getMsisdn() {
		return this.msisdn;
	}

	public void setMsisdn(final String msisdn) {
		this.msisdn = msisdn;
	}

	public String getCnic() {
		return this.cnic;
	}

	public void setCnic(final String cnic) {
		this.cnic = cnic;
	}

	public String getTransactionID() {
		return this.transactionID;
	}

	public void setTransactionID(final String transectionID) {
		this.transactionID = transectionID;
	}
}