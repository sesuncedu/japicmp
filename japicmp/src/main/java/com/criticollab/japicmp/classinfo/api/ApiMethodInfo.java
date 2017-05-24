package com.criticollab.japicmp.classinfo.api;

import java.util.List;

public interface ApiMethodInfo {
	ApiExceptionsAttribute getExceptionsAttribute();

	int getLineNumber(int i);

	ApiAttributeInfo getAttribute(String tag);

	List<ApiAttributeInfo> getAttributes();
}
