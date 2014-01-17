package com.googlecode.transloader;

import java.lang.reflect.Method;

/**
 * Describes a method invocation by method name, parameter type names and parameters.
 * 
 * @author Jeremy Wales
 */
public final class InvocationDescription {
	private static final String[] NO_PARAMS = new String[] {};
	private final String methodName;
	private final String[] parameterTypeNames;
	private final Object[] parameters;

	/**
	 * Constructs an <code>InvocationDescription</code> with the given method name.
	 * 
	 * @param methodName the name of a zero-parameter method
	 */
	public InvocationDescription(String methodName) {
		this(methodName, NO_PARAMS, NO_PARAMS);
	}

	/**
	 * Constructs an <code>InvocationDescription</code> with the given method name and parameter.
	 * 
	 * @param methodName the name of a single-parameter method
	 * @param parameter a parameter whose concrete implementation <code>Class</code> has the same name as its type
	 *            declared in the targeted method (therefore cannot be <code>null</code>)
	 */
	public InvocationDescription(String methodName, Object parameter) {
		this(methodName, new Object[] {parameter});
	}

	/**
	 * Constructs an <code>InvocationDescription</code> with the given method name and parameters.
	 * 
	 * @param methodName the name of a multi-parameter method
	 * @param parameters some parameters whose concrete implementation <code>Class</code>es have the same names as
	 *            their types declared in the targeted method (therefore cannot contain <code>null</code>s)
	 */
	public InvocationDescription(String methodName, Object[] parameters) {
		this(methodName, getClasses(parameters), parameters);
	}

	/**
	 * Constructs an <code>InvocationDescription</code> with the given method name, parameter type and parameter.
	 * 
	 * @param methodName the name of a single-parameter method
	 * @param parameterType a <code>Class</code> whose name is the same as the parameter type declared in the method
	 * @param parameter the parameter to the method invocation (can be <code>null</code>)
	 */
	public InvocationDescription(String methodName, Class parameterType, Object parameter) {
		// TODO test InvocationDescription(String methodName, Class parameterType, Object parameter)
		this(methodName, new Class[] {parameterType}, new Object[] {parameter});
	}

	/**
	 * Constructs an <code>InvocationDescription</code> with the given method name, parameter type name and parameter.
	 * 
	 * @param methodName the name of a single-parameter method
	 * @param parameterTypeName the name of the parameter type declared in the method
	 * @param parameter the parameter to the method invocation (can be <code>null</code>)
	 */
	public InvocationDescription(String methodName, String parameterTypeName, Object parameter) {
		// TODO test InvocationDescription(String methodName, String parameterTypeName, Object parameter)
		this(methodName, new String[] {parameterTypeName}, new Object[] {parameter});
	}

	/**
	 * Constructs an <code>InvocationDescription</code> with the given method name, parameter types and parameters.
	 * 
	 * @param methodName the name of a multi-parameter method
	 * @param parameterTypes some <code>Class</code>es whose names are the same as the parameter types declared in
	 *            the targeted method
	 * @param parameters the parameters to the method invocation (cannot be but can <i>contain</i> <code>null</code>)
	 */
	public InvocationDescription(String methodName, Class[] parameterTypes, Object[] parameters) {
		this(methodName, getNames(parameterTypes), parameters);
	}

	/**
	 * Constructs an <code>InvocationDescription</code> with the given method name, parameter type name and parameter.
	 * 
	 * @param methodName the name of a single-parameter method
	 * @param parameterTypeNames the names of the parameter types declared in the method (cannot be or contain
	 *            <code>null</code>)
	 * @param parameters the parameters to the method invocation (cannot be but can <i>contain</i> <code>null</code>)
	 */
	public InvocationDescription(String methodName, String[] parameterTypeNames, Object[] parameters) {
		Assert.areNotNull(methodName, parameters);
		Assert.areNotNull(parameterTypeNames);
		// TODO test different number of params to paramTypes
		Assert.areSameLength(parameterTypeNames, parameters);
		this.methodName = methodName;
		this.parameterTypeNames = parameterTypeNames;
		this.parameters = parameters;
	}

	/**
	 * Constructs an <code>InvocationDescription</code> with the given {@link Method} and parameters.
	 * 
	 * @param method the <code>Method</code> to invoke
	 * @param parameters the parameters to the method invocation (can actually be <code>null</code>)
	 */
	public InvocationDescription(Method method, Object[] parameters) {
		this(((Method) Assert.isNotNull(method)).getName(), method.getParameterTypes(), parameters == null ? NO_PARAMS
				: parameters);
	}

	private static Class[] getClasses(Object[] objects) {
		Assert.areNotNull(objects);
		Class[] classes = new Class[objects.length];
		for (int i = 0; i < objects.length; i++) {
			classes[i] = objects[i].getClass();
		}
		return classes;
	}

	private static String[] getNames(Class[] classes) {
		Assert.areNotNull(classes);
		String[] names = new String[classes.length];
		for (int i = 0; i < classes.length; i++) {
			names[i] = classes[i].getName();
		}
		return names;
	}

	/**
	 * Gets the name of the method to be invoked.
	 * 
	 * @return the method name
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * Gets the names of the parameter types of the method to be invoked.
	 * 
	 * @return the parameter type names
	 */
	public String[] getParameterTypeNames() {
		return parameterTypeNames;
	}

	/**
	 * Gets the parameters to be passed to the method to be invoked.
	 * 
	 * @return the parameters
	 */
	public Object[] getParameters() {
		return parameters;
	}
}
