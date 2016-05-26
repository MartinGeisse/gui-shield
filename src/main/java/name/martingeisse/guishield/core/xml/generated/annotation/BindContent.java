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
import name.martingeisse.guishield.core.xml.content.ContentParser;

/**
 * This annotation can be used to bind element content to a method that
 * takes the parsed type of the element content as its parameter type, such as
 * a setter method.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BindContent {

	/**
	 * <p>
	 * Allows to use an explicit parser to parse the content.
	 * </p>
	 * <p>
	 * Note that this annotation attribute has array type to allow leaving the parser unspecified --
	 * passing more than one parser is not allowed.
	 * </p>
	 * 
	 * @return the parser class
	 */
	public Class<? extends ContentParser<?>>[] parser() default {};

}
