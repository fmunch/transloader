package com.googlecode.transloader;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ClassUtils;

/**
 * The wrapper appropriate for wrapping around all <code>Class</code>es from potentially foreign
 * <code>ClassLoader</code>s.
 * 
 * @author Jeremy Wales
 */
public final class ClassWrapper {
	private final Class wrappedClass;

	/**
	 * Constructs a new <code>ClassWrapper</code> around the given <code>Class</code>. Note that using
	 * implementation of {@link Transloader} is the recommended way to produce these.
	 * 
	 * @param classToWrap the <code>Class</code> to wrap
	 */
	public ClassWrapper(Class classToWrap) {
		wrappedClass = classToWrap;
	}

	/**
	 * Indicates whether or not <code>null</code> is what is wrapped.
	 * 
	 * @return true if the wrapped "Class" is actually <code>null</code>
	 */
	public boolean isNull() {
		return wrappedClass == null;
	}

	/**
	 * Provides direct access to the wrapped <code>Class</code>.
	 * 
	 * @return the actual wrapped <code>Class</code> without any wrapping
	 */
	public Class getUnwrappedSelf() {
		// TODO test Class getUnwrappedSelf() or remove
		return wrappedClass;
	}

	/**
	 * Indicates whether or not the wrapped <code>Class</code> is assignable to a <code>Class</code> with the given
	 * name. It takes a parameter of type <code>String</code> instead of <code>Class</code> so that the test can be
	 * performed for <code>Class</code>es that do not have an equivalent in the caller's <code>ClassLoader</code>.
	 * 
	 * @param typeName the name of the type to check against
	 * @return true if the wrapped <code>Class</code> is assignable to a <code>Class</code> with the given name
	 */
	public boolean isAssignableTo(String typeName) {
		Assert.isNotNull(typeName);
		return classIsAssignableToType(wrappedClass, typeName);
	}

	/**
	 * Loads the <code>Class</code> with the given name from the given <code>ClassLoader</code>.
	 * 
	 * @param className the name of the <code>Class</code>
	 * @param classLoader the <code>ClassLoader</code> with which to load it
	 * @return the <code>Class</code> with the given name loaded from the given <code>ClassLoader</code>
	 * @throws TransloaderException if the <code>Class</code> cannot be found in the given <code>ClassLoader</code>
	 */
	public static Class getClass(String className, ClassLoader classLoader) {
		Assert.areNotNull(className, classLoader);
		try {
			return ClassUtils.getClass(classLoader, className, false);
		} catch (ClassNotFoundException e) {
			// TODO test ClassNotFoundException
			throw new TransloaderException(
					"Unable to load Class '" + className + "' from ClassLoader '" + classLoader + "'.", e);
		}
	}

	/**
	 * Loads the <code>Class</code>es with the given names from the given <code>ClassLoader</code>.
	 * 
	 * @param classNames the names of the <code>Class</code>es
	 * @param classLoader the <code>ClassLoader</code> with which to load them
	 * @return the <code>Class</code>es with the given names loaded from the given <code>ClassLoader</code>
	 * @throws TransloaderException if even one of the <code>Class</code>es cannot be found in the given
	 *             <code>ClassLoader</code>
	 */
	public static Class[] getClasses(String[] classNames, ClassLoader classLoader) {
		Assert.areNotNull(classNames, classLoader);
		Class[] classes = new Class[classNames.length];
		for (int i = 0; i < classes.length; i++) {
			classes[i] = getClass(classNames[i], classLoader);
		}
		return classes;
	}

	private static boolean classIsAssignableToType(Class rootClass, String typeName) {
		List allClasses = new ArrayList();
		allClasses.add(rootClass);
		allClasses.addAll(ClassUtils.getAllSuperclasses(rootClass));
		allClasses.addAll(ClassUtils.getAllInterfaces(rootClass));
		return ClassUtils.convertClassesToClassNames(allClasses).contains(typeName);
	}
}
