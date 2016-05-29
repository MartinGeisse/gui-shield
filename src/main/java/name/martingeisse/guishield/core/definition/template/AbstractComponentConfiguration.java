/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.definition.template;

import javax.xml.stream.XMLStreamException;

/**
 * Base class for component group configurations.
 */
public abstract class AbstractComponentConfiguration implements ComponentConfiguration {

	/**
	 * the baseId
	 */
	private String baseId;

	/**
	 * Constructor.
	 */
	public AbstractComponentConfiguration() {
	}

	@Override
	public void assemble(ConfigurationAssembler assembler) throws XMLStreamException {
		this.baseId = getBaseIdPrefix() + assembler.getComponentGroupAccumulatorSize();
		assembler.addComponentGroup(this);
	}
	
	/**
	 * Returns the prefix for the component base ID. Subclasses may override this
	 * method to provide a prefix that gives a hint towards the meaning of the
	 * components, making the generated Wicket markup easier to understand
	 * (especially for debugging purposes).
	 * 
	 * The default base ID prefix is "component".
	 * 
	 * @return the component base ID prefix
	 */
	protected String getBaseIdPrefix() {
		return "component";
	}
	
	/**
	 * Getter method for the component base id.
	 * @return the id
	 */
	public final String getComponentBaseId() {
		return baseId;
	}

}
