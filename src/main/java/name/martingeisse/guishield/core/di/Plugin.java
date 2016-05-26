/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.di;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.multibindings.Multibinder;

/**
 * A plugin for the Server-Blob. This class implements utility methods to
 * support the extension point system. Each plugin should extend this class
 * and add the provided extensions.
 */
public abstract class Plugin extends AbstractModule {

	/**
	 * Defines an extension point.
	 * 
	 * Each extension for the extension point defined here must be an implementation of
	 * the extension point interface. The set of extensions actually defined by plugins
	 * for this extension point can be obtained as an injected Set&lt;T&gt;.
	 * 
	 * @param extensionPointInterface the interface that extensions for the defined
	 * extension point must implement
	 */
	public <T> void defineExtensionPoint(Class<T> extensionPointInterface) {
		Multibinder.newSetBinder(binder(), extensionPointInterface);
	}
	
	/**
	 * Adds an extension for an extension point.
	 * 
	 * The extension must be an instantiable class that implements the extension point
	 * interface.
	 * 
	 * Creating a concrete implementation is done by Guice, so it will have its dependencies
	 * injected. Decorate the extension class with {@link Inject} and scope annotations
	 * as usual for Guice.
	 * 
	 * There is no requirement that the extension point gets defined *before* all its
	 * extensions, only that it gets defined at some point. Ensuring such an order would be
	 * impossible anyway since plugins get their {@link #configure()} method called in an
	 * undefined order.
	 * 
	 * @param extensionPointInterface the extension point interface
	 * @param extensionClass the concrete implementation class for this extension
	 */
	public <T> void extend(Class<T> extensionPointInterface, Class<? extends T> extensionClass) {
		Multibinder.newSetBinder(binder(), extensionPointInterface).addBinding().to(extensionClass);
	}
	
}
