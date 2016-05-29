/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.template.other;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.apache.wicket.validation.IValidator;
import name.martingeisse.guiserver.template.basic.EchoTextConfiguration;
import name.martingeisse.guiserver.template.basic.EnclosureConfiguration;
import name.martingeisse.guiserver.template.basic.IfConfiguration;
import name.martingeisse.guiserver.template.basic.IncludeBackendConfiguration;
import name.martingeisse.guiserver.template.basic.LazyLoadContainerConfiguration;
import name.martingeisse.guiserver.template.basic.LinkConfiguration;
import name.martingeisse.guiserver.template.basic.ListViewConfiguration;
import name.martingeisse.guiserver.template.basic.PieChartConfiguration;
import name.martingeisse.guiserver.template.basic.TabPanelConfiguration;
import name.martingeisse.guiserver.template.basic.form.CheckboxConfiguration;
import name.martingeisse.guiserver.template.basic.form.FieldPathFeedbackPanelConfiguration;
import name.martingeisse.guiserver.template.basic.form.FormConfiguration;
import name.martingeisse.guiserver.template.basic.form.SubmitButtonConfiguration;
import name.martingeisse.guiserver.template.basic.form.TextFieldConfiguration;
import name.martingeisse.guiserver.template.basic.form.ValidatorParser;
import name.martingeisse.guiserver.template.demo.ComponentDemoConfiguration;
import name.martingeisse.guiserver.template.demo.MarkupContentAndSourceCode;
import name.martingeisse.guiserver.template.demo.MarkupContentAndSourceCodeParser;
import name.martingeisse.guiserver.template.model.BackendJsonModelConfiguration;
import name.martingeisse.guishield.core.builtin.basic.PanelReferenceConfiguration;
import name.martingeisse.guishield.core.definition.template.ComponentConfiguration;
import name.martingeisse.guishield.core.definition.template.MarkupContent;
import name.martingeisse.guishield.core.xml.content.ContentParser;
import name.martingeisse.guishield.core.xml.oldstuff.DelegatingContentParser;
import name.martingeisse.guishield.core.xml.value.BooleanValueParser;
import name.martingeisse.guishield.core.xml.value.IntegerValueParser;
import name.martingeisse.guishield.core.xml.value.StringValueParser;

/**
 * Parses the templates for user-defined pages, panels, and so on.
 */
public final class TemplateParser implements ContentParser<MarkupContent<ComponentConfiguration>> {

	/**
	 * the INSTANCE
	 */
	public static final TemplateParser INSTANCE = new TemplateParser();

	/**
	 * the parser
	 */
	private final ContentParser<MarkupContent<ComponentConfiguration>> parser;

	/**
	 * Constructor.
	 */
	public TemplateParser() {
		try {

			// register known element parsers
			@SuppressWarnings("unchecked")
			Class<IValidator<?>> validatorClass = (Class<IValidator<?>>)(Class<?>)IValidator.class;
			builder.addElementParser(validatorClass, new ValidatorParser(builder));

			// register known content parsers
			builder.addContentParser(MarkupContentAndSourceCode.class, new MarkupContentAndSourceCodeParser(recursiveMarkupParser));

			// known component special tags
			builder.autoAddComponentElementParser(EchoTextConfiguration.class);
			builder.autoAddComponentElementParser(EnclosureConfiguration.class);
			builder.autoAddComponentElementParser(IncludeBackendConfiguration.class);
			builder.autoAddComponentElementParser(LazyLoadContainerConfiguration.class);
			builder.autoAddComponentElementParser(LinkConfiguration.class);
			builder.autoAddComponentElementParser(FieldPathFeedbackPanelConfiguration.class);
			builder.autoAddComponentElementParser(FormConfiguration.class);
			builder.autoAddComponentElementParser(PieChartConfiguration.class);
			builder.autoAddComponentElementParser(SubmitButtonConfiguration.class);
			builder.autoAddComponentElementParser(TabPanelConfiguration.class);
			builder.autoAddComponentElementParser(TextFieldConfiguration.class);
			builder.autoAddComponentElementParser(CheckboxConfiguration.class);
			builder.autoAddComponentElementParser(PanelReferenceConfiguration.class);
			builder.autoAddComponentElementParser(ComponentDemoConfiguration.class);
			builder.autoAddComponentElementParser(ListViewConfiguration.class);
			builder.autoAddComponentElementParser(IfConfiguration.class);

			// models
			builder.autoAddComponentElementParser(BackendJsonModelConfiguration.class);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
