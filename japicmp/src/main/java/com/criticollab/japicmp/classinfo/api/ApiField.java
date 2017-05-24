package com.criticollab.japicmp.classinfo.api;

public interface ApiField {
	String getName() ;

	ApiFieldInfo getFieldInfo();

	String getSignature();

	byte[] getAttribute(String name);

	int getModifiers();

	Object getConstantValue();

	ClassApiSignature getDeclaringClass();
}
