/*
 * CVVerticalLabel.java
 * Created on 2004/05/07
 * 
 * Copyright (c) 2005 PlatBox Project. All rights reserved.
 */
package clib.view.chart.viewer.view.axis;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

/**
 * Class CVVerticalLabel.
 * 
 * @author macchan
 * @version $Id: CVVerticalLabel.java,v 1.1 2005/03/08 03:04:46 bam Exp $
 */
public class CVVerticalLabel extends JComponent {

	private static final long serialVersionUID = 1L;

	private static final BufferedImageOp OP = new AffineTransformOp(
			AffineTransform.getRotateInstance(Math.toRadians(90)),
			AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
	private String text = "";
	private BufferedImage buf;

	/**
	 * Constructor for CVVerticalLabel.
	 */
	public CVVerticalLabel() {
		super();
	}

	public void setText(String text) {
		if (text == null) {
			return;
		}
		this.text = text;
		refreshImage();
	}

	private void refreshImage() {
		if (getFont() == null) {
			return;
		}
		int width = SwingUtilities.computeStringWidth(
				getFontMetrics(getFont()), text);
		width = Math.max(1, width);
		int height = getWidth();
		height = Math.max(1, height);

		buf = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2d = buf.createGraphics();
		g2d.setColor(getForeground());
		g2d.setFont(getFont());
		g2d.drawString(text, 0, height * 2 / 3);
		g2d.dispose();
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (buf == null) {
			refreshImage();
		}

		if (buf != null) {
			if (g instanceof Graphics2D) {
				((Graphics2D) g).drawImage(buf, OP, getWidth(), getHeight() / 2
						- buf.getWidth() / 2);
			}
		}
	}

}