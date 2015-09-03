/*
 * CVDataSet.java
 * Created on 2004/05/05
 * 
 * Copyright (c) 2005 PlatBox Project. All rights reserved.
 */
package clib.view.chart.viewer.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import clib.view.chart.model.CPointList;
import clib.view.chart.model.ICDataSet;

/**
 * Class CVDataSet.
 * 
 * @author macchan
 * @version $Id: CVDataSet.java,v 1.1 2005/03/08 03:04:46 bam Exp $
 */
public class CVDataSet {

	// Relations
	private transient ICDataSet model;

	// Variables
	private Color color = Color.black;
	private boolean selected = false;
	private CVAxis axisX = new CVAxis();
	private CVAxis axisY = new CVAxis();
	private CVDataSet dependentX;
	private CVDataSet dependentY;

	// Cashes
	private CPointList scaledPoints;

	public CVDataSet(ICDataSet model) {
		this.model = model;
		axisX.setModel(model.getAxisX());
		axisY.setModel(model.getAxisY());
	}

	public ICDataSet getModel() {

		return model;
	}

	public void setModel(ICDataSet model) {
		this.model = model;
	}

	public CVAxis getAxisX() {
		if (dependentX != null && dependentX != this) {
			return dependentX.getAxisX();
		}
		return axisX;
	}

	public CVAxis getAxisY() {
		if (dependentY != null && dependentY != this) {
			return dependentY.getAxisY();
		}
		return axisY;
	}

	public CVDataSet getDependentX() {
		return dependentX;
	}

	public CVDataSet getDependentY() {
		return dependentY;
	}

	public List<CVDataSet> getDependentXList() {
		List<CVDataSet> list = new ArrayList<CVDataSet>();
		list.add(this);
		CVDataSet data = this;
		while (data.getDependentX() != this && data.getDependentX() != null) {
			list.add(data.getDependentX());
			data = data.getDependentX();
		}
		return list;
	}

	public List<CVDataSet> getDependentYList() {
		List<CVDataSet> list = new ArrayList<CVDataSet>();
		list.add(this);
		CVDataSet data = this;
		while (data.getDependentY() != this && data.getDependentY() != null) {
			list.add(data.getDependentY());
			data = data.getDependentY();
		}
		return list;
	}

	public void setDependentX(CVDataSet set) {
		dependentX = set;
	}

	public void setDependentY(CVDataSet set) {
		dependentY = set;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public void update() {
		createPointsCash();
	}

	private void createPointsCash() {
		this.scaledPoints = model.getPoints();
		double sx = getAxisX().getScale().getValue();
		double sy = getAxisY().getScale().getValue();
		scaledPoints.scale(sx, sy);
	}

	public CPointList getScaledPoints() {
		if (scaledPoints == null) {
			createPointsCash();
		}
		return new CPointList(scaledPoints);
	}

	public String toString() {
		return model.getLabel();
	}

}