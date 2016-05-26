/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.xml.element;

import java.util.Set;
import javax.xml.stream.XMLStreamReader;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import name.martingeisse.guiserver.xml.ParserAmbiguityException;

/**
 * Provides easy access to all {@link RegisteredComponentParser}s.
 */
@Singleton
public class ComponentParserRegistry {

	private final Provider<Set<RegisteredComponentParser>> parserSetProvider;

	/**
	 * Constructor.
	 * @param parserSetProvider (injected)
	 */
	@Inject
	public ComponentParserRegistry(final Provider<Set<RegisteredComponentParser>> parserSetProvider) {
		this.parserSetProvider = parserSetProvider;
	}

	/**
	 * Selects a parser for an element. The reader must be positioned at a starting tag.
	 *
	 * @param reader the XML stream reader
	 * @return the selected parser, or null if none was found
	 * @throws ParserAmbiguityException if more than one registered parser could parse the element
	 */
	public RegisteredComponentParser selectParser(final XMLStreamReader reader) throws ParserAmbiguityException {
		RegisteredComponentParser selectedParser = null;
		for (RegisteredComponentParser parser : parserSetProvider.get()) {
			if (parser.canHandleElement(reader)) {
				if (selectedParser == null) {
					selectedParser = parser;
				} else {
					throw new ParserAmbiguityException("element " + reader.getName() + " can be handled both by " + selectedParser + " and " + parser);
				}
			}
		}
		return selectedParser;
	}

}
