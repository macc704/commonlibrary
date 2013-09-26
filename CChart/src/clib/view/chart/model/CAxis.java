/**
 * CAxis.java
 * Copyright (c) 2005 PlatBox Project. All rights reserved.
 */
package clib.view.chart.model;

/**
 * グラフモデルがもつ軸（Ｘ軸、Ｙ軸）の情報です 「軸の名前」と「軸の単位の名前」をもちます
 * 
 * @author Administrator
 * @version $Id: CAxis.java,v 1.1 2005/03/08 03:04:45 bam Exp $
 */
public class CAxis {

	// 軸の名前です
	private String name = null;
	// 軸の単位の名前です
	private CUnit unit = null;

	/**
	 * Constructor for CAxis.
	 */
	public CAxis(String name, CUnit unit) {
		this.name = name;
		this.unit = unit;
	}

	public String getName() {
		return this.name;
	}

	public String getUnitName() {
		return this.unit.getName();
	}

}