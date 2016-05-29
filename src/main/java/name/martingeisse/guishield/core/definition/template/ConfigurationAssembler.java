/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.definition.template;

import java.util.List;
import javax.xml.stream.XMLStreamWriter;

/**
 * Helper class that takes component-enhanced markup and
 * creates the final component/markup/snippet configuration from
 * that.
 */
public final class ConfigurationAssembler {

	/**
	 * the markupWriter
	 */
	private final XMLStreamWriter markupWriter;

	/**
	 * the componentGroupAccumulator
	 */
	private final List<ComponentConfiguration> componentGroupAccumulator;

	/**
	 * Constructor.
	 * @param markupWriter the object used to write Wicket markup
	 * @param componentGroupAccumulator a list that accumulates the component configurations
	 */
	public ConfigurationAssembler(XMLStreamWriter markupWriter, List<ComponentConfiguration> componentGroupAccumulator) {
		this.markupWriter = markupWriter;
		this.componentGroupAccumulator = componentGroupAccumulator;
	}

	/**
	 * Getter method for the markupWriter.
	 * @return the markupWriter
	 */
	public XMLStreamWriter getMarkupWriter() {
		return markupWriter;
	}
	
	/**
	 * Returns the size of the component group accumulator, i.e. the number of
	 * components accumulated so far.
	 * 
	 * @return the component group accumulator size
	 */
	public int getComponentGroupAccumulatorSize() {
		return componentGroupAccumulator.size();
	}
	
	/**
	 * Adds a component to the component group accumulator
	 * @param component the component to add
	 */
	public void addComponentGroup(ComponentConfiguration component) {
		componentGroupAccumulator.add(component);
	}
	
	/**
	 * Creates an assembler like this one but with a different component group accumulator.
	 * 
	 * @param componentGroupAccumulator the component group accumulator to use
	 * @return the new assembler
	 */
	public ConfigurationAssembler withComponentGroupAccumulator(List<ComponentConfiguration> componentGroupAccumulator) {
		return new ConfigurationAssembler(markupWriter, componentGroupAccumulator);
	}
	
}
