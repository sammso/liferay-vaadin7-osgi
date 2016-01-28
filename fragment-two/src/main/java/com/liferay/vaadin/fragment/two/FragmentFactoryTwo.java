package com.liferay.vaadin.fragment.two;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.vaadin.fragment.api.FragmentFactory;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

@Component(service = FragmentFactory.class)
public class FragmentFactoryTwo implements FragmentFactory {

	@Override
	public com.vaadin.ui.Component getFragment() {
		_log.error("FragmentFactoryTwo getFragment");
		VerticalLayout verticalLayout = new VerticalLayout();

		Label clickedTimes = new Label();

		clickedTimes.setValue(String.valueOf(Integer.MAX_VALUE));

		verticalLayout.addComponent(new Button("Click me", (event) -> {
			Notification.show("Thank you for clicking!");
			String value = clickedTimes.getValue();

			int number = Integer.valueOf(value);
			clickedTimes.setValue(String.valueOf(--number));
		}));

		verticalLayout.addComponent(clickedTimes);

		return verticalLayout;
	}

	private Log _log = LogFactoryUtil.getLog(FragmentFactoryTwo.class);

}
