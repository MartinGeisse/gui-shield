/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.definition;

import java.io.StringReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import name.martingeisse.guishield.core.builtin.basic.PageDefinitionParser;
import name.martingeisse.guishield.core.builtin.basic.PanelDefinitionParser;

/**
 * TODO refactor to an extension based system
 */
@Singleton
public class XmlParserSwitch {

	private final PageDefinitionParser pageParser;
	private final PanelDefinitionParser panelParser;

	/**
	 * Constructor.
	 * @param pageParser (injected)
	 * @param panelParser (injected)
	 */
	@Inject
	public XmlParserSwitch(final PageDefinitionParser pageParser, final PanelDefinitionParser panelParser) {
		this.pageParser = pageParser;
		this.panelParser = panelParser;
	}

	/**
	 * Parses a page definition.
	 *
	 * @param sourceCode the page's source code
	 * @return the page definition
	 * @throws XMLStreamException ...
	 */
	public ResourceDefinition parse(final String sourceCode) throws XMLStreamException {
		final XMLStreamReader reader = XMLInputFactory.newFactory().createXMLStreamReader(new StringReader(sourceCode));
		if (reader.getEventType() != XMLStreamConstants.START_DOCUMENT) {
			throw new IllegalStateException("reader is not at the START_DOCUMENT event");
		}
		reader.next();
		if (reader.getNamespaceURI() == null) {
			throw new RuntimeException("invalid document element -- expected special element");
		}
		final String localName = reader.getLocalName();
		if (localName.equals("page")) {
			return pageParser.parse(reader);
		} else if (localName.equals("panel")) {
			return panelParser.parse(reader);
		} else {
			throw new RuntimeException("invalid document element -- unknown configuration element type: " + localName);
		}
	}

}
