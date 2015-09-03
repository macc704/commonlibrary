/*
 * CVEasySettingPanel.java
 * Created on 2004/05/07
 * 
 * Copyright (c) 2005 PlatBox Project. All rights reserved.
 */
package clib.view.chart.viewer.view;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import clib.view.chart.controller.CVChartController;
import clib.view.chart.viewer.resource.ChartResource;
import clib.view.panels.CVerticalFlowLayout;

/**
 * Class CVEasySettingPanel.
 * 
 * @author macchan
 * @version $Id: CVEasySettingPanel.java,v 1.1 2005/03/08 03:04:44 bam Exp $
 */
public class CVEasySettingPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	// Relation
	private CVChartController container;

	// Components
	private JCheckBox autoScallingXCheckBox = new JCheckBox();
	private JCheckBox autoScallingYCheckBox = new JCheckBox();
	private JCheckBox autoFollowingUpXCheckBox = new JCheckBox();
	private JCheckBox showGridXCheckBox = new JCheckBox();
	private JCheckBox showGridYCheckBox = new JCheckBox();

	/**
	 * Constructor for CVEasySettingPanel.
	 */
	public CVEasySettingPanel() {

		initialize();
	}

	private void initialize() {

		setLayout(new CVerticalFlowLayout(2));

		autoScallingXCheckBox.setText(ChartResource
				.get("CVEasySettingPanel.Auto_Scalling_for_X_Axis")); //$NON-NLS-1$
		add(autoScallingXCheckBox);

		autoScallingYCheckBox.setText(ChartResource
				.get("CVEasySettingPanel.Auto_Scalling_for_Y_Axis")); //$NON-NLS-1$
		add(autoScallingYCheckBox);

		autoFollowingUpXCheckBox.setText(ChartResource
				.get("CVEasySettingPanel.Auto_Following_up_for_X_Axis")); //$NON-NLS-1$
		add(autoFollowingUpXCheckBox);

		showGridXCheckBox.setText(ChartResource
				.get("CVEasySettingPanel.Show_Grid_for_X_Axis")); //$NON-NLS-1$
		add(showGridXCheckBox);

		showGridYCheckBox.setText(ChartResource
				.get("CVEasySettingPanel.Show_Gird_for_Y_Axis")); //$NON-NLS-1$
		add(showGridYCheckBox);

	}

	private void initializeState() {

		autoScallingXCheckBox.setSelected(container.getChart()
				.isAutoScallingX());
		autoScallingYCheckBox.setSelected(container.getChart()
				.isAutoScallingY());
		autoFollowingUpXCheckBox.setSelected(container.getChart()
				.isFollowingUpX());
		showGridXCheckBox.setSelected(container.getChart().isShowGridX());
		showGridYCheckBox.setSelected(container.getChart().isShowGridY());
	}

	private void hook() {

		autoScallingXCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {

				container.getChart().setAutoScallingX(
						autoScallingXCheckBox.isSelected());
				container.refreshViews();
			}
		});
		autoScallingYCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {

				container.getChart().setAutoScallingY(
						autoScallingYCheckBox.isSelected());
				container.refreshViews();
			}
		});
		autoFollowingUpXCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {

				container.getChart().setFollowingUpX(
						autoFollowingUpXCheckBox.isSelected());
				container.refreshViews();
			}
		});
		showGridXCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {

				container.getChart().setShowGridX(
						showGridXCheckBox.isSelected());
				container.refreshViews();
			}
		});
		showGridYCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {

				container.getChart().setShowGridY(
						showGridYCheckBox.isSelected());
				container.refreshViews();
			}
		});
	}

	public void setContainer(CVChartController container) {

		this.container = container;
		initializeState();
		hook();
	}

}