package com.googlecode.transloader.test.fixture;

import com.googlecode.transloader.test.Triangulate;

public class SerializableWithFinalFields extends Serializable {
	private final String string = Triangulate.anyString();
	private final int intField;

	public SerializableWithFinalFields(Integer integer) {
		intField = integer.intValue();
	}
}
