/*
 * CVChartController.java
 * Created on 2004/05/06
 * 
 * Copyright (c) 2005 PlatBox Project. All rights reserved.
 */
package clib.view.chart.controller;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JViewport;
import javax.swing.SwingUtilities;

import clib.view.chart.viewer.model.CVChart;
import clib.view.chart.viewer.model.CVDataSet;
import clib.view.chart.viewer.view.CVChartContainerPanel;

/**
 * Class CVChartController.
 * 
 * @author macchan
 * @version $Id: CVChartController.java,v 1.1 2005/03/08 03:04:46 bam Exp $
 */
public class CVChartController {

	public static final int MARGIN = 25;

	// Components
	private CVChart chart = new CVChart();
	private CVChartContainerPanel containerPanel;

	// Cashes
	private Point2D origin = new Point2D.Double(0, 0);
	private Dimension canvasSize;

	private ComponentListener resizeListener;

	private List<ICVChartDataSelectionListener> selectionListeners = new ArrayList<ICVChartDataSelectionListener>();

	/**
	 * Constructor for CVChartController.
	 */
	public CVChartController() {
		containerPanel = new CVChartContainerPanel(this);
		hookResizeListener();
	}

	public void hookResizeListener() {
		if (resizeListener == null) {
			resizeListener = new ComponentAdapter() {
				public void componentShown(ComponentEvent e) {
					refreshViews();
				}

				public void componentResized(ComponentEvent e) {
					refreshViews();
				}
			};
			getContainerPanel().getGraphCanvasScrollPane().getViewport()
					.addComponentListener(resizeListener);
		}
	}

	public void unhookResizeListener() {
		if (resizeListener != null) {
			getContainerPanel().getGraphCanvasScrollPane().getViewport()
					.removeComponentListener(resizeListener);
			resizeListener = null;
		}
	}

	public Window getOwner() {
		return SwingUtilities.getWindowAncestor(containerPanel);
	}

	public void setDataSelectionByPointer(List<String> pointers) {
		Map<String, CVDataSet> map = new HashMap<String, CVDataSet>();
		for (CVDataSet data : getChart().getDataSets()) {
			map.put(data.getModel().getLabel(), data);
		}
		List<CVDataSet> targets = new ArrayList<CVDataSet>();
		for (String pointer : pointers) {
			if (map.containsKey(pointer)) {
				targets.add(map.get(pointer));
			} else {
				// don't care
			}
		}
		// 下と重複するけど，外からの更新の場合再び外にfireしたくないので，コピーしてある．
		for (CVDataSet data : getChart().getDataSets()) {
			data.setSelected(false);
		}
		for (CVDataSet selected : targets) {
			selected.setSelected(true);
		}
		refreshViews();
	}

	public void setDataSelection(List<CVDataSet> selectedDatum) {
		for (CVDataSet data : getChart().getDataSets()) {
			data.setSelected(false);
		}
		for (CVDataSet selected : selectedDatum) {
			selected.setSelected(true);
		}
		refreshViews();
		fireSelectionStateChanged();
	}

	public void addDataSelection(List<CVDataSet> selectedDatum) {
		for (CVDataSet selected : selectedDatum) {
			selected.setSelected(true);
		}
		refreshViews();
		fireSelectionStateChanged();
	}

	public void clearDataSelection() {
		for (CVDataSet data : getChart().getDataSets()) {
			data.setSelected(false);
		}
		refreshViews();
		fireSelectionStateChanged();
	}

	public void refreshViews() {
		synchronized (containerPanel.getTreeLock()) {
			Dimension size = getContainerPanel().getGraphCanvasScrollPane()
					.getViewport().getSize();
			size.width -= MARGIN * 2;
			size.height -= MARGIN * 2;
			chart.autoScalling(size);
			chart.refreshView();

			refreshCanvasSize();
			refreshCanvas();
			refreshOrigin();
			repaintCanvas();

			containerPanel.refreshView();

			if (chart.isFollowingUpX()) {
				autoFollowingUp();
			}

		}
	}

	private void autoFollowingUp() {
		synchronized (containerPanel.getTreeLock()) {
			JViewport viewport = containerPanel.getGraphCanvasScrollPane()
					.getViewport();
			Point currentViewPosition = viewport.getViewPosition();
			Point newViewPosition = new Point(currentViewPosition);
			newViewPosition.x = viewport.getView().getSize().width
					- viewport.getSize().width;
			if (!currentViewPosition.equals(newViewPosition)) {
				viewport.setViewPosition(newViewPosition);
			}
		}
	}

	public CVChartContainerPanel getContainerPanel() {
		return containerPanel;
	}

	public CVChart getChart() {
		return chart;
	}

	void refreshCanvasSize() {
		double maxX = calcMaxX();
		double maxY = calcMaxY();
		double minX = calcMinX();
		double minY = calcMinY();

		double w = maxX - minX + MARGIN * 2;
		double h = maxY - minY + MARGIN * 2;

		this.canvasSize = new Dimension((int) w, (int) h);
	}

	void refreshCanvas() {
		containerPanel.getGraphCanvas().setPreferredSize(getCanvasSize());
		containerPanel.getGraphCanvas().revalidate();
	}

	void repaintCanvas() {
		containerPanel.getGraphCanvas().repaint();
	}

	void refreshOrigin() {
		double minX = calcMinX();
		double minY = calcMinY();
		int bottom = Math.max(canvasSize.height, getContainerPanel()
				.getGraphCanvasScrollPane().getHeight());
		this.origin = new Point2D.Double(-minX + MARGIN, bottom + minY - MARGIN);
	}

	/***********************
	 * :: Cash Getters
	 ***********************/

	public Point2D getOrigin() {
		return origin;
	}

	public Dimension getCanvasSize() {
		return canvasSize;
	}

	/**********************
	 * Calculate Max, Min
	 **********************/

	private double calcMaxX() {
		double max = 0;
		for (Iterator<CVDataSet> i = chart.getDataSets().iterator(); i
				.hasNext();) {
			CVDataSet data = (CVDataSet) i.next();
			max = Math.max(max, data.getScaledPoints().getMaxX());
		}
		return max;
	}

	private double calcMaxY() {
		double max = 0;
		for (Iterator<CVDataSet> i = chart.getDataSets().iterator(); i
				.hasNext();) {
			CVDataSet data = (CVDataSet) i.next();
			max = Math.max(max, data.getScaledPoints().getMaxY());
		}
		return max;
	}

	private double calcMinX() {
		double min = 0;
		for (Iterator<CVDataSet> i = chart.getDataSets().iterator(); i
				.hasNext();) {
			CVDataSet data = (CVDataSet) i.next();
			min = Math.min(min, data.getScaledPoints().getMinX());
		}
		return min;
	}

	private double calcMinY() {
		double min = 0;
		for (Iterator<CVDataSet> i = chart.getDataSets().iterator(); i
				.hasNext();) {
			CVDataSet data = (CVDataSet) i.next();
			min = Math.min(min, data.getScaledPoints().getMinY());
		}
		return min;
	}

	/**********************
	 * Listener
	 **********************/

	public void addDataSelectionListener(ICVChartDataSelectionListener listener) {
		selectionListeners.add(listener);
	}

	public void removeDataSelectionListener(
			ICVChartDataSelectionListener listener) {
		selectionListeners.remove(listener);
	}

	protected void fireSelectionStateChanged() {
		List<CVDataSet> selected = new ArrayList<CVDataSet>();
		for (CVDataSet dataset : getChart().getDataSets()) {
			if (dataset.isSelected()) {
				selected.add(dataset);
			}
		}
		for (ICVChartDataSelectionListener listener : selectionListeners) {
			listener.selectionStateChanged(selected);
		}
	}

	private boolean navigationEnabled = false;
	private CVNavigator navigator = new CVNavigator();

	public boolean isNavigationEnabled() {
		return navigationEnabled;
	}

	public void setNavigationEnabled(boolean navigationEnabled) {
		this.navigationEnabled = navigationEnabled;
	}

	public CVNavigator getNavigator() {
		if (navigationEnabled) {
			return navigator;
		} else {
			throw new IllegalStateException();
		}
	}

}