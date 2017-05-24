package com.criticollab.japicmp.classinfo.api;

import javassist.bytecode.annotation.MemberValue;

import java.util.Set;

public interface ApiAnnotation {
	String getTypeName();

	Set<String> getMemberNames();

	MemberValue getMemberValue(String memberName);
}
