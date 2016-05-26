/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.xml.content;

import name.martingeisse.guiserver.template.MarkupContent;

/**
 * Parses XML content to produce mixed raw/component markup. This interface is used
 * for dependency injection.
 */
public interface MarkupContentParser extends ContentParser<MarkupContent> {
}
