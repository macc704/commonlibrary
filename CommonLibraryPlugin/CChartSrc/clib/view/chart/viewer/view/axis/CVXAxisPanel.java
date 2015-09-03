/*
 * CVXAxisPanel.java
 * Created on 2004/05/05
 * 
 * Copyright (c) 2005 PlatBox Project. All rights reserved.
 */
package clib.view.chart.viewer.view.axis;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JViewport;
import javax.swing.SwingConstants;

import clib.view.chart.controller.CVChartController;
import clib.view.chart.viewer.model.CVAxis;

/**
 * Class CVXAxisPanel.
 * 
 * @author macchan
 * @version $Id: CVXAxisPanel.java,v 1.1 2005/03/08 03:04:46 bam Exp $
 */
public class CVXAxisPanel extends CVAbstractAxisPanel {

	private static final long serialVersionUID = 1L;

	private JViewport viewport = new JViewport();
	private XMeasureCanvas measureCanvas = new XMeasureCanvas();
	private XAxisDescriptionPanel descriptionPanel;

	/**
	 * Constructor for CVXAxisPanel.
	 */
	public CVXAxisPanel(CVChartController container, CVAxis axis) {
		super(container, axis);
		initialize();
	}

	private void initialize() {
		// setLayout(new GridLayout(2, 0));
		setLayout(new BorderLayout());
		viewport.setView(measureCanvas);
		viewport.setBackground(Color.white);
		descriptionPanel = new XAxisDescriptionPanel(getController(),
				getModel());
		add(viewport, BorderLayout.CENTER);
		add(descriptionPanel, BorderLayout.SOUTH);
	}

	/**
	 * @see clib.view.chart.viewer.resource.CVAbstractAxisPanel.viewer.AbstractAxisPanel#refreshViews()
	 */
	public void update() {
		measureCanvas.setPreferredSize(new Dimension(getController()
				.getCanvasSize().width, MEASURE_WIDTH));
		measureCanvas.revalidate();
		measureCanvas.repaint();
	}

	/**
	 * @see clib.view.chart.viewer.resource.CVAbstractAxisPanel.viewer.AbstractAxisPanel#viewPositionChanged(java.awt.Point)
	 */
	public void viewPositionChanged(Point position) {
		Point p = viewport.getViewPosition();
		p.x = position.x;
		viewport.setViewPosition(p);
	}

	/**
	 * @see clib.view.chart.viewer.view.axis.CVAbstractAxisPanel#getMeasureCanvas()
	 */
	public JComponent getMeasureCanvas() {
		return this.measureCanvas;
	}

	/**
	 * 目盛りを書くCanvas
	 */
	class XMeasureCanvas extends JComponent {

		private static final long serialVersionUID = 1L;

		public XMeasureCanvas() {
			addMouseListener(mouseHandler);
			addMouseMotionListener(mouseHandler);

		}

		MouseAdapter mouseHandler = new MouseAdapter() {
			// boolean dragged = false;

			public void mousePressed(MouseEvent e) {
				// dragged = false;
				handle(e);
			}

			public void mouseDragged(MouseEvent e) {
				// dragged = true;
				handle(e);
			}

			public void mouseReleased(MouseEvent e) {
				// if (dragged) {
				getController().getNavigator().fireValueSetFinished();
				// dragged = false;
				// }
			}

			private void handle(MouseEvent e) {
				if (getController().isNavigationEnabled()) {
					getController().getNavigator()
							.setValue(getModelX(e.getX()));
					getController().refreshViews();
				}
			}
		};

		private double getModelX(int viewX) {
			Point2D origin = getController().getOrigin();
			double scale = getModel().getScale().getValue();
			double x = (viewX - origin.getX()) / scale;
			return x;
		}

		public void paintComponent(Graphics g) {
			Point2D origin = getController().getOrigin();
			int rightSide = getWidth();
			int leftSide = 0;

			double step = getModel().getMeasure().getStep();
			double scale = getModel().getScale().getValue();
			double measure = step * scale;
			if (measure < 1) {
				return;
			}

			// 正の数を書く
			for (int i = 0; measure * i + origin.getX() < rightSide; i++) {
				drawMeasure(g, origin, step, scale, measure, i);
			}
			// 負の数を書く
			for (int i = 0; measure * i + origin.getX() > leftSide; i--) {
				drawMeasure(g, origin, step, scale, measure, i);
			}
		}

		private void drawMeasure(Graphics g, Point2D origin, double step,
				double scale, double measure, int i) {

			int x = (int) (measure * i) + (int) origin.getX();

			// 目盛りを書く
			int len = MEASURE_LENGTH;
			if ((i % 5) == 0) { // 5回に1回長く書く
				len *= 2;
			}
			g.drawLine(x, 0, x, len);

			// 数値を書く
			if ((i % 10) == 0) { // 10回に1回
				double value = step * i;
				g.drawString(formatValue(value), x, len * 2);
			}
		}
	}

	class XAxisDescriptionPanel extends CVAbstractAxisDescriptionPanel {

		private static final long serialVersionUID = 1L;

		public XAxisDescriptionPanel(CVChartController container, CVAxis axis) {
			super(container, axis);
			initialize();
		}

		private void initialize() {
			setLayout(new BorderLayout());
			JLabel label = new JLabel();
			label.setText(createLabelText());
			label.setHorizontalAlignment(SwingConstants.CENTER);
			label.setHorizontalTextPosition(SwingConstants.CENTER);
			add(label, BorderLayout.CENTER);
			add(createButtonsPanel(), BorderLayout.WEST);
		}

		protected LayoutManager createButtonPanelLayout(JComponent comp) {
			comp.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			return new FlowLayout(FlowLayout.LEADING, 0, 0);
		}

	}

}