/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.xml.generated;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.apache.commons.lang3.StringUtils;
import name.martingeisse.guiserver.xml.XmlUtil;
import name.martingeisse.guiserver.xml.content.ContentParser;
import name.martingeisse.guiserver.xml.element.ElementParser;

/**
 * Parses and stores content by expecting child elements only and passing
 * each element to a delegate binding.
 * 
 * @param <C> the type of the container object
 * @param <P> the parser type that defines the semantics with respect to the XML reader's position
 */
public final class ContentPropertiesBinding<C, P extends ContentParser<?>> implements PropertiesBinding<C, P> {

	/**
	 * the elementBinding
	 */
	private final PropertiesBinding<C, ? extends ElementParser<?>> elementBinding;

	/**
	 * Constructor.
	 * @param elementBinding the binding to use for each element
	 */
	public ContentPropertiesBinding(PropertiesBinding<C, ElementParser<?>> elementBinding) {
		this.elementBinding = elementBinding;
	}

	@Override
	public void bind(XMLStreamReader reader, C target) throws XMLStreamException {
		loop: while (true) {
			switch (reader.getEventType()) {

			case XMLStreamConstants.START_ELEMENT:
				if (reader.getNamespaceURI() == null) {
					throw new RuntimeException("special element expected, found <" + reader.getLocalName() + ">");
				} else {
					elementBinding.bind(reader, target);
				}
				XmlUtil.skipWhitespace(reader);
				break;

			case XMLStreamConstants.END_ELEMENT:
				break loop;

			case XMLStreamConstants.CDATA:
			case XMLStreamConstants.CHARACTERS:
			case XMLStreamConstants.ENTITY_REFERENCE:
				if (!StringUtils.isBlank(reader.getText())) {
					throw new RuntimeException("unexpected text content: " + reader.getText().trim());
				}
				reader.next();
				break;
				
			case XMLStreamConstants.SPACE:
			case XMLStreamConstants.COMMENT:
				reader.next();
				break;
				
			default:
				throw new RuntimeException("invalid XML event while skipping nested content: " + reader.getEventType());

			}
		}
	}

}
