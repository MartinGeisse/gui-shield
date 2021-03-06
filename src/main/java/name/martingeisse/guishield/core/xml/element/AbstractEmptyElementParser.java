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

/**
 * Base class to implement a simple parser that accepts only empty elements, taking all
 * necessary information from attributes.
 * 
 * TODO refactor using lambdas
 * 
 * @param <T> the type being parsed
 */
public abstract class AbstractEmptyElementParser<T> implements ElementParser<T> {

	// override
	@Override
	public final T parse(XMLStreamReader reader) throws XMLStreamException {
		String elementLocalName = reader.getLocalName();
		T result = createResult(reader);
		reader.next();
		XmlUtil.skipWhitespace(reader);
		if (reader.getEventType() != XMLStreamConstants.END_ELEMENT) {
			throw new RuntimeException("unexpected content in element " + elementLocalName);
		}
		reader.next();
		return result;
	}
	
	/**
	 * Creates the result from the stream reader. The reader is at the START_ELEMENT event when this method
	 * gets called. This method should not move the reader.
	 * 
	 * @param reader the XML stream reader
	 * @return the parser result
	 * @throws XMLStreamException on XML stream processing errors
	 */
	protected abstract T createResult(XMLStreamReader reader) throws XMLStreamException;

}
