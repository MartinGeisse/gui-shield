/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.definition.template;

import java.util.function.Consumer;
import org.apache.wicket.Component;

/**
 * Base class for component configurations that corresponding to a single Wicket component, or no component at all.
 */
public abstract class AbstractSingleComponentConfiguration extends AbstractComponentConfiguration {

	/**
	 * Constructor.
	 */
	public AbstractSingleComponentConfiguration() {
	}

	/**
	 * Getter method for the component id.
	 * @return the component id
	 */
	public final String getComponentId() {
		return getComponentBaseId();
	}

	// override
	@Override
	public void buildComponents(Consumer<Component> consumer) {
		Component component = buildComponent();
		if (component != null) {
			consumer.accept(component);
		}
	}

	/**
	 * Builds the Wicket component for this configuration.
	 * 
	 * @return the component, or null if no component is needed
	 */
	protected abstract Component buildComponent();

}
