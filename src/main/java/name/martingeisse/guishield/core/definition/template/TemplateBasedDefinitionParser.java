/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.definition.template;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import com.google.common.collect.ImmutableList;
import name.martingeisse.guishield.core.definition.ResourceDefinition;
import name.martingeisse.guishield.core.xml.content.ContentParser;

/**
 * Base class for all configuration elements that use a template,
 * such as pages or panels.
 */
public abstract class TemplateBasedDefinitionParser {

	/**
	 * the templateParser
	 */
	private final ContentParser<MarkupContent> templateParser;

	/**
	 * Constructor.
	 * @param templateParser the template parser
	 */
	public TemplateBasedDefinitionParser(ContentParser<MarkupContent> templateParser) {
		this.templateParser = templateParser;
	}

	/**
	 * Parses the resource definition.
	 * @param reader the XML stream reader
	 * @return the resource definition
	 * @throws XMLStreamException ...
	 */
	public ResourceDefinition parse(XMLStreamReader reader) throws XMLStreamException {

		// parse the file to obtain markup content
		reader.next();
		MarkupContent markupContent = templateParser.parse(reader);

		// assemble the final component configuration tree from the markup content
		String wicketMarkup;
		ComponentConfigurationList components;
		List<Snippet> snippetAccumulator = new ArrayList<>();
		try {
			StringWriter stringWriter = new StringWriter();
			XMLStreamWriter markupWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(stringWriter);
			markupWriter.writeStartDocument();
			writeWicketMarkupIntro(markupWriter);
			List<ComponentConfiguration> componentGroupAccumulator = new ArrayList<>();
			ConfigurationAssembler assembler = new ConfigurationAssembler(markupWriter, componentGroupAccumulator, snippetAccumulator);
			markupContent.assemble(assembler);
			writeWicketMarkupOutro(markupWriter);
			markupWriter.writeEndDocument();
			wicketMarkup = stringWriter.toString();
			components = new ComponentConfigurationList(ImmutableList.copyOf(componentGroupAccumulator));
		} catch (XMLStreamException e) {
			throw new RuntimeException(e);
		}

		// TODO
		System.out.println("------------------------------------------");
		System.out.println("wicket markup for configuration element");
		System.out.println();
		System.out.println(wicketMarkup);
		System.out.println("------------------------------------------");
		// TODO

		ImmutableList<Snippet> snippets = ImmutableList.copyOf(snippetAccumulator);
		return createResourceDefinition(new Template(wicketMarkup, components, snippets));
	}

	/**
	 * Writes the markup "intro" at the top of the wicket markup file.
	 * 
	 * @param markupWriter the XML writer that is used to write the markup file
	 * @throws XMLStreamException on XML processing errors
	 */
	protected abstract void writeWicketMarkupIntro(XMLStreamWriter markupWriter) throws XMLStreamException;
	
	/**
	 * Writes the markup "outro" at the bottom of the wicket markup file.
	 * 
	 * @param markupWriter the XML writer that is used to write the markup file
	 * @throws XMLStreamException on XML processing errors
	 */
	protected abstract void writeWicketMarkupOutro(XMLStreamWriter markupWriter) throws XMLStreamException;
	
	/**
	 * Creates the configuration element.
	 * 
	 * @param path the path
	 * @param template the template
	 * @return the configuration element
	 */
	protected abstract ResourceDefinition createResourceDefinition(Template template);
	
}
