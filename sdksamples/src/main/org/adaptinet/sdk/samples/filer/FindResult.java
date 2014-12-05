package org.adaptinet.sdk.samples.filer;

import org.adaptinet.sdk.messaging.*;

public class FindResult extends Object {
	String fileName = null;

	long length = 0;

	long lastModified = 0;

	Address address = null;

	int packetsRequested = 0;

	public FindResult(String fileName, long length, long lastModified,
			Address address) {
		this.fileName = fileName;
		this.length = length;
		this.lastModified = lastModified;
		this.address = address;
	}

	public String getFileName() {
		return fileName;
	}

	public Long getLength() {
		return new Long(length);
	}

	public Long getLastModified() {
		return new Long(lastModified);
	}

	public Address getAddress() {
		return address;
	}

	public int getPacketsRequested() {
		return packetsRequested;
	}

	public void addPacketsRequested() {
		packetsRequested++;
	}
}