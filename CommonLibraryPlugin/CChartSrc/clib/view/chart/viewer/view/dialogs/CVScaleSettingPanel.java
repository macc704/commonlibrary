/*
 * CVScaleSettingPanel.java
 * Created on 2004/05/12
 * 
 * Copyright (c) 2005 PlatBox Project. All rights reserved.
 */
package clib.view.chart.viewer.view.dialogs;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import clib.view.chart.controller.CVChartController;
import clib.view.chart.viewer.model.CVAxis;
import clib.view.chart.viewer.resource.ChartResource;
import clib.view.panels.CVerticalFlowLayout;

/**
 * Class CVScaleSettingPanel.
 * 
 * @author macchan
 * @version $Id: CVScaleSettingPanel.java,v 1.1 2005/03/08 03:04:45 bam Exp $
 */
public class CVScaleSettingPanel extends JPanel implements ItemListener {

	private static final long serialVersionUID = 1L;

	private CVChartController container;
	private CVAxis axis;

	private JCheckBox checkbox = new JCheckBox();
	private ManualSettingPanel manualSettingPanel = new ManualSettingPanel();

	/**
	 * Constructor for CVScaleSettingPanel.
	 */
	public CVScaleSettingPanel(CVChartController container, CVAxis axis) {
		this.container = container;
		this.axis = axis;
		initialize();
	}

	private void initialize() {
		setLayout(new CVerticalFlowLayout());

		manualSettingPanel.initialize();
		initializeData();
		checkbox.setText(ChartResource.get("CVScaleSettingPanel.Auto")); //$NON-NLS-1$

		add(checkbox);
		add(manualSettingPanel);

		hook();
	}

	public void initializeData() {
		checkbox.setSelected(axis.getScale().isAuto());
		manualSettingPanel.setEnabled(!axis.getScale().isAuto());
	}

	private void hook() {
		checkbox.addItemListener(this);
	}

	public void itemStateChanged(ItemEvent e) {
		update();
	}

	public void update() {
		axis.getScale().setAuto(checkbox.isSelected());
		manualSettingPanel.setEnabled(!checkbox.isSelected());
	}

	public void apply() {
		manualSettingPanel.apply();
	}

	class ManualSettingPanel extends CVAbstractDoubleValueSettingPanel {
		private static final long serialVersionUID = 1L;

		protected String getUnit() {
			return ChartResource.get("CVScaleSettingPanel.Percent"); //$NON-NLS-1$
		}

		protected double getValue() {
			return axis.getScale().getValue() * 100;
		}

		protected void setValue(double value) {
			axis.getScale().setValue(value / 100);
		}

		protected void applied() {
			container.refreshViews();
		}

	}

}