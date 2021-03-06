/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.xml;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.util.StreamReaderDelegate;

import org.apache.commons.lang3.StringUtils;

/**
 * Implements a "tee" reader that copies all events read to another
 * {@link XMLStreamWriter} supplied at construction.
 * 
 * Events are copied in the {@link #next()} method *before* moving
 * to the next event.
 * 
 * The {@link #setSkipNext(boolean)} method can be used to set a flag
 * that causes {@link #next()} to skip copying one event.
 */
public final class XmlStreamReaderTee extends StreamReaderDelegate {

	/**
	 * the writer
	 */
	private final XMLStreamWriter writer;
	
	/**
	 * the trim
	 */
	private final boolean trim;
	
	/**
	 * the skipNext
	 */
	private boolean skipNext;

	/**
	 * Constructor.
	 * @param reader the reader to read from
	 * @param writer the writer to write to
	 * @param trim whether to trim character content
	 * @throws XMLStreamException on XML processing errors
	 */
	public XmlStreamReaderTee(XMLStreamReader reader, XMLStreamWriter writer, boolean trim) throws XMLStreamException {
		super(reader);
		this.writer = writer;
		this.trim = trim;
		this.skipNext = false;
	}
	
	/**
	 * Setter method for the skipNext.
	 * @param skipNext the skipNext to set
	 */
	public void setSkipNext(boolean skipNext) {
		this.skipNext = skipNext;
	}

	// override
	@Override
	public void close() throws XMLStreamException {
		super.close();
		writer.close();
	}

	// override
	@Override
	public int next() throws XMLStreamException {
		if (skipNext) {
			skipNext = false;
		} else {
			int event = getEventType();
			switch (event) {
			
			case XMLStreamConstants.START_DOCUMENT:
			case XMLStreamConstants.END_DOCUMENT:
			case XMLStreamConstants.DTD:
			case XMLStreamConstants.ATTRIBUTE:
			case XMLStreamConstants.ENTITY_DECLARATION:
			case XMLStreamConstants.NAMESPACE:
			case XMLStreamConstants.NOTATION_DECLARATION:
				break;
				
			case XMLStreamConstants.START_ELEMENT:
				if (getNamespaceURI() == null) {
					writer.writeStartElement(getLocalName());
				} else {
					QName name = getName();
					writer.writeStartElement(name.getPrefix(), name.getLocalPart(), name.getNamespaceURI());
				}
				{
					int count = getAttributeCount();
					for (int i = 0; i < count; i++) {
						if (getAttributeNamespace(i) == null) {
							writer.writeAttribute(getAttributeLocalName(i), getAttributeValue(i));
						} else {
							QName name = getAttributeName(i);
							writer.writeAttribute(name.getPrefix(), name.getNamespaceURI(), name.getLocalPart(), getAttributeValue(i));
						}
					}
				}
				break;
				
			case XMLStreamConstants.END_ELEMENT:
				writer.writeEndElement();
				break;
				
			case XMLStreamConstants.CHARACTERS:
			case XMLStreamConstants.SPACE:
				writer.writeCharacters(getAutotrimmedText());
				break;
				
			case XMLStreamConstants.CDATA:
				writer.writeCData(getAutotrimmedText());
				break;
				
			case XMLStreamConstants.COMMENT:
				writer.writeComment(getAutotrimmedText());
				break;
				
			case XMLStreamConstants.PROCESSING_INSTRUCTION:
				if (getPIData() == null) {
					writer.writeProcessingInstruction(getPITarget());
				} else {
					writer.writeProcessingInstruction(getPITarget(), getPIData());
				}
				break;
				
			case XMLStreamConstants.ENTITY_REFERENCE:
				writer.writeEntityRef(getLocalName());
				break;
				
			}
		}
		return super.next();
	}
	
	/**
	 * 
	 */
	private String getAutotrimmedText() {
		return (trim ? StringUtils.strip(getText(), " \t") : getText());
	}

	// override
	@Override
	public int nextTag() throws XMLStreamException {
		loop: while (true) {
			int eventType = next();
			switch (eventType) {

			case XMLStreamConstants.START_ELEMENT:
			case XMLStreamConstants.END_ELEMENT:
				break loop;
				
			case XMLStreamConstants.SPACE:
			case XMLStreamConstants.COMMENT:
			case XMLStreamConstants.PROCESSING_INSTRUCTION:
				break;

			case XMLStreamConstants.CDATA:
			case XMLStreamConstants.CHARACTERS: {
				String text = getText();
				if (!StringUtils.isBlank(text)) {
					throw newNextTagException();
				}
				break;
			}
				
			default:
				throw newNextTagException();
				
			}
		}
		return getEventType();
	}
	
	/**
	 * 
	 */
	private XMLStreamException newNextTagException() {
		return new XMLStreamException("expected start or end tag", getLocation());
	}
	
}
