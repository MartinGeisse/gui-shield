/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.xml.generated;

import java.lang.reflect.Method;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import name.martingeisse.guiserver.xml.XmlParser;

/**
 * Binds the value returned by a parser to a method (e.g. a setter method)
 * that accepts a value of the parsed type.
 *
 * @param <C> the type of the container object
 * @param <T> the parsed type, which is also the parameter type of the method
 * @param <P> the parser type
 */
public final class ParserToMethodBinding<C, T, P extends XmlParser<T>> implements PropertiesBinding<C, P> {

	/**
	 * the parser
	 */
	private final P parser;

	/**
	 * the method
	 */
	private final Method method;

	/**
	 * Constructor.
	 * @param parser the parser
	 * @param method the target method
	 */
	public ParserToMethodBinding(P parser, Method method) {
		this.parser = parser;
		this.method = method;
	}

	@Override
	public void bind(XMLStreamReader reader, C target) throws XMLStreamException {
		try {
			method.invoke(target, parser.parse(reader));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
