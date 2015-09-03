/*
 * CTimeIndicationPainter.java
 * Created on Apr 4, 2011 
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.timeline.pane;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import clib.view.timeline.model.CTimeModel;
import clib.view.timeline.model.CTimeTransformationModel;

/**
 * @author macchan
 */
public class CTimeIndicatorPainter {

	private Color color = Color.BLACK;

	private CTimeModel timeModel = new CTimeModel();
	private CTimeTransformationModel transModel = new CTimeTransformationModel();

	public CTimeIndicatorPainter() {
	}

	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * @return the transModel
	 */
	public CTimeTransformationModel getTransModel() {
		return transModel;
	}

	public void setTransModel(CTimeTransformationModel model) {
		this.transModel = model;
	}

	/**
	 * @return the timeModel
	 */
	public CTimeModel getTimeModel() {
		return timeModel;
	}

	public void setTimeModel(CTimeModel timeModel) {
		this.timeModel = timeModel;
	}

	public void draw(Graphics2D g2d, int height, int yStart, int yEnd) {
		if (transModel == null) {
			return;
		}

		if (timeModel == null) {
			return;
		}

		int timeX = (int) transModel.time2X(timeModel.getTime());
		String startString = timeModel.toString();

		g2d.setStroke(new BasicStroke(3));
		g2d.setColor(this.color);
		g2d.drawLine(timeX, 0, timeX, height);
		g2d.drawString(startString, timeX, yStart);
	}

}
