package com.googlecode.transloader.clone.reflect;

/**
 * A strategy interface for customising how normal objects (as opposed to arrays) are instantiated by
 * {@link ReflectionCloningStrategy}.
 * 
 * @author Jeremy Wales
 */
public interface InstantiationStrategy {
	/**
	 * Creates a new instance of the given type.
	 * 
	 * @param type the type of which to make a new instance
	 * @return the new instance of the given <code>type</code>
	 * @throws Exception can throw any <code>Exception</code> depending on the implementation
	 */
	Object newInstance(Class type) throws Exception;
}
