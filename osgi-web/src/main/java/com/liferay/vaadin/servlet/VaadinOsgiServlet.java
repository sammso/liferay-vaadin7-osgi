/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.vaadin.servlet;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.Filter;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * This servlet has been created to serve static resources from
 * vaadin-client-compiled.jar and vaadin-themes.jar
 * 
 * @author Sampsa Sohlman
 */
@Component(immediate = true, property = {
		"osgi.http.whiteboard.servlet.name=Vaadin Resources Servlet",
		"osgi.http.whiteboard.servlet.pattern="
				+ VaadinOsgiServlet.VAADIN7_PATH + "/*",
		"service.ranking:Integer=100" }, service = { Servlet.class })
public class VaadinOsgiServlet extends HttpServlet
		implements
		Servlet,
		ServiceTrackerCustomizer<ServletContext, ServiceReference<ServletContext>> {

	public VaadinOsgiServlet() {
		_mimeTypeMap = new HashMap<>();

		_mimeTypeMap.put(".js", "text/javascript");
		_mimeTypeMap.put(".css", "text/css");
		_mimeTypeMap.put(".jpg", "image/jpeg");
		_mimeTypeMap.put(".jpeg", "image/jpeg");
		_mimeTypeMap.put(".gif", "image/gif");
		_mimeTypeMap.put(".png", "image/png");
		_mimeTypeMap.put(".ttf", "font/truetype");
		_mimeTypeMap.put(".woff", "application/x-font-woff");
	}

	@Override
	public ServiceReference<ServletContext> addingService(
			ServiceReference<ServletContext> serviceReference) {

		String contextPath = (String) serviceReference
				.getProperty("osgi.web.contextpath");

		return serviceReference;
	}

	@Override
	public void modifiedService(
			ServiceReference<ServletContext> serviceReference,
			ServiceReference<ServletContext> trackedServiceReference) {

		removedService(serviceReference, trackedServiceReference);

	}

	@Override
	public void removedService(
			ServiceReference<ServletContext> serviceReference,
			ServiceReference<ServletContext> trackedServiceReference) {

	}

	@Activate
	@Modified
	protected void activate(ComponentContext componentContext,
			Map<String, Object> properties) throws Exception {

		if (_serviceTracker != null) {
			_serviceTracker.close();
		}

		Filter filter = FrameworkUtil
				.createFilter("(&(objectClass="
						+ ServletContext.class.getName()
						+ ")(osgi.web.contextpath=*))");

		_serviceTracker = new ServiceTracker<>(
				componentContext.getBundleContext(), filter, this);

		_serviceTracker.open();
	}

	@Deactivate
	protected void deactivate() {
		_serviceTracker.close();

		_serviceTracker = null;
	}

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		String requestURI = request.getRequestURI();

		int pos = requestURI.indexOf(VAADIN7_PATH) + VAADIN7_PATH.length();

		if (pos < 0) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			if (_log.isDebugEnabled()) {
				_log.debug("VAADIN7_PATH not found from URL ignored"
						+ requestURI);
			}
			return;
		}

		String path = requestURI.substring(pos);

		int lastDotPos = requestURI.lastIndexOf('.');

		if (lastDotPos < 0) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			if (_log.isDebugEnabled()) {
				_log.debug("Url doesn not contain dot so content type is not found. URL: "
						+ requestURI);
			}
			return;
		}

		String ext = requestURI.substring(lastDotPos);

		String contentType = _mimeTypeMap.get(ext.toLowerCase());

		if (contentType == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			if (_log.isDebugEnabled()) {
				_log.debug("Content type not found for URL " + requestURI);
			}
			return;
		}

		InputStream in = getClass().getResourceAsStream(path);

		if (in == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			if (_log.isDebugEnabled()) {
				_log.debug("Resource not found from classpath for URL: "
						+ requestURI);
			}
			return;
		}

		response.setContentType(contentType);

		OutputStream out = response.getOutputStream();

		byte[] buffer = new byte[1024];
		int len = in.read(buffer);
		while (len != -1) {
			out.write(buffer, 0, len);
			len = in.read(buffer);
		}

		out.close();
	}

	public static final String VAADIN7_PATH = "/vaadin7";

	private Map<String, String> _mimeTypeMap;

	private ServiceTracker<ServletContext, ServiceReference<ServletContext>> _serviceTracker;

	private static final Log _log = LogFactoryUtil
			.getLog(VaadinOsgiServlet.class);

}