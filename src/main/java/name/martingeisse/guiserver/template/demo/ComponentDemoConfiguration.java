/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.template.demo;

import name.martingeisse.guiserver.component.ComponentDemoBorder;
import name.martingeisse.guiserver.template.IConfigurationSnippet;
import name.martingeisse.guishield.core.definition.template.AbstractSingleContainerConfiguration;
import name.martingeisse.guishield.core.definition.template.ComponentConfiguration;
import name.martingeisse.guishield.core.definition.template.MarkupContent;
import name.martingeisse.guishield.core.xml.builder.StructuredElement;
import name.martingeisse.guishield.core.xml.generated.annotation.BindContent;
import name.martingeisse.guishield.core.xml.generated.annotation.BindElement;
import org.apache.wicket.MarkupContainer;

/**
 * Combines an arbitrary template section with a "source"
 * code snippet that is intended to be the template source
 * code for that section.
 */
@StructuredElement
@BindElement(localName = "demo")
public class ComponentDemoConfiguration extends AbstractSingleContainerConfiguration implements IConfigurationSnippet {

	/**
	 * the sourceCode
	 */
	private String sourceCode;

	/**
	 * the snippetHandle
	 */
	private int snippetHandle;

	/**
	 * Getter method for the sourceCode.
	 * @return the sourceCode
	 */
	public String getSourceCode() {
		return sourceCode;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.template.AbstractSingleContainerConfiguration#setMarkupContent(name.martingeisse.guiserver.template.MarkupContent)
	 */
	@Override
	public void setMarkupContent(MarkupContent<ComponentConfiguration> markupContent) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Setter method for the markupContentAndSourceCode.
	 * @param markupContentAndSourceCode the markupContentAndSourceCode to set
	 */
	@BindContent
	public void setMarkupContentAndSourceCode(MarkupContentAndSourceCode markupContentAndSourceCode) {
		super.setMarkupContent(markupContentAndSourceCode.getMarkupContent());
		this.sourceCode = markupContentAndSourceCode.getSourceCode();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.IConfigurationSnippet#setSnippetHandle(int)
	 */
	@Override
	public void setSnippetHandle(int handle) {
		this.snippetHandle = handle;
	}

	/**
	 * Getter method for the snippetHandle.
	 * @return the snippetHandle
	 */
	public int getSnippetHandle() {
		return snippetHandle;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.template.AbstractComponentGroupConfiguration#getBaseIdPrefix()
	 */
	@Override
	protected String getBaseIdPrefix() {
		return "demo";
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.template.AbstractSingleContainerConfiguration#buildContainer()
	 */
	@Override
	protected MarkupContainer buildContainer() {
		return new ComponentDemoBorder(getComponentId(), this);
	}

}
