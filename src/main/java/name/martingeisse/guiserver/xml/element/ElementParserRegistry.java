/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.xml.element;

import java.util.HashMap;
import java.util.Map;

/**
 * This class keeps a set of known element parsers, indexed by the type
 * of elements parsed.
 */
public final class ElementParserRegistry {

	/**
	 * the parsers
	 */
	@SuppressWarnings("rawtypes")
	private final Map<Class, ElementParser> parsers = new HashMap<>();

	/**
	 * Adds a parser to this registry.
	 * 
	 * @param type the type of elements the parser must handle
	 * @param parser the parser
	 */
	public <T> void addParser(Class<T> type, ElementParser<T> parser) {
		parsers.put(type, parser);
	}

	/**
	 * Obtains a parser based on the type of parsed elements.
	 * 
	 * @param type the type of elements the parser must handle
	 * @return the parser, or null if no parser exists for that type
	 */
	@SuppressWarnings("unchecked")
	public <T> ElementParser<T> getParser(Class<T> type) {
		return parsers.get(type);
	}

}