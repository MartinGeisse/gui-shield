/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.di;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.servlet.GuiceServletContextListener;

/**
 * This listener initializes Guice and the plugin system on servlet context start.
 */
public class BootstrappingServletContextListener extends GuiceServletContextListener {

    // override
    @Override
    protected Injector getInjector() {
    	
    	//
    	// TODO move plugin list to central class
    	//
    	Module[] modules = {
    		new MyServletModule(),
    		new CorePlugin(),
    	};
    	
    	Injector injector = Guice.createInjector(modules);
    	injector.getInstance(ApplicationBootstrapper.class).run();
        return injector;
    }

}

