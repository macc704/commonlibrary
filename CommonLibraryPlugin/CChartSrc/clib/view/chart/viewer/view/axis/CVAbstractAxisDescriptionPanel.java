/*
 * CVAbstractAxisDescriptionPanel.java
 * Created on 2004/05/07
 * 
 * Copyright (c) 2005 PlatBox Project. All rights reserved.
 */
package clib.view.chart.viewer.view.axis;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import clib.common.thread.ICTask;
import clib.view.actions.CAction;
import clib.view.actions.CActionUtils;
import clib.view.chart.controller.CVChartController;
import clib.view.chart.viewer.model.CVAxis;
import clib.view.chart.viewer.resource.ChartResource;
import clib.view.chart.viewer.view.dialogs.CVAxisSettingDialogPanel;

/**
 * Class CVAbstractAxisDescriptionPanel.
 * 
 * @author macchan
 */
public abstract class CVAbstractAxisDescriptionPanel extends JComponent {

	private static final long serialVersionUID = 1L;

	private CVChartController container;
	private CVAxis axis;

	private CAction zoomInAction = CActionUtils.createAction(ChartResource
			.get("CVAbstractAxisDescriptionPanel.Zoom_In_Button"),
			new ICTask() {
				public void doTask() {
					zoom(2);
				}
			});
	private CAction zoomOutAction = CActionUtils.createAction(ChartResource
			.get("CVAbstractAxisDescriptionPanel.Zoom_Out_Button"),
			new ICTask() {
				public void doTask() {
					zoom(0.5);
				}
			});
	private CAction openDialogAction = CActionUtils.createAction(ChartResource
			.get("CVAbstractAxisDescriptionPanel.Open_Axis_Button"),
			new ICTask() {
				public void doTask() {
					openDialog();
				}
			});

	/**
	 * Constructor for CVAbstractAxisDescriptionPanel.
	 */
	public CVAbstractAxisDescriptionPanel(CVChartController container,
			CVAxis axis) {
		this.axis = axis;
		this.container = container;
	}

	protected JPanel createButtonsPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(createButtonPanelLayout(panel));
		panel.add(createButton(zoomInAction));
		panel.add(createButton(openDialogAction));
		panel.add(createButton(zoomOutAction));
		return panel;
	}

	private JButton createButton(Action action) {
		JButton button = new JButton(action);
		button.setHorizontalTextPosition(JButton.CENTER);
		button.setVerticalTextPosition(JButton.BOTTOM);
		Dimension d = new Dimension();
		d.width = CVAbstractAxisPanel.MEASURE_WIDTH;
		d.height = CVAbstractAxisPanel.MEASURE_WIDTH;
		button.setPreferredSize(d);
		// button.setMargin(new Insets(0, 0, 0, 0));
		button.setMargin(new Insets(-3, -3, -3, -3));
		return button;
	}

	protected abstract LayoutManager createButtonPanelLayout(JComponent comp);

	private void zoom(double power) {
		axis.getScale().zoom(power);
		container.refreshViews();
	}

	private void openDialog() {
		CVAxisSettingDialogPanel panel = new CVAxisSettingDialogPanel(
				container, axis);
		JOptionPane.showMessageDialog(null, panel, ChartResource
				.get("CVAbstractAxisDescriptionPanel.Axis_Setting"), //$NON-NLS-1$
				JOptionPane.PLAIN_MESSAGE);
		panel.apply();
	}

	public CVAxis getAxis() {
		return axis;
	}

	public CVChartController getController() {
		return container;
	}

}