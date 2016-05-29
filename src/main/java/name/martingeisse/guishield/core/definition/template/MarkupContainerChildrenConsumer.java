/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.definition.template;

import java.util.function.Consumer;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;

/**
 * Consumes components by adding them to a markup container.
 */
public final class MarkupContainerChildrenConsumer implements Consumer<Component> {

	/**
	 * the container
	 */
	private final MarkupContainer container;

	/**
	 * Constructor.
	 * @param container the container to add components to
	 */
	public MarkupContainerChildrenConsumer(MarkupContainer container) {
		this.container = container;
	}

	// override
	@Override
	public void accept(Component component) {
		container.add(component);
	}

}
