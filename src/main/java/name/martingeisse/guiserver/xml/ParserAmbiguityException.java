/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.xml;

/**
 * Indicates a situation where an input file can be parsed in more than one way due to conflicting parsers being provided by plugins.
 */
public class ParserAmbiguityException extends ParseException {

	/**
	 * Constructor.
	 */
	public ParserAmbiguityException() {
		super();
	}

	/**
	 * Constructor.
	 * @param message the message
	 */
	public ParserAmbiguityException(final String message) {
		super(message);
	}

	/**
	 * Constructor.
	 * @param cause the cause
	 */
	public ParserAmbiguityException(final Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor.
	 * @param message the message
	 * @param cause the cause
	 */
	public ParserAmbiguityException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
