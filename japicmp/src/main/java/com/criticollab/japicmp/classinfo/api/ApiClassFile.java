package com.criticollab.japicmp.classinfo.api;

import java.util.List;

public interface ApiClassFile {
	ApiAttributeInfo getAttribute(String name);

	List<ApiAttributeInfo> getAttributes();
}
