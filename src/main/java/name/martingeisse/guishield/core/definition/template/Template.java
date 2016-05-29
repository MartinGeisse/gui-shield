/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.definition.template;

import com.google.common.collect.ImmutableList;

/**
 * Common base class for the content specifications for configuration elements
 * such as pages, panels, and so on.
 *
 * This class stores wicket markup and the configuration for the components and
 * models to attach to that markup.
 */
public final class Template {

	private final String wicketMarkup;
	private final ComponentConfigurationList components;
	private final ImmutableList<Snippet> snippets;

	/**
	 * Constructor.
	 * @param wicketMarkup the wicket markup
	 * @param components the components to attach to that markup
	 * @param snippets the snippets
	 */
	public Template(final String wicketMarkup, final ComponentConfigurationList components, final ImmutableList<Snippet> snippets) {
		this.wicketMarkup = wicketMarkup;
		this.components = components;
		this.snippets = snippets;
	}

	/**
	 * Getter method for the wicketMarkup.
	 * @return the wicketMarkup
	 */
	public String getWicketMarkup() {
		return wicketMarkup;
	}

	/**
	 * Getter method for the components.
	 * @return the components
	 */
	public ComponentConfigurationList getComponents() {
		return components;
	}

	/**
	 * Getter method for the snippets.
	 * @return the snippets
	 */
	public ImmutableList<Snippet> getSnippets() {
		return snippets;
	}

}
