/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.template;

import java.util.List;

import javax.xml.stream.XMLStreamException;

/**
 * A piece of properly nested markup that may contain components of type C.
 */
public final class MarkupContent {

	/**
	 * the entries
	 */
	private final MarkupContentEntry<ComponentGroupConfiguration>[] entries;

	/**
	 * Constructor.
	 * @param entries the entries
	 */
	public MarkupContent(MarkupContentEntry<ComponentGroupConfiguration>[] entries) {
		this.entries = entries;
	}

	/**
	 * Constructor for a list of components without raw markup.
	 * @param components the components
	 */
	@SuppressWarnings("unchecked")
	public MarkupContent(List<ComponentGroupConfiguration> components) {
		this.entries = createArray(components.size());
		for (int i=0; i<components.size(); i++) {
			entries[i] = new MarkupContentEntry.ComponentGroup<ComponentGroupConfiguration>(components.get(i));
		}
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	private MarkupContentEntry<ComponentGroupConfiguration>[] createArray(int size) {
		return (MarkupContentEntry<ComponentGroupConfiguration>[])(new MarkupContentEntry<?>[size]);
	}
	
	/**
	 * Assembles the configuration.
	 * 
	 * @param assembler the configuration assembler
	 * @throws XMLStreamException on XML stream processing errors
	 */
	public void assemble(ConfigurationAssembler<ComponentGroupConfiguration> assembler) throws XMLStreamException {
		for (MarkupContentEntry<ComponentGroupConfiguration> entry : entries) {
			entry.assemble(assembler);
		}
	}

}
