/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.definition.template;

import java.util.function.Consumer;
import javax.xml.stream.XMLStreamException;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;

/**
 * Common interface for the configuration of one or more Wicket components.
 *
 * The common case for this interface is to contain the configuration for a
 * single component, but multiple components are possible. (Rationale: If
 * this interface was designed to result in a single component only, then
 * groups with more than one component would have to use a dummy
 * {@link WebMarkupContainer} just to fulfil the contract).
 *
 * This interface knows how to build the Wicket components and how to
 * generate the corresponding markup in the enclosing page or panel.
 */
public interface ComponentConfiguration {

	/**
	 * Assembles the configuration.
	 *
	 * @param assembler the configuration assembler
	 * @throws XMLStreamException on XML stream processing errors
	 */
	public void assemble(ConfigurationAssembler assembler) throws XMLStreamException;

	/**
	 * Builds the wicket components for this group and gives them to the specified consumer.
	 *
	 * @param consumer the consumer to give components to
	 */
	public void buildComponents(Consumer<Component> consumer);

}
