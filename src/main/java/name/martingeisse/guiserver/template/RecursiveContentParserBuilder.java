/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.template;

import name.martingeisse.guishield.core.xml.content.ContentParser;
import name.martingeisse.guishield.core.xml.content.ContentParserRegistry;
import name.martingeisse.guishield.core.xml.element.ElementParser;
import name.martingeisse.guishield.core.xml.element.ElementParserRegistry;
import name.martingeisse.guishield.core.xml.element.NameSelectedElementParser;
import name.martingeisse.guishield.core.xml.generated.ElementParserBuilder;
import name.martingeisse.guishield.core.xml.generated.annotation.BindAttribute;
import name.martingeisse.guishield.core.xml.generated.annotation.BindContent;
import name.martingeisse.guishield.core.xml.generated.annotation.BindElement;
import name.martingeisse.guishield.core.xml.generated.annotation.BindNestedElement;
import name.martingeisse.guishield.core.xml.value.ValueParser;
import name.martingeisse.guishield.core.xml.value.ValueParserRegistry;

/**
 * This class builds the parser for the XML format.
 * 
 * This class also allows to obtain the unfinished component element parser being
 * built to parse component elements, to use it while building parser loops. Like
 * the markup content parser, this parser is not fully functional yet.
 *
 * @param <C> the type of components used in markup content
 */
public final class RecursiveContentParserBuilder<C> {
	
	/**
	 * the valueParserRegistry
	 */
	private final ValueParserRegistry valueParserRegistry;
	
	/**
	 * the elementParserRegistry
	 */
	private final ElementParserRegistry elementParserRegistry;
	
	/**
	 * the contentParserRegistry
	 */
	private final ContentParserRegistry contentParserRegistry;

	/**
	 * the elementParserBuilder
	 */
	private final ElementParserBuilder elementParserBuilder;

	/**
	 * the componentElementParser
	 */
	private final NameSelectedElementParser<C> componentElementParser;
	
	/**
	 * Constructor.
	 */
	public RecursiveContentParserBuilder() {
		valueParserRegistry = new ValueParserRegistry();
		elementParserRegistry = new ElementParserRegistry();
		contentParserRegistry = new ContentParserRegistry();
		elementParserBuilder = new ElementParserBuilder(valueParserRegistry, elementParserRegistry, contentParserRegistry);
		componentElementParser = new NameSelectedElementParser<>();
	}

	/**
	 * Adds a value parser that can be used for {@link BindAttribute} annotations.
	 * 
	 * @param type the parsed type
	 * @param parser the value parser
	 */
	public <T> void addValueParser(Class<T> type, ValueParser<T> parser) {
		valueParserRegistry.addParser(type, parser);
	}

	/**
	 * Adds an element parser that can be used for {@link BindNestedElement} annotations.
	 * 
	 * @param type the parsed type
	 * @param parser the element parser
	 */
	public <T> void addElementParser(Class<T> type, ElementParser<T> parser) {
		elementParserRegistry.addParser(type, parser);
	}
	
	/**
	 * Adds an content parser that can be used for {@link BindContent} annotations.
	 * 
	 * @param type the parsed type
	 * @param parser the content parser
	 */
	public <T> void addContentParser(Class<T> type, ContentParser<T> parser) {
		contentParserRegistry.addParser(type, parser);
	}

	/**
	 * Getter method for the elementParserBuilder.
	 * @return the elementParserBuilder
	 */
	public ElementParserBuilder getElementParserBuilder() {
		return elementParserBuilder;
	}
	
	/**
	 * Adds a component group configuration class to this builder. The class must be annotated
	 * with {@link BindElement}.
	 * 
	 * @param targetClass the class to add
	 */
	public void autoAddComponentElementParser(Class<? extends C> targetClass) {
		BindElement annotation = targetClass.getAnnotation(BindElement.class);
		if (annotation == null) {
			throw new RuntimeException("class " + targetClass + " is not annotated with @RegisterComponentElement");
		}
		addComponentElementParser(annotation.localName(), elementParserBuilder.build(targetClass));
	}

	/**
	 * Adds a component group configuration parser to this builder.
	 * 
	 * @param localElementName the local element name
	 * @param parser the parser
	 */
	public void addComponentElementParser(String localElementName, ElementParser<? extends C> parser) {
		componentElementParser.addParser(localElementName, parser);
	}

	/**
	 * Getter method for the componentElementParser.
	 * @return the componentElementParser
	 */
	public NameSelectedElementParser<C> getComponentElementParser() {
		return componentElementParser;
	}

}
