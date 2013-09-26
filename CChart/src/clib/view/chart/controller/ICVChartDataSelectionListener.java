/*
 * ICVChartDataSelectionListener.java
 * Created on May 6, 2011 
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.chart.controller;

import java.util.List;

import clib.view.chart.viewer.model.CVDataSet;

/**
 * @author macchan
 * 
 */
public interface ICVChartDataSelectionListener {
	public void selectionStateChanged(List<CVDataSet> selectedSets);
}
