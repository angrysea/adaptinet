package org.adaptinet.sdk.scripting;

public interface ScriptValue {

	public void setName(String name);

	public String getName();

	public Object getObject();

	public void setObject(Object value);

	public int compareTo(Object s);

	public boolean equals(ScriptValue s);
}