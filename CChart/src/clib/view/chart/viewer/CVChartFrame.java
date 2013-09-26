/*
 * CVChartFrame.java
 * Created on Jul 14, 2010 
 * Copyright(c) 2010 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.chart.viewer;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;

import clib.view.chart.controller.CVChartController;

/**
 * @author macchan
 * 
 */
public class CVChartFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private CVChartController controller;

	public CVChartFrame() {
		this(new CVChartController());
	}

	public CVChartFrame(CVChartController control) {
		this.controller = control;

		setTitle("Graph");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 700, 500);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(controller.getContainerPanel(),
				BorderLayout.CENTER);
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				controller.refreshViews();
			}
		});
	}

	/**
	 * @return the controller
	 */
	public CVChartController getController() {
		return controller;
	}
}
