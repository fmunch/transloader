package com.googlecode.transloader.test.fixture;

public class HiearchyWithFieldsBottom extends HiearchyWithFieldsMiddle {
	private boolean bottomBooleanField;

	public HiearchyWithFieldsBottom(NonCommonJavaObject superClassFieldValue, int intFieldValue,
			String fieldValueForWithStringField, boolean booleanFieldValue) {
		super(superClassFieldValue, intFieldValue, fieldValueForWithStringField);
		bottomBooleanField = booleanFieldValue;
	}
}
