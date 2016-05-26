/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.definition;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import org.apache.commons.collections.iterators.ArrayIterator;
import org.apache.commons.lang3.StringUtils;

/**
 * The path that selects a definition object.
 */
public final class DefinitionPath implements Iterable<String> {

	private final String[] segments;

	private DefinitionPath(final String[] segments, final boolean copy) {
		this.segments = copy ? Arrays.copyOf(segments, segments.length) : segments;
	}

	/**
	 * Constructor.
	 * @param path the path
	 */
	public DefinitionPath(final String path) {
		segments = StringUtils.split(path, '/'); // strips leading / trailing slash
	}

	/**
	 * Constructor.
	 * @param segments the path segments
	 */
	public DefinitionPath(final String[] segments) {
		this(segments, true);
	}

	/**
	 * Constructor.
	 * @param segments the path segments
	 */
	public DefinitionPath(final Collection<String> segments) {
		this(segments.toArray(new String[segments.size()]), false);
	}

	/**
	 * Getter method for the segmentCount.
	 * @return the segmentCount
	 */
	public int getSegmentCount() {
		return segments.length;
	}

	/**
	 * Gets a single path segment.
	 *
	 * @param index the segment index
	 * @return the segment
	 */
	public String getSegment(final int index) {
		return segments[index];
	}

	/**
	 * Gets a new array containing the segments of this path.
	 * 
	 * @return the segments
	 */
	public String[] getSegments() {
		return Arrays.copyOf(segments, segments.length);
	}
	
	/**
	 * Checks if the segments of this path are equal to the specified segments.
	 * 
	 * @param segments the segments to compare to
	 * @return true if both segment lists are of the same length and contain the same segments
	 */
	public boolean matches(String[] segments) {
		return Arrays.equals(segments, this.segments);
	}
	
	// override
	@Override
	public String toString() {
		return "/" + StringUtils.join(segments, '/');
	}

	// override
	@Override
	public Iterator<String> iterator() {
		@SuppressWarnings("unchecked")
		Iterator<String> iterator = new ArrayIterator(segments);
		return iterator;
	}
	
}
