/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.template;

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import name.martingeisse.guiserver.xml.content.ContentParser;
import name.martingeisse.guiserver.xml.element.ElementParser;

/**
 * Parses XML content to produce mixed raw/component markup.
 * 
 * This parser delegates to a configurable {@link ElementParser} to handle component elements.
 */
public final class MarkupContentParser implements ContentParser<MarkupContent<ComponentGroupConfiguration>> {

	/**
	 * the specialElementComponentParser
	 */
	private ElementParser<ComponentGroupConfiguration> specialElementComponentParser;

	/**
	 * Constructor.
	 */
	public MarkupContentParser() {
	}

	/**
	 * Constructor.
	 * @param specialElementComponentParser the special-element-to-component-configuration-parser
	 */
	public MarkupContentParser(ElementParser<ComponentGroupConfiguration> specialElementComponentParser) {
		this.specialElementComponentParser = specialElementComponentParser;
	}

	/**
	 * Getter method for the specialElementComponentParser.
	 * @return the specialElementComponentParser
	 */
	public ElementParser<ComponentGroupConfiguration> getSpecialElementComponentParser() {
		return specialElementComponentParser;
	}

	/**
	 * Setter method for the specialElementComponentParser.
	 * @param specialElementComponentParser the specialElementComponentParser to set
	 */
	public void setSpecialElementComponentParser(ElementParser<ComponentGroupConfiguration> specialElementComponentParser) {
		this.specialElementComponentParser = specialElementComponentParser;
	}

	@Override
	public MarkupContent parse(XMLStreamReader reader) throws XMLStreamException {
		if (specialElementComponentParser == null) {
			throw new IllegalStateException("component element parser not set");
		}
		List<MarkupContentEntry> entries = new ArrayList<MarkupContentEntry>();
		int nesting = 0;
		loop: while (true) {
			switch (reader.getEventType()) {

			case XMLStreamConstants.START_ELEMENT:
				if (reader.getNamespaceURI() != null) {
					C component = specialElementComponentParser.parse(reader);
					entries.add(new MarkupContentEntry.ComponentGroup(component));
				} else {
					MarkupContentEntry.Attribute[] attributes = new MarkupContentEntry.Attribute[reader.getAttributeCount()];
					for (int i = 0; i < attributes.length; i++) {
						String attributeLocalName = reader.getAttributeLocalName(i);
						String attributeValue = reader.getAttributeValue(i);
						attributes[i] = new MarkupContentEntry.Attribute(attributeLocalName, attributeValue);
					}
					entries.add(new MarkupContentEntry.RawOpeningTag(reader.getLocalName(), attributes));
					reader.next();
					nesting++;
				}
				break;

			case XMLStreamConstants.END_ELEMENT:
				if (nesting > 0) {
					entries.add(new MarkupContentEntry.RawClosingTag());
					reader.next();
					nesting--;
					break;
				} else {
					break loop;
				}

			case XMLStreamConstants.CDATA:
			case XMLStreamConstants.CHARACTERS:
			case XMLStreamConstants.SPACE:
			case XMLStreamConstants.ENTITY_REFERENCE:
				entries.add(new MarkupContentEntry.Characters(reader.getText()));
				reader.next();
				break;

			case XMLStreamConstants.COMMENT:
				reader.next();
				break;

			default:
				throw new RuntimeException("invalid XML event while skipping nested content: " + reader.getEventType());

			}
		}
		return new MarkupContent(listToArray(entries));
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	private MarkupContentEntry[] listToArray(List<MarkupContentEntry> entries) {
		return entries.toArray((MarkupContentEntry[])(new MarkupContentEntry<?>[entries.size()]));
	}

}
