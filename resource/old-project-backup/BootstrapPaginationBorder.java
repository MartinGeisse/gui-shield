/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.component.pagination;

import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.model.IModel;

/**
 * Shows pagination controls before and after the border body.
 */
public class BootstrapPaginationBorder extends Border {

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the model
	 */
	public BootstrapPaginationBorder(String id, IModel<PaginationResponseData> model) {
		super(id, model);
	}

	/**
	 * Getter method for the model.
	 * @return the model
	 */
	@SuppressWarnings("unchecked")
	public IModel<PaginationResponseData> getModel() {
		return (IModel<PaginationResponseData>)getDefaultModel();
	}

	/**
	 * Setter method for the model.
	 * @param model the model to set
	 */
	public void setModel(IModel<PaginationResponseData> model) {
		setDefaultModel(model);
	}
	
}
