/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.definition;

import javax.xml.stream.XMLStreamException;
import com.google.inject.Inject;
import name.martingeisse.guishield.core.backend.BackendRequester;

/**
 * This class is glue code between the definition repository and the backend requester.
 * 
 * TODO leave interface in definition package, move implementation to backend package
 */
public class DefinitionLoader {

	private final BackendRequester requester;
	private final XmlParserSwitch xmlParserSwitch;

	/**
	 * Constructor.
	 * @param requester (injected)
	 * @param xmlParserSwitch (injected)
	 */
	@Inject
	public DefinitionLoader(final BackendRequester requester, XmlParserSwitch xmlParserSwitch) {
		this.requester = requester;
		this.xmlParserSwitch = xmlParserSwitch;
	}

	/**
	 * Loads a definition.
	 * 
	 * @param path the path
	 * @return the definition
	 */
	public ResourceDefinition loadDefinition(final DefinitionPath path) {
		try {
			return xmlParserSwitch.parse(requester.requestDefinition(path));
		} catch (XMLStreamException e) {
			throw new RuntimeException(e);
		}
	}

}
