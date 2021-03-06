/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.xml.element;

import java.util.HashMap;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * This element parser selects one of several parsers based on
 * the element name.
 * 
 * @param <T> the parsed type
 */
public class NameSelectedElementParser<T> implements ElementParser<T> {

	/**
	 * the parsers
	 */
	private final Map<String, ElementParser<? extends T>> parsers = new HashMap<String, ElementParser<? extends T>>();

	/**
	 * Adds a sub-parser to this parser.
	 * 
	 * @param localElementName the local element name that selects the specified parser
	 * @param parser the parser to invoke for that element name
	 */
	public void addParser(String localElementName, ElementParser<? extends T> parser) {
		parsers.put(localElementName, parser);
	}

	// override
	@Override
	public final T parse(XMLStreamReader reader) throws XMLStreamException {
		ElementParser<? extends T> selectedParser = parsers.get(reader.getLocalName());
		if (selectedParser == null) {
			throw new RuntimeException("unknown special element: " + reader.getLocalName());
		}
		return selectedParser.parse(reader);
	}

}
