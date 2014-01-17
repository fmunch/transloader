package com.googlecode.transloader.test.fixture;

import com.googlecode.transloader.test.Triangulate;

public class WithNonCommonJavaFields extends NonCommonJavaObject {
	private NonCommonJavaObject object = new WithStringField(Triangulate.anyString());
	private NonCommonJavaType type;

	public WithNonCommonJavaFields(NonCommonJavaType fieldValue) {
		type = fieldValue;
	}
}
