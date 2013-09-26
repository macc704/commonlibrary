/*
 * CAbstractDataSet.java
 * Copyright (c) 2005 PlatBox Project.  All rights reserved.
 */
package clib.view.chart.model;

/**
 * GraphModelの実装です グラフのラベル（名前）、およびＸ軸、Ｙ軸の情報をもちます
 * 
 * @author macchan
 * @version $Id: CAbstractDataSet.java,v 1.2 2006/08/12 05:16:52 bam Exp $
 */
public abstract class CAbstractDataSet implements ICDataSet {

	private String label = null;
	private Object dataSource;

	private CAxis axisX = null;
	private CAxis axisY = null;

	/**
	 * Constructor for CAbstractDataSet.
	 */
	public CAbstractDataSet(CAxis axisX, CAxis axisY, Object dataSource) {
		this.axisX = axisX;
		this.axisY = axisY;
		this.dataSource = dataSource;
	}

	/**
	 * Constructor for CAbstractDataSet.
	 */
	public CAbstractDataSet(String label, CAxis axisX, CAxis axisY,
			Object dataSource) {
		this.label = label;
		this.axisX = axisX;
		this.axisY = axisY;
		this.dataSource = dataSource;
	}

	/**
	 * @see org.platbox.ICDataSet.GraphModel#getLabel()
	 */
	public String getLabel() {
		return this.label;
	}

	/**
	 * Sets the label.
	 * 
	 * @param label
	 *            The label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @see org.platbox.ICDataSet.GraphModel#getAxisX()
	 */
	public CAxis getAxisX() {
		return this.axisX;
	}

	/**
	 * @see org.platbox.ICDataSet.GraphModel#getAxisY()
	 */
	public CAxis getAxisY() {
		return this.axisY;
	}

	/**
	 * Returns data source of the graph model
	 * 
	 * @return dataSource
	 */
	public Object getDataSource() {
		return dataSource;
	}

}