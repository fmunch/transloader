package com.googlecode.transloader;

import com.googlecode.transloader.clone.CloningStrategy;

/**
 * The API by which to wrap objects that reference <code>Class</code>es from foreign <code>ClassLoader</code>s.
 * 
 * @author Jeremy Wales
 */
public interface Transloader {
	/**
	 * The default implementation of <code>Transloader</code> which will produce {@link ObjectWrapper}s configured
	 * with {@link CloningStrategy#MAXIMAL} for <code>Object</code>s and {@link ClassWrapper}s for
	 * <code>Class</code>es.
	 */
	Transloader DEFAULT = new DefaultTransloader(CloningStrategy.MAXIMAL);

	/**
	 * Wraps the given object in an <code>ObjectWrapper</code>.
	 * 
	 * @param objectToWrap the object to wrap
	 * @return the wrapper around the given object
	 */
	ObjectWrapper wrap(Object objectToWrap);

	/**
	 * Wraps the given <code>Class</code> in a <code>ClassWrapper</code>.
	 * 
	 * @param classToWrap the <code>Class</code> to wrap
	 * @return the wrapper around the given <code>Class</code>
	 */
	ClassWrapper wrap(Class classToWrap);
}
