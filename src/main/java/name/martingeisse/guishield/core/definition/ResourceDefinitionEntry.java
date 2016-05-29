/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.definition;

/**
 * Wraps a {@link ResourceDefinition}.
 */
public final class ResourceDefinitionEntry {

	private final DefinitionPath path;
	private final int serialNumber;
	private final ResourceDefinition definition;

	/**
	 * Constructor.
	 */
	ResourceDefinitionEntry(final DefinitionPath path, final int serialNumber, final ResourceDefinition definition) {
		this.path = path;
		this.serialNumber = serialNumber;
		this.definition = definition;
	}

	/**
	 * Getter method for the path.
	 * @return the path
	 */
	public DefinitionPath getPath() {
		return path;
	}

	/**
	 * Getter method for the serialNumber.
	 * @return the serialNumber
	 */
	public int getSerialNumber() {
		return serialNumber;
	}

	/**
	 * Getter method for the definition.
	 * @return the definition
	 */
	public ResourceDefinition getDefinition() {
		return definition;
	}

}
