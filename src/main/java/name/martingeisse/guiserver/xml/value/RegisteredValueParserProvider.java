/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.xml.value;

/**
 * This is an extension point to register parsers based on Java types. Consider the case that a binding
 * maps a value (e.g. an XML attribute) to a setter in Java. The Java type accepted by the setter is known, but
 * without an explicit parser specification, the parser is per se unknown. The parent parser then looks for a
 * standard value parser provider that can provide a parser for that type.
 */
public interface RegisteredValueParserProvider {

	/**
	 * Provides a value parser for the specified type.
	 * 
	 * @param type the type
	 * @return the parser
	 */
	public ValueParser<?> getValueParser(Class<?> type);
	
}
