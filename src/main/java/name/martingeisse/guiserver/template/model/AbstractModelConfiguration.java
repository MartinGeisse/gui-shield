/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.template.model;

import name.martingeisse.guiserver.component.model.ModelProvider;
import name.martingeisse.guiserver.component.model.SimpleModelProvidingContainer;
import name.martingeisse.guishield.core.definition.template.AbstractSingleContainerConfiguration;
import name.martingeisse.guishield.core.xml.generated.annotation.BindAttribute;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.model.IModel;

/**
 * Base class for container configurations that provide a model through
 * a {@link ModelProvider}.
 */
public abstract class AbstractModelConfiguration extends AbstractSingleContainerConfiguration {

	/**
	 * the modelName
	 */
	private String modelName;
	
	/**
	 * Setter method for the modelName.
	 * @param modelName the modelName to set
	 */
	@BindAttribute(name = "name")
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	
	
	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.template.AbstractSingleContainerConfiguration#buildContainer()
	 */
	@Override
	protected MarkupContainer buildContainer() {
		return new SimpleModelProvidingContainer(getComponentId(), buildModel(), modelName);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.template.AbstractComponentGroupConfiguration#getBaseIdPrefix()
	 */
	@Override
	protected String getBaseIdPrefix() {
		return "model";
	}
	
	/**
	 * Builds the model for a component.
	 * @return the model
	 */
	protected abstract IModel<?> buildModel();
	
}
