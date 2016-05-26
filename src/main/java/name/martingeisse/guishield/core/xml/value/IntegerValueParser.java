/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.xml.value;

/**
 * This parser recognizes integer values.
 */
public final class IntegerValueParser implements ValueParser<Integer> {

	/**
	 * The shared instance of this class.
	 */
	public static final IntegerValueParser INSTANCE = new IntegerValueParser();
	
	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xml.value.ValueParser#parse(java.lang.String)
	 */
	@Override
	public Integer parse(String value) {
		try {
			return Integer.valueOf(value);
		} catch (NumberFormatException e) {
			throw new ValueParseException();
		}
	}
	
}
