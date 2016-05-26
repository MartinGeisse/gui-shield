/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.xml.value;

import com.google.inject.Singleton;

/**
 * Provides the built-in parsers.
 */
@Singleton
public class BuiltinValueParserProvider implements RegisteredValueParserProvider {

	// override
	@Override
	public ValueParser<?> getValueParser(final Class<?> type) {
		if (type == Boolean.class || type == Boolean.TYPE) {
			return BooleanValueParser.INSTANCE;
		} else if (type == Integer.class || type == Integer.TYPE) {
			return IntegerValueParser.INSTANCE;
		} else if (type == String.class) {
			return StringValueParser.INSTANCE;
		} else if (type.isEnum()) {
			return new EnumValueParser<>(type.asSubclass(Enum.class));
		} else {
			return null;
		}
	}

}
