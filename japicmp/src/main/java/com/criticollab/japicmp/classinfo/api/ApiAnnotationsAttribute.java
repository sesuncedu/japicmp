package com.criticollab.japicmp.classinfo.api;

public interface ApiAnnotationsAttribute extends ApiAttributeInfo{
	String visibleTag = "RuntimeVisibleAnnotations";

	ApiAnnotation[] getAnnotations();
}
