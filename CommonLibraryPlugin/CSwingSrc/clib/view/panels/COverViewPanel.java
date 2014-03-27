/*
 * COverViewPanel.java
 * Created on 2004/05/05
 * 
 * Copyright (c) 2005 PlatBox Project. All rights reserved.
 */
package clib.view.panels;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Class COverViewPanel.
 * 
 * @author macchan
 * @version $Id: COverViewPanel.java,v 1.1 2005/03/08 03:04:46 bam Exp $
 */
public class COverViewPanel extends JPanel implements ChangeListener {

	private static final long serialVersionUID = 1L;

	private static final BufferedImageOp OP = new AffineTransformOp(
			AffineTransform.getTranslateInstance(0, 0),
			AffineTransformOp.TYPE_NEAREST_NEIGHBOR);

	private JViewport viewport;
	private BufferedImage bufImage;

	/*************************
	 * Constructors
	 *************************/

	/**
	 * Constructor for COverViewPanel.
	 */
	public COverViewPanel() {
		initialize();
	}

	/**
	 * Constructor for COverViewPanel.
	 */
	public COverViewPanel(JViewport viewport) {
		initialize();
		setViewport(viewport);
	}

	private void initialize() {
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				moveViewPoint(e);
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				moveViewPoint(e);
			}
		});
	}

	/*************************
	 * Setter for Viewport
	 *************************/

	public void setViewport(JViewport viewport) {
		if (this.viewport != null) {
			unhookListeners();
		}

		this.viewport = viewport;

		if (this.viewport != null) {
			hookListeners();
		}
	}

	private void unhookListeners() {
		viewport.removeChangeListener(this);
	}

	private void hookListeners() {
		viewport.addChangeListener(this);
	}

	/*************************
	 * Operations
	 *************************/

	private void moveViewPoint(MouseEvent e) {

		if (viewport == null) {
			return;
		}

		int x, y;
		Dimension compSize = viewport.getViewSize();
		Dimension viewSize = viewport.getSize();

		// まずは、指された座標とする
		x = e.getX();
		y = e.getY();

		// スケール済みの絶対座標に変換する
		Scale scale = getScale();
		x = (int) ((double) x * scale.reverseX());
		y = (int) ((double) y * scale.reverseY());

		// 中心位置を左上の座標に変換する
		x -= (viewSize.width / 2);
		y -= (viewSize.height / 2);

		// 範囲外に行かないようにする
		x = Math.max(0, x);
		y = Math.max(0, y);
		x = Math.min(x, compSize.width - viewSize.width);
		y = Math.min(y, compSize.height - viewSize.height);

		// 移動する
		viewport.setViewPosition(new Point(x, y));
	}

	public BufferedImage getImage() {
		return bufImage;
	}

	/*************************
	 * Paint Strategy
	 *************************/

	public void update() {
		createBufImage();
		repaint();
	}

	private void createBufImage() {
		if (viewport == null) {
			return;
		}
		if (getWidth() <= 0 || getHeight() <= 0) {
			return;
		}

		// Prepare Buffered Image
		bufImage = new BufferedImage(getWidth(), getHeight(),
				BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2d = bufImage.createGraphics();

		// Sets Scale
		Scale scale = getScale();
		AffineTransform tx = AffineTransform.getScaleInstance(scale.x, scale.y);
		g2d.setTransform(tx);

		// Draw
		JComponent comp = (JComponent) viewport.getView();
		comp.print(g2d); // paint ではなく print

		// Post process
		g2d.dispose();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		createBufImage(); // Temporary

		// PreProcess
		Graphics2D g2d = (Graphics2D) g;

		// Draw Overview
		if (bufImage != null) {
			g2d.drawImage(bufImage, OP, 0, 0);
		}

		// Draw Position Rect
		if (bufImage != null) {
			drawPositionRect(g2d);
		}
	}

	private void drawPositionRect(Graphics2D g2d) {
		// Preprocess
		g2d.setColor(Color.red);
		g2d.setStroke(new BasicStroke(2f));

		// Scaling
		Rectangle viewRect = viewport.getViewRect();
		Scale scale = getScale();
		scaleRect(viewRect, scale);

		// Draw
		g2d.draw(viewRect);
	}

	private void scaleRect(Rectangle viewRect, Scale scale) {
		viewRect.x = (int) ((double) viewRect.x * scale.x);
		viewRect.y = (int) ((double) viewRect.y * scale.y);
		viewRect.width = (int) ((double) viewRect.width * scale.x);
		viewRect.height = (int) ((double) viewRect.height * scale.y);
	}

	private Scale getScale() {
		JComponent comp = (JComponent) viewport.getView();
		int w = getWidth();
		int h = getHeight();
		double sx = (double) w / (double) comp.getWidth();
		double sy = (double) h / (double) comp.getHeight();
		return new Scale(sx, sy);
	}

	/*************************
	 * Event Handlers
	 *************************/

	/**
	 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 */
	public void stateChanged(ChangeEvent e) {
		update();
	}

}

class Scale {

	public double x;
	public double y;

	/**
	 * Constructor for Dimension.
	 */
	Scale(double x, double y) {

		this.x = x;
		this.y = y;
	}

	public double getX() {

		return x;
	}

	public double getY() {

		return y;
	}

	public double reverseX() {

		return 1d / x;
	}

	public double reverseY() {

		return 1d / y;
	}

}