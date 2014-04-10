/*
 * CVChart.java
 * Created on 2004/05/05
 * 
 * Copyright (c) 2005 PlatBox Project. All rights reserved.
 */
package clib.view.chart.viewer.model;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import clib.crewlib.OMultipleValueMap;
import clib.view.chart.controller.CVChartController;
import clib.view.chart.model.ICDataSet;

/**
 * Class CVChart.
 * 
 * @author macchan
 * @version $Id: CVChart.java,v 1.2 2006/08/12 05:16:52 bam Exp $
 */
public class CVChart {

	public static final Color[] COLORS = new Color[] { Color.BLACK, Color.BLUE,
			Color.RED, Color.GREEN, Color.MAGENTA, Color.CYAN, Color.ORANGE,
			Color.PINK, Color.YELLOW };

	// Relations
	private CVChartController container;

	// Vaiables
	private List<CVDataSet> datasets = new ArrayList<CVDataSet>();

	private boolean followingUpX = false;
	private boolean autoScallingX = true;
	private boolean autoScallingY = true;
	private boolean showGridX = true;
	private boolean showGridY = true;

	public CVChart() {
	}

	public void clear() {
		datasets.clear();
	}

	public void addData(ICDataSet model) {
		CVDataSet data = new CVDataSet(model);

		// 軸の依存性の設定をする　050214青山
		if (!datasets.isEmpty()) {
			CVDataSet lastData = (CVDataSet) this.datasets
					.get(datasets.size() - 1);

			data.setDependentX(lastData);
			data.setDependentY(lastData);
		}

		datasets.add(data);
		data.setColor(getInitialColor(datasets.indexOf(data)));
	}

	public void removeData(CVDataSet dataSet) {
		datasets.remove(dataSet);
	}

	public List<CVDataSet> getDataSets() {
		return new ArrayList<CVDataSet>(datasets);
	}

	public List<CVAxis> getAxisListX() {
		Set<CVAxis> axises = new LinkedHashSet<CVAxis>();
		for (CVDataSet data : datasets) {
			axises.add(data.getAxisX());
		}
		return new ArrayList<CVAxis>(axises);
	}

	public List<CVAxis> getAxisListY() {
		Set<CVAxis> axises = new LinkedHashSet<CVAxis>();
		for (CVDataSet data : datasets) {
			axises.add(data.getAxisY());
		}
		return new ArrayList<CVAxis>(axises);
	}

	public void refreshView() {
		updateDataSets();
	}

	public void autoScalling(Dimension canvasSize) {

		// Create Map
		OMultipleValueMap<CVAxis, CVDataSet> axisMapX = new OMultipleValueMap<CVAxis, CVDataSet>();
		OMultipleValueMap<CVAxis, CVDataSet> axisMapY = new OMultipleValueMap<CVAxis, CVDataSet>();
		for (CVDataSet data : datasets) {
			axisMapX.put(data.getAxisX(), data);
			axisMapY.put(data.getAxisY(), data);
		}

		// Auto Scalling X
		for (CVAxis axis : axisMapX.getKeys()) {
			List<CVDataSet> datasets = axisMapX.get(axis);
			autoScallingX(axis, datasets, canvasSize.width);
		}

		// Auto Scalling Y;
		for (CVAxis axis : axisMapY.getKeys()) {
			List<CVDataSet> datasets = axisMapY.get(axis);
			autoScallingY(axis, datasets, canvasSize.height);
		}
	}

	private void autoScallingX(CVAxis axis, List<CVDataSet> datasets,
			double canvasW) {
		if (autoScallingX || axis.getScale().isAuto()) {
			double maxX = calcOriginalMaxX(datasets);
			double minX = calcOriginalMinX(datasets);
			double dataW = maxX - minX;
			double scale = canvasW / dataW;
			axis.getScale().setValue(scale);
		}
	}

	private void autoScallingY(CVAxis axis, List<CVDataSet> datasets,
			double canvasH) {

		if (autoScallingY || axis.getScale().isAuto()) {
			double maxY = calcOriginalMaxY(datasets);
			double minY = calcOriginalMinY(datasets);
			double dataH = maxY - minY;
			double scale = canvasH / dataH;
			axis.getScale().setValue(scale);
		}
	}

	private void updateDataSets() {
		for (CVDataSet data : datasets) {
			data.update();
		}
	}

	public Color getInitialColor(int i) {
		return COLORS[i % COLORS.length];
	}

	public CVChartController getContainer() {
		return container;
	}

	public void setContainer(CVChartController container) {
		this.container = container;
	}

	public boolean isAutoScallingX() {
		return autoScallingX;
	}

	public boolean isAutoScallingY() {
		return autoScallingY;
	}

	public boolean isFollowingUpX() {
		return followingUpX;
	}

	public void setAutoScallingX(boolean autoscalling) {
		autoScallingX = autoscalling;
	}

	public void setAutoScallingY(boolean autoscalling) {
		autoScallingY = autoscalling;
	}

	public void setFollowingUpX(boolean autoscalling) {
		followingUpX = autoscalling;
	}

	/**********************
	 * Calculate Max, Min
	 **********************/

	private double calcOriginalMaxX(List<CVDataSet> datasets) {
		double max = 0;
		for (CVDataSet data : datasets) {
			max = Math.max(max, data.getModel().getPoints().getMaxX());
		}
		return max;
	}

	private double calcOriginalMaxY(List<CVDataSet> datasets) {
		double max = 0;
		for (CVDataSet data : datasets) {
			max = Math.max(max, data.getModel().getPoints().getMaxY());
		}
		return max;
	}

	private double calcOriginalMinX(List<CVDataSet> datasets) {
		double min = 0;
		for (CVDataSet data : datasets) {
			min = Math.min(min, data.getModel().getPoints().getMinX());
		}
		return min;
	}

	private double calcOriginalMinY(List<CVDataSet> datasets) {
		double min = 0;
		for (CVDataSet data : datasets) {
			min = Math.min(min, data.getModel().getPoints().getMinY());
		}
		return min;
	}

	public boolean isShowGridX() {
		return showGridX;
	}

	public boolean isShowGridY() {
		return showGridY;
	}

	public void setShowGridX(boolean showGridX) {
		this.showGridX = showGridX;
	}

	public void setShowGridY(boolean showGridY) {
		this.showGridY = showGridY;
	}

}