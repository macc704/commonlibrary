/*
 * PhaseTimeLinePane.java
 * Created on Mar 24, 2011 
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.timeline.testapp;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import clib.view.timeline.pane.CAbstractTimeLinePane;

/**
 * @author macchan
 * 
 */
public class StudentTimeLinePane extends CAbstractTimeLinePane<Student> {

	private static final long serialVersionUID = 1L;

	public StudentTimeLinePane() {
	}

	public JComponent createLeftPanel(Student model) {
		JComponent panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new BorderLayout());
		JLabel label = new JLabel(model.getName());
		panel.add(label);
		return panel;
	}

	public JComponent createRightPanel(Student model) {
		StudentTimelinePanel mpanel = new StudentTimelinePanel(
				getTimelinePane().getTimeTransModel(), model);
		mpanel.setOpaque(false);
		return mpanel;
	}

	// /*
	// * (non-Javadoc)
	// *
	// * @see clib.view.timeline.pane.CAbstractTimeLinePane#getComponentHeight()
	// */
	// @Override
	// public int getComponentHeight() {
	// return 25;
	// }

}
