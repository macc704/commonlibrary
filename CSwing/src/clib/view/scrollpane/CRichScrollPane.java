/*
 * CRichScrollPane.java
 * Created on Mar 24, 2011 
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.scrollpane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * ドラッグスクロールと選択機能を持つスクロールペイン
 * 
 * @author macchan
 */
public class CRichScrollPane extends JScrollPane {

	private static final long serialVersionUID = 1L;

	private JPanel viewPanel;

	private CHandMoveInScrollPaneMouseHandler handlerA;
	private SelectionMouseHandler handlerB;

	/**
	 * Constructors
	 */
	public CRichScrollPane() {
		setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		viewPanel = new JPanel() {
			private static final long serialVersionUID = 1L;

			protected void paintChildren(Graphics g) {
				super.paintChildren(g);
				Rectangle selectionRect = handlerB.getSelectionRect();
				if (selectionRect != null) {
					Graphics2D g2d = (Graphics2D) g;
					g2d.setColor(Color.RED);
					g2d.draw(selectionRect);
				}
			};
		};
		viewPanel.setLayout(new BorderLayout());
		setViewportView(viewPanel);

		handlerA = new CHandMoveInScrollPaneMouseHandler(getViewport());
		viewPanel.addMouseListener(handlerA);
		viewPanel.addMouseMotionListener(handlerA);

		handlerB = new SelectionMouseHandler();
		viewPanel.addMouseListener(handlerB);
		viewPanel.addMouseMotionListener(handlerB);
	}

	public void setView(JComponent c) {
		viewPanel.add(c);
	}

	public void setSelectionHandler(CSelectionHandler handler) {
		this.handlerB.setSelectionHandler(handler);
	}
}

class SelectionMouseHandler extends MouseAdapter implements MouseMotionListener {

	private boolean pressing = false;
	private Point pressP;
	private Rectangle selectionRect;

	private CSelectionHandler handler;

	/********************************************************
	 * Constructors
	 ********************************************************/

	public SelectionMouseHandler() {
	}

	/********************************************************
	 * Public Interface
	 ********************************************************/

	public void setSelectionHandler(CSelectionHandler handler) {
		this.handler = handler;
	}

	public Rectangle getSelectionRect() {
		return selectionRect;
	}

	/********************************************************
	 * Handler
	 ********************************************************/

	public void mousePressed(MouseEvent e) {
		if (e.isShiftDown()) {
			pressP = e.getPoint();
			pressing = true;
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (selectionRect != null) {
			pressing = false;
			handler.handleSelection(selectionRect);
			selectionRect = null;
			e.getComponent().repaint();
		}
	}

	public void mouseDragged(MouseEvent e) {
		if (pressing && handler != null) {
			Point p = e.getPoint();
			int x = pressP.x < p.x ? pressP.x : p.x;
			int y = pressP.y < p.y ? pressP.y : p.y;
			int w = pressP.x > p.x ? pressP.x - p.x : p.x - pressP.x;
			int h = pressP.y > p.y ? pressP.y - p.y : p.y - pressP.y;
			selectionRect = new Rectangle(x, y, w, h);
			e.getComponent().repaint();
		}
	}

	public void mouseMoved(MouseEvent e) {
	}
}
