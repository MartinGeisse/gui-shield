/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.di;

import name.martingeisse.guishield.core.xml.element.RegisteredComponentParser;
import name.martingeisse.guishield.core.xml.value.BuiltinValueParserProvider;
import name.martingeisse.guishield.core.xml.value.RegisteredValueParserProvider;

/**
 * This is a pseudo-plugin that adds the core extension points and extensions.
 */
public final class CorePlugin extends Plugin {

	// override
	@Override
	protected void configure() {
		
		defineExtensionPoint(ApplicationStartupListener.class);
		
		defineExtensionPoint(RegisteredValueParserProvider.class);
		extend(RegisteredValueParserProvider.class, BuiltinValueParserProvider.class);
		
		defineExtensionPoint(RegisteredComponentParser.class);
		// TODO standard components
		
	}

}
