/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.definition.template;

import java.util.List;

import javax.xml.stream.XMLStreamException;

/**
 * A piece of properly nested markup with component configurations.
 */
public final class MarkupContent {

	/**
	 * the entries
	 */
	private final MarkupContentEntry[] entries;

	/**
	 * Constructor.
	 * @param entries the entries
	 */
	public MarkupContent(MarkupContentEntry[] entries) {
		this.entries = entries;
	}

	/**
	 * Constructor for a list of components without raw markup.
	 * @param components the components
	 */
	@SuppressWarnings("unchecked")
	public MarkupContent(List<ComponentConfiguration> components) {
		this.entries = new MarkupContentEntry[components.size()];
		for (int i=0; i<components.size(); i++) {
			entries[i] = new MarkupContentEntry.ComponentGroup(components.get(i));
		}
	}
	
	/**
	 * Assembles the configuration.
	 * 
	 * @param assembler the configuration assembler
	 * @throws XMLStreamException on XML stream processing errors
	 */
	public void assemble(ConfigurationAssembler assembler) throws XMLStreamException {
		for (MarkupContentEntry entry : entries) {
			entry.assemble(assembler);
		}
	}

}
