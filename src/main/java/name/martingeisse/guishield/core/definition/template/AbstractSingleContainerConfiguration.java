/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.definition.template;

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamException;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;

import com.google.common.collect.ImmutableList;
import name.martingeisse.guishield.core.xml.generated.annotation.BindContent;

/**
 * Base class for component group configurations that correspond to a single {@link MarkupContainer}.
 */
public abstract class AbstractSingleContainerConfiguration extends AbstractSingleComponentConfiguration {

	/**
	 * the markupContent
	 */
	private MarkupContent markupContent;

	/**
	 * the children
	 */
	private ComponentConfigurationList children;

	/**
	 * Setter method for the markupContent.
	 * @param markupContent the markupContent to set
	 */
	@BindContent
	public void setMarkupContent(MarkupContent markupContent) {
		this.markupContent = markupContent;
	}
	
	// override
	@Override
	public void assemble(ConfigurationAssembler assembler) throws XMLStreamException {
		super.assemble(assembler);
		assembleContainerIntro(assembler);
		assembleContainerContents(assembler);
		assembleContainerOutro(assembler);
	}

	/**
	 * Assembles the container "intro". The default implementation renders an opening DIV
	 * tag with the component's wicket:id.
	 * 
	 * @param assembler the configuration assembler
	 * @throws XMLStreamException on XML stream processing errors
	 */
	protected void assembleContainerIntro(ConfigurationAssembler assembler) throws XMLStreamException {
		writeOpeningComponentTag(assembler, "div");
	}

	/**
	 * Writes the opening component tag, using the specified local element name and taking
	 * the wicket:id attribute from the {@link #getComponentId()} method. This method is useful for
	 * implementing {@link #assembleContainerIntro(ConfigurationAssembler)}.
	 * 
	 * Calling this method may be followed by writeAttribute() calls to the XML writer,
	 * to add further attributes to the component tag.
	 * 
	 * @param assembler the configuration assembler
	 * @param localName the local element name
	 * @throws XMLStreamException on XML stream processing errors
	 */
	protected final void writeOpeningComponentTag(ConfigurationAssembler assembler, String localName) throws XMLStreamException {
		assembler.getMarkupWriter().writeStartElement(localName);
		assembler.getMarkupWriter().writeAttribute("wicket:id", getComponentId());
	}

	/**
	 * Assembles the container contents. The default implementation invokes the assemble() method
	 * on the markup content, collecting all component group configurations from that markup in a new
	 * list, and stores that list as this component's children.
	 * 
	 * @param assembler the configuration assembler
	 * @throws XMLStreamException on XML stream processing errors
	 */
	protected void assembleContainerContents(ConfigurationAssembler assembler) throws XMLStreamException {
		List<ComponentConfiguration> childrenAccumulator = new ArrayList<ComponentConfiguration>();
		ConfigurationAssembler subAssembler = assembler.withComponentGroupAccumulator(childrenAccumulator);
		markupContent.assemble(subAssembler);
		this.children = new ComponentConfigurationList(ImmutableList.copyOf(childrenAccumulator));
	}
	
	/**
	 * Assembles the container "outro". The default implementation renders a single
	 * closing tag. This is appropriate if the intro renders a corresponding opening
	 * tag (and possibly other content, but no other *unclosed* opening tag).
	 * 
	 * @param assembler the configuration assembler
	 * @throws XMLStreamException on XML stream processing errors
	 */
	protected void assembleContainerOutro(ConfigurationAssembler assembler) throws XMLStreamException {
		assembler.getMarkupWriter().writeEndElement();
	}
	
	/**
	 * Getter method for the children.
	 * @return the children
	 */
	protected final ComponentConfigurationList getChildren() {
		return children;
	}

	// override
	@Override
	public Component buildComponent() {
		MarkupContainer container = buildContainer();
		children.buildAndAddComponents(container);
		return container;
	}

	/**
	 * Builds the container itself, not adding any children.
	 * @return the container
	 */
	protected abstract MarkupContainer buildContainer();

}
