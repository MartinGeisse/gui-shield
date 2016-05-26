/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.xml.content;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * This implementation of {@link ContentParser} just delegates to
 * another implementation, but allows to replace that implementation.
 */
public final class DelegatingContentParser<T> implements ContentParser<T> {

	/**
	 * the delegate
	 */
	private ContentParser<T> delegate;

	/**
	 * Getter method for the delegate.
	 * @return the delegate
	 */
	public ContentParser<T> getDelegate() {
		return delegate;
	}

	/**
	 * Setter method for the delegate.
	 * @param delegate the delegate to set
	 */
	public void setDelegate(ContentParser<T> delegate) {
		this.delegate = delegate;
	}

	// override
	@Override
	public T parse(XMLStreamReader reader) throws XMLStreamException {
		return delegate.parse(reader);
	}

}