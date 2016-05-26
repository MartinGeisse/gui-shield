/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.xml.value;


/**
 * This parser allows the (lowercase) names of the enum constants of
 * type T and converts them to the corresponding enum objects. Any
 * other value triggers an error.
 * 
 * @param <T> the enum type
 */
public final class EnumValueParser<T extends Enum<T>> implements ValueParser<T> {

	/**
	 * the enumClass
	 */
	private final Class<T> enumClass;

	/**
	 * Constructor.
	 * @param enumClass the class object for the enum type
	 */
	public EnumValueParser(Class<T> enumClass) {
		for (T enumConstant : enumClass.getEnumConstants()) {
			if (!enumConstant.name().equals(enumConstant.name().toUpperCase())) {
				throw new IllegalArgumentException("invalid enum constant for type " + enumClass + " -- constant name contains lowercase characters: " + enumConstant);
			}
		}
		this.enumClass = enumClass;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xml.value.ValueParser#parse(java.lang.String)
	 */
	@Override
	public T parse(String value) {
		if (!value.equals(value.toLowerCase())) {
			throw new ValueParseException("invalid value for enum type " + enumClass + " (value container uppercase characters)");
		}
		try {
			return Enum.valueOf(enumClass, value.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new ValueParseException("invalid value for enum type " + enumClass + " -- no such constant: " + value);
		}
	}

}
