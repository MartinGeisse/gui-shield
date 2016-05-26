/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.definition;

import com.google.inject.Inject;

/**
 * This repository stores the definition of the GUI.
 *
 * TODO this class should cache the definitions
 */
public class DefinitionRepository {

	private final DefinitionLoader loader;

	/**
	 * Constructor.
	 * @param loader (injected)
	 */
	@Inject
	public DefinitionRepository(final DefinitionLoader loader) {
		this.loader = loader;
	}

	/**
	 * @param path the path
	 * @return the definition object
	 */
	public String getDefinition(final DefinitionPath path) {
		return loader.loadDefinition(path);
	}

}
