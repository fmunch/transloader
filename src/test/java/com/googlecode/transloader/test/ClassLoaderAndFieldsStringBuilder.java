package com.googlecode.transloader.test;

import java.lang.reflect.Array;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;

import com.googlecode.transloader.clone.reflect.CyclicReferenceSafeTraverser;
import com.googlecode.transloader.clone.reflect.FieldDescription;
import com.googlecode.transloader.clone.reflect.FieldReflector;
import com.googlecode.transloader.clone.reflect.CyclicReferenceSafeTraverser.Traversal;

public class ClassLoaderAndFieldsStringBuilder {
	private static final String FIELD_SEPERATOR = " ";
	private static final String OPEN_BRACKET = "[" + FIELD_SEPERATOR;
	private static final String CLOSE_BRACKET = "]";
	private static final CyclicReferenceSafeTraverser CYCLIC_REFERENCE_TRAVERSER = new CyclicReferenceSafeTraverser();

	public static String toString(Object object) {
		StringBuffer buffer = new StringBuffer();
		append(buffer, object);
		return buffer.toString();
	}

	private static void append(final StringBuffer buffer, final Object object) {
		Traversal toStringTraversal = new Traversal() {
			public Object traverse(Object currentObject, Map referenceHistory) throws Exception {
				Class objectClass = object.getClass();
				String className = getName(objectClass);
				referenceHistory.put(currentObject, className + "<circular reference>");
				appendClassAndClassLoader(buffer, objectClass);
				appendFields(object, buffer);
				return buffer.toString();
			}
		};
		try {
			CYCLIC_REFERENCE_TRAVERSER.performWithoutFollowingCircles(toStringTraversal, object);
		} catch (Exception e) {
			throw new NestableRuntimeException(e);
		}
	}

	private static String getName(Class clazz) {
		return ClassUtils.getShortClassName(clazz);
	}

	private static void appendFields(Object object, StringBuffer buffer) {
		buffer.append(OPEN_BRACKET);
		FieldReflector reflector = new FieldReflector(object);
		FieldDescription[] fieldDescriptions = reflector.getAllInstanceFieldDescriptions();
		for (int i = 0; i < fieldDescriptions.length; i++) {
			FieldDescription description = fieldDescriptions[i];
			try {
				buffer.append(description.getFieldName()).append("=");
				Object fieldValue = reflector.getValue(description);
				if (fieldValue == null) {
					buffer.append("null");
				} else if (fieldValue.getClass().isArray()) {
					appendArray(buffer, fieldValue);
				} else {
					appendValue(buffer, fieldValue, description.isPrimitive());
				}
				buffer.append(FIELD_SEPERATOR);
			} catch (Exception e) {
				throw new NestableRuntimeException(e);
			}
		}
		buffer.append(CLOSE_BRACKET);
	}

	private static void appendValue(StringBuffer buffer, Object fieldValue, boolean primitive) {
		if (fieldValue == null) {
			buffer.append("null");
		} else if (primitive || fieldBasedStringIsNotDeterministic(fieldValue)) {
			buffer.append(fieldValue);
		} else {
			append(buffer, fieldValue);
		}
	}

	private static boolean fieldBasedStringIsNotDeterministic(Object fieldValue) {
		return fieldValue instanceof String || fieldValue instanceof Map || fieldValue instanceof Set;
	}

	private static void appendArray(StringBuffer buffer, Object array) {
		Class arrayClass = array.getClass();
		appendClassAndClassLoader(buffer, arrayClass);
		buffer.append(OPEN_BRACKET);
		for (int i = 0; i < Array.getLength(array); i++) {
			appendValue(buffer, Array.get(array, i), arrayClass.getComponentType().isPrimitive());
			buffer.append(FIELD_SEPERATOR);
		}
		buffer.append(CLOSE_BRACKET);
	}

	private static void appendClassAndClassLoader(StringBuffer toStringBuffer, Class clazz) {
		toStringBuffer.append(getName(clazz)).append("(").append(clazz.getClassLoader()).append(")");
	}
}
