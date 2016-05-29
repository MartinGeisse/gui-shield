/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.xml.generated;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import name.martingeisse.guishield.core.definition.template.MarkupContent;
import name.martingeisse.guishield.core.xml.attribute.AttributeParser;
import name.martingeisse.guishield.core.xml.attribute.SimpleAttributeParser;
import name.martingeisse.guishield.core.xml.content.ContentParser;
import name.martingeisse.guishield.core.xml.content.MarkupContentParser;
import name.martingeisse.guishield.core.xml.element.ElementParser;
import name.martingeisse.guishield.core.xml.element.ElementValueParser;
import name.martingeisse.guishield.core.xml.generated.annotation.AttributeValueBindingOptionality;
import name.martingeisse.guishield.core.xml.generated.annotation.BindAttribute;
import name.martingeisse.guishield.core.xml.generated.annotation.BindContent;
import name.martingeisse.guishield.core.xml.generated.annotation.BindNestedElement;
import name.martingeisse.guishield.core.xml.value.RegisteredValueParserProvider;
import name.martingeisse.guishield.core.xml.value.ValueParser;

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
			final List<PropertiesBinding<T, ? extends AttributeParser<?>>> attributeBindings = new ArrayList<>();
			final NameSelectedPropertiesBinding<T, ElementParser<?>> elementBinding = new NameSelectedPropertiesBinding<T, ElementParser<?>>();
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
					final PropertiesBinding<T, ElementParser<?>> binding = createElementBinding(method);
					elementBinding.addBinding(elementAnnotation.localName(), binding);
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
	@SuppressWarnings("unchecked")
	private <T, P> PropertiesBinding<T, AttributeParser<P>> createAttributeBinding(final Method method) throws Exception {
		final Class<?> methodParameterType = determineParameterType(BindAttribute.class, method);

		// extract data from the annotation
		final BindAttribute annotation = method.getAnnotation(BindAttribute.class);
		final String name = annotation.name();
		final boolean optional = (annotation.optionality() != AttributeValueBindingOptionality.MANDATORY);
		final String defaultValue = (annotation.optionality() == AttributeValueBindingOptionality.OPTIONAL_WITH_DEFAULT ? annotation.defaultValue() : null);
		if (annotation.optionality() == AttributeValueBindingOptionality.OPTIONAL && methodParameterType.isPrimitive()) {
			throw new RuntimeException("cannot bind an optional attribute without default value to a parameter of primitive type. Attribute name: " + name);
		}
		final Class<?> explicitType = optional(annotation.type(), "explicit attribute type");
		final Class<? extends ValueParser<?>> explicitValueParserClass = optional(annotation.valueParser(), "explicit attribute's value parser");

		// determine the value parser
		ValueParser<?> valueParser;
		if (explicitValueParserClass != null) {
			valueParser = explicitValueParserClass.newInstance();
		} else if (explicitType != null) {
			if (!methodParameterType.isAssignableFrom(explicitType)) {
				throw new RuntimeException("incompatible conversion type " + explicitType + " for method " + method);
			}
			valueParser = findValueParserForType(explicitType);
		} else {
			valueParser = findValueParserForType(methodParameterType);
		}
		final ValueParser<P> typedValueParser = (ValueParser<P>)valueParser;

		// build the binding
		final AttributeParser<P> attributeParser;
		if (annotation.optionality() == AttributeValueBindingOptionality.OPTIONAL_WITH_DEFAULT) {
			attributeParser = new SimpleAttributeParser<>(name, defaultValue, typedValueParser);
		} else {
			attributeParser = new SimpleAttributeParser<>(name, optional, typedValueParser);
		}
		final PropertiesBinding<T, AttributeParser<P>> binding = new ParserToMethodBinding<T, P, AttributeParser<P>>(attributeParser, method);
		return binding;

	}

	/**
	 * Creates a binding between an element and a method. This is a separate
	 * method to allow P (the type of the constructor parameter) to be used as a static
	 * type variable.
	 *
	 * @throws Exception on errors
	 */
	@SuppressWarnings("unchecked")
	private <T> PropertiesBinding<T, ElementParser<?>> createElementBinding(final Method method) throws Exception {
		final Class<?> methodParameterType = determineParameterType(BindNestedElement.class, method);

		// extract data from the annotation
		final BindNestedElement annotation = method.getAnnotation(BindNestedElement.class);
		final Class<?> explicitType = optional(annotation.type(), "explicit attribute type");
		final Class<? extends ValueParser<?>> explicitValueParserClass = optional(annotation.valueParser(), "explicit element's value parser");
		final Class<? extends ElementParser<?>> explicitElementParserClass = optional(annotation.elementParser(), "explicit element parser");

		// determine the element parser
		ElementParser<?> elementParser;
		if (explicitElementParserClass != null) {
			elementParser = explicitElementParserClass.newInstance();
		} else {
			final ValueParser<?> valueParser;
			if (explicitValueParserClass != null) {
				valueParser = explicitValueParserClass.newInstance();
			} else if (explicitType != null) {
				if (!methodParameterType.isAssignableFrom(explicitType)) {
					throw new RuntimeException("incompatible conversion type " + explicitType + " for method " + method);
				}
				valueParser = findValueParserForType(explicitType);
			} else {
				valueParser = findValueParserForType(methodParameterType);
			}
			elementParser = new ElementValueParser<>(valueParser);
		}

		// determine the element parser
		final ElementParser<Object> typedElementParser = (ElementParser<Object>)elementParser;

		// build the binding
		final PropertiesBinding<T, ElementParser<Object>> binding = new ParserToMethodBinding<T, Object, ElementParser<Object>>(typedElementParser, method);
		return (PropertiesBinding<T, ElementParser<?>>)(PropertiesBinding<?, ?>)binding;

	}

	/**
	 * Creates a binding between element content and a method. This is a separate
	 * method to allow P (the type of the constructor parameter) to be used as a static
	 * type variable.
	 *
	 * @throws Exception on errors
	 */
	@SuppressWarnings("unchecked")
	private <T, P> PropertiesBinding<T, ContentParser<P>> createContentBinding(final Method method) throws Exception {
		final Class<?> methodParameterType = determineParameterType(BindContent.class, method);
		final BindContent annotation = method.getAnnotation(BindContent.class);
		final Class<? extends ContentParser<?>> explicitParserClass = optional(annotation.parser(), "explicit parser for content binding");
		final ContentParser<?> parser;
		if (explicitParserClass == null) {
			if (methodParameterType != MarkupContent.class) {
				throw new RuntimeException("explicit parser needed for parsing XML content to type " + methodParameterType);
			}
			parser = defaultContentParser;
		} else {
			parser = explicitParserClass.newInstance();
		}
		final ContentParser<P> typedParser = (ContentParser<P>)parser;
		return new ParserToMethodBinding<T, P, ContentParser<P>>(typedParser, method);
	}

	//
	private Class<?> determineParameterType(final Class<? extends Annotation> annotationClass, final Method method) {
		if (method.getParameterCount() != 1) {
			throw new RuntimeException("@" + annotationClass.getSimpleName() + " used for method with wrong number of parameters: " + method);
		}
		return method.getParameterTypes()[0];
	}

	//
	private <T> T optional(final T[] values, final String what) {
		if (values.length > 1) {
			throw new RuntimeException("more than one " + what + " specified");
		}
		return (values.length == 0 ? null : values[0]);
	}

	private ValueParser<?> findValueParserForType(final Class<?> type) {
		for (final RegisteredValueParserProvider parserProvider : valueParserProviderSetProvider.get()) {
			final ValueParser<?> parser = parserProvider.getValueParser(type);
			if (parser != null) {
				return parser;
			}
		}
		throw new RuntimeException("no value parser for type: " + type);
	}

}
