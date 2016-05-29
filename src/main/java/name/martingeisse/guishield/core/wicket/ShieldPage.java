/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.wicket;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.IMarkupCacheKeyProvider;
import org.apache.wicket.markup.IMarkupResourceStreamProvider;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.StringResourceStream;
import name.martingeisse.guishield.core.builtin.basic.PageDefinition;
import name.martingeisse.guishield.core.definition.DefinitionPath;
import name.martingeisse.guishield.core.definition.DefinitionRepository;
import name.martingeisse.guishield.core.definition.ResourceDefinitionEntry;

/**
 * <p>
 * This is the main page class that uses a page definition loaded from a remote server.
 * </p>
 * <p>
 * Note: This class must be final because the request mapper currently can't handle subclasses.
 * </p>
 */
public final class ShieldPage extends WebPage implements IMarkupCacheKeyProvider, IMarkupResourceStreamProvider {

	/**
	 *
	 */
	public static final String PAGE_DEFINITION_PATH_PARAMETER_NAME = "__gui_server__page_definition_path__";
	
	private final DefinitionPath definitionPath;
	private transient ResourceDefinitionEntry cachedResourceDefinitionEntry = null;
	private transient PageDefinition cachedPageDefinition = null;
	
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
		definitionPath = new DefinitionPath(definitionPathText);
		
		// load the definition for this page
		loadDefinition();
		
	}

	private void loadDefinition() {
		if (cachedResourceDefinitionEntry == null) {
			cachedResourceDefinitionEntry = MyWicketApplication.get().getDependency(DefinitionRepository.class).getDefinition(definitionPath);
			cachedPageDefinition = null;
		}
		if (cachedPageDefinition == null) {
			if (!(cachedResourceDefinitionEntry.getDefinition() instanceof PageDefinition)) {
				throw new AbortWithHttpErrorCodeException(404);
			}
			cachedPageDefinition = (PageDefinition)cachedResourceDefinitionEntry.getDefinition();
		}
	}

	// override
	@Override
	protected void onInitialize() {
		super.onInitialize();
		loadDefinition();
		cachedPageDefinition.getTemplate().getComponents().buildAndAddComponents(this);
	}

	// override
	@Override
	public String getCacheKey(MarkupContainer container, Class<?> containerClass) {
		if (container != this) {
			throw new IllegalArgumentException("a ShieldPage cannot be used to provide a markup cache key for other components than itself");
		}
		loadDefinition();
		return "definition:" + definitionPath + ':' + cachedResourceDefinitionEntry.getSerialNumber();
	}

	// override
	@Override
	public IResourceStream getMarkupResourceStream(MarkupContainer container, Class<?> containerClass) {
		if (container != this) {
			throw new IllegalArgumentException("a ShieldPage cannot be used to provide a markup resource stream for other components than itself");
		}
		loadDefinition();
		return new StringResourceStream(cachedPageDefinition.getTemplate().getWicketMarkup());
	}

}
