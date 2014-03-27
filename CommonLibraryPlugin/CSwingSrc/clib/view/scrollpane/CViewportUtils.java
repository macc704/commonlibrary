/*
 * CViewportUtils.java
 * Created on Mar 25, 2011 
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.scrollpane;

import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JViewport;

/**
 * @author macchan
 * 
 */
public class CViewportUtils {

	public static void moveViewPosition(Dimension delta, JViewport vp) {
		Point p = vp.getViewPosition();
		p.translate(delta.width, delta.height);
		setViewPosition(p, vp);
	}

	public static void setViewPosition(Point p, JViewport vp) {
		Point arranged = arrangeViewPositionRange(p, vp);
		vp.setViewPosition(arranged);
	}

	public static Point arrangeViewPositionRange(Point p, JViewport vp) {
		return arrangeViewPositionRange(p, vp.getViewSize(), vp.getExtentSize());
	}

	private static Point arrangeViewPositionRange(Point p,
			Dimension componentSize, Dimension viewSize) {
		int x = p.x;
		int y = p.y;
		x = Math.max(0, x);
		y = Math.max(0, y);
		x = Math.min(x, componentSize.width - viewSize.width);
		y = Math.min(y, componentSize.height - viewSize.height);
		return new Point(x, y);
	}
}
