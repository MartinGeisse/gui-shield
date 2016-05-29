/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.builtin.basic;

import name.martingeisse.guiserver.configuration.ConfigurationHolder;
import name.martingeisse.guishield.core.definition.template.Snippet;
import name.martingeisse.guishield.core.definition.template.SnippetHolder;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.IMarkupCacheKeyProvider;
import org.apache.wicket.markup.IMarkupResourceStreamProvider;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.StringResourceStream;


TODO a panel is a snippet holder, and its configuration is a snippet. Should
the search for a snippet holder start at its parent? How exactly does the whole thing work?
	
/**
 * This component gets used for a gui:panel reference to a
 * gui:panel definition in a panel template.
 */
public class ConfigurationDefinedPanel extends Panel implements IMarkupCacheKeyProvider, IMarkupResourceStreamProvider, SnippetHolder {

	/**
	 * the panelReferenceConfigurationHandle
	 */
	private final int panelReferenceConfigurationHandle;

	/**
	 * the cachedPanelReferenceConfiguration
	 */
	private transient PanelReferenceConfiguration cachedPanelReferenceConfiguration;

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param panelReferenceConfiguration the panel reference configuration
	 */
	public ConfigurationDefinedPanel(String id, PanelReferenceConfiguration panelReferenceConfiguration) {
		super(id);
		this.panelReferenceConfigurationHandle = panelReferenceConfiguration.getSnippetHandle();
		this.cachedPanelReferenceConfiguration = panelReferenceConfiguration;
	}

	/**
	 * Getter method for the panel configuration.
	 * @return the panel configuration.
	 */
	public final PanelReferenceConfiguration getPanelReferenceConfiguration() {
		if (cachedPanelReferenceConfiguration == null) {
			cachedPanelReferenceConfiguration = (PanelReferenceConfiguration)ConfigurationHolder.needRequestUniverseConfiguration().getSnippet(panelReferenceConfigurationHandle);
		}
		return cachedPanelReferenceConfiguration;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.IMarkupCacheKeyProvider#getCacheKey(org.apache.wicket.MarkupContainer, java.lang.Class)
	 */
	@Override
	public String getCacheKey(MarkupContainer container, Class<?> containerClass) {
		if (container != this) {
			throw new IllegalArgumentException("a UserDefinedPanel cannot be used to provide a markup cache key for other components than itself");
		}
		return ConfigurationHolder.needRequestUniverseConfiguration().getSerialNumber() + ':' + getClass().getName() + ':' + getPanelReferenceConfiguration().getPanelConfiguration();
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.IMarkupResourceStreamProvider#getMarkupResourceStream(org.apache.wicket.MarkupContainer, java.lang.Class)
	 */
	@Override
	public IResourceStream getMarkupResourceStream(MarkupContainer container, Class<?> containerClass) {
		if (container != this) {
			throw new IllegalArgumentException("a UserDefinedPanel cannot be used to provide a markup resource stream for other components than itself");
		}
		return new StringResourceStream(getPanelReferenceConfiguration().getPanelConfiguration().getTemplate().getWicketMarkup());
	}

	
	// override
	@Override
	public Snippet getSnippet(int snippetHandle) {
		loadDefinition();
		return cacheddef.getTemplate().getSnippets().get(snippetHandle);
	}

}
