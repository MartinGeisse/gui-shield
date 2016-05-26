/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.xml;

import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang3.StringUtils;

/**
 * Wraps another XML stream writer and pretty-prints the output.
 */
public class PrettyPrintXmlStreamWriter implements XMLStreamWriter {

	/**
	 * Formatter states
	 */
	private static enum State {
		START_OF_LINE,
		INDENTED,
		RIGHT_AFTER_TAG,
		WITHIN_TEXT
	}
	
	/**
	 * the WHITESPACE
	 */
	private static final String WHITESPACE = " \t\n\r";
	
	/**
	 * the wrapped
	 */
	private final XMLStreamWriter wrapped;

	/**
	 * the indentationSegment
	 */
	private final String indentationSegment;

	/**
	 * the indentationLevel
	 */
	private int indentationLevel;
	
	/**
	 * the rightAfterTag
	 */
	private State state;
	
	/**
	 * the wasStartTag
	 */
	private boolean wasStartTag;
	
	/**
	 * the retainedWhitespace
	 */
	private String retainedWhitespace;

	/**
	 * Constructor.
	 * @param wrapped the wrapped XML stream writer
	 * @param indentationSegment a single segment to use for indentation
	 */
	public PrettyPrintXmlStreamWriter(XMLStreamWriter wrapped, String indentationSegment) {
		this.wrapped = wrapped;
		this.indentationSegment = indentationSegment;
		this.indentationLevel = 0;
		this.state = State.START_OF_LINE;
		this.wasStartTag = false;
		this.retainedWhitespace = null;
	}

	/**
	 * Getter method for the wrapped.
	 * @return the wrapped
	 */
	public XMLStreamWriter getWrapped() {
		return wrapped;
	}

	private void writeIndentation() throws XMLStreamException {
		for (int i=0; i<indentationLevel; i++) {
			wrapped.writeCharacters(indentationSegment);
		}
	}
	
	/**
	 * 
	 */
	private void prepareIndented() throws XMLStreamException {
		if (state != State.START_OF_LINE) {
			wrapped.writeCharacters("\n");
		}
		writeIndentation();
		state = State.INDENTED;
	}
	
	/**
	 * 
	 */
	private void onBeforeStartingTag() throws XMLStreamException {
		retainedWhitespace = null;
		prepareIndented();
	}

	/**
	 * 
	 */
	private void onAfterStartingTag() throws XMLStreamException {
		indentationLevel++;
		state = State.RIGHT_AFTER_TAG;
		wasStartTag = true;
	}

	/**
	 * 
	 */
	private void onBeforeEndingTag() throws XMLStreamException {
		retainedWhitespace = null;
		indentationLevel--;
		if (state == State.RIGHT_AFTER_TAG && wasStartTag) {
			// append directly after the start tag; will be collapsed to an open-close element
		} else {
			prepareIndented();
		}
	}

	/**
	 * 
	 */
	private void onAfterEndingTag() throws XMLStreamException {
		state = State.RIGHT_AFTER_TAG;
		wasStartTag = false;
	}

	/**
	 * 
	 */
	private void flushRetainedWhitespace() throws XMLStreamException {
		if (retainedWhitespace != null) {
			wrapped.writeCharacters(retainedWhitespace);
			retainedWhitespace = null;
		}
	}
	
	/**
	 * @param text
	 */
	private void writeText(String text) throws XMLStreamException {
		if (state == State.RIGHT_AFTER_TAG) {
			text = StringUtils.stripStart(text, WHITESPACE);
			if (text.isEmpty()) {
				return;
			}
		}
		if (state != State.WITHIN_TEXT) {
			prepareIndented();
		}
		flushRetainedWhitespace();
		{
			String oldText = text;
			text = StringUtils.stripEnd(text, WHITESPACE);
			if (text.length() != oldText.length()) {
				retainedWhitespace = oldText.substring(text.length());
			}
		}
		wrapped.writeCharacters(text);
		state = State.WITHIN_TEXT;
	}
	
	/**
	 * 
	 */
	private void onBeforeInvincibleText() throws XMLStreamException {
		flushRetainedWhitespace();
		if (state != State.WITHIN_TEXT) {
			prepareIndented();
		}
	}
	
	/**
	 * 
	 */
	private void onAfterInvincibleText() throws XMLStreamException {
		state = State.WITHIN_TEXT;
	}

	// override
	@Override
	public void writeStartElement(String localName) throws XMLStreamException {
		onBeforeStartingTag();
		wrapped.writeStartElement(localName);
		onAfterStartingTag();
	}

	// override
	@Override
	public void writeStartElement(String namespaceURI, String localName) throws XMLStreamException {
		onBeforeStartingTag();
		wrapped.writeStartElement(namespaceURI, localName);
		onAfterStartingTag();
	}

	// override
	@Override
	public void writeStartElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
		onBeforeStartingTag();
		wrapped.writeStartElement(prefix, localName, namespaceURI);
		onAfterStartingTag();
	}

	// override
	@Override
	public void writeEmptyElement(String namespaceURI, String localName) throws XMLStreamException {
		onBeforeStartingTag();
		wrapped.writeEmptyElement(namespaceURI, localName);
		onAfterEndingTag();
	}

	// override
	@Override
	public void writeEmptyElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
		onBeforeStartingTag();
		wrapped.writeEmptyElement(prefix, localName, namespaceURI);
		onAfterEndingTag();
	}

	// override
	@Override
	public void writeEmptyElement(String localName) throws XMLStreamException {
		onBeforeStartingTag();
		wrapped.writeEmptyElement(localName);
		onAfterEndingTag();
	}

	// override
	@Override
	public void writeEndElement() throws XMLStreamException {
		onBeforeEndingTag();
		wrapped.writeEndElement();
		onAfterEndingTag();
	}

	// override
	@Override
	public void writeEndDocument() throws XMLStreamException {
		wrapped.writeEndDocument();
	}

	// override
	@Override
	public void close() throws XMLStreamException {
		wrapped.close();
	}

	// override
	@Override
	public void flush() throws XMLStreamException {
		wrapped.flush();
	}

	// override
	@Override
	public void writeAttribute(String localName, String value) throws XMLStreamException {
		wrapped.writeAttribute(localName, value);
	}

	// override
	@Override
	public void writeAttribute(String prefix, String namespaceURI, String localName, String value) throws XMLStreamException {
		wrapped.writeAttribute(prefix, namespaceURI, localName, value);
	}

	// override
	@Override
	public void writeAttribute(String namespaceURI, String localName, String value) throws XMLStreamException {
		wrapped.writeAttribute(namespaceURI, localName, value);
	}

	// override
	@Override
	public void writeNamespace(String prefix, String namespaceURI) throws XMLStreamException {
		wrapped.writeNamespace(prefix, namespaceURI);
	}

	// override
	@Override
	public void writeDefaultNamespace(String namespaceURI) throws XMLStreamException {
		wrapped.writeDefaultNamespace(namespaceURI);
	}

	// override
	@Override
	public void writeComment(String data) throws XMLStreamException {
		onBeforeStartingTag();
		wrapped.writeComment(data);
		onAfterEndingTag();
	}

	// override
	@Override
	public void writeProcessingInstruction(String target) throws XMLStreamException {
		onBeforeStartingTag();
		wrapped.writeProcessingInstruction(target);
		onAfterEndingTag();
	}

	// override
	@Override
	public void writeProcessingInstruction(String target, String data) throws XMLStreamException {
		onBeforeStartingTag();
		wrapped.writeProcessingInstruction(target, data);
		onAfterEndingTag();
	}

	// override
	@Override
	public void writeCData(String data) throws XMLStreamException {
		onBeforeInvincibleText();
		wrapped.writeCData(data);
		onAfterInvincibleText();
	}

	// override
	@Override
	public void writeDTD(String dtd) throws XMLStreamException {
		wrapped.writeDTD(dtd);
	}

	// override
	@Override
	public void writeEntityRef(String name) throws XMLStreamException {
		onBeforeInvincibleText();
		wrapped.writeEntityRef(name);
		onAfterInvincibleText();
	}

	// override
	@Override
	public void writeStartDocument() throws XMLStreamException {
		wrapped.writeStartDocument();
	}

	// override
	@Override
	public void writeStartDocument(String version) throws XMLStreamException {
		wrapped.writeStartDocument(version);
	}

	// override
	@Override
	public void writeStartDocument(String encoding, String version) throws XMLStreamException {
		wrapped.writeStartDocument(encoding, version);
	}

	// override
	@Override
	public void writeCharacters(String text) throws XMLStreamException {
		writeText(text);
	}

	// override
	@Override
	public void writeCharacters(char[] text, int start, int len) throws XMLStreamException {
		writeText(new String(text, start, len));
	}

	// override
	@Override
	public String getPrefix(String uri) throws XMLStreamException {
		return wrapped.getPrefix(uri);
	}

	// override
	@Override
	public void setPrefix(String prefix, String uri) throws XMLStreamException {
		wrapped.setPrefix(prefix, uri);
	}

	// override
	@Override
	public void setDefaultNamespace(String uri) throws XMLStreamException {
		wrapped.setDefaultNamespace(uri);
	}

	// override
	@Override
	public void setNamespaceContext(NamespaceContext context) throws XMLStreamException {
		wrapped.setNamespaceContext(context);
	}

	// override
	@Override
	public NamespaceContext getNamespaceContext() {
		return wrapped.getNamespaceContext();
	}

	// override
	@Override
	public Object getProperty(String name) throws IllegalArgumentException {
		return wrapped.getProperty(name);
	}

}
