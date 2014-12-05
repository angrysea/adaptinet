package org.adaptinet.sdk.processoragent;

import org.adaptinet.sdk.loader.ClasspathLoader;
import org.adaptinet.sdk.messaging.Envelope;
import org.adaptinet.sdk.registry.ProcessorEntry;

public abstract class ProcessorBase {
	
	protected String name = null;

	protected ClasspathLoader loader = null;

	@SuppressWarnings("unused")
	private ProcessorAgent agent = null;

	public ProcessorBase() {
	}

	public void init(ClasspathLoader loader, ProcessorAgent agent) {
		this.agent = agent;
		this.loader = loader;
	}

	public void startProcessor(ProcessorEntry entry) throws Exception {
		throw new Exception("MethodNotSupported");
	}

	public void cleanupProcessor() {
	}

	public abstract boolean preProcessMessage(Envelope env);

	public abstract Object process(Envelope env) throws Exception;

	public abstract Object execute(String methodName, Object request) throws Exception;
}
