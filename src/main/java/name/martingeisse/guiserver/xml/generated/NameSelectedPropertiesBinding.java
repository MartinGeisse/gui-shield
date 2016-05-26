/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.xml.generated;

import java.util.HashMap;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import name.martingeisse.guiserver.xml.XmlParser;

/**
 * This binding selects one of several bindings based on
 * the element name.
 * 
 * @param <C> the type of the container object
 * @param <P> the parser type that defines the semantics with respect to the XML reader's position
 */
public final class NameSelectedPropertiesBinding<C, P extends XmlParser<?>> implements PropertiesBinding<C, P> {

	/**
	 * the bindings
	 */
	private final Map<String, PropertiesBinding<C, P>> bindings = new HashMap<>();

	/**
	 * Adds a properties binding.
	 * 
	 * @param localElementName the local element name that selects the specified binding
	 * @param binding the binding to invoke for that element name
	 */
	public void addBinding(String localElementName, PropertiesBinding<C, P> binding) {
		bindings.put(localElementName, binding);
	}

	@Override
	public void bind(XMLStreamReader reader, C target) throws XMLStreamException {
		PropertiesBinding<C, P> selectedBinding = bindings.get(reader.getLocalName());
		if (selectedBinding == null) {
			throw new RuntimeException("unknown property element: " + reader.getLocalName());
		}
		selectedBinding.bind(reader, target);
	}

}
