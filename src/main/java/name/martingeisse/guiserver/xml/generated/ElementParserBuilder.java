/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.xml.generated;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import name.martingeisse.guiserver.xml.attribute.AttributeParser;
import name.martingeisse.guiserver.xml.attribute.SimpleAttributeParser;
import name.martingeisse.guiserver.xml.content.ContentParser;
import name.martingeisse.guiserver.xml.content.MarkupContentParser;
import name.martingeisse.guiserver.xml.element.ElementContentParser;
import name.martingeisse.guiserver.xml.element.ElementParser;
import name.martingeisse.guiserver.xml.generated.annotation.AttributeValueBindingOptionality;
import name.martingeisse.guiserver.xml.generated.annotation.BindAttribute;
import name.martingeisse.guiserver.xml.generated.annotation.BindContent;
import name.martingeisse.guiserver.xml.generated.annotation.BindNestedElement;
import name.martingeisse.guiserver.xml.value.RegisteredValueParserProvider;
import name.martingeisse.guiserver.xml.value.ValueParser;

/**
 * Helps build a {@link ClassInstanceElementParser}.
 */
@Singleton
public final class ElementParserBuilder {

	private final Provider<Set<RegisteredValueParserProvider>> valueParserProviderSetProvider;
	private final MarkupContentParser defaultContentParser;

	/**
	 * Constructor.
	 * @param valueParserProviderSetProvider (injected)
	 * @param defaultContentParser (injected)
	 */
	@Inject
	public ElementParserBuilder(final Provider<Set<RegisteredValueParserProvider>> valueParserProviderSetProvider, final MarkupContentParser defaultContentParser) {
		this.valueParserProviderSetProvider = valueParserProviderSetProvider;
		this.defaultContentParser = defaultContentParser;
	}

	/**
	 * Builds the binding.
	 * @param targetClass the class to build a binding for
	 * @return the element parser for that class
	 */
	public <T> ElementParser<T> build(final Class<? extends T> targetClass) {
		try {
			final Constructor<? extends T> constructor = targetClass.getConstructor();
			List<PropertiesBinding<T, ? extends AttributeParser<?>>> attributeBindings = new ArrayList<>();
			NameSelectedPropertiesBinding<T, ElementParser<?>> elementBinding = new NameSelectedPropertiesBinding<T, ElementParser<?>>();
			boolean hasElementBinding = false;
			PropertiesBinding<T, ? extends ContentParser<?>> contentBinding = null;
			for (final Method method : targetClass.getMethods()) {
				final BindAttribute attributeAnnotation = method.getAnnotation(BindAttribute.class);
				final BindNestedElement elementAnnotation = method.getAnnotation(BindNestedElement.class);
				final BindContent contentAnnotation = method.getAnnotation(BindContent.class);
				if (count(attributeAnnotation) + count(elementAnnotation) + count(contentAnnotation) > 1) {
					throw new RuntimeException("cannot use more than one of " + BindAttribute.class + ", " + BindNestedElement.class + ", " + BindContent.class + " for method " + method.getName() + " of class " + targetClass);
				} else if (attributeAnnotation != null) {
					attributeBindings.add(createAttributeBinding(method));
				} else if (elementAnnotation != null) {
					elementBinding.addBinding(elementAnnotation.localName(), createElementBinding(method));
					hasElementBinding = true;
				} else if (contentAnnotation != null) {
					if (contentBinding != null) {
						throw new RuntimeException("multiple content bindings for class " + targetClass);
					}
					contentBinding = createContentBinding(method);
				}
			}
			@SuppressWarnings("unchecked")
			PropertiesBinding<T, ? extends AttributeParser<?>>[] attributeBindingsArray = (PropertiesBinding<T, ? extends AttributeParser<?>>[])(new PropertiesBinding<?, ?>[attributeBindings.size()]);
			attributeBindingsArray = attributeBindings.toArray(attributeBindingsArray);
			if (hasElementBinding) {
				if (contentBinding != null) {
					throw new RuntimeException("class " + targetClass + " has both child-element-to-property-binding(s) and a content-to-property-binding");
				} else {
					contentBinding = new ContentPropertiesBinding<>(elementBinding);
				}
			}
			return new ClassInstanceElementParser<T>(constructor, attributeBindingsArray, contentBinding);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	//
	private int count(final Object o) {
		return (o == null ? 0 : 1);
	}

	/**
	 * Creates a binding between an attribute and a method. This is a separate
	 * method to allow P (the type of the constructor parameter) to be used as a static
	 * type variable.
	 *
	 * @throws Exception on errors
	 */
	private <T, P> PropertiesBinding<T, AttributeParser<P>> createAttributeBinding(final Method method) throws Exception {
		final Class<?> parameterType = determineParameterType(BindAttribute.class, method);

		// extract data from the annotation
		final BindAttribute annotation = method.getAnnotation(BindAttribute.class);
		final String name = annotation.name();
		final boolean optional = (annotation.optionality() != AttributeValueBindingOptionality.MANDATORY);
		final String defaultValue = (annotation.optionality() == AttributeValueBindingOptionality.OPTIONAL_WITH_DEFAULT ? annotation.defaultValue() : null);
		if (annotation.optionality() == AttributeValueBindingOptionality.OPTIONAL && parameterType.isPrimitive()) {
			throw new RuntimeException("cannot bind an optional attribute without default value to a parameter of primitive type. Attribute name: " + name);
		}

		// determine the value parser
		final ValueParser<?> untypedValueParser = determineParser(method, annotation.type(), parameterType, t -> valueParserRegistry.getParser(t));
		@SuppressWarnings("unchecked")
		final ValueParser<P> valueParser = (ValueParser<P>)untypedValueParser;

		// build the binding
		final AttributeParser<P> attributeParser = new SimpleAttributeParser<>(name, optional, defaultValue, valueParser);
		PropertiesBinding<T, AttributeParser<P>> binding = new ParserToMethodBinding<T, P, AttributeParser<P>>(attributeParser, method);
		return binding;

	}

	/**
	 * Creates a binding between an element and a method. This is a separate
	 * method to allow P (the type of the constructor parameter) to be used as a static
	 * type variable.
	 *
	 * @throws Exception on errors
	 */
	private <T, P> PropertiesBinding<T, ElementParser<P>> createElementBinding(final Method method) throws Exception {
		final Class<?> parameterType = determineParameterType(BindNestedElement.class, method);

		// extract data from the annotation
		final BindNestedElement annotation = method.getAnnotation(BindNestedElement.class);

		// determine the element parser
		final Function<Class<?>, ? extends ElementParser<P>> parserProvider = this::getOrCreateElementParser;
		final ElementParser<?> untypedElementParser = determineParser(method, annotation.type(), parameterType, parserProvider);
		@SuppressWarnings("unchecked")
		final ElementParser<P> elementParser = (ElementParser<P>)untypedElementParser;

		// build the binding
		PropertiesBinding<T, ElementParser<P>> binding = new ParserToMethodBinding<T, P, ElementParser<P>>(elementParser, method);
		return binding;

	}

	/**
	 *
	 */
	private <T> ElementParser<T> getOrCreateElementParser(final Class<?> targetClass) {
		@SuppressWarnings("unchecked")
		final Class<T> targetClassTyped = (Class<T>)targetClass;

		// check for a pre-registered or previously created parser
		ElementParser<T> parser = elementParserRegistry.getParser(targetClassTyped);
		if (parser != null) {
			return parser;
		}

		// check if a structured-element parser can be created automatically
		if (targetClass.getAnnotation(StructuredElement.class) != null) {
			parser = build(targetClassTyped);
			elementParserRegistry.addParser(targetClassTyped, parser);
			return parser;
		}

		// check if we can create a parser by wrapping a content parser
		final ContentParser<T> contentParser = contentParserRegistry.getParser(targetClassTyped);
		if (contentParser != null) {
			parser = new ElementContentParser<>(contentParser);
			elementParserRegistry.addParser(targetClassTyped, parser);
			return parser;
		}

		throw new RuntimeException("cannot find parser for this class, and no @StructuredElement annotation is present: " + targetClass);
	}

	/**
	 * Creates a binding between element content and a method. This is a separate
	 * method to allow P (the type of the constructor parameter) to be used as a static
	 * type variable.
	 *
	 * @throws Exception on errors
	 */
	private <T, P> PropertiesBinding<T, ContentParser<P>> createContentBinding(final Method method) throws Exception {
		final Class<?> parameterType = determineParameterType(BindContent.class, method);

		// extract data from the annotation
		final BindContent annotation = method.getAnnotation(BindContent.class);

		// determine the content parser
		final ContentParser<?> untypedContentParser = determineParser(method, annotation.type(), parameterType, t -> contentParserRegistry.getParser(t));
		@SuppressWarnings("unchecked")
		final ContentParser<P> contentParser = (ContentParser<P>)untypedContentParser;

		// build the binding
		PropertiesBinding<T, ContentParser<P>> binding = new ParserToMethodBinding<T, P, ContentParser<P>>(contentParser, method);
		return binding;

	}

	//
	private Class<?> determineParameterType(final Class<? extends Annotation> annotationClass, final Method method) {
		if (method.getParameterCount() != 1) {
			throw new RuntimeException("@" + annotationClass.getSimpleName() + " used for method with wrong number of parameters: " + method);
		}
		return method.getParameterTypes()[0];
	}

	//
	private <P> P determineParser(final Method method, final Class<?> specifiedType, final Class<?> parameterType, final Function<Class<?>, ? extends P> parserRegistry) throws Exception {
		final Class<?> conversionType = (specifiedType == void.class ? parameterType : specifiedType);
		if (!parameterType.isAssignableFrom(conversionType)) {
			throw new RuntimeException("incompatible conversion type " + conversionType + " for method " + method);
		}
		final P parser = parserRegistry.apply(conversionType);
		if (parser == null) {
			throw new RuntimeException("no parser available for conversion type " + conversionType + " for method " + method);
		}
		return parser;
	}

}
