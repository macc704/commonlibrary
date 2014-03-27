/*
 * CWindowManager.java
 * Created on Jun 27, 2010 
 * Copyright(c) 2010 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.windowmanager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import clib.common.thread.CThread;

/**
 * Class CWindowManager. This class manage windows to layout them.
 * 
 * @author macchan
 */
public class CWindowManager extends WindowAdapter {

	public static JFrame createFrame(JComponent comp) {
		JFrame frame = new JFrame();
		frame.setLocation(100, 100);
		frame.setSize(800, 600);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(comp, BorderLayout.CENTER);
		return frame;
	}

	private JFrame parentFrame;
	private List<JFrame> frames = new ArrayList<JFrame>();
	private int animationFrameCount = 30;

	public CWindowManager() {
	}

	public synchronized JFrame createOpen(JPanel panel) {
		JFrame f = new JFrame();
		f.getContentPane().add(panel);
		f.pack();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		open(f);
		return f;
	}

	public synchronized void open(JFrame f) {
		f.addWindowListener(this);
		this.frames.add(f);
		if (parentFrame != null) {
			f.setSize(this.parentFrame.getSize());
			CWindowCentraizer.centerWindow(f);
		}
		f.setVisible(true);
	}

	public synchronized void closeAll() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				for (JFrame frame : new ArrayList<JFrame>(frames)) {
					frame.setVisible(false);
				}
			}
		});
	}

	public JFrame getParentFrame() {
		return parentFrame;
	}

	public void setToParent(JFrame f) {
		parentFrame = f;
		parentFrame.addWindowListener(new WindowAdapter() {
			public void windowIconified(WindowEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						for (JFrame frame : frames) {
							if (!frame.equals(parentFrame)) {
								frame.setState(JFrame.ICONIFIED);
							}
						}
					}
				});
			}

			public void windowDeiconified(WindowEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						for (JFrame frame : frames) {
							if (!frame.equals(parentFrame)) {
								frame.setState(JFrame.NORMAL);
							}
						}
					}
				});
			}

			public void windowClosed(WindowEvent e) {
				closeAll();
			}
		});
		parentFrame.addWindowFocusListener(normalListener);
	}

	public synchronized void layoutWithoutAnimation() {
		int margin = 50;

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (screen.width - (margin * 2)) / 2;
		int height = (screen.height - (margin * 2)) / 2;

		try {
			frames.get(0).setBounds(margin, margin, width, height);
			frames.get(1).setBounds(margin + width, margin, width, height);
			frames.get(2).setBounds(margin, margin + height, width, height);
			frames.get(3).setBounds(margin + width, margin + height, width,
					height);
		} catch (Exception ex) {

		}
	}

	public synchronized void layoutWithAnimation() {
		int margin = 50;

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (screen.width - (margin * 2)) / 2;
		int height = (screen.height - (margin * 2)) / 2;

		try {
			layoutOneFrameWithAnimation(frames.get(0), new CRectangle(margin,
					margin, width, height));
			layoutOneFrameWithAnimation(frames.get(1), new CRectangle(margin
					+ width, margin, width, height));
			layoutOneFrameWithAnimation(frames.get(2), new CRectangle(margin,
					margin + height, width, height));
			layoutOneFrameWithAnimation(frames.get(3), new CRectangle(margin
					+ width, margin + height, width, height));
		} catch (Exception ex) {

		}
	}

	private CThread layoutOneFrameWithAnimation(Frame f, CRectangle bounds) {
		CAccelaratedTransition trans = new CAccelaratedTransition(f, bounds,
				animationFrameCount);
		CThread thread = new CThread(trans);
		thread.setInterval(600 / animationFrameCount);
		thread.start();
		thread.waitForStop();
		return thread;
	}

	/*****************************************************
	 * Window State Management Listening System
	 *****************************************************/

	public synchronized void windowClosed(WindowEvent e) {
		e.getWindow().removeWindowListener(this);
		this.frames.remove(e.getWindow());
	}

	// ちょっとトリッキーなコード（ピンポン現象が起こるため）

	WindowFocusListener normalListener = new WindowFocusListener() {
		public void windowLostFocus(WindowEvent e) {
		}

		public void windowGainedFocus(WindowEvent e) {
			parentFrame.removeWindowFocusListener(normalListener);
			parentFrame.addWindowFocusListener(frontingListener);
			for (JFrame frame : frames) {
				if (!frame.equals(parentFrame)) {
					frame.toFront();
				}
			}
			parentFrame.toFront();
		}
	};

	WindowFocusListener frontingListener = new WindowFocusListener() {
		public void windowLostFocus(WindowEvent e) {
		}

		public void windowGainedFocus(WindowEvent e) {
			parentFrame.removeWindowFocusListener(frontingListener);
			parentFrame.addWindowFocusListener(normalListener);
		}
	};

	/*
	 * Test
	 */
	public static void main(String[] args) {

		CWindowManager wm = new CWindowManager();

		for (int i = 0; i < 3; i++) {
			JPanel p = new JPanel(true);
			p.setBackground(Color.WHITE);
			p.setPreferredSize(new Dimension(300, 400));
			wm.createOpen(p);
		}

		try {
			Thread.sleep(1000);
		} catch (Exception ex) {

		}

		wm.layoutWithAnimation();
	}
}
