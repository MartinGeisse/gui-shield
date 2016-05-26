/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.xml.attribute;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import name.martingeisse.guishield.core.xml.generated.annotation.BindAttribute;
import name.martingeisse.guishield.core.xml.value.ValueParser;

/**
 * The default implementation of {@link AttributeParser}. This class
 * reads a single attribute with a fixed name and uses a
 * {@link ValueParser} to parse its value.
 *
 * Note that the default value is specified in the same format as in the
 * XML, that is, the default value gets parsed just as any other value.
 * This is because of the strongly restricted types that can be used in
 * the default value property of {@link BindAttribute} annotations.
 *
 * @param <T> the attribute value type
 */
public final class SimpleAttributeParser<T> implements AttributeParser<T> {

	/**
	 * the name
	 */
	private final String name;

	/**
	 * the optional
	 */
	private final boolean optional;

	/**
	 * the valueParser
	 */
	private final ValueParser<T> valueParser;

	/**
	 * the defaultValue
	 */
	private final T defaultValue;

	/**
	 * Constructor for a mandatory attribute.
	 *
	 * @param name the attribute name
	 * @param valueParser the parser for the attribute value
	 */
	public SimpleAttributeParser(final String name, final ValueParser<T> valueParser) {
		this(name, false, valueParser, null);
	}

	/**
	 * Constructor
	 *
	 * @param name the attribute name
	 * @param optional whether the attribute is optional
	 * @param valueParser the parser for the attribute value
	 */
	public SimpleAttributeParser(final String name, final boolean optional, final ValueParser<T> valueParser) {
		this(name, optional, valueParser, null);
	}

	/**
	 * Constructor
	 *
	 * @param name the attribute name
	 * @param defaultValue the default value (unparsed)
	 * @param valueParser the parser for the attribute value
	 */
	public SimpleAttributeParser(final String name, final String defaultValue, final ValueParser<T> valueParser) {
		this(name, true, valueParser, defaultValue == null ? null : valueParser.parse(defaultValue));
	}

	/**
	 * Constructor
	 *
	 * @param name the attribute name
	 * @param valueParser the parser for the attribute value
	 * @param defaultValue the default value (parsed)
	 */
	public SimpleAttributeParser(final String name, final ValueParser<T> valueParser, final T defaultValue) {
		this(name, true, valueParser, defaultValue);
	}

	/**
	 * Constructor.
	 */
	private SimpleAttributeParser(final String name, final boolean optional, final ValueParser<T> valueParser, final T defaultValue) {
		if (name == null) {
			throw new IllegalArgumentException("name argument is null");
		}
		if (valueParser == null) {
			throw new IllegalArgumentException("valueParser argument is null");
		}
		this.name = name;
		this.optional = optional;
		this.valueParser = valueParser;
		this.defaultValue = defaultValue;
	}

	/**
	 * Getter method for the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Getter method for the optional.
	 * @return the optional
	 */
	public boolean isOptional() {
		return optional;
	}

	/**
	 * Getter method for the valueParser.
	 * @return the valueParser
	 */
	public ValueParser<T> getValueParser() {
		return valueParser;
	}

	/**
	 * Getter method for the defaultValue.
	 * @return the defaultValue
	 */
	public T getDefaultValue() {
		return defaultValue;
	}

	// override
	@Override
	public T parse(final XMLStreamReader reader) throws XMLStreamException {
		final String specifiedValue = reader.getAttributeValue(null, name);
		if (specifiedValue == null) {
			if (optional) {
				return defaultValue;
			} else {
				throw new MissingAttributeException(name);
			}
		} else {
			return valueParser.parse(specifiedValue);
		}
	}

}
