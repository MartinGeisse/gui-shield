/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.template.basic.form;

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.component.FieldPathBehavior;
import name.martingeisse.guishield.core.definition.template.AbstractSingleComponentConfiguration;
import name.martingeisse.guishield.core.definition.template.ComponentConfiguration;
import name.martingeisse.guishield.core.definition.template.ConfigurationAssembler;
import name.martingeisse.guishield.core.xml.builder.StructuredElement;
import name.martingeisse.guishield.core.xml.generated.annotation.AttributeValueBindingOptionality;
import name.martingeisse.guishield.core.xml.generated.annotation.BindAttribute;
import name.martingeisse.guishield.core.xml.generated.annotation.BindElement;
import name.martingeisse.guishield.core.xml.generated.annotation.BindNestedElement;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.validation.IValidator;

/**
 * Represents a text field.
 */
@StructuredElement
@BindElement(localName = "textField")
public final class TextFieldConfiguration extends AbstractSingleComponentConfiguration {

	/**
	 * the name
	 */
	private String name;

	/**
	 * the required
	 */
	private boolean required;

	/**
	 * the validators
	 */
	private List<IValidator<?>> validators = new ArrayList<>();

	/**
	 * Getter method for the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter method for the name.
	 * @param name the name to set
	 */
	@BindAttribute(name = "name")
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Getter method for the required.
	 * @return the required
	 */
	public boolean isRequired() {
		return required;
	}

	/**
	 * Setter method for the required.
	 * @param required the required to set
	 */
	@BindAttribute(name = "required", optionality = AttributeValueBindingOptionality.OPTIONAL_WITH_DEFAULT, defaultValue = "true")
	public void setRequired(boolean required) {
		this.required = required;
	}

	/**
	 * Adds a validator to this form field.
	 * @param validator the validator to add
	 */
	@BindNestedElement(localName = "validation")
	public void addValidator(IValidator<?> validator) {
		validators.add(validator);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.AbstractComponentConfiguration#assemble(name.martingeisse.guiserver.xml.result.ConfigurationAssembler)
	 */
	@Override
	public void assemble(ConfigurationAssembler<ComponentConfiguration> assembler) throws XMLStreamException {
		super.assemble(assembler);
		assembler.getMarkupWriter().writeEmptyElement("input");
		assembler.getMarkupWriter().writeAttribute("type", "text");
		assembler.getMarkupWriter().writeAttribute("wicket:id", getComponentId());
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.ComponentConfiguration#buildComponent()
	 */
	@Override
	public Component buildComponent() {
		TextField<?> textField = new TextField<>(getComponentId());
		textField.setRequired(required);
		textField.add(new FieldPathBehavior(name));
		for (IValidator<?> validator : validators) {
			addValidator(textField, validator);
		}
		return textField;
	}

	@SuppressWarnings({
		"unchecked", "rawtypes"
	})
	private void addValidator(TextField textField, IValidator validator) {
		textField.add(validator);
	}

}
