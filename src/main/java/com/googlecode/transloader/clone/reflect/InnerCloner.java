package com.googlecode.transloader.clone.reflect;

interface InnerCloner {
	Object instantiateClone(Object original, ClassLoader targetClassLoader) throws Exception;

	void cloneContent(Object original, Object clone, ClassLoader targetClassLoader) throws Exception;
}
