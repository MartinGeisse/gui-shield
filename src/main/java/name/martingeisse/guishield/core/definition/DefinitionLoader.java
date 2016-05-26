/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.definition;

import com.google.inject.Inject;
import name.martingeisse.guishield.core.backend.BackendRequester;

/**
 * This class is glue code between the definition repository and the backend requester.
 * 
 * TODO leave interface in definition package, move implementation to backend package
 */
public class DefinitionLoader {

	private final BackendRequester requester;

	/**
	 * Constructor.
	 * @param requester (injected)
	 */
	@Inject
	public DefinitionLoader(final BackendRequester requester) {
		this.requester = requester;
	}

	/**
	 * Loads a definition.
	 * 
	 * @param path the path
	 * @return the definition
	 */
	public String loadDefinition(final DefinitionPath path) {
		return requester.requestDefinition(path);
	}

}
