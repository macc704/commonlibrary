/*
 * CAnimationImagePanel.java
 * Created on Jul 22, 2010 
 * Copyright(c) 2010 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.image;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import clib.common.thread.CRunnableThread;

/**
 * @author macchan
 */
public class CAnimationImagePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private CAnimationImage animImage;
	private CRunnableThread thread;

	public CAnimationImagePanel(Object sender, String name) {
		animImage = new CAnimationImage(sender, name);
		BufferedImage image = animImage.getImage();
		setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));

		thread = new CRunnableThread() {
			public void handleProcessStep() {
				animImage.coundUp();
				repaint();
			}
		};
		thread.setInterval(100);

		addAncestorListener(new AncestorListener() {

			public void ancestorRemoved(AncestorEvent event) {
				thread.stop();
			}

			public void ancestorMoved(AncestorEvent event) {
			}

			public void ancestorAdded(AncestorEvent event) {
				thread.start();
			}
		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		BufferedImage image = animImage.getImage();
		System.out.println(image.getWidth() + "," + image.getHeight());
		g.drawImage(image, 0, 0, this);
	}
}
