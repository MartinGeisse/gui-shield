/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.definition.template;

/**
 * <p>
 * Instances of this interface are template-related objects that
 * are assigned a locally unique handle which can later be used
 * to retrieve them from the template.
 * </p>
 *
 * <p>
 * This interface is typically implemented by subclasses of {@link ComponentConfiguration}.
 * Such handles are used to allow Wicket components to refer to the snippets that define
 * them without them being stateful components.
 * </p>
 */
public interface Snippet {

	/**
	 * Notifies this snippet about the handle assigned to it.
	 * @param handle the handle
	 */
	public void setSnippetHandle(int handle);

}
