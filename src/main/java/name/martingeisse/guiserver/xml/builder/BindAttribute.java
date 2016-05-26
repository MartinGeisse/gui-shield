/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.xml.builder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation can be used to bind an attribute to a method that takes
 * the parsed type of the attribute as its parameter type, such as a
 * setter method.
 * 
 * The {@link #type()} property of this annotation (if present) or the type of
 * the method parameter is used to infer the parser. 
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
	 * Allows to specify the parsed type directly, overriding the parameter type of
	 * the method. The parser associated with the specified type must still be the
	 * parameter type or a subtype, otherwise invoking the method will fail.
	 * 
	 * The default void.class causes the type to be inferred from the method
	 * signature.
	 */
	public Class<?> type() default void.class;
	
}