/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.template.basic;

import javax.xml.stream.XMLStreamException;
import name.martingeisse.guiserver.template.model.NamedModelReferenceBehavior;
import name.martingeisse.guishield.core.definition.template.AbstractSingleComponentConfiguration;
import name.martingeisse.guishield.core.definition.template.ComponentConfiguration;
import name.martingeisse.guishield.core.definition.template.ConfigurationAssembler;
import name.martingeisse.guishield.core.xml.builder.StructuredElement;
import name.martingeisse.guishield.core.xml.generated.annotation.BindAttribute;
import name.martingeisse.guishield.core.xml.generated.annotation.BindElement;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;

/**
 * This configuration outputs text from a model. Any HTML special characters
 * will be escaped.
 */
@StructuredElement
@BindElement(localName = "text")
public final class EchoTextConfiguration extends AbstractSingleComponentConfiguration {

	/**
	 * the modelReferenceSpecification
	 */
	private String modelReferenceSpecification;

	/**
	 * Setter method for the modelReferenceSpecification.
	 * @param modelReferenceSpecification the modelReferenceSpecification to set
	 */
	@BindAttribute(name = "model")
	public void setModelReferenceSpecification(String modelReferenceSpecification) {
		this.modelReferenceSpecification = modelReferenceSpecification;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.AbstractComponentConfiguration#assemble(name.martingeisse.guiserver.xmlbind.result.ConfigurationAssembler)
	 */
	@Override
	public void assemble(ConfigurationAssembler<ComponentConfiguration> assembler) throws XMLStreamException {
		super.assemble(assembler);
		assembler.getMarkupWriter().writeEmptyElement("wicket:container");
		assembler.getMarkupWriter().writeAttribute("wicket:id", getComponentId());
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.ComponentConfiguration#buildComponent()
	 */
	@Override
	public Component buildComponent() {
		return new Label(getComponentId()).add(new NamedModelReferenceBehavior(modelReferenceSpecification));
	}

}
