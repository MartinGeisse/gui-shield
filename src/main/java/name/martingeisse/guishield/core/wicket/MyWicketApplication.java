/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.wicket;

import java.util.Set;
import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.mapper.parameter.PageParametersEncoder;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.util.Types;
import name.martingeisse.guishield.core.builtin.basic.ConfigurationDefinedPage;

/**
 * The Wicket {@link WebApplication} implementation.
 */
public class MyWicketApplication extends WebApplication {

	/**
	 * the RENDER_PROFILING
	 */
	public static final boolean RENDER_PROFILING = false;

	private final Injector injector;

	/**
	 * Constructor.
	 * @param injector the Guice injector
	 */
	@Inject
	public MyWicketApplication(final Injector injector) {
		this.injector = injector;
	}

	/**
	 * Retrieves a dependency from the injector.
	 *
	 * @param type the type of the object to retrieve
	 * @return the object
	 */
	public <T> T getDependency(final Class<T> type) {
		return injector.getInstance(type);
	}

	/**
	 * Retrieves a set of dependencies from the injector.
	 *
	 * @param type the type of the objects to retrieve
	 * @return the objects
	 */
	public <T> Set<T> getDependencies(final Class<T> type) {
		@SuppressWarnings("unchecked")
		TypeLiteral<Set<T>> typeLiteral = (TypeLiteral<Set<T>>)TypeLiteral.get(Types.setOf(type));
		return injector.getInstance(Key.get(typeLiteral));
	}

	/**
	 * Getter method for the injector.
	 * @return the injector
	 */
	public Injector getInjector() {
		return injector;
	}
	
	/**
	 * @return the application
	 */
	public static MyWicketApplication get() {
		return (MyWicketApplication)WebApplication.get();
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.protocol.http.WebApplication#init()
	 */
	@Override
	protected void init() {
		super.init();
		getMarkupSettings().setStripWicketTags(true);
		getMarkupSettings().setDefaultMarkupEncoding("utf-8");
		mount(new MyRequestMapper(new PageParametersEncoder()));
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<? extends Page> getHomePage() {
		return ConfigurationDefinedPage.class;
	}

}
