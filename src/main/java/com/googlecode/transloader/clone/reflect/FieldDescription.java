package com.googlecode.transloader.clone.reflect;

import com.googlecode.transloader.Assert;

/**
 * Describes a field by its name, declaring class name and whether or not it is it of primitive type.
 * 
 * @author Jeremy Wales
 */
public final class FieldDescription {
	private final String declaringClassName;
	private final String fieldName;
	private final boolean primitive;

	/**
	 * Constructs a <code>FieldDescription</code> with the given declaring <code>Class</code>, field name and
	 * declared field type.
	 * 
	 * @param declaringClass the <code>Class</code> that declares the field
	 * @param fieldName the name of the field
	 * @param declaredType the declared type of the field
	 */
	public FieldDescription(Class declaringClass, String fieldName, Class declaredType) {
		Assert.areNotNull(declaringClass, fieldName, declaredType);
		this.declaringClassName = declaringClass.getName();
		this.fieldName = fieldName;
		this.primitive = declaredType.isPrimitive();
	}

	/**
	 * Gets the name of the <code>Class</code> that declares the field.
	 * 
	 * @return the declaring class name
	 */
	public String getDeclaringClassName() {
		return declaringClassName;
	}

	/**
	 * Gets the name of the field.
	 * 
	 * @return the field name
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * Indicates whether or not the declared type of the field is primitive.
	 * 
	 * @return <code>true</code> if the field is primitive
	 */
	public boolean isPrimitive() {
		return primitive;
	}
}
