/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.xml.value;

import name.martingeisse.guishield.core.xml.ParseException;

/**
 * Indicates a problem while parsing a textual value, e.g. an XML attribute.
 */
public class ValueParseException extends ParseException {

	/**
	 * Constructor.
	 */
	public ValueParseException() {
		super();
	}

	/**
	 * Constructor.
	 * @param message the message
	 */
	public ValueParseException(final String message) {
		super(message);
	}

	/**
	 * Constructor.
	 * @param cause the cause
	 */
	public ValueParseException(final Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor.
	 * @param message the message
	 * @param cause the cause
	 */
	public ValueParseException(final String message, final Throwable cause) {
		super(message, cause);
	}
	
}
