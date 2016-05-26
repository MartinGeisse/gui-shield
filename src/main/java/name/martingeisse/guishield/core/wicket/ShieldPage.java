/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.wicket;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import name.martingeisse.guishield.core.definition.DefinitionPath;
import name.martingeisse.guishield.core.definition.DefinitionRepository;

/**
 * <p>
 * This is the main page class that uses a page definition loaded from a remote server.
 * </p>
 * <p>
 * Note: This class must be final because the request mapper currently can't handle subclasses.
 * </p>
 */
public final class ShieldPage extends WebPage {

	/**
	 *
	 */
	public static final String PAGE_DEFINITION_PATH_PARAMETER_NAME = "__gui_server__page_definition_path__";
	
	/**
	 * Constructor.
	 * 
	 * @param parameters the page parameters
	 */
	public ShieldPage(final PageParameters parameters) {
		super(parameters);

		// extract the definition path
		String definitionPathText = parameters.get(PAGE_DEFINITION_PATH_PARAMETER_NAME).toString();
		if (definitionPathText == null) {
			throw new IllegalArgumentException("definition path parameter is missing");
		}
		DefinitionPath definitionPath = new DefinitionPath(definitionPathText);
		
		// load the definition for this page
		String definition = MyWicketApplication.get().getDependency(DefinitionRepository.class).getDefinition(definitionPath);
		if (definition == null) {
			throw new AbortWithHttpErrorCodeException(404);
		}
		
		// TODO
		add(new Label("definition", definition));
		
	}
}
