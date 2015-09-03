/*
 * CTimeIndicationPanel.java
 * Created on Mar 7, 2011 
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.timeline.pane;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

/**
 * @author macchan
 */
public class CTimeIndicatorShowablePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private CTimeIndicatorPainterManager manager = new CTimeIndicatorPainterManager();

	public CTimeIndicatorShowablePanel(CTimeIndicatorPainterManager manager) {
		this.manager = manager;
	}

	protected void paintChildren(Graphics g) {
		super.paintChildren(g);

		for (CTimeIndicatorPainter painter : manager.getIndicators()) {
			painter.draw((Graphics2D) g, getHeight(), -100, -100);
		}
	}
}
