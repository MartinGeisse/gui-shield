/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.di;

import name.martingeisse.guiserver.xml.value.BuiltinStandardValueParserProvider;
import name.martingeisse.guiserver.xml.value.StandardValueParserProvider;

/**
 * This is a pseudo-plugin that adds the core extension points and extensions.
 */
public final class CorePlugin extends Plugin {

	// override
	@Override
	protected void configure() {
		defineExtensionPoint(ApplicationStartupListener.class);
		defineExtensionPoint(StandardValueParserProvider.class);
		extend(StandardValueParserProvider.class, BuiltinStandardValueParserProvider.class);
	}

}
