/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guishield.core.wicket;

import org.apache.wicket.RequestListenerInterface;
import org.apache.wicket.core.request.handler.BookmarkableListenerInterfaceRequestHandler;
import org.apache.wicket.core.request.handler.BookmarkablePageRequestHandler;
import org.apache.wicket.core.request.handler.IPageRequestHandler;
import org.apache.wicket.core.request.handler.ListenerInterfaceRequestHandler;
import org.apache.wicket.core.request.handler.PageAndComponentProvider;
import org.apache.wicket.core.request.handler.PageProvider;
import org.apache.wicket.core.request.handler.RenderPageRequestHandler;
import org.apache.wicket.core.request.mapper.AbstractComponentMapper;
import org.apache.wicket.core.request.mapper.MapperUtils;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.IRequestHandlerDelegate;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.info.ComponentInfo;
import org.apache.wicket.request.mapper.info.PageComponentInfo;
import org.apache.wicket.request.mapper.info.PageInfo;
import org.apache.wicket.request.mapper.parameter.IPageParametersEncoder;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.lang.Args;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import name.martingeisse.guishield.core.builtin.basic.ConfigurationDefinedPage;
import name.martingeisse.guishield.core.definition.DefinitionPath;

/**
 * This mapper extracts the path, puts it into a parameter and passes that to a {@link ConfigurationDefinedPage}.
 */
public final class MyRequestMapper extends AbstractComponentMapper {

	private static Logger logger = LoggerFactory.getLogger(MyRequestMapper.class);

	protected final IPageParametersEncoder pageParametersEncoder;

	/**
	 * Constructor.
	 * @param pageParametersEncoder ...
	 */
	public MyRequestMapper(final IPageParametersEncoder pageParametersEncoder) {
		this.pageParametersEncoder = pageParametersEncoder;
	}

	// override
	@Override
	public IRequestHandler mapRequest(final Request request) {
		final Url url = request.getUrl();
		final PageComponentInfo pageComponentInfo = getPageComponentInfo(url);
		final DefinitionPath path = new DefinitionPath(url.getSegments());
		PageParameters pageParameters = extractPageParameters(request, url.getSegments().size(), pageParametersEncoder);
		if (pageParameters == null) {
			pageParameters = new PageParameters();
		}
		if (pageParameters.get(ConfigurationDefinedPage.PAGE_DEFINITION_PATH_PARAMETER_NAME).isNull()) {
			pageParameters.add(ConfigurationDefinedPage.PAGE_DEFINITION_PATH_PARAMETER_NAME, path);
		}
		if (pageComponentInfo == null) {

			// bookmarkable case -- creates new page instance
			final PageProvider provider = new PageProvider(ConfigurationDefinedPage.class, pageParameters);
			provider.setPageSource(getContext());
			return new RenderPageRequestHandler(provider);

		} else if (pageComponentInfo.getPageInfo().getPageId() != null && pageComponentInfo.getComponentInfo() == null) {
			
			// hybrid instance/bookmarkable case -- tries to use instance, but creates new instance if that failed
			final PageProvider provider = new PageProvider(pageComponentInfo.getPageInfo().getPageId(), ConfigurationDefinedPage.class, pageParameters, null);
			provider.setPageSource(getContext());
			final PageParameters constructionPageParameters = provider.hasPageInstance() ? provider.getPageInstance().getPageParameters() : new PageParameters();
			if (PageParameters.equals(constructionPageParameters, pageParameters) == false) {
				return new RenderPageRequestHandler(new PageProvider(ConfigurationDefinedPage.class, pageParameters));
			}
			return new RenderPageRequestHandler(provider);

		} else if (pageComponentInfo.getComponentInfo() != null) {
			
			// instance + listener case
			final PageInfo pageInfo = pageComponentInfo.getPageInfo();
			final ComponentInfo componentInfo = pageComponentInfo.getComponentInfo();
			if (componentInfo == null) {
				logger.warn("Cannot extract the listener interface for PageComponentInfo: '{}'", pageComponentInfo);
				return null;
			}
			final RequestListenerInterface listenerInterface = requestListenerInterfaceFromString(componentInfo.getListenerInterface());
			if (listenerInterface == null) {
				logger.warn("Unknown listener interface '{}'", componentInfo.getListenerInterface());
				return null;
			}
			final PageAndComponentProvider provider = new PageAndComponentProvider(pageInfo.getPageId(), ConfigurationDefinedPage.class, pageParameters, componentInfo.getRenderCount(), componentInfo.getComponentPath());
			provider.setPageSource(getContext());
			return new ListenerInterfaceRequestHandler(provider, listenerInterface, componentInfo.getBehaviorId());
			
		} else if (pageComponentInfo.getPageInfo().getPageId() == null) {
			
			// bookmarkable case -- creates new page instance
			final PageProvider provider = new PageProvider(ConfigurationDefinedPage.class, pageParameters);
			provider.setPageSource(getContext());
			return new RenderPageRequestHandler(provider);
			
		} else {
			return null;
		}
	}

	// override
	@Override
	public Url mapHandler(IRequestHandler requestHandler) {
		while (requestHandler instanceof IRequestHandlerDelegate) {
			requestHandler = ((IRequestHandlerDelegate)requestHandler).getDelegateHandler();
		}
		if (requestHandler instanceof BookmarkablePageRequestHandler) {

			// bookmarkable rendering case
			final BookmarkablePageRequestHandler handler = (BookmarkablePageRequestHandler)requestHandler;
			if (!handler.getPageClass().equals(ConfigurationDefinedPage.class)) {
				return null;
			}
			return buildUrl(new PageComponentInfo(new PageInfo(), null), handler.getPageParameters());

		} else if (requestHandler instanceof RenderPageRequestHandler) {

			// non-bookmarkable rendering case
			final RenderPageRequestHandler handler = (RenderPageRequestHandler)requestHandler;
			if (!handler.getPageClass().equals(ConfigurationDefinedPage.class)) {
				return null;
			}
			if (handler.getPageProvider().isNewPageInstance()) {
				return buildUrl(new PageComponentInfo(new PageInfo(), null), handler.getPageParameters());
			}
			final IRequestablePage page = handler.getPage();
			if (page instanceof ConfigurationDefinedPage) {
				final PageInfo info = getPageInfo(handler);
				final PageComponentInfo pageComponentInfo = new PageComponentInfo(info, null);
				return buildUrl(pageComponentInfo, handler.getPageParameters());
			} else {
				return null;
			}

		} else if (requestHandler instanceof BookmarkableListenerInterfaceRequestHandler) {

			// bookmarkable listener case
			final BookmarkableListenerInterfaceRequestHandler handler = (BookmarkableListenerInterfaceRequestHandler)requestHandler;
			if (!handler.getPageClass().equals(ConfigurationDefinedPage.class)) {
				return null;
			}
			final RequestListenerInterface listenerInterface = handler.getListenerInterface();
			Integer renderCount = null;
			if (listenerInterface.isIncludeRenderCount()) {
				renderCount = handler.getRenderCount();
			}
			final PageInfo pageInfo = getPageInfo(handler);
			final ComponentInfo componentInfo = new ComponentInfo(renderCount, requestListenerInterfaceToString(listenerInterface), handler.getComponentPath(), handler.getBehaviorIndex());
			final PageParameters parameters = new PageParameters(handler.getPage().getPageParameters()).mergeWith(handler.getPageParameters());
			return buildUrl(new PageComponentInfo(pageInfo, componentInfo), parameters);

		} else if (requestHandler instanceof ListenerInterfaceRequestHandler) {

			// non-bookmarkable listener case
			final ListenerInterfaceRequestHandler handler = (ListenerInterfaceRequestHandler)requestHandler;
			final IRequestablePage page = handler.getPage();
			if (!handler.getPageClass().equals(ConfigurationDefinedPage.class)) {
				return null;
			}
			final RequestListenerInterface listenerInterface = handler.getListenerInterface();
			Integer renderCount = null;
			if (listenerInterface.isIncludeRenderCount()) {
				renderCount = page.getRenderCount();
			}
			final PageInfo pageInfo = getPageInfo(handler);
			final ComponentInfo componentInfo = new ComponentInfo(renderCount, requestListenerInterfaceToString(listenerInterface), handler.getComponentPath(), handler.getBehaviorIndex());
			final PageParameters parameters = new PageParameters(page.getPageParameters());
			return buildUrl(new PageComponentInfo(pageInfo, componentInfo), parameters.mergeWith(handler.getPageParameters()));

		}
		return null;
	}

	private PageInfo getPageInfo(final IPageRequestHandler handler) {
		Args.notNull(handler, "handler");
		Integer pageId = null;
		if (handler.isPageInstanceCreated()) {
			final IRequestablePage page = handler.getPage();
			if (page.isPageStateless() == false) {
				pageId = page.getPageId();
			}
		}
		return new PageInfo(pageId);
	}

	private Url buildUrl(final PageComponentInfo pageComponentInfo, final PageParameters pageParameters) {
		final String definitionPathText = pageParameters.get(ConfigurationDefinedPage.PAGE_DEFINITION_PATH_PARAMETER_NAME).toString();
		if (definitionPathText == null) {
			throw new IllegalArgumentException("cannot generate URL -- no page definition path parameter");
		}
		final DefinitionPath definitionPath = new DefinitionPath(definitionPathText);
		final Url url = new Url();
		for (final String s : definitionPath) {
			url.getSegments().add(s);
		}
		encodePageComponentInfo(url, pageComponentInfo);
		return encodePageParameters(url, new PageParameters(pageParameters).remove(ConfigurationDefinedPage.PAGE_DEFINITION_PATH_PARAMETER_NAME, (String[])null), pageParametersEncoder);
	}

	// override
	@Override
	public int getCompatibilityScore(Request request) {
		// this mapper can accept any URL, so it should have a low compatibility score to allow other mappers to take precedence
		// if they recognize a URL.
		return -1;
	}
	
	// override
	@Override
	protected void removeMetaParameter(final Url urlCopy) {
		if (MapperUtils.parsePageComponentInfoParameter(urlCopy.getQueryParameters().get(0)) != null) {
			urlCopy.getQueryParameters().remove(0);
		}
	}

}
