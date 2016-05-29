/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.builtin.basic;

import name.martingeisse.guishield.core.definition.ResourceDefinition;
import name.martingeisse.guishield.core.definition.template.Template;

/**
 * The definition for a page.
 */
public class PageDefinition implements ResourceDefinition {

	private final Template template;

	/**
	 * Constructor.
	 * @param template the page template
	 */
	public PageDefinition(final Template template) {
		this.template = template;
	}

	/**
	 * Getter method for the template.
	 * @return the template
	 */
	public Template getTemplate() {
		return template;
	}

}
