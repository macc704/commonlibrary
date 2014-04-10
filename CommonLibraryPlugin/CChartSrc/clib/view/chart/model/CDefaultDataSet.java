/*
 * CDefaultDataSet.java
 * Copyright (c) 2005 PlatBox Project.  All rights reserved.
 */
package clib.view.chart.model;

import java.awt.geom.Point2D;

/**
 * 実際に使用されるグラフモデルです AbstractGraphModelから継承したXY座標の情報および、折れ線を構成するGraphPoint群を保持します
 * 
 * @author macchan
 * @version $Id: CDefaultDataSet.java,v 1.2 2006/08/12 05:16:52 bam Exp $
 */
public class CDefaultDataSet extends CAbstractDataSet {

	private static final double MAX = Float.MAX_VALUE;
	private static final double MIN = Float.MIN_VALUE;

	// 折れ線を構成するGraphPoint群です
	private CPointList points = new CPointList();
	private boolean isActive = true;

	/**
	 * Constructor for CDefaultDataSet.
	 * 
	 * @param label
	 * @param axisX
	 * @param axisY
	 */
	public CDefaultDataSet(String label, CAxis axisX, CAxis axisY,
			Object dataSource) {
		super(label, axisX, axisY, dataSource);
	}

	/**
	 * Constructor for CDefaultDataSet.
	 */
	public CDefaultDataSet(CAxis x, CAxis y, Object dataSource) {
		super(x, y, dataSource);
	}

	public synchronized void addPoint(double x, double y) {
		addPoint(new Point2D.Double(x, y));
	}

	public synchronized void addPoint(Point2D p) {
		if (p.getX() > MAX) {
			// too many
			p.setLocation(MAX, p.getY());
		}

		if (p.getX() < MIN) {
			// too few
			p.setLocation(MIN, p.getY());
		}

		if (p.getY() > MAX) {
			// too many
			p.setLocation(p.getX(), MAX);
		}

		if (p.getY() < MIN) {
			// too few
			p.setLocation(p.getX(), MIN);
		}

		this.points.add(p);
	}

	public synchronized CPointList getPoints() {
		return new CPointList(this.points);
	}

	public synchronized void clear() {
		this.points.clear();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "CDefaultDataSet :" + this.getLabel();
	}

	public boolean isActive() {
		return isActive;
	}

	public void deactivate() {
		this.isActive = false;
	}

}