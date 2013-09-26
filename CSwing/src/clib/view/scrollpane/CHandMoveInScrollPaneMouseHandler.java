/*
 * CHandMoveInScrollPaneMouseHandler.java
 * Created on May 6, 2011 
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.scrollpane;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JViewport;

/**
 * @author macchan
 * 
 */
public class CHandMoveInScrollPaneMouseHandler extends MouseAdapter {

	private enum State {
		PRESSED, DRAGGING, RELEASED
	};

	private State state = State.RELEASED;
	private Point pressP;

	private JViewport viewport;

	/********************************************************
	 * Constructors
	 ********************************************************/

	public CHandMoveInScrollPaneMouseHandler(JViewport viewport) {
		this.viewport = viewport;
	}

	/********************************************************
	 * Handlers
	 ********************************************************/

	public void mousePressed(MouseEvent e) {
		if (!e.isShiftDown()) {
			pressP = e.getPoint();
			state = State.PRESSED;
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (state == State.PRESSED || state == State.DRAGGING) {
			e.getComponent().setCursor(
					Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			state = State.RELEASED;
		}
	}

	public void mouseDragged(MouseEvent e) {
		if (state == State.PRESSED) {
			e.getComponent().setCursor(
					Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			state = State.DRAGGING;
		}
		if (state == State.DRAGGING) {
			Point p = e.getPoint();
			Dimension d = new Dimension(p.x - pressP.x, p.y - pressP.y);
			Point viewP = viewport.getViewPosition();
			Point newViewP = new Point(viewP.x - d.width, viewP.y - d.height);
			newViewP = CViewportUtils.arrangeViewPositionRange(newViewP,
					viewport);
			viewport.setViewPosition(newViewP);
		}
	}

}
