/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.definition.template;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * Used to represent the actual contents of a {@link MarkupContent} object.
 */
public abstract class MarkupContentEntry {

	/**
	 * Assembles the configuration.
	 * 
	 * @param assembler the configuration assembler
	 * @throws XMLStreamException on XML stream processing errors
	 */
	public abstract void assemble(ConfigurationAssembler assembler) throws XMLStreamException;

	/**
	 * Represents an attribute for an element.
	 */
	public static final class Attribute {

		/**
		 * the localName
		 */
		private final String localName;

		/**
		 * the value
		 */
		private final String value;

		/**
		 * Constructor.
		 * @param localName the local attribute name
		 * @param value the attribute value
		 */
		public Attribute(String localName, String value) {
			this.localName = localName;
			this.value = value;
		}

		/**
		 * Writes this attribute to the specified writer.
		 * @param writer the writer
		 * @throws XMLStreamException on XML stream processing errors
		 */
		public void writeTo(XMLStreamWriter writer) throws XMLStreamException {
			writer.writeAttribute(localName, value);
		}

	}

	/**
	 * Represents an opening tag of a raw (non-special) element.
	 */
	public static final class RawOpeningTag extends MarkupContentEntry {

		/**
		 * the localName
		 */
		private final String localName;

		/**
		 * the attributes
		 */
		private final Attribute[] attributes;

		/**
		 * Constructor.
		 * @param localName the local element name
		 * @param attributes the attributes
		 */
		public RawOpeningTag(String localName, Attribute[] attributes) {
			this.localName = localName;
			this.attributes = attributes;
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.guiserver.xml.result.MarkupContentEntry#assemble(name.martingeisse.guiserver.xml.result.ConfigurationAssembler)
		 */
		@Override
		public void assemble(ConfigurationAssembler assembler) throws XMLStreamException {
			assembler.getMarkupWriter().writeStartElement(localName);
			for (Attribute attribute : attributes) {
				attribute.writeTo(assembler.getMarkupWriter());
			}
		}
	}

	/**
	 * Represents a closing tag of a raw (non-special) element.
	 */
	public static final class RawClosingTag extends MarkupContentEntry {

		/* (non-Javadoc)
		 * @see name.martingeisse.guiserver.xml.result.MarkupContentEntry#assemble(name.martingeisse.guiserver.xml.result.ConfigurationAssembler)
		 */
		@Override
		public void assemble(ConfigurationAssembler assembler) throws XMLStreamException {
			assembler.getMarkupWriter().writeEndElement();
		}

	}

	/**
	 * Represents character content.
	 */
	public static final class Characters extends MarkupContentEntry {

		/**
		 * the text
		 */
		private final String text;

		/**
		 * Constructor.
		 * @param text the text
		 */
		public Characters(String text) {
			this.text = text;
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.guiserver.xml.result.MarkupContentEntry#assemble(name.martingeisse.guiserver.xml.result.ConfigurationAssembler)
		 */
		@Override
		public void assemble(ConfigurationAssembler assembler) throws XMLStreamException {
			assembler.getMarkupWriter().writeCharacters(text);
		}

	}

	/**
	 * Represents a component group configuration.
	 */
	public static final class ComponentGroup extends MarkupContentEntry {

		/**
		 * the configuration
		 */
		private final ComponentConfiguration configuration;

		/**
		 * Constructor.
		 * @param configuration the component group configuration
		 */
		public ComponentGroup(ComponentConfiguration configuration) {
			this.configuration = configuration;
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.guiserver.xml.result.MarkupContentEntry#assemble(name.martingeisse.guiserver.xml.result.ConfigurationAssembler)
		 */
		@Override
		public void assemble(ConfigurationAssembler assembler) throws XMLStreamException {
			configuration.assemble(assembler);
		}

	}

}
