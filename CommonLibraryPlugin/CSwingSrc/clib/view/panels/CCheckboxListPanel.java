/*
 * CCheckboxListPanel.java
 * Created on Mar 4, 2011 
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.panels;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * @author macchan
 * 
 */
public class CCheckboxListPanel<T> extends JPanel {

	private static final long serialVersionUID = 1L;

	// models
	private Map<JCheckBox, T> models = new LinkedHashMap<JCheckBox, T>();
	private Map<T, JCheckBox> checkboxes = new LinkedHashMap<T, JCheckBox>();

	// views
	private JScrollPane scroll = new JScrollPane();
	private JPanel listPanel = CPanelUtils.createListPanel();
	private JPanel allPanel = CPanelUtils.createListPanel();
	private JCheckBox checkboxAll = new JCheckBox("All");

	public CCheckboxListPanel() {
		initialize();
	}

	public CCheckboxListPanel(List<T> models) {
		initialize();
		addModels(models);
	}

	private void initialize() {
		setLayout(new BorderLayout());

		scroll.setViewportView(listPanel);
		add(scroll, BorderLayout.CENTER);

		checkboxAll.setOpaque(false);
		allPanel.add(checkboxAll);
		// add(allPanel, BorderLayout.SOUTH);
		allPanel.setBorder(BorderFactory.createRaisedBevelBorder());
		scroll.setColumnHeaderView(allPanel);

		checkboxAll.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				processAllCheckButton();
			}
		});
	}

	private void processAllCheckButton() {
		boolean selected = checkboxAll.isSelected();
		for (JCheckBox checkbox : models.keySet()) {
			checkbox.setSelected(selected);
		}
	}

	public void addModels(List<T> models) {
		for (T model : models) {
			addModel(model);
		}
	}

	public void addModel(T model) {
		JCheckBox checkbox = new JCheckBox(model.toString());
		checkbox.setOpaque(false);
		listPanel.add(checkbox);
		models.put(checkbox, model);
		checkboxes.put(model, checkbox);
	}

	public List<T> getSelectedModels() {
		List<T> selecteds = new ArrayList<T>();
		for (JCheckBox checkbox : checkboxes.values()) {
			if (checkbox.isSelected()) {
				selecteds.add(models.get(checkbox));
			}
		}
		return selecteds;
	}

	/**
	 * @param models
	 */
	public void setSelection(List<T> models) {
		clearSelection();
		addSelection(models);
	}

	/**
	 * @param models
	 */
	public void addSelection(List<T> models) {
		for (T model : models) {
			if (checkboxes.containsKey(model)) {
				JCheckBox checkbox = checkboxes.get(model);
				checkbox.setSelected(true);
			} else {
				System.err.println(model + "is not found.");
			}
		}
	}

	public void clearSelection() {
		for (JCheckBox checkbox : checkboxes.values()) {
			checkbox.setSelected(false);
		}
	}
}
