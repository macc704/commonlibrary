/*
 * CVAbstractDataDependentSettingPanel.java
 * Created on 2004/05/07
 * 
 * Copyright (c) 2005 PlatBox Project. All rights reserved.
 */
package clib.view.chart.viewer.view.dialogs;

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Iterator;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import clib.view.chart.controller.CVChartController;
import clib.view.chart.viewer.model.CVDataSet;

/**
 * Class CVAbstractDataDependentSettingPanel.
 * 
 * @author macchan
 * @version $Id: CVAbstractDataDependentSettingPanel.java,v 1.1 2005/03/08
 *          03:04:45 bam Exp $
 */
public abstract class CVAbstractDataDependentSettingPanel extends JPanel
		implements ItemListener {

	private static final long serialVersionUID = 1L;

	private CVChartController container;
	private CVDataSet data;

	private JCheckBox checkbox = new JCheckBox();
	@SuppressWarnings("rawtypes")
	private JComboBox combobox = new JComboBox();

	/**
	 * Constructor for CVAbstractDataDependentSettingPanel.
	 */
	public CVAbstractDataDependentSettingPanel(CVChartController container,
			CVDataSet data) {

		this.container = container;
		this.data = data;
		initialize();
	}

	private void initialize() {
		initializeComboBox();
		add(checkbox);
		add(combobox);
		initializeData();
		hook();
	}

	@SuppressWarnings("unchecked")
	private void initializeComboBox() {
		for (Iterator<CVDataSet> i = container.getChart().getDataSets()
				.iterator(); i.hasNext();) {
			CVDataSet data = (CVDataSet) i.next();
			combobox.addItem(data);
		}
	}

	void hook() {
		checkbox.addItemListener(this);
		combobox.addItemListener(this);
	}

	public void itemStateChanged(ItemEvent e) {
		update();
		updated();
	}

	private void initializeData() {
		checkbox.setSelected(isDependent());
		combobox.setEnabled(isDependent());
		combobox.setSelectedItem(getDependent());
	}

	public void update() {
		combobox.setBackground(Color.WHITE);

		// Update model from Checkbox and Combobox
		combobox.setEnabled(checkbox.isSelected());
		CVDataSet newDependent = null;
		if (checkbox.isSelected()) {
			CVDataSet selectedData = (CVDataSet) combobox.getSelectedItem();
			if (selectedData != null
					&& getDependentList(selectedData).contains(data)) {
				combobox.setBackground(Color.RED);
			} else {
				newDependent = selectedData;
			}
		}
		setDependent(newDependent);
	}

	public CVDataSet getData() {

		return data;
	}

	public abstract List<CVDataSet> getDependentList(CVDataSet data);

	public abstract void setDependent(CVDataSet data);

	public abstract void updated();

	public abstract CVDataSet getDependent();

	public abstract boolean isDependent();

}