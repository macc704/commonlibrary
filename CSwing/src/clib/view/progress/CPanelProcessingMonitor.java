/*
 * CPanelProcessingMonitor.java
 * Created on Sep 16, 2010 
 * Copyright(c) 2010 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.progress;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import clib.common.thread.CTaskManager;
import clib.common.thread.CThread;
import clib.common.thread.ICTask;
import clib.common.utils.CFrameTester;
import clib.common.utils.ICProgressMonitor;
import clib.view.windowmanager.CWindowCentraizer;

/**
 * @author macchan
 * 
 */
public class CPanelProcessingMonitor extends JPanel implements
		ICProgressMonitor {

	private static final long serialVersionUID = 1L;

	private CProgressTitlePanel processing = new CProgressTitlePanel();
	private CProgressBarPanel progressBar = new CProgressBarPanel();
	private Frame owner;
	private JButton cancelButton = new JButton("cancel");

	private CTaskManager manager;
	private JDialog dialog;

	public CPanelProcessingMonitor() {
		this(null, true);
	}

	public CPanelProcessingMonitor(boolean putCancelButton) {
		this(null, putCancelButton);
	}

	public CPanelProcessingMonitor(Frame owner, boolean putCancelButton) {
		this.owner = owner;
		setLayout(new BorderLayout());
		add(processing, BorderLayout.NORTH);
		add(progressBar, BorderLayout.CENTER);

		if (putCancelButton) {
			JPanel panel = new JPanel();
			panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			panel.add(cancelButton);
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (manager != null) {
						manager.forceStop();
						manager = null;
					}
					if (dialog != null) {
						dialog.dispose();
						dialog = null;
					}
				}
			});
			add(panel, BorderLayout.SOUTH);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see clib.common.utils.ICProgressMonitor#progress(int)
	 */
	public void progress(int x) {
		progressBar.progress(x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see clib.common.utils.ICProgressMonitor#setMax(int)
	 */
	public void setMax(int n) {
		progressBar.setMax(n);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see clib.common.utils.ICProgressMonitor#setWorkTitle(java.lang.String)
	 */
	public void setWorkTitle(String title) {
		processing.setTitle(title);
	}

	public void doTaskWithDialog(ICTask task) {
		doTaskWithDialog("", task);
	}

	public void doTaskWithDialog(String windowTitle, ICTask task) {
		if (manager != null) {
			return;
		}
		manager = new CTaskManager();

		dialog = new JDialog(owner, true);
		dialog.setTitle(windowTitle);
		dialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		dialog.getContentPane().add(this);
		dialog.pack();
		CWindowCentraizer.centerWindow(dialog);

		manager.addTask(task);
		manager.addTask(new ICTask() {
			public void doTask() {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						if (dialog != null) {
							dialog.dispose();
							dialog = null;
						}
					}
				});
			}
		});
		manager.addTask(new ICTask() {
			public void doTask() {
				if (manager != null) {
					manager.stop();
					manager = null;
				}
			}
		});

		manager.start();

		if (manager.isTaskProcessing() && dialog != null) {
			dialog.setVisible(true);
		}
	}

	public static void main(String[] args) {
		CPanelProcessingMonitor panel = new CPanelProcessingMonitor();
		CFrameTester.open(panel).pack();

		panel.setMax(200);
		for (int i = 0; i <= 200; i++) {
			CThread.sleep(10);
			panel.progress(2);
		}
	}
}
