package org.adaptinet.sdk.xmltools.parser;

import org.adaptinet.sdk.xmltoolsex.ParserException;

public interface DeclHandler {

	public abstract void elementDecl(String name, String model)
			throws ParserException;

	public abstract void attributeDecl(String eName, String aName, String type,
			String valueDefault, String value) throws ParserException;

	public abstract void internalEntityDecl(String name, String value)
			throws ParserException;

	public abstract void externalEntityDecl(String name, String publicId,
			String systemId) throws ParserException;
}
