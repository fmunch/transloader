package com.googlecode.transloader.test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;

public class ProductionClassFinder {
	private static final String CLASS_EXTENSION = ".class";
	private static final char PACKAGE_SEPERATOR = '.';
	private static final char DIRECTORY_SEPERATOR = '/';

	private ProductionClassFinder() {
	}

	public static Class[] getAllProductionClasses(Class exampleProductionClass, String nonProductionRootPackageName) {
		try {
			File rootDirectory = getRootDirectoryOf(exampleProductionClass);
			List classes = new ArrayList();
			Collection classFiles = FileUtils.listFiles(rootDirectory, new String[] {"class"}, true);
			for (Iterator iterator = classFiles.iterator(); iterator.hasNext();) {
				File classFile = (File) iterator.next();
				String className = getClassName(rootDirectory, classFile);
				if (!className.startsWith(nonProductionRootPackageName)) classes.add(ClassUtils.getClass(className));
			}
			return (Class[]) classes.toArray(new Class[classes.size()]);
		} catch (Exception e) {
			throw new NestableRuntimeException(e);
		}
	}

	private static File getRootDirectoryOf(Class clazz) throws MalformedURLException {
		String classResourceName =
				DIRECTORY_SEPERATOR + clazz.getName().replace(PACKAGE_SEPERATOR, DIRECTORY_SEPERATOR) + CLASS_EXTENSION;
		String classUrlString = clazz.getResource(classResourceName).toExternalForm();
		String rootDirUrlString = StringUtils.removeEnd(classUrlString, classResourceName);
		return new File(new URL(rootDirUrlString).getFile());
	}

	private static String getClassName(File rootDir, File classFile) {
		return StringUtils.removeEnd(getRelativePath(rootDir, classFile), CLASS_EXTENSION).replace(DIRECTORY_SEPERATOR,
				PACKAGE_SEPERATOR);
	}

	private static String getRelativePath(File rootDirectory, File file) {
		return file.getAbsolutePath().substring(rootDirectory.getAbsolutePath().length() + 1).replace('\\',
				DIRECTORY_SEPERATOR);
	}
}
