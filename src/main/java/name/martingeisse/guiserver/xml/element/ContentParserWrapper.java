/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.xml.element;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import name.martingeisse.guiserver.xml.content.ContentParser;

/**
 * This wrapper wraps a content parser and makes it behave as an
 * element parser. This element parser ignores the element tags
 * and its attributes, and just parses the element content.
 */
public final class ContentParserWrapper<T> implements ElementParser<T> {

	/**
	 * the contentParser
	 */
	private final ContentParser<T> contentParser;

	/**
	 * Constructor.
	 * @param contentParser the wrapped content parser
	 */
	public ContentParserWrapper(ContentParser<T> contentParser) {
		this.contentParser = contentParser;
	}

	// override
	@Override
	public T parse(XMLStreamReader reader) throws XMLStreamException {
		reader.next();
		T result = contentParser.parse(reader);
		if (reader.getEventType() != XMLStreamConstants.END_ELEMENT) {
			throw new RuntimeException("delegate content parser did not leave the XML stream reader at an END_ELEMENT");
		}
		reader.next();
		return result;
	}

}
