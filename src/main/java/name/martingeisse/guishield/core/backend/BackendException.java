/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.backend;

/**
 * Indicates a problem while requesting content from the backend.
 */
public class BackendException extends RuntimeException {

	/**
	 * Constructor.
	 */
	public BackendException() {
		super();
	}

	/**
	 * Constructor.
	 * @param message the message
	 */
	public BackendException(final String message) {
		super(message);
	}

	/**
	 * Constructor.
	 * @param cause the cause
	 */
	public BackendException(final Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor.
	 * @param message the message
	 * @param cause the cause
	 */
	public BackendException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
