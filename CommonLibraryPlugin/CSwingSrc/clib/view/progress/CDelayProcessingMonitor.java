/*
 * CDelayProcessingMonitor.java
 * Created on May 6, 2011 
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.progress;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import clib.common.thread.CThread;
import clib.common.thread.ICRunnable;
import clib.common.thread.ICTask;
import clib.common.thread.ICThreadStateListener;
import clib.common.utils.ICProgressMonitor;
import clib.view.windowmanager.CWindowCentraizer;

/**
 * @author macchan
 */
public class CDelayProcessingMonitor extends JPanel implements
		ICProgressMonitor {

	private static Timer timer = new Timer(
			"Timer for CDelayProcessingMonitor (in Static)", true);

	private static final long serialVersionUID = 1L;

	private CProgressTitlePanel progressTitle = new CProgressTitlePanel();
	private CProgressBarPanel progressBar = new CProgressBarPanel();
	private JPanel progressThreadStop = new JPanel();

	public CDelayProcessingMonitor() {
		initialize();
	}

	private void initialize() {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		add(progressTitle, BorderLayout.NORTH);
		add(progressBar, BorderLayout.CENTER);
		progressThreadStop.setLayout(new BorderLayout());
		add(progressThreadStop, BorderLayout.SOUTH);
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
		progressTitle.setTitle(title);
	}

	public static void doTaskWithDialog(final ICProgressTask task, Frame owner,
			final long delay, String... names) {

		// create panel and thread
		final CDelayProcessingMonitor panel = new CDelayProcessingMonitor();
		final CThread thread = new CThread(new COneTimeRunnable(new ICTask() {
			public void doTask() {
				task.doTask(panel);
			}
		}));
		if (names.length > 0) {
			thread.setName(names[0]);
		}
		panel.progressThreadStop.add(new CProgressThreadStopPanel(thread));

		// create dialog
		final JDialog dialog = new JDialog(owner, true);
		dialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		dialog.getContentPane().add(panel);
		dialog.pack();
		CWindowCentraizer.centerWindow(dialog);

		// create dialog opening setting
		thread.addStateListener(new ICThreadStateListener() {
			public void threadStarted() {
				timer.schedule(new TimerTask() {
					public void run() {
						if (thread.isRunning()) {
							dialog.setVisible(true);
						}
					}
				}, delay);
			}

			public void threadStopped() {
				dialog.dispose();
			}
		});

		// start
		thread.start();
	}

	public static void main(String[] args) throws Exception {
		CDelayProcessingMonitor.doTaskWithDialog(new TestTask("Task1", 5),
				null, 2000);
		CDelayProcessingMonitor.doTaskWithDialog(new TestTask("Task2", 50),
				null, 2000);
		CDelayProcessingMonitor.doTaskWithDialog(new TestTask("Task3", 1000),
				null, 2000);
	}

	static class TestTask implements ICProgressTask {
		String name = "";
		int n = 0;

		public TestTask(String name, int n) {
			this.name = name;
			this.n = n;
		}

		public void doTask(ICProgressMonitor monitor) {
			System.out.println(name + "start");
			monitor.setMax(n);
			monitor.setWorkTitle(name);
			for (int i = 0; i < n; i++) {
				CThread.sleep(100);
				monitor.progress(1);
			}
			System.out.println(name + "end");
		}
	}
}

class COneTimeRunnable implements ICRunnable {

	private boolean finished = false;
	private ICTask task;

	public COneTimeRunnable(ICTask task) {
		this.task = task;
	}

	public boolean allowStart() {
		return task != null;
	}

	public final void handlePrepareStart() {
	}

	public final void handlePrepareStop() {
	}

	public void handleProcessStep() {
		task.doTask();
		finished = true;
	}

	public boolean isFinished() {
		return finished;
	}
}
