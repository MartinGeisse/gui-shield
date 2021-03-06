/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.wicket;

import org.apache.wicket.protocol.http.IWebApplicationFactory;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WicketFilter;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Extends the regular Wicket filter to create the application object using
 * Guice's binding for {@link WebApplication}.
 */
public class MyWicketFilter extends WicketFilter {

    private final Provider<WebApplication> applicationProvider;

    /**
     * Constructor.
     * @param applicationProvider the provider for the Wicket {@link WebApplication}
     */
    @Inject
    public MyWicketFilter(Provider<WebApplication> applicationProvider) {
        this.applicationProvider = applicationProvider;
    }

    @Override
    protected IWebApplicationFactory getApplicationFactory() {
        return new IWebApplicationFactory() {

            @Override
            public WebApplication createApplication(WicketFilter filter) {
                return applicationProvider.get();
            }

            @Override
            public void destroy(WicketFilter filter) {
            }

        };
    }

}
