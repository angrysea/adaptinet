package com.db.instrument.client;

import org.adaptinet.sdk.requestclient.XMLClient;

public class Client {

	static String requestBegin = "<?xml><GetInstitutionRequest></GetInstitutionRequest>";
	
	public static void main(String[] args) {
		
		XMLClient client = new XMLClient();
		client.setTrx("Institution/GetInstitutions");
		for(int i = 0; i < 1; i++) {
			try {
				String ret = client.doTransmit(requestBegin);// + Integer.toString(i+1)+requestEnd);
				System.out.println(ret);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
