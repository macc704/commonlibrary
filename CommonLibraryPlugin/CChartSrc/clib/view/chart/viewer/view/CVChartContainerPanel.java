/*
 * CVChartContainerPanel.java
 * Created on 2004/05/05
 * 
 * Copyright (c) 2005 PlatBox Project. All rights reserved.
 */
package clib.view.chart.viewer.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import clib.view.chart.controller.CVChartController;
import clib.view.chart.viewer.export.CVExportImageController;
import clib.view.chart.viewer.resource.ChartResource;
import clib.view.chart.viewer.view.axis.CVXAxisContainerPanel;
import clib.view.chart.viewer.view.axis.CVYAxisContainerPanel;
import clib.view.panels.COverViewPanel;
import clib.view.scrollpane.CHandMoveInScrollPaneMouseHandler;

/**
 * Class CVChartContainerPanel.
 * 
 * @author macchan
 * @version $Id: CVChartContainerPanel.java,v 1.1 2005/03/08 03:04:44 bam Exp $
 */
public class CVChartContainerPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	// Relations
	private CVChartController controller;

	// Components
	private JScrollPane graphCanvasScrollPane = new JScrollPane();
	private CVChartCanvas graphCanvas = new CVChartCanvas();
	private CVXAxisContainerPanel xAxisContainerPanel = new CVXAxisContainerPanel();
	private CVYAxisContainerPanel yAxisContainerPanel = new CVYAxisContainerPanel();
	private CVChartDataSetListPanel datalistPanel = new CVChartDataSetListPanel();
	private CVEasySettingPanel easySettingPanel = new CVEasySettingPanel();

	/**
	 * Constructor for CVChartContainerPanel.
	 */
	public CVChartContainerPanel(CVChartController container) {
		this.controller = container;
		initialize();
	}

	private void initialize() {
		setLayout(new BorderLayout());
		JSplitPane split = new JSplitPane();
		split.add(createCenterPanel(), JSplitPane.RIGHT);
		split.add(createEastPanel(), JSplitPane.LEFT);
		split.setDividerLocation(220);
		add(split);
	}

	private JPanel createCenterPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new CVContainerBorderLayout());

		graphCanvas.setContainer(controller);
		graphCanvasScrollPane.setViewportView(graphCanvas);
		CHandMoveInScrollPaneMouseHandler handler = new CHandMoveInScrollPaneMouseHandler(
				graphCanvasScrollPane.getViewport());
		graphCanvas.addMouseListener(handler);
		graphCanvas.addMouseMotionListener(handler);
		panel.add(graphCanvasScrollPane, BorderLayout.CENTER);

		xAxisContainerPanel.setViewport(graphCanvasScrollPane.getViewport());
		xAxisContainerPanel.setController(controller);
		panel.add(xAxisContainerPanel, BorderLayout.SOUTH);

		yAxisContainerPanel.setViewport(graphCanvasScrollPane.getViewport());
		yAxisContainerPanel.setController(controller);
		panel.add(yAxisContainerPanel, BorderLayout.WEST);
		return panel;
	}

	private JPanel createEastPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		split.add(createDescriptionPanel(), JSplitPane.TOP);
		split.add(createSouthEastPanel(), JSplitPane.BOTTOM);
		split.setResizeWeight(0.75d);
		panel.add(split);
		return panel;
	}

	private JPanel createDescriptionPanel() {
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder(ChartResource
				.get("CVChartContainerPanel.Data_Explanation"))); //$NON-NLS-1$
		panel.setLayout(new BorderLayout());
		datalistPanel.setContainer(controller);
		panel.add(datalistPanel);
		return panel;
	}

	private JPanel createSouthEastPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(createEasySettingPanel());

		// JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		// split.add(createEasySettingPanel(), JSplitPane.TOP);
		// split.add(createOverviewPanel(), JSplitPane.BOTTOM);
		// split.setResizeWeight(0.75d);
		// panel.add(split);

		return panel;
	}

	private JPanel createEasySettingPanel() {
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder(ChartResource
				.get("CVChartContainerPanel.Easy_Setting"))); //$NON-NLS-1$
		panel.setLayout(new BorderLayout());
		easySettingPanel.setContainer(controller);
		panel.add(new JScrollPane(easySettingPanel));
		return panel;
	}

	public JPanel createOverviewPanel() {
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder(ChartResource
				.get("CVChartContainerPanel.Overview"))); //$NON-NLS-1$
		panel.setLayout(new BorderLayout());
		COverViewPanel overviewPanel = new COverViewPanel(graphCanvasScrollPane
				.getViewport());
		overviewPanel.setPreferredSize(new Dimension(120, 90));
		panel.add(overviewPanel);
		return panel;
	}

	public CVExportImageController createExportImageController() {
		return new CVExportImageController(graphCanvas, xAxisContainerPanel,
				yAxisContainerPanel);
	}

	/**********************
	 * Update
	 **********************/

	public void refreshView() {
		xAxisContainerPanel.refreshView();
		yAxisContainerPanel.refreshView();

		datalistPanel.refreshView();
	}

	public CVChartCanvas getGraphCanvas() {
		return graphCanvas;
	}

	public JScrollPane getGraphCanvasScrollPane() {
		return graphCanvasScrollPane;
	}
}