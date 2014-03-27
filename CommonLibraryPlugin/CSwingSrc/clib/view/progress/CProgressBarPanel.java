/*
 * CProgressBarPanel.java
 * Created on Jul 22, 2010 
 * Copyright(c) 2010 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.progress;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BoundedRangeModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import clib.common.thread.CThread;
import clib.common.utils.CFrameTester;
import clib.common.utils.ICProgressMonitor;

/**
 * 
 * @author macchan
 */
public class CProgressBarPanel extends JPanel implements ICProgressMonitor {

	private static final long serialVersionUID = 1L;

	private JLabel progressLabel = new JLabel("()");
	private JProgressBar progressBar = new JProgressBar();

	private int max;
	private int current;

	public CProgressBarPanel() {
		setLayout(new BorderLayout());
		BoundedRangeModel model = progressBar.getModel();
		model.setMinimum(0);
		model.setMaximum(100);
		add(progressBar, BorderLayout.CENTER);
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		panel.add(progressLabel);
		add(panel, BorderLayout.SOUTH);
	}

	public void setWorkTitle(String title) {
	}

	public void setMax(int max) {
		this.max = max;
		current = 0;
		update();
	}

	public void progress(int n) {
		current += n;
		update();
	}

	public void update() {
		double ratio = ((double) current / (double) max) * 100d;
		BoundedRangeModel model = progressBar.getModel();
		model.setValue((int) ratio);
		progressLabel.setText("(" + current + "/" + max + ")");
	}

	public static void main(String[] args) {
		CProgressBarPanel panel = new CProgressBarPanel();
		CFrameTester.open(panel).pack();

		panel.setMax(200);
		for (int i = 0; i <= 200; i++) {
			CThread.sleep(100);
			panel.progress(2);
		}
	}
}
