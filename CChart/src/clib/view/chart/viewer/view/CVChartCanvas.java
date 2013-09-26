/*
 * CVChartCanvas.java
 * Created on 2004/05/05
 * 
 * Copyright (c) 2005 PlatBox Project. All rights reserved.
 */
package clib.view.chart.viewer.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import clib.view.chart.controller.CVChartController;
import clib.view.chart.model.CPointList;
import clib.view.chart.viewer.model.CVAxis;
import clib.view.chart.viewer.model.CVDataSet;

/**
 * Class CVChartCanvas.
 * 
 * @author macchan
 * @version $Id: CVChartCanvas.java,v 1.1 2005/03/08 03:04:44 bam Exp $
 */
public class CVChartCanvas extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final int WIDTH_GRID = 1;
	private static final int WIDTH_GRID_IMPORTANT = 2;

	private static final int WIDTH_LINE = 2;
	private static final int WIDTH_LINE_SELECTED = 4;

	private static final int DETECTION_THRESHOLD = 5;

	/**********************
	 * Variables
	 **********************/

	private CVChartController controller;

	private Map<Line2D, CVDataSet> lines = new LinkedHashMap<Line2D, CVDataSet>();

	/**********************
	 * Constructor
	 **********************/

	/**
	 * Constructor for CVChartCanvas.
	 */
	public CVChartCanvas() {
		setBackground(Color.white);
	}

	/**********************
	 * Accessors
	 **********************/

	public void setContainer(CVChartController container) {
		this.controller = container;
		initialize();
	}

	private void initialize() {
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.getClickCount() != 1) {
					return;
				}

				CVDataSet data = search(e.getPoint());

				if (data == null) {
					controller.clearDataSelection();
					return;
				}

				List<CVDataSet> datum = new ArrayList<CVDataSet>();
				datum.add(data);
				if (!e.isShiftDown()) {
					controller.setDataSelection(datum);
				} else {// isShiftDown()
					controller.addDataSelection(datum);
				}
			}
		});
	}

	private CVDataSet search(Point2D p) {
		// Map<Double, Line2D> candidates = new TreeMap<Double, Line2D>();
		Line2D candidate = null;
		double min = Double.MAX_VALUE;
		for (Line2D line : lines.keySet()) {
			double distance = line.ptSegDist(p);
			if (distance < DETECTION_THRESHOLD) {
				if (distance < min) {
					candidate = line;
					min = distance;
				}
				// candidates.put(distance, line);
			}
		}
		return lines.get(candidate);
	}

	/**********************
	 * Paint Strategy
	 **********************/

	public void paintComponent(Graphics g) {
		synchronized (getTreeLock()) {
			super.paintComponent(g);

			Graphics2D g2d = (Graphics2D) g;
			drawGrids(g2d);
			lines.clear();
			drawDataSets(g2d);

			if (controller.isNavigationEnabled()) {
				drawNavigation(g2d);
			}
		}
	}

	/**********************
	 * Navigation線関係
	 **********************/

	private void drawNavigation(Graphics2D g) {
		CVAxis xAxis = getXAxis();
		if (xAxis != null) {
			Point2D origin = controller.getOrigin();
			double sX = xAxis.getScale().getValue();
			double value = controller.getNavigator().getValue();
			double x = (sX * value) + origin.getX();

			Color c = g.getColor();
			g.setColor(Color.RED);
			setPenWidth(g, 4);
			g.drawLine((int) x, 0, (int) x, getHeight());
			setPenWidth(g, 1);
			g.setColor(c);
		}
	}

	/**********************
	 * データ関係
	 **********************/

	private void drawDataSets(Graphics2D g) {
		List<CVDataSet> dataSets = controller.getChart().getDataSets();

		// reorder for that the selected line can override the others.
		List<CVDataSet> unselected = new ArrayList<CVDataSet>();
		List<CVDataSet> selected = new ArrayList<CVDataSet>();
		for (CVDataSet dataset : dataSets) {
			if (dataset.isSelected()) {
				selected.add(dataset);
			} else {
				unselected.add(dataset);
			}
		}
		for (CVDataSet dataset : unselected) {
			drawOneDataSet(g, dataset);
		}
		for (CVDataSet dataset : selected) {
			drawOneDataSet(g, dataset);
		}
	}

	private void drawOneDataSet(Graphics2D g, CVDataSet data) {
		Color oldColor = g.getColor();
		Stroke oldStroke = g.getStroke();
		g.setColor(data.getColor());
		if (data.isSelected()) {
			setPenWidth(g, WIDTH_LINE_SELECTED);
		} else {
			setPenWidth(g, WIDTH_LINE);
		}

		CPointList points = data.getScaledPoints();
		Point2D previous = null;
		Point2D current = null;
		for (Iterator<Point2D> i = points.iterator(); i.hasNext(); previous = current) {
			current = i.next();
			if (previous != null) {
				Line2D line = drawDataLine(g, previous, current);
				lines.put(line, data);
			}
		}

		if (data.isSelected() && current != null) {
			g.setColor(Color.BLACK);
			String label = data.getModel().getLabel();
			Point2D p = translateForCanvas(current);
			int w = SwingUtilities
					.computeStringWidth(g.getFontMetrics(), label);
			g.drawString(label, (int) p.getX() - w, (int) p.getY());
		}

		g.setColor(oldColor);
		g.setStroke(oldStroke);
	}

	private Line2D drawDataLine(Graphics2D g, Point2D p1, Point2D p2) {
		p1 = translateForCanvas(p1);
		p2 = translateForCanvas(p2);
		Line2D line = new Line2D.Double(p1, p2);
		g.draw(line);
		return line;
	}

	private Point2D translateForCanvas(Point2D p) {
		Point2D origin = controller.getOrigin();
		Point2D translated = new Point2D.Double(p.getX(), p.getY());
		translated.setLocation(translated.getX() + origin.getX(), origin.getY()
				- translated.getY());
		return translated;
	}

	/*********************************
	 * 罫線関係
	 *********************************/

	private void drawGrids(Graphics g) {
		Color oldColor = g.getColor();
		g.setColor(Color.GRAY);

		CVAxis xAxis = getXAxis();
		if (xAxis != null && controller.getChart().isShowGridX()) {
			drawGridX(g, xAxis);
		}

		CVAxis yAxis = getYAxis();
		if (yAxis != null && controller.getChart().isShowGridY()) {
			drawGridY(g, yAxis);
		}

		g.setColor(oldColor);
	}

	private CVAxis getXAxis() {
		List<CVAxis> axisListX = controller.getChart().getAxisListX();
		if (!axisListX.isEmpty()) {
			return axisListX.get(0);
		}
		return null;
	}

	private CVAxis getYAxis() {
		List<CVAxis> axisListY = controller.getChart().getAxisListY();
		if (!axisListY.isEmpty()) {
			return axisListY.get(0);
		}
		return null;
	}

	public void drawGridX(Graphics g, CVAxis axis) {
		Point2D origin = controller.getOrigin();
		int rightSide = getWidth();
		int leftSide = 0;

		double step = axis.getMeasure().getStep();
		double scale = axis.getScale().getValue();
		double measure = step * scale;
		if (measure < 1) {
			return;
		}

		// 正の数を書く
		for (int i = 0; measure * i + origin.getX() < rightSide; i++) {
			drawGridX(g, origin, step, scale, measure, i);
		}
		// 負の数を書く
		for (int i = 0; measure * i + origin.getX() > leftSide; i--) {
			drawGridX(g, origin, step, scale, measure, i);
		}
	}

	private void drawGridX(Graphics g, Point2D origin, double step,
			double scale, double measure, int i) {
		int x = (int) (measure * i) + (int) origin.getX();

		if (i % 5 == 0) {
			if (i == 0) {
				setPenWidth(g, WIDTH_GRID_IMPORTANT);
				g.drawLine(x, 0, x, getHeight());
				setPenWidth(g, WIDTH_GRID);
			} else {
				g.drawLine(x, 0, x, getHeight());
			}
		} else {
			// setPenWidth(g, 1);
		}

	}

	public void drawGridY(Graphics g, CVAxis axis) {
		Point2D origin = controller.getOrigin();
		int top = 0;
		int bottom = getHeight();

		double step = axis.getMeasure().getStep();
		double scale = axis.getScale().getValue();
		double measure = step * scale;
		if (measure < 1) {
			return;
		}

		// 正の数を書く
		for (int i = 0; -(measure * i) + origin.getY() > top; i++) {
			drawGridY(g, origin, step, scale, measure, i);
		}
		// 負の数を書く
		for (int i = 0; -(measure * i) + origin.getY() < bottom; i--) {
			drawGridY(g, origin, step, scale, measure, i);
		}
	}

	private void drawGridY(Graphics g, Point2D origin, double step,
			double scale, double measure, int i) {

		int y = -(int) (measure * i) + (int) origin.getY();

		if ((i % 5) == 0) {
			if (i == 0) {
				setPenWidth(g, 2);
				g.drawLine(0, y, getWidth(), y);
				setPenWidth(g, 1);
			}
			g.drawLine(0, y, getWidth(), y);
		}
	}

	private void setPenWidth(Graphics g, int width) {
		if (g instanceof Graphics2D) {
			((Graphics2D) g).setStroke(new BasicStroke(width));
		}
	}

}