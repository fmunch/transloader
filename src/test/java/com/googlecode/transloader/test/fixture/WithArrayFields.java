package com.googlecode.transloader.test.fixture;

import com.googlecode.transloader.test.Triangulate;

public class WithArrayFields extends NonCommonJavaObject {
	private int[] ints = {Triangulate.anyInt(), Triangulate.anyInt()};
	private Object[] objects = {Triangulate.anyString(), new WithPrimitiveFields(), Triangulate.anyString()};
	private boolean[] noBooleans = {};
	private NonCommonJavaObject[] nonCommonJavaObjects = {new WithStringField(Triangulate.anyString())};
	private NonCommonJavaType[] nonCommonJavaTypes = {new WithPrimitiveFields()};
}
