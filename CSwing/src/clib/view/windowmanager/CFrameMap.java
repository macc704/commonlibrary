/*
 * CFrameMap.java
 * Created on Jul 15, 2010 
 * Copyright(c) 2010 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.windowmanager;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

/**
 * @author macchan
 */
public class CFrameMap<K extends JFrame, V> {

	private Map<K, V> frames = new LinkedHashMap<K, V>();

	public void put(final K frame, V value) {
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				frames.remove(frame);
			}
		});
		frames.put(frame, value);
	}

	public V get(K frame) {
		return frames.get(frame);
	}

	public List<K> getFrames() {
		return new ArrayList<K>(frames.keySet());
	}

	public List<V> getValues() {
		return new ArrayList<V>(frames.values());
	}

}
