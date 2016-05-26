/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.xml;

/**
 * Indicates a problem while parsing the XML.
 */
public class ParseException extends RuntimeException {

	/**
	 * Constructor.
	 */
	public ParseException() {
		super();
	}

	/**
	 * Constructor.
	 * @param message the message
	 */
	public ParseException(final String message) {
		super(message);
	}

	/**
	 * Constructor.
	 * @param cause the cause
	 */
	public ParseException(final Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor.
	 * @param message the message
	 * @param cause the cause
	 */
	public ParseException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
