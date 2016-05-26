/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.template.basic;

import name.martingeisse.guiserver.component.FirstChildEnclosureContainer;
import name.martingeisse.guiserver.template.AbstractSingleContainerConfiguration;
import name.martingeisse.guiserver.xml.builder.StructuredElement;
import name.martingeisse.guiserver.xml.generated.annotation.BindElement;
import org.apache.wicket.MarkupContainer;

/**
 * Configuration for a (wicket:enclosure)-like container.
 */
@StructuredElement
@BindElement(localName = "enclosure")
public final class EnclosureConfiguration extends AbstractSingleContainerConfiguration {

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.AbstractContainerConfiguration#buildContainer()
	 */
	@Override
	protected MarkupContainer buildContainer() {
		return new FirstChildEnclosureContainer(getComponentId());
	}

}
