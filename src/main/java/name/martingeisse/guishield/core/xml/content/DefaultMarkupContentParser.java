/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.xml.content;

import java.util.ArrayList;
import java.util.List;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import name.martingeisse.guishield.core.definition.template.ComponentConfiguration;
import name.martingeisse.guishield.core.definition.template.MarkupContent;
import name.martingeisse.guishield.core.definition.template.MarkupContentEntry;
import name.martingeisse.guishield.core.xml.ParseException;
import name.martingeisse.guishield.core.xml.element.ComponentParserRegistry;
import name.martingeisse.guishield.core.xml.element.ElementParser;
import name.martingeisse.guishield.core.xml.element.RegisteredComponentParser;

/**
 * Parses XML content to produce mixed raw/component markup.
 *
 * This parser delegates to a configurable {@link ElementParser} to handle component elements.
 */
@Singleton
public final class DefaultMarkupContentParser implements MarkupContentParser {

	private final Provider<ComponentParserRegistry> componentParserRegistryProvider;

	/**
	 * Constructor.
	 * @param componentParserRegistryProvider (injected)
	 */
	@Inject
	public DefaultMarkupContentParser(final Provider<ComponentParserRegistry> componentParserRegistryProvider) {
		this.componentParserRegistryProvider = componentParserRegistryProvider;
	}

	@Override
	public MarkupContent parse(final XMLStreamReader reader) throws XMLStreamException {
		final List<MarkupContentEntry> entries = new ArrayList<MarkupContentEntry>();
		int nesting = 0;
		loop: while (true) {
			switch (reader.getEventType()) {

				case XMLStreamConstants.START_ELEMENT:
					if (reader.getNamespaceURI() != null) {
						RegisteredComponentParser componentParser = componentParserRegistryProvider.get().selectParser(reader);
						if (componentParser == null) {
							throw new ParseException("unknown component: " + reader.getNamespaceURI() + ':' + reader.getLocalName());
						}
						final ComponentConfiguration configuration = componentParser.parse(reader);
						entries.add(new MarkupContentEntry.ComponentGroup(configuration));
					} else {
						final MarkupContentEntry.Attribute[] attributes = new MarkupContentEntry.Attribute[reader.getAttributeCount()];
						for (int i = 0; i < attributes.length; i++) {
							final String attributeLocalName = reader.getAttributeLocalName(i);
							final String attributeValue = reader.getAttributeValue(i);
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
		return new MarkupContent(entries.toArray(new MarkupContentEntry[entries.size()]));
	}

}
