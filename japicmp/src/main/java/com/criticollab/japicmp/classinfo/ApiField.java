package com.criticollab.japicmp.classinfo;

public interface ApiField {
	public String getName() ;

	ApiFieldInfo getFieldInfo();

	String getSignature();

	byte[] getAttribute(String name);

	int getModifiers();

	Object getConstantValue();
}
