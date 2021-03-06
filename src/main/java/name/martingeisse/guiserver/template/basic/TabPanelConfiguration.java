/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.template.basic;

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import name.martingeisse.guiserver.configuration.ConfigurationHolder;
import name.martingeisse.guiserver.template.IConfigurationSnippet;
import name.martingeisse.guiserver.template.other.UrlSubpathComponentGroupConfiguration;
import name.martingeisse.guishield.core.definition.template.AbstractSingleComponentConfiguration;
import name.martingeisse.guishield.core.definition.template.AbstractSingleContainerConfiguration;
import name.martingeisse.guishield.core.definition.template.ComponentConfiguration;
import name.martingeisse.guishield.core.definition.template.ConfigurationAssembler;
import name.martingeisse.guishield.core.xml.builder.StructuredElement;
import name.martingeisse.guishield.core.xml.generated.annotation.AttributeValueBindingOptionality;
import name.martingeisse.guishield.core.xml.generated.annotation.BindAttribute;
import name.martingeisse.guishield.core.xml.generated.annotation.BindElement;
import name.martingeisse.guishield.core.xml.generated.annotation.BindNestedElement;
import name.martingeisse.wicket.component.misc.PageParameterDrivenTabPanel;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.IMarkupSourcingStrategy;
import org.apache.wicket.markup.html.panel.PanelMarkupSourcingStrategy;

/**
 * Represents a tab panel.
 */
@StructuredElement
@BindElement(localName = "tabPanel")
public final class TabPanelConfiguration extends AbstractSingleComponentConfiguration implements IConfigurationSnippet, UrlSubpathComponentGroupConfiguration {

	/**
	 * the parameterName
	 */
	private String parameterName;

	/**
	 * the tabs
	 */
	private List<TabEntry> tabs = new ArrayList<TabEntry>();

	/**
	 * the snippetHandle
	 */
	private int snippetHandle;

	/**
	 * Getter method for the parameterName.
	 * @return the parameterName
	 */
	public String getParameterName() {
		String id = getComponentId();
		if (id == null) {
			throw new IllegalStateException("cannot determine parameter name before an ID has been assigned");
		}
		return (parameterName == null ? id : parameterName);
	}
	
	/**
	 * Setter method for the parameterName.
	 * @param parameterName the parameterName to set
	 */
	@BindAttribute(name = "parameter", optionality = AttributeValueBindingOptionality.OPTIONAL)
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
	
	/**
	 * Adds a tab.
	 * @param tab the tab to add
	 */
	@BindNestedElement(localName = "tab")
	public void addTab(TabEntry tab) {
		tabs.add(tab);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.AbstractComponentConfiguration#assemble(name.martingeisse.guiserver.xmlbind.result.ConfigurationAssembler)
	 */
	@Override
	public void assemble(ConfigurationAssembler<ComponentConfiguration> assembler) throws XMLStreamException {
		super.assemble(assembler);
		XMLStreamWriter writer = assembler.getMarkupWriter();
		String id = getComponentId();
		writer.writeStartElement("div");
		writer.writeAttribute("wicket:id", id + "-container");
		writer.writeEmptyElement("div");
		writer.writeAttribute("wicket:id", id);
		for (TabEntry tab : tabs) {
			tab.assemble(assembler);
		}
		writer.writeEndElement();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.IConfigurationSnippet#setSnippetHandle(int)
	 */
	@Override
	public void setSnippetHandle(int handle) {
		this.snippetHandle = handle;
	}

	/**
	 * Getter method for the snippetHandle.
	 * @return the snippetHandle
	 */
	public int getSnippetHandle() {
		return snippetHandle;
	}

	/**
	 * Getter method for the tabs.
	 * @return the tabs
	 */
	List<TabEntry> getTabs() {
		return tabs;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.ComponentConfiguration#buildComponent()
	 */
	@Override
	public Component buildComponent() {
		final WebMarkupContainer tabPanelContainer = new WebMarkupContainer(getComponentId() + "-container");
		final PageParameterDrivenTabPanel tabPanel = new MyTabPanel(getComponentId(), getParameterName(), this, tabPanelContainer);
		for (TabEntry tab : tabs) {
			tabPanel.addTab(tab.getTabInfo());
		}
		tabPanelContainer.add(tabPanel);
		return tabPanelContainer;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.UrlSubpathComponentConfiguration#getSubpathSegmentParameterNames()
	 */
	@Override
	public String[] getSubpathSegmentParameterNames() {
		return new String[] {
			getParameterName()
		};
	}

	/**
	 * Represents a tab in the panel.
	 */
	@StructuredElement
	public static final class TabEntry extends AbstractSingleContainerConfiguration {

		/**
		 * the title
		 */
		private String title;
		
		/**
		 * the selector
		 */
		private String selector;

		/**
		 * the tabInfo
		 */
		private PageParameterDrivenTabPanel.AbstractTabInfo tabInfo;

		/**
		 * Getter method for the title.
		 * @return the title
		 */
		public String getTitle() {
			return title;
		}
		
		/**
		 * Setter method for the title.
		 * @param title the title to set
		 */
		@BindAttribute(name = "title")
		public void setTitle(String title) {
			this.title = title;
		}
		
		/**
		 * Getter method for the selector.
		 * @return the selector
		 */
		public String getSelector() {
			return selector;
		}
		
		/**
		 * Setter method for the selector.
		 * @param selector the selector to set
		 */
		@BindAttribute(name = "selector")
		public void setSelector(String selector) {
			this.selector = selector;
		}
		
		/**
		 * Getter method for the tabInfo.
		 * @return the tabInfo
		 */
		public PageParameterDrivenTabPanel.AbstractTabInfo getTabInfo() {
			if (tabInfo == null) {
				tabInfo = new PageParameterDrivenTabPanel.TabInfo(title, selector);
			}
			return tabInfo;
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.guiserver.configuration.content.AbstractContainerConfiguration#assembleContainerIntro(name.martingeisse.guiserver.xmlbind.result.ConfigurationAssembler)
		 */
		@Override
		protected void assembleContainerIntro(ConfigurationAssembler<ComponentConfiguration> assembler) throws XMLStreamException {
			writeOpeningComponentTag(assembler, "wicket:fragment");
		}
		
		/* (non-Javadoc)
		 * @see name.martingeisse.guiserver.configurationNew.content.ComponentConfiguration#buildComponent()
		 */
		@Override
		public Component buildComponent() {
			return null;
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.guiserver.configuration.content.AbstractContainerConfiguration#buildContainer()
		 */
		@Override
		protected MarkupContainer buildContainer() {
			throw new UnsupportedOperationException();
		}

		/**
		 * Builds the component for a TabEntry.
		 * 
		 * @return the component
		 */
		public MarkupContainer buildTabEntry(String callingMarkupId, MarkupContainer markupProvider) {
			MarkupContainer container = new Fragment(callingMarkupId, getComponentId(), markupProvider);
			getChildren().buildAndAddComponents(container);
			return container;
		}

	}

	/**
	 * Specialized tab panel implementation.
	 */
	public static final class MyTabPanel extends PageParameterDrivenTabPanel {

		/**
		 * the markupProvider
		 */
		private final WebMarkupContainer markupProvider;

		/**
		 * the snippetHandle
		 */
		private final int snippetHandle;

		/**
		 * the cachedTabPanelConfiguration
		 */
		private transient TabPanelConfiguration cachedTabPanelConfiguration;

		/**
		 * Constructor.
		 * @param id the wicket id
		 * @param parameterName the name of the page parameter that contains the tab selector
		 * @param tabPanelConfiguration the configuration object
		 * @param markupProvider the component that contains the markup fragments for the tabs
		 */
		public MyTabPanel(String id, String parameterName, TabPanelConfiguration tabPanelConfiguration, WebMarkupContainer markupProvider) {
			super(id, parameterName);
			this.snippetHandle = tabPanelConfiguration.getSnippetHandle();
			this.cachedTabPanelConfiguration = tabPanelConfiguration;
			this.markupProvider = markupProvider;
		}

		/**
		 * Getter method for the tab panel configuration.
		 * @return the tab panel configuration.
		 */
		public final TabPanelConfiguration getTabPanelConfiguration() {
			if (cachedTabPanelConfiguration == null) {
				cachedTabPanelConfiguration = (TabPanelConfiguration)ConfigurationHolder.needRequestUniverseConfiguration().getSnippet(snippetHandle);
			}
			return cachedTabPanelConfiguration;
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.wicket.component.misc.PageParameterDrivenTabPanel#createBody(java.lang.String, java.lang.String)
		 */
		@Override
		protected Component createBody(String id, String selector) {
			for (TabEntry tabEntry : getTabPanelConfiguration().getTabs()) {
				AbstractTabInfo tabInfo = tabEntry.getTabInfo();
				if (tabInfo instanceof TabInfo) {
					if (selector.equals(((TabInfo)tabInfo).getSelector())) {
						return tabEntry.buildTabEntry(id, markupProvider);
					}
				} else {
					throw new RuntimeException("cannot handle AbstractTabInfo objects other than TabInfo right now");
				}
			}
			return new EmptyPanel(id);
		}

		/* (non-Javadoc)
		 * @see org.apache.wicket.markup.html.panel.Panel#newMarkupSourcingStrategy()
		 */
		@Override
		protected IMarkupSourcingStrategy newMarkupSourcingStrategy() {
			return new PanelMarkupSourcingStrategy(true);
		}

	}

}
