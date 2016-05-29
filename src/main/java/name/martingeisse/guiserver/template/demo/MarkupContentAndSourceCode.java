/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.template.demo;

import name.martingeisse.guishield.core.definition.template.ComponentConfiguration;
import name.martingeisse.guishield.core.definition.template.MarkupContent;


/**
 * Wraps a piece of markup content and its XML source code.
 */
public final class MarkupContentAndSourceCode {

	/**
	 * the markupContent
	 */
	private final MarkupContent<ComponentConfiguration> markupContent;

	/**
	 * the sourceCode
	 */
	private final String sourceCode;

	/**
	 * Constructor.
	 * @param markupContent the parsed markup content
	 * @param sourceCode the source code
	 */
	public MarkupContentAndSourceCode(MarkupContent<ComponentConfiguration> markupContent, String sourceCode) {
		this.markupContent = markupContent;
		this.sourceCode = sourceCode;
	}

	/**
	 * Getter method for the markupContent.
	 * @return the markupContent
	 */
	public MarkupContent<ComponentConfiguration> getMarkupContent() {
		return markupContent;
	}

	/**
	 * Getter method for the sourceCode.
	 * @return the sourceCode
	 */
	public String getSourceCode() {
		return sourceCode;
	}

}
