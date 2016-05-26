/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.di;


/**
 * This is an extension point that allows plugins to run code at application startup.
 */
public interface ApplicationStartupListener extends Runnable {
}
