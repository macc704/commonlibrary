/*
 * CVAxisSettingDialogPanel.java
 * Created on 2004/05/07
 * 
 * Copyright (c) 2005 PlatBox Project. All rights reserved.
 */
package clib.view.chart.viewer.view.dialogs;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import clib.view.chart.controller.CVChartController;
import clib.view.chart.viewer.model.CVAxis;
import clib.view.chart.viewer.resource.ChartResource;
import clib.view.panels.CVerticalFlowLayout;

/**
 * Class CVAxisSettingDialogPanel.
 * 
 * @author macchan
 * @version $Id: CVAxisSettingDialogPanel.java,v 1.1 2005/03/08 03:04:45 bam Exp
 *          $
 */
public class CVAxisSettingDialogPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private CVChartController container;
	private CVAxis axis;

	private CVMeasureSettingPanel measureSettingPanel;
	private CVScaleSettingPanel scaleSettingPanel;

	/**
	 * Constructor for CVAxisSettingDialogPanel.
	 */
	public CVAxisSettingDialogPanel(CVChartController container, CVAxis axis) {

		this.container = container;
		this.axis = axis;
		initialize();
	}

	private void initialize() {

		measureSettingPanel = new CVMeasureSettingPanel(container, axis);
		measureSettingPanel.setBorder(BorderFactory
				.createTitledBorder(ChartResource
						.get("CVAxisSettingDialogPanel.Measure"))); //$NON-NLS-1$

		scaleSettingPanel = new CVScaleSettingPanel(container, axis);
		scaleSettingPanel.setBorder(BorderFactory
				.createTitledBorder(ChartResource
						.get("CVAxisSettingDialogPanel.Scale"))); //$NON-NLS-1$

		setLayout(new CVerticalFlowLayout(2));
		add(measureSettingPanel);
		add(scaleSettingPanel);
	}

	public void apply() {

		measureSettingPanel.apply();
		scaleSettingPanel.apply();
		container.refreshViews();
	}

}