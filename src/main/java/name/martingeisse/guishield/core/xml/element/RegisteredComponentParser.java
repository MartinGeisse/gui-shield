/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.xml.element;

import javax.xml.stream.XMLStreamReader;
import name.martingeisse.guishield.core.definition.template.ComponentConfiguration;

/**
 * This is an extension point to register parsers that turn XML elements into {@link ComponentConfiguration}s.
 * Once registered, such an XML element will be recognized when found in unrelated XML
 * that doesn't use specific element parsers. This is the standard procedure to use plugin-defined
 * components.
 */
public interface RegisteredComponentParser extends ElementParser<ComponentConfiguration> {

	/**
	 * Given an XML stream reader positioned at a starting tag, this method determines
	 * whether this parser can handle the element being started. This method must not
	 * move the reader away from the starting tag.
	 * 
	 * @param reader the XML stream reader
	 * @return true if this parser can handle the element, false if not
	 */
	public boolean canHandleElement(XMLStreamReader reader);
	
}
