/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.backend;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
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
		final PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
		connectionManager.setMaxTotal(200);
		connectionManager.setDefaultMaxPerRoute(20);
		httpClient = HttpClients.custom().setConnectionManager(connectionManager).setDefaultCookieStore(new NullCookieStore()).build();
	}

	/**
	 * Requests a GUI definition object.
	 *
	 * @param path the path
	 * @return the definition object
	 */
	public String requestDefinition(final DefinitionPath path) {
		final String url = "http://localhost/demo-gui" + path;
		final HttpResponse response = get(url);
		final Charset charset = getContentTypeCharset(response);
		try (Reader reader = new InputStreamReader(response.getEntity().getContent(), charset)) {
			return IOUtils.toString(reader);
		} catch (final IOException e) {
			throw new BackendException("I/O errors while reading configuration", e);
		}
	}

	/**
	 * Fetches a response from the backend using a GET request.
	 * Throws an exception on errors.
	 *
	 * @param url the URL to fetch from
	 * @return the response
	 */
	private HttpResponse get(final String url) {
		final HttpGet httpGet = new HttpGet(url);
		final HttpResponse response;
		try {
			response = httpClient.execute(httpGet);
		} catch (final Exception e) {
			throw new BackendException("could not connect to configuration storage", e);
		}
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new BackendException("got wrong status code for " + url + " -- " + response.getStatusLine());
		}
		return response;
	}

	/**
	 * Returns the base content type of the specified response.
	 *
	 * @param response the response
	 * @return the base content type, or null if not specified
	 */
	@SuppressWarnings("unused")
	private static String getBaseContentType(final HttpResponse response) {
		final Header contentType = response.getEntity().getContentType();
		if (contentType == null) {
			return null;
		}
		final HeaderElement[] elements = contentType.getElements();
		return (elements.length == 0 ? null : elements[0].getName());
	}

	/**
	 * Returns the charset from the content type of the specified response.
	 *
	 * @param response the response
	 * @return the charset, using ISO-8859-1 as the fallback if not specified or not known
	 */
	private static Charset getContentTypeCharset(final HttpResponse response) {
		final Header contentType = response.getEntity().getContentType();
		if (contentType.getElements().length > 0) {
			final HeaderElement element = contentType.getElements()[0];
			final NameValuePair charsetNvp = element.getParameterByName("charset");
			if (charsetNvp != null) {
				final String charsetName = charsetNvp.getValue();
				try {
					return Charset.forName(charsetName);
				} catch (final Exception e) {
				}
			}
		}
		return StandardCharsets.UTF_8;
	}

}
