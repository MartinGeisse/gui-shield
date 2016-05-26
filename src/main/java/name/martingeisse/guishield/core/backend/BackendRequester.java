/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.backend;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import name.martingeisse.guishield.core.definition.DefinitionPath;

/**
 * Sends requests to the backend.
 */
public class BackendRequester {

	private final HttpClient httpClient;

	
	/**
	 * Constructor.
	 */
	public BackendRequester() {
		httpClient = HttpClientBuilder.create().build();
	}
	
	/**
	 * Requests a GUI definition object.
	 *
	 * @param path the path
	 * @return the definition object
	 */
	public String requestDefinition(final DefinitionPath path) {
		try {
			HttpResponse response = httpClient.execute(new HttpGet("http://localhost/demo-gui" + path));
			return IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
