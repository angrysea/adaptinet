package org.adaptinet.sdk.xmltools.xmlconverter;

import java.io.InputStream;
import java.util.Collection;
import org.xml.sax.SAXException;

interface LoaderBase {

	public abstract void xmlLoad(InputStream inputstream) throws Exception,
			SAXException;

	public abstract void xmlLoad(String s) throws Exception, SAXException;

	public abstract ElementType getElementType(String s);

	public abstract Collection<ElementType> getElementTypes();

	public abstract int start();

	public abstract void end();

	public abstract String next(boolean externalizable, boolean cachable);
}
