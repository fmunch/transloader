package com.googlecode.transloader.test.fixture;

public class HiearchyWithFieldsMiddle extends WithNonCommonJavaFields {
	private int middleIntField;
	private WithStringField middleFieldWithStringField;

	public HiearchyWithFieldsMiddle(NonCommonJavaObject superClassFieldValue, int intFieldValue,
			String fieldValueForWithStringField) {
		super(superClassFieldValue);
		middleIntField = intFieldValue;
		middleFieldWithStringField = new WithStringField(fieldValueForWithStringField);
	}
}
