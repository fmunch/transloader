package com.googlecode.transloader.clone;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;

import org.apache.commons.io.input.ClassLoaderObjectInputStream;
import org.apache.commons.lang.SerializationException;
import org.apache.commons.lang.SerializationUtils;

import com.googlecode.transloader.Assert;

/**
 * A <code>CloningStrategy</code> that uses Java Serialization as its mechanism.
 * 
 * @author Jeremy Wales
 */
public final class SerializationCloningStrategy implements CloningStrategy {
	/**
	 * {@inheritDoc}
	 * 
	 * @throws ClassCastException if the given <code>original</code> object is not {@link Serializable}
	 * @throws SerializationException if serialization fails
	 * @throws IOException if input fails during deserialization
	 * @throws ClassNotFoundException if the <code>targetClassLoader</code> cannot find a required class
	 */
	public Object cloneObjectUsingClassLoader(Object original, ClassLoader targetClassLoader)
			throws ClassCastException, SerializationException, IOException, ClassNotFoundException {
		Assert.areNotNull(original, targetClassLoader);
		byte[] serializedOriginal = SerializationUtils.serialize((Serializable) original);
		return new ClassLoaderObjectInputStream(targetClassLoader, new ByteArrayInputStream(serializedOriginal)).readObject();
	}
}
