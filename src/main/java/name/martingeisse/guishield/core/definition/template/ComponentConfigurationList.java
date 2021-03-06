/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.definition.template;

import org.apache.wicket.MarkupContainer;

import com.google.common.collect.ImmutableList;

/**
 * Contains the configuration for multiple component groups, usually for the
 * children of a {@link MarkupContainer}.
 */
public final class ComponentConfigurationList {

	/**
	 * the configurations
	 */
	private final ImmutableList<ComponentConfiguration> configurations;

	/**
	 * Constructor.
	 * @param configurations the wrapped configurations
	 */
	public ComponentConfigurationList(ImmutableList<ComponentConfiguration> configurations) {
		this.configurations = configurations;
	}
	
	/**
	 * Getter method for the configurations.
	 * @return the configurations
	 */
	public ImmutableList<ComponentConfiguration> getConfigurations() {
		return configurations;
	}

	/**
	 * Builds all components from the configurations in this list and adds them to
	 * the specified parent.
	 * 
	 * @param parent the parent container to add the components to
	 */
	public void buildAndAddComponents(MarkupContainer parent) {
		for (ComponentConfiguration configuration : configurations) {
			configuration.buildComponents(new MarkupContainerChildrenConsumer(parent));
		}
	}

}
