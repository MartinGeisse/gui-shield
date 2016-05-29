/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.definition.template;

import org.apache.wicket.Component;

/**
 * Implemented by wicket components that originate from a configuration
 * with a {@link Template} and are thus able to map snippet handles to
 * the corresponding snippets. For each component that is defined by a
 * snippet, the nearest ancestor component implementing this interface
 * provides its snippet.
 */
public interface SnippetHolder {

	/**
	 * Returns the snippet for the specified handle.
	 * 
	 * @param snippetHandle the snippet handle
	 * @return the snippet
	 */
	public Snippet getSnippet(int snippetHandle);
	
	/**
	 * Finds the snippet holder for the specified snippet user. The snippet
	 * holder is the nearest ancestor implementing {@link SnippetHolder}.
	 * 
	 * @param snippetUser the snippet user
	 * @return the snipper holder
	 */
	public static SnippetHolder findSnippetHolder(Component snippetUser) {
		return snippetUser.findParent(SnippetHolder.class);
	}
	
}
