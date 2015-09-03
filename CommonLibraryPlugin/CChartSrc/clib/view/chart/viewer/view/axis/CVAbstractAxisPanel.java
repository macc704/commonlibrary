/*
 * CVAbstractAxisPanel.java
 * Created on 2004/05/05
 * 
 * Copyright (c) 2005 PlatBox Project. All rights reserved.
 */
package clib.view.chart.viewer.view.axis;

import java.awt.Color;
import java.awt.Point;
import java.text.DecimalFormat;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.text.NumberFormatter;

import clib.view.chart.controller.CVChartController;
import clib.view.chart.viewer.model.CVAxis;

/**
 * Class CVAbstractAxisPanel.
 * 
 * @author macchan
 * @version $Id: CVAbstractAxisPanel.java,v 1.1 2005/03/08 03:04:46 bam Exp $
 */
public abstract class CVAbstractAxisPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private static NumberFormatter FORMATTER = new NumberFormatter(
			new DecimalFormat("0.##"));
	public static final int MEASURE_WIDTH = 30;
	public static final int MEASURE_LENGTH = 5;

	private CVAxis axis;
	private CVChartController container;

	/**
	 * Constructor for CVAbstractAxisPanel.
	 */
	public CVAbstractAxisPanel(CVChartController container, CVAxis axis) {
		this.container = container;
		this.axis = axis;
		setBackground(Color.white);
	}

	public CVAxis getModel() {
		return axis;
	}

	public CVChartController getController() {
		return container;
	}

	protected String formatValue(double value) {
		try {
			return FORMATTER.valueToString(new Double(value));
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	protected String createLabelText() {
		String name = getModel().getModel().getName();
		if (name == null || name.length() == 0) {
			name = "No AxisName";
		}

		String unitName = getModel().getModel().getUnitName();
		if (unitName == null || unitName.length() == 0) {
			unitName = "Unknown";
		}

		return name + "(" + unitName + ")";
	}

	public abstract void update();

	public abstract JComponent getMeasureCanvas();

	public abstract void viewPositionChanged(Point position);

}