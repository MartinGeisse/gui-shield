/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.xml.element;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import name.martingeisse.guishield.core.xml.XmlParser;

/**
 * This parser parses an XML element and creates an object of
 * type T from it.
 *
 * @param <T> the type of parsed objects
 */
public interface ElementParser<T> extends XmlParser<T> {

	/**
	 * Parses an object from an XML element and returns it. The stream must
	 * be at the opening tag of the element. This method moves the stream
	 * right after the closing tag of the element.
	 * 
	 * @param reader the XML stream reader
	 * @return the parsed object
	 * @throws XMLStreamException on XML processing errors
	 */
	@Override
	public T parse(XMLStreamReader reader) throws XMLStreamException;

}
