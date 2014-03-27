/*
 * CVerticalLayoutPanel.java
 * Copyright (c) 2002 Boxed-Economy Project.  All rights reserved.
 */
package clib.view.panels;

import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JPanel;

/**
 * @author macchan
 * @version $Id: CVerticalLayoutPanel.java,v 1.2 2004/07/15 10:27:03 bam Exp $
 */
public class CVerticalLayoutPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private CVerticalFlowLayout layout = new CVerticalFlowLayout();

	/*********************************
	 * Constructors
	 *********************************/

	/**
	 * Constructor for CVerticalLayoutPanel.
	 */
	public CVerticalLayoutPanel() {
		this.setLayout(layout);
	}

	/**
	 * Constructor for CVerticalLayoutPanel.
	 */
	public CVerticalLayoutPanel(int margin) {
		layout.setMargin(margin);
		this.setLayout(layout);
	}

	/*********************************
	 * Special Override Methods
	 *********************************/

	protected void addImpl(Component comp, Object constraints, int index) {
		if (!(comp instanceof JPanel)) {
			JPanel panel = new JPanel();
			panel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
			panel.add(comp);
			comp = panel;
		}
		super.addImpl(comp, constraints, index);
	}

}
