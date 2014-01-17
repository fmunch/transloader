package com.googlecode.transloader.test.fixture;

import com.googlecode.transloader.test.ClassLoaderAndFieldsStringBuilder;

public class NonCommonJavaObject implements NonCommonJavaType {
	public String toString() {
		return ClassLoaderAndFieldsStringBuilder.toString(this);
	}
}
