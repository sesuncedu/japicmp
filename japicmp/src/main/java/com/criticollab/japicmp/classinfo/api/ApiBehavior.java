package com.criticollab.japicmp.classinfo.api;

public interface ApiBehavior {
	String getLongName();

	String getName();

	String getSignature();

	ApiMethodInfo getMethodInfo();

	byte[] getAttribute(String tag);

	int getModifiers();

	ClassApiSignature getDeclaringClass();
}
