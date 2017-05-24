package com.criticollab.japicmp.classinfo.api;

import java.io.InputStream;

public interface ClassApiSignatureSource {
	void appendSystemPath();

	void appendClassPath(String classPathEntry);

	ClassApiSignature makeClass(InputStream inputStream) throws Exception;

	ClassApiSignature get(String className) throws ClassNotFoundException;


}
