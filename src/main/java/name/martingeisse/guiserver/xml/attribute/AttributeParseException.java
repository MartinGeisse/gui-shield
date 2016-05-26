/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.xml.attribute;

import name.martingeisse.guiserver.xml.ParseException;

/**
 * Indicates a problem while parsing an attribute.
 */
public class AttributeParseException extends ParseException {

	private final String attributeName;

	/**
	 * Constructor.
	 * @param attributeName the name of the attribute
	 */
	public AttributeParseException(final String attributeName) {
		super();
		this.attributeName = attributeName;
	}

	/**
	 * Constructor.
	 * @param attributeName the name of the attribute
	 * @param message the message
	 */
	public AttributeParseException(final String attributeName, final String message) {
		super(message);
		this.attributeName = attributeName;
	}

	/**
	 * Constructor.
	 * @param attributeName the name of the attribute
	 * @param cause the cause
	 */
	public AttributeParseException(final String attributeName, final Throwable cause) {
		super(cause);
		this.attributeName = attributeName;
	}

	/**
	 * Constructor.
	 * @param attributeName the name of the attribute
	 * @param message the message
	 * @param cause the cause
	 */
	public AttributeParseException(final String attributeName, final String message, final Throwable cause) {
		super(message, cause);
		this.attributeName = attributeName;
	}

	/**
	 * Getter method for the attributeName.
	 * @return the attributeName
	 */
	public String getAttributeName() {
		return attributeName;
	}

}
