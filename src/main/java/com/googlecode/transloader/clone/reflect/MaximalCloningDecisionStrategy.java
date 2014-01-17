package com.googlecode.transloader.clone.reflect;

import com.googlecode.transloader.Assert;

/**
 * When injected into a {@link ReflectionCloningStrategy}, decides that all given objects and those that they reference
 * should be cloned.
 * 
 * @author Jeremy Wales
 */
public final class MaximalCloningDecisionStrategy implements CloningDecisionStrategy {
	/**
	 * Decides that all objects should be shallow copied.
	 * 
	 * @param original ignored; returns <code>true</code> regardless
	 * @param targetClassLoader ignored; returns <code>true</code> regardless
	 * @return <code>true</code> always
	 */
	public boolean shouldCloneObjectItself(Object original, ClassLoader targetClassLoader) {
		Assert.areNotNull(original, targetClassLoader);
		return true;
	}

	/**
	 * Decides that all objects have their references copied.
	 * 
	 * @param original ignored; returns <code>true</code> regardless
	 * @param targetClassLoader ignored; returns <code>true</code> regardless
	 * @return <code>true</code> always
	 */
	public boolean shouldCloneObjectContent(Object original, ClassLoader targetClassLoader) {
		return shouldCloneObjectItself(original, targetClassLoader);
	}
}
