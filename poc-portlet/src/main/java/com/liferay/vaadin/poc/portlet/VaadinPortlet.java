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
package com.liferay.vaadin.poc.portlet;

import javax.portlet.Portlet;

import org.osgi.service.component.annotations.Component;

@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.display-category=category.vaadin",
		"com.liferay.portlet.instanceable=true",
		"javax.portlet.display-name=Vaadin OSGi PoC Portlet",
		"javax.portlet.init-param.UI=com.liferay.vaadin.poc.ui.PortletUI",
		"javax.portlet.security-role-ref=power-user,user"
	},
	service = Portlet.class
)
/**
 * @author Sampsa Sohlman
 */
public class VaadinPortlet extends com.vaadin.server.VaadinPortlet {

}