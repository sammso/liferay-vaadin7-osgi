package com.liferay.vaadin.fragment.portlet;

import org.osgi.service.component.annotations.Component;

import com.vaadin.server.VaadinPortlet;

@Component(
		immediate = true,
		property = {
			"com.liferay.portlet.display-category=category.vaadin",
			"com.liferay.portlet.instanceable=true",
			"javax.portlet.display-name=Vaadin OSGi Fragment Portlet",
			"javax.portlet.init-param.UI=com.liferay.vaadin.fragment.ui.UI",
			"javax.portlet.security-role-ref=power-user,user"
		},
		service = javax.portlet.Portlet.class
	)
public class Portlet extends VaadinPortlet {

}
