/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.xml;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.apache.commons.lang3.StringUtils;

/**
 * Utility methods for parsing XML.
 */
public final class XmlUtil {

	/**
	 * Prevent instantiation.
	 */
	private XmlUtil() {
	}

	/**
	 * Skips all pure-whitespace events. If any non-whitespace text events are encountered, this
	 * method throws an exception.
	 *
	 * @param reader the XML stream reader
	 * @throws XMLStreamException on XML processing errors
	 */
	public static void skipWhitespace(final XMLStreamReader reader) throws XMLStreamException {
		while (true) {
			switch (reader.getEventType()) {

				case XMLStreamConstants.SPACE:
				case XMLStreamConstants.COMMENT:
					reader.next();
					break;

				case XMLStreamConstants.CDATA:
				case XMLStreamConstants.CHARACTERS:
				case XMLStreamConstants.ENTITY_REFERENCE: {
					final String text = reader.getText();
					if (!StringUtils.isBlank(text)) {
						throw new RuntimeException("expected pure whitespace, found '" + text.trim() + "'");
					}
					reader.next();
					break;
				}

				default:
					return;

			}
		}
	}
	
	/**
	 * Reads all text-like events and returns their content as a string. This moves the reader to
	 * the first event that is not text-like.
	 * 
	 * @param reader the XML stream reader
	 * @return the parsed value
	 * @throws XMLStreamException on XML processing errors
	 */
	public static String readText(final XMLStreamReader reader) throws XMLStreamException {
		StringBuilder builder = new StringBuilder();
		while (true) {
			switch (reader.getEventType()) {

				case XMLStreamConstants.SPACE:
				case XMLStreamConstants.COMMENT:
				case XMLStreamConstants.CDATA:
				case XMLStreamConstants.CHARACTERS:
				case XMLStreamConstants.ENTITY_REFERENCE:
					builder.append(reader.getText());
					break;

				default:
					return builder.toString();

			}
		}
	}

}
