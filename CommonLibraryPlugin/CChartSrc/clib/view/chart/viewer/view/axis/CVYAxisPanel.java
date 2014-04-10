/*
 * CVYAxisPanel.java
 * Created on 2004/05/05
 * 
 * Copyright (c) 2005 PlatBox Project. All rights reserved.
 */
package clib.view.chart.viewer.view.axis;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.geom.Point2D;

import javax.swing.JComponent;
import javax.swing.JViewport;

import clib.view.chart.controller.CVChartController;
import clib.view.chart.viewer.model.CVAxis;
import clib.view.panels.CVerticalFlowLayout;

/**
 * Class CVYAxisPanel.
 * 
 * @author macchan
 * @version $Id: CVYAxisPanel.java,v 1.1 2005/03/08 03:04:46 bam Exp $
 */
public class CVYAxisPanel extends CVAbstractAxisPanel {

	private static final long serialVersionUID = 1L;

	private JViewport viewport = new JViewport();
	private YMeasureCanvas measureCanvas = new YMeasureCanvas();
	private YAxisDescriptionPanel descriptionPanel;

	/**
	 * Constructor for CVYAxisPanel.
	 */
	public CVYAxisPanel(CVChartController container, CVAxis axis) {
		super(container, axis);
		initialize();
	}

	private void initialize() {
		setLayout(new BorderLayout());
		viewport.setView(measureCanvas);
		viewport.setBackground(Color.white);
		descriptionPanel = new YAxisDescriptionPanel(getController(), getModel());
		add(descriptionPanel, BorderLayout.WEST);
		add(viewport, BorderLayout.CENTER);
	}

	/**
	 * @see clib.view.chart.viewer.resource.CVAbstractAxisPanel.viewer.AbstractAxisPanel#refreshViews()
	 */
	public void update() {
		measureCanvas.setPreferredSize(new Dimension(MEASURE_WIDTH,
				getController().getCanvasSize().height));
		measureCanvas.revalidate();
		measureCanvas.repaint();
	}

	/**
	 * @see clib.view.chart.viewer.resource.CVAbstractAxisPanel.viewer.AbstractAxisPanel#viewPositionChanged(java.awt.Point)
	 */
	public void viewPositionChanged(Point position) {
		Point p = viewport.getViewPosition();
		p.y = position.y;
		viewport.setViewPosition(p);
	}

	/**
	 * @see clib.view.chart.viewer.view.axis.CVAbstractAxisPanel#getMeasureCanvas()
	 */
	public JComponent getMeasureCanvas() {
		return this.measureCanvas;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

	/**
	 * 目盛りを書くCanvas
	 */
	class YMeasureCanvas extends JComponent {
		private static final long serialVersionUID = 1L;

		public void paintComponent(Graphics g) {
			Point2D origin = getController().getOrigin();
			int top = 0;
			int bottom = getHeight();

			double step = getModel().getMeasure().getStep();
			double scale = getModel().getScale().getValue();
			double measure = step * scale;
			if (measure < 1) {
				return;
			}

			// 正の数を書く
			for (int i = 0; -(measure * i) + origin.getY() > top; i++) {
				drawMeasure(g, origin, step, scale, measure, i);
			}
			// 負の数を書く
			for (int i = 0; -(measure * i) + origin.getY() < bottom; i--) {
				drawMeasure(g, origin, step, scale, measure, i);
			}
		}

		private void drawMeasure(Graphics g, Point2D origin, double step,
				double scale, double measure, int i) {

			int y = -(int) (measure * i) + (int) origin.getY();
			y = y + 1; // ScrollPaneのBorderの分を足す

			// 目盛りを書く
			int len = MEASURE_LENGTH;
			if ((i % 5) == 0) { // 5回に1回長く書く
				len *= 2;
			}
			g.drawLine(getWidth() - len, y, getWidth(), y);

			// 数値を書く
			if ((i % 10) == 0) { // 10回に1回
				double value = step * i;
				g.drawString(formatValue(value), 0, y);
			}
		}
	}

	class YAxisDescriptionPanel extends CVAbstractAxisDescriptionPanel {
		private static final long serialVersionUID = 1L;

		public YAxisDescriptionPanel(CVChartController container, CVAxis axis) {
			super(container, axis);
			initialize();
		}

		private void initialize() {
			setLayout(new BorderLayout());
			CVVerticalLabel label = new CVVerticalLabel();
			label.setText(createLabelText());
			add(label, BorderLayout.CENTER);
			add(createButtonsPanel(), BorderLayout.SOUTH);
		}

		protected LayoutManager createButtonPanelLayout(JComponent comp) {
			return new CVerticalFlowLayout(0);
		}

	}

}