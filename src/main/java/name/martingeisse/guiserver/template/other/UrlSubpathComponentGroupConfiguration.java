/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.template.other;

import name.martingeisse.guishield.core.definition.template.ComponentConfiguration;

/**
 * This interface is implemented by component group configurations that
 * want to provide URL subpaths.
 */
public interface UrlSubpathComponentGroupConfiguration extends ComponentConfiguration {

	/**
	 * @return the names of the page parameters mapped to subpath segments
	 */
	public String[] getSubpathSegmentParameterNames();
	
}
