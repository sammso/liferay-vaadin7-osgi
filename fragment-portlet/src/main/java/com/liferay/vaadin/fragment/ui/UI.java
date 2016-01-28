package com.liferay.vaadin.fragment.ui;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.vaadin.fragment.api.FragmentFactory;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

@Theme("valo")
@SuppressWarnings("serial")
/**
 * @author Sampsa Sohlman
 */
public class UI extends com.vaadin.ui.UI implements 
	ServiceTrackerCustomizer<FragmentFactory, FragmentFactory> {

	@Override
	public FragmentFactory addingService(
			ServiceReference<FragmentFactory> serviceReference) {
		
		_log.error("addingService");
		
		Bundle bundle = FrameworkUtil.getBundle(getClass());
		BundleContext bundleContext = bundle.getBundleContext();
		
		FragmentFactory fragmentFactory = 
			bundleContext.getService(serviceReference);
		
		access(()-> {
			Component component = fragmentFactory.getFragment();
			_componentRegistry.put(serviceReference, Arrays.asList(component));
			
			VerticalLayout verticalLayout = (VerticalLayout) getContent();
			verticalLayout.addComponent(component);			
		});
	
		return fragmentFactory;
	}

	@Override
	public void modifiedService(
			ServiceReference<FragmentFactory> reference, 
			FragmentFactory service) {
		// 
		
	}

	@Override
	public void removedService(
			ServiceReference<FragmentFactory> reference, 
			FragmentFactory service) {
		
		_log.error("removedService");
		
		access(() -> {
			VerticalLayout verticalLayout = (VerticalLayout) getContent();
			_componentRegistry.get(reference).forEach(
				component -> {
					verticalLayout.removeComponent(component);
				}
			);
			_componentRegistry.remove(reference);
		});
		
	}
	
	@Override
	protected void init(VaadinRequest request) {
		_log.error("init");
		setPollInterval(1000);
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setWidth(100, Unit.PERCENTAGE);
		setContent(verticalLayout);

		Bundle bundle = FrameworkUtil.getBundle(getClass());
		BundleContext bundleContext = bundle.getBundleContext();

		ServiceTracker<FragmentFactory, FragmentFactory> serviceTracker = 
			new ServiceTracker<FragmentFactory, FragmentFactory>(
				bundleContext, FragmentFactory.class, this);
		
		serviceTracker.open();

		addDetachListener((event) -> {
			serviceTracker.close();
		});
	}	

	private Map<ServiceReference<FragmentFactory>, List<Component>> 
		_componentRegistry = new HashMap<>();

	private Log _log = LogFactoryUtil.getLog(UI.class);
}
