/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.template.model;

import name.martingeisse.guiserver.backend.BackendHttpClient;
import name.martingeisse.guishield.core.xml.builder.StructuredElement;
import name.martingeisse.guishield.core.xml.generated.annotation.BindAttribute;
import name.martingeisse.guishield.core.xml.generated.annotation.BindElement;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 *
 */
@StructuredElement
@BindElement(localName = "backendJsonModel")
public class BackendJsonModelConfiguration extends AbstractModelConfiguration {

	/**
	 * the url
	 */
	private String url;

	/**
	 * Setter method for the url.
	 * @param url the url to set
	 */
	@BindAttribute(name = "url")
	public void setUrl(String url) {
		this.url = url;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.template.model.AbstractModelConfiguration#buildModel()
	 */
	@Override
	protected IModel<?> buildModel() {
		return new MyModel(url);
	}

	/**
	 * The model implementation.
	 */
	private static final class MyModel extends LoadableDetachableModel<Object> {

		/**
		 * the url
		 */
		private final String url;

		/**
		 * Constructor.
		 * @param url the URL to load from
		 */
		public MyModel(String url) {
			this.url = url;
		}

		/* (non-Javadoc)
		 * @see org.apache.wicket.model.LoadableDetachableModel#load()
		 */
		@Override
		protected Object load() {
			return BackendHttpClient.getJson(url).getValue();
		}

	}
}
