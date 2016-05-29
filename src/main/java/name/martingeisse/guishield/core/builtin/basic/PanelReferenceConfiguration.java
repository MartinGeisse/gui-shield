/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.builtin.basic;

import javax.xml.stream.XMLStreamException;
import name.martingeisse.guiserver.configuration.ConfigurationHolder;
import name.martingeisse.guiserver.configuration.element.xml.PanelConfiguration;
import name.martingeisse.guiserver.template.IConfigurationSnippet;
import name.martingeisse.guishield.core.definition.DefinitionPath;
import name.martingeisse.guishield.core.definition.template.AbstractSingleComponentConfiguration;
import name.martingeisse.guishield.core.definition.template.ComponentConfiguration;
import name.martingeisse.guishield.core.definition.template.ConfigurationAssembler;
import name.martingeisse.guishield.core.xml.builder.StructuredElement;
import name.martingeisse.guishield.core.xml.generated.annotation.BindAttribute;
import name.martingeisse.guishield.core.xml.generated.annotation.BindElement;
import org.apache.wicket.Component;

/**
 * Configuration for using a gui:panel defined elsewhere.
 */
@BindElement(localName = "panel")
public class PanelReferenceConfiguration extends AbstractSingleComponentConfiguration {

	private DefinitionPath sourcePath;
	
	/**
	 * the panelConfiguration
	 */
	private PanelConfiguration panelConfiguration;
	
	/**
	 * the snippetHandle
	 */
	private int snippetHandle;

	/**
	 * Setter method for the sourcePath.
	 * @param sourcePath the sourcePath to set
	 */
	@BindAttribute(name = "src")
	public void setSourcePath(String sourcePath) {
		this.sourcePath = new DefinitionPath(sourcePath);
	}
	

	/**
	 * @return the panel configuration
	 */
	public PanelConfiguration getPanelConfiguration() {
		if (panelConfiguration == null) {
			panelConfiguration = ConfigurationHolder.needRequestUniverseConfiguration().getElementOrNull(PanelConfiguration.class, sourcePath);
			if (panelConfiguration == null) {
				throw new RuntimeException("no such panel configuration: " + sourcePath);
			}
		}
		return panelConfiguration;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.AbstractComponentConfiguration#assemble(name.martingeisse.guiserver.xmlbind.result.ConfigurationAssembler)
	 */
	@Override
	public void assemble(ConfigurationAssembler<ComponentConfiguration> assembler) throws XMLStreamException {
		super.assemble(assembler);
		assembler.getMarkupWriter().writeEmptyElement("div");
		assembler.getMarkupWriter().writeAttribute("wicket:id", getComponentId());
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.ComponentConfiguration#buildComponent()
	 */
	@Override
	public Component buildComponent() {
		ConfigurationDefinedPanel panel = new ConfigurationDefinedPanel(getComponentId(), this);
		// TODO add children from the panel configuration
		return panel;
	}

}
