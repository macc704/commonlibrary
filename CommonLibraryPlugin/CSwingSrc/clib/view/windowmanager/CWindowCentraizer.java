/*
 * CWindowCentraizer.java
 * Created on Jul 16, 2010 
 * Copyright(c) 2010 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.windowmanager;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;

/**
 * @author macchan
 * 
 */
public class CWindowCentraizer {
	public static void centerWindow(Window window) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = window.getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}

		window.setLocation((screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);
	}
}
