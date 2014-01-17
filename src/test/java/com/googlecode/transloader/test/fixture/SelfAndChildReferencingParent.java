package com.googlecode.transloader.test.fixture;

public class SelfAndChildReferencingParent extends WithStringField {
	private Object self = this;
	private Object child;

	public SelfAndChildReferencingParent(String stringFieldValue) {
		super(stringFieldValue);
	}

	void setChild(Object child) {
		this.child = child;
	}
}
