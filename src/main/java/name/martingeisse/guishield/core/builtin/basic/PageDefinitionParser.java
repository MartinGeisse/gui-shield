/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.builtin.basic;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import name.martingeisse.guishield.core.definition.TemplateBasedDefinitionParser;
import name.martingeisse.guishield.core.definition.template.Template;
import name.martingeisse.guishield.core.xml.content.MarkupContentParser;

/**
 * Parses a page template.
 */
@Singleton
public class PageDefinitionParser extends TemplateBasedDefinitionParser {

	/**
	 * Constructor.
	 * @param templateParser the template parser
	 */
	@Inject
	public PageDefinitionParser(MarkupContentParser templateParser) {
		super(templateParser);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.element.xml.TemplateBasedElementParser#writeWicketMarkupIntro(javax.xml.stream.XMLStreamWriter)
	 */
	@Override
	protected void writeWicketMarkupIntro(XMLStreamWriter markupWriter) throws XMLStreamException {
//		markupWriter.writeStartElement("html");
//		markupWriter.writeStartElement("body");
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.element.xml.TemplateBasedElementParser#writeWicketMarkupOutro(javax.xml.stream.XMLStreamWriter)
	 */
	@Override
	protected void writeWicketMarkupOutro(XMLStreamWriter markupWriter) throws XMLStreamException {
//		markupWriter.writeEndElement();
//		markupWriter.writeEndElement();
	}

	// override
	@Override
	protected PageDefinition createResourceDefinition(Template template) {
		return new PageDefinition(template);
	}

}
