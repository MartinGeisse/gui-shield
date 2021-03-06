/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.component;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.resource.PackageResourceReference;

/**
 * Helper functions for use in head sections and header contributors.
 */
public class WicketHeadUtil {

	/**
	 * Prevent instantiation.
	 */
	private WicketHeadUtil() {
	}
	
	/**
	 * Renders a CSS reference to a file that is placed in the package of the
	 * specified class and whose name is the simple name of the class, with the ".css"
	 * extension appended. The reference is identified by the class's canonical name.
	 * 
	 * @param response the header response to which the reference shall be added
	 * @param c the class whose stylesheet shall be included
	 */
	public static void includeClassStylesheet(IHeaderResponse response, Class<?> c) {
		response.render(CssHeaderItem.forReference(new PackageResourceReference(c, c.getSimpleName() + ".css")));
	}
	
	/**
	 * Renders a Javascript reference to a file that is placed in the package of the
	 * specified class and whose name is the simple name of the class, with the ".js"
	 * extension appended. The reference is identified by the class's canonical name.
	 * 
	 * @param response the header response to which the reference shall be added
	 * @param c the class whose code shall be included
	 */
	public static void includeClassJavascript(IHeaderResponse response, Class<?> c) {
		response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(c, c.getSimpleName() + ".js")));
	}
	
}
