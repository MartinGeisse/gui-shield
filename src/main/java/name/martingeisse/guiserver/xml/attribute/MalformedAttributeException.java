/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.xml.attribute;

import name.martingeisse.guiserver.xml.value.ValueParseException;

/**
 * Indicates a problem while parsing an attribute value due to a malformed value. The nested exception is the {@link ValueParseException}
 * that describes the problem.
 */
public class MalformedAttributeException extends AttributeParseException {

	/**
	 * Constructor.
	 * @param attributeName the name of the attribute
	 * @param cause the cause
	 */
	public MalformedAttributeException(final String attributeName, final ValueParseException cause) {
		super(attributeName, "error while parsing the value of attribute " + attributeName, cause);
	}

}
