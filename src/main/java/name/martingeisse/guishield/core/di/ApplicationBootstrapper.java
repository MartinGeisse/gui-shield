/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.di;

import java.util.Set;
import com.google.inject.Inject;

/**
 * This class continues the work that {@link BootstrappingServletContextListener} started.
 * In contrast to that listener, this bootstrapper can have its dependencies injected
 * by Guice.
 */
final class ApplicationBootstrapper {

	private final Set<ApplicationStartupListener> applicationStartupListeners;

	/**
	 * Constructor.
	 * @param applicationStartupListeners (injected)
	 */
	@Inject
	public ApplicationBootstrapper(final Set<ApplicationStartupListener> applicationStartupListeners) {
		this.applicationStartupListeners = applicationStartupListeners;
	}

	/**
	 * The main entry point to this class.
	 */
	void run() {
		for (ApplicationStartupListener applicationStartupListener : applicationStartupListeners) {
			applicationStartupListener.run();
		}
	}

}
