/*
 * CVXAxisContainerPanel.java
 * Created on 2004/05/05
 * 
 * Copyright (c) 2005 PlatBox Project. All rights reserved.
 */
package clib.view.chart.viewer.view.axis;

import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.util.List;

import clib.view.chart.viewer.model.CVAxis;

/**
 * Class CVXAxisContainerPanel.
 * 
 * @author macchan
 * @version $Id: CVXAxisContainerPanel.java,v 1.1 2005/03/08 03:04:46 bam Exp $
 */
public class CVXAxisContainerPanel extends CVAbstractAxisContainerPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for CVXAxisContainerPanel.
	 */
	public CVXAxisContainerPanel() {
		super();
	}

	/**
	 * @see clib.view.chart.viewer.resource.CVAbstractAxisContainerPanel.viewer.AbstractAxisContainerPanel#createLayout()
	 */
	public LayoutManager createLayout() {
		return new GridLayout(0, 1);
	}

	/**
	 * @see clib.view.chart.viewer.resource.CVAbstractAxisContainerPanel.viewer.AbstractAxisContainerPanel#createPanel(clib.view.chart.viewer.resource.newgraph.model.GDataSet)
	 */
	public CVAbstractAxisPanel createPanel(CVAxis axis) {
		return new CVXAxisPanel(getController(), axis);
	}

	/**
	 * @see clib.view.chart.viewer.view.axis.CVAbstractAxisContainerPanel#getAxisList()
	 */
	public List<CVAxis> getAxisList() {
		return getController().getChart().getAxisListX();
	}

}