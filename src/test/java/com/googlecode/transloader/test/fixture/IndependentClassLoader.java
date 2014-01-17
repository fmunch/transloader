package com.googlecode.transloader.test.fixture;

import java.net.URL;
import java.net.URLClassLoader;

public final class IndependentClassLoader extends URLClassLoader {
	private static final ClassLoader INSTANCE = new IndependentClassLoader();

	public static ClassLoader getInstance() {
		return INSTANCE;
	}

	private IndependentClassLoader() {
		super(getAppClassLoaderUrls(), null);
	}

	private static URL[] getAppClassLoaderUrls() {
		URLClassLoader appClassLoader = (URLClassLoader) IndependentClassLoader.class.getClassLoader();
		return appClassLoader.getURLs();
	}
}
