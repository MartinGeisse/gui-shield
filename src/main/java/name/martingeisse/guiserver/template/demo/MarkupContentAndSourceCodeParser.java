/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.template.demo;

import java.io.StringWriter;
import java.io.Writer;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import name.martingeisse.guiserver.template.ComponentGroupConfiguration;
import name.martingeisse.guiserver.template.MarkupContent;
import name.martingeisse.guishield.core.xml.PrettyPrintXmlStreamWriter;
import name.martingeisse.guishield.core.xml.XmlStreamReaderTee;
import name.martingeisse.guishield.core.xml.content.ContentParser;

/**
 * A parser for {@link MarkupContentAndSourceCode} that wraps a parser for
 * {@link MarkupContent} and also copies the parsed XML code.
 */
public class MarkupContentAndSourceCodeParser implements ContentParser<MarkupContentAndSourceCode> {

	/**
	 * the wrappedParser
	 */
	private final ContentParser<MarkupContent<ComponentGroupConfiguration>> wrappedParser;

	/**
	 * Constructor.
	 * @param wrappedParser the wrapped content parser
	 */
	public MarkupContentAndSourceCodeParser(ContentParser<MarkupContent<ComponentGroupConfiguration>> wrappedParser) {
		this.wrappedParser = wrappedParser;
	}

	@Override
	public MarkupContentAndSourceCode parse(XMLStreamReader reader) throws XMLStreamException {
		StringWriter stringWriter = new StringWriter();
		XMLStreamWriter writer = buildXmlStreamWriter(stringWriter);
		MarkupContent<ComponentGroupConfiguration> markupContent = wrappedParser.parse(new XmlStreamReaderTee(reader, writer, true));
		String result = stringWriter.toString().replace(" xmlns:gui=\"http://guiserver.martingeisse.name/v1\"", "").trim();
		
		// TODO handle empty elements in the PrettyPrintXmlStreamWriter
		// unfortunately, XMLStreamWriter doesn't generate empty elements automatically, and we cannot easily
		// detect them while copying from a reader...
		result = result.replaceAll("(\\<[^\\<\\>\\/\\\"]+(?:\\\"[^\\\"]*\\\"[^\\<\\>\\/\\\"]*)*)\\>\\<\\/[^\\<\\>\\/]+\\>", "$1/>");
		
		return new MarkupContentAndSourceCode(markupContent, result);
	}
	
	/**
	 * 
	 */
	private XMLStreamWriter buildXmlStreamWriter(Writer w) throws XMLStreamException {
		XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
		xmlOutputFactory.setProperty("javax.xml.stream.isRepairingNamespaces", true);
		return new PrettyPrintXmlStreamWriter(xmlOutputFactory.createXMLStreamWriter(w), "    ");
	}

}
