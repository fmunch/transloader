package com.googlecode.transloader;

import org.apache.commons.lang.exception.NestableRuntimeException;

/**
 * The <code>RuntimeException</code> thrown by the Transloader library itself.
 * 
 * @author Jeremy Wales
 */
public final class TransloaderException extends NestableRuntimeException {
	private static final long serialVersionUID = -8191856547135359324L;

	/**
	 * Constructs a new <code>TransloaderException</code> with the given detail message and nested
	 * <code>Throwable</code>.
	 * 
	 * @param message the error message
	 * @param cause the <code>Exception</code> that caused this one to be thrown
	 */
	public TransloaderException(String message, Exception cause) {
		super((String) Assert.isNotNull(message), (Exception) Assert.isNotNull(cause));
	}
}
