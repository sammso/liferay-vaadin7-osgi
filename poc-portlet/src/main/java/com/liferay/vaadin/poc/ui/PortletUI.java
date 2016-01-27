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
package com.liferay.vaadin.poc.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import java.util.Date;

@Theme("valo")
@SuppressWarnings("serial")
/**
 * @author Sampsa Sohlman
 */
public class PortletUI extends UI {
	@Override
	protected void init(VaadinRequest request) {
		final VerticalLayout layout = new VerticalLayout();
		layout.setWidth(100, Unit.PERCENTAGE);
		setContent(layout);
		notes = new BeanItemContainer<Note>(Note.class);

		notes.addBean(new Note(noteCounter++, new Date(), "example note"));

		table = new Table("Notes", notes);
		table.setVisibleColumns(new Object[] { "time", "text" });
		table.setPageLength(7);
		table.setBuffered(false);
		table.setSelectable(true);
		table.setWidth(100, Unit.PERCENTAGE);

		textField = new TextField("Text");
		textField.setWidth(100, Unit.PERCENTAGE);
		layout.addComponent(textField);

		Button addButton = new Button("Add");
		addButton.setWidth(100, Unit.PERCENTAGE);
		layout.addComponent(addButton);
		removeButton = new Button("Remove");
		removeButton.setWidth(100, Unit.PERCENTAGE);
		removeButton.setEnabled(false);
		layout.addComponent(removeButton);

		layout.addComponent(table);

		addButton.addClickListener((event) -> {
			notes.addBean(new Note(noteCounter++, new Date(), textField
					.getValue()));
		});

		removeButton.addClickListener((event) -> {
			if (selectedNote != null) {
				notes.removeItem(selectedNote);
				selectedNote = null;
			}
			removeButton.setEnabled(selectedNote != null);
		});

		// Handle selection change.
		table.addValueChangeListener((event) -> {
			selectedNote = (Note) table.getValue();
			removeButton.setEnabled(selectedNote != null);
		});
	}

	int noteCounter = 1;

	Table table;
	TextField textField;
	BeanItemContainer<Note> notes;
	Note selectedNote;
	Button removeButton;
}