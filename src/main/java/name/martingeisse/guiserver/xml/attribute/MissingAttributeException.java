/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.guiserver.xml.attribute;

/**
 * This exception type gets thrown if a required attribute is missing.
 */
public final class MissingAttributeException extends AttributeParseException {

	/**
	 * Constructor.
	 * @param attributeName the attribute name
	 */
	public MissingAttributeException(String attributeName) {
		super(attributeName, "attribute '" + attributeName + "' is missing");
	}

}
