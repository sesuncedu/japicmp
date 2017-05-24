package com.criticollab.japicmp.classinfo.api;

import java.util.List;

public interface ApiFieldInfo {
	ApiAttributeInfo getAttribute(String tag);

	List<ApiAttributeInfo> getAttributes();
}
