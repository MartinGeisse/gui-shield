/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.xml.generated.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import name.martingeisse.guishield.core.xml.value.ValueParser;

/**
 * This annotation can be used to bind an attribute to a method that takes
 * the parsed type of the attribute as its parameter type, such as a
 * setter method.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BindAttribute {

	/**
	 * @return the attribute name
	 */
	public String name();
	
	/**
	 * @return whether the attribute is mandatory or optional and whether it has
	 * a default value
	 */
	public AttributeValueBindingOptionality optionality() default AttributeValueBindingOptionality.MANDATORY;
	
	/**
	 * @return the default value (only used if the optionality is
	 * OPTIONAL_WITH_DEFAULT).
	 */
	public String defaultValue() default "";

	/**
	 * <p>
	 * Allows to specify the parsed type directly, overriding the parameter type of
	 * the method. The parser associated with the specified type must still be the
	 * parameter type or a subtype, otherwise invoking the method will fail.
	 * </p>
	 * <p>
	 * Note that this annotation attribute has array type to allow leaving the type unspecified --
	 * passing more than one type is not allowed.
	 * </p>
	 */
	public Class<?>[] type() default {};

	/**
	 * <p>
	 * Allows to use an explicit parser to parse the attribute.
	 * </p>
	 * <p>
	 * Note that this annotation attribute has array type to allow leaving the parser unspecified --
	 * passing more than one parser is not allowed.
	 * </p>
	 * 
	 * @return the parser class
	 */
	public Class<? extends ValueParser<?>>[] valueParser() default {};
	
}
