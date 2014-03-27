/*
 * CFrameList.java
 * Created on Jul 15, 2010 
 * Copyright(c) 2010 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.windowmanager;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

/**
 * @author macchan
 * 
 */
public class CFrameList<T extends JFrame> {

	private List<T> frames = new ArrayList<T>();

	public void add(final T frame) {
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				frames.remove(frame);
			}
		});
		frames.add(frame);
	}

	public List<T> getFrames() {
		return frames;
	}

}
