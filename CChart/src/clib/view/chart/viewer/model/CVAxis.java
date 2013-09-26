/*
 * CVAxis.java
 * Created on 2004/05/05
 * 
 * Copyright (c) 2005 PlatBox Project. All rights reserved.
 */
package clib.view.chart.viewer.model;

import clib.view.chart.model.CAxis;

/**
 * Class CVAxis.
 * 
 * @author macchan
 * @version $Id: CVAxis.java,v 1.1 2005/03/08 03:04:46 bam Exp $
 */
public class CVAxis {

	private CVScale scale = new CVScale();
	private CVMeasure measure = new CVMeasure(this);
	private CAxis model;

	public CVAxis() {
	}

	public CVMeasure getMeasure() {
		return measure;
	}

	public CVScale getScale() {
		return scale;
	}

	public CAxis getModel() {
		return model;
	}

	public void setModel(CAxis axis) {
		model = axis;
	}

}