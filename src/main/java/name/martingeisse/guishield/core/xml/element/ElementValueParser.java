/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.xml.element;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import name.martingeisse.guishield.core.xml.XmlUtil;
import name.martingeisse.guishield.core.xml.value.ValueParser;

/**
 * This wrapper wraps a value parser and makes it behave as an
 * element parser. This element parser ignores the element tags
 * and its attributes, and just parses the element's content as
 * a value. This implies that no nested tags are allowed.
 * 
 * @param <T> the parsed type
 */
public final class ElementValueParser<T> implements ElementParser<T> {

	private final ValueParser<T> valueParser;

	/**
	 * Constructor.
	 * @param valueParser the wrapped value parser
	 */
	public ElementValueParser(ValueParser<T> valueParser) {
		this.valueParser = valueParser;
	}

	// override
	@Override
	public T parse(XMLStreamReader reader) throws XMLStreamException {
		reader.next();
		T result = valueParser.parse(XmlUtil.readText(reader));
		if (reader.getEventType() != XMLStreamConstants.END_ELEMENT) {
			throw new RuntimeException("delegate value parser did not leave the XML stream reader at an END_ELEMENT");
		}
		reader.next();
		return result;
	}

}
