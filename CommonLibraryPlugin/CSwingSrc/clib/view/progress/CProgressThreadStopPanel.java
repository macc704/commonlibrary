/*
 * CProgressThreadStopPanel.java
 * Created on May 6, 2011 
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.progress;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import clib.common.thread.CThread;
import clib.common.thread.ICTask;
import clib.view.actions.CAction;
import clib.view.actions.CActionUtils;

/**
 * @author macchan
 * 
 */
public class CProgressThreadStopPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private CThread thread;

	public CProgressThreadStopPanel(CThread thread) {
		this.thread = thread;
		initialize();
	}

	private void initialize() {
		setLayout(new FlowLayout(FlowLayout.RIGHT));
		add(new JButton(createStopAction()));
	}

	public CAction createStopAction() {
		CAction stopAction = CActionUtils.createAction("Stop", new ICTask() {

			public void doTask() {
				thread.stop();
			}
		});
		return stopAction;
	}
}
