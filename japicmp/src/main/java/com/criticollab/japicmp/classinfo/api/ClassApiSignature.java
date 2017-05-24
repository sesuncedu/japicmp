package com.criticollab.japicmp.classinfo.api;

import java.util.NoSuchElementException;

public interface ClassApiSignature {
	ApiField getDeclaredField(String fieldName);

	ApiClassFile getClassFile();

	byte[] getAttribute(String name);

	String getName();

	boolean isAnnotation();

	boolean isEnum();

	boolean isInterface();

	ApiField[] getDeclaredFields();

	ApiMethod[] getDeclaredMethods();

	ApiConstructor[] getDeclaredConstructors();

	ClassApiSignature getSuperclass() throws NoSuchElementException;

	int getModifiers();

	ClassApiSignature[] getInterfaces();

	ClassApiSignature getDeclaringClass() throws ClassNotFoundException;

	String getPackageName();

	ClassApiSignatureSource getClassPool();

	boolean subtypeOf(ClassApiSignature superType);

	boolean subclassOf(ClassApiSignature superClass);

}
