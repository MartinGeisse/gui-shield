/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.xml.element;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Delegates parsing to another {@link ElementParser}, but allows to wrap/transform
 * the result.
 * 
 * @param <ORIG> the original type
 * @param <WRAP> the wrapped type
 */
public abstract class ElementParserWrapper<ORIG, WRAP> implements ElementParser<WRAP> {

	/**
	 * the originalParser
	 */
	private final ElementParser<? extends ORIG> originalParser;

	/**
	 * Constructor.
	 * @param originalParser the original parser
	 */
	public ElementParserWrapper(ElementParser<? extends ORIG> originalParser) {
		this.originalParser = originalParser;
	}

	/**
	 * Wraps the result.
	 * 
	 * @param original the original result
	 * @return the wrapped result
	 */
	protected abstract WRAP wrapResult(ORIG original);

	// override
	@Override
	public final WRAP parse(XMLStreamReader reader) throws XMLStreamException {
		return wrapResult(originalParser.parse(reader));
	}

}