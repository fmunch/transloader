package com.googlecode.transloader.test.fixture;

public interface NonCommonJavaTypeWithMethods extends NonCommonJavaType {
	String getStringField();

	void setStringField(String stringFieldValue);

	String concatenate(NonCommonJavaType first, NonCommonJavaType second);
}
