package org.adaptinet.sdk.processoragent;

public abstract class SystemProcessor extends ProcessorBase {

	public Object execute(String methodName, Object request) throws Exception {
		throw new Exception("Execute not implemented");
	}
}
