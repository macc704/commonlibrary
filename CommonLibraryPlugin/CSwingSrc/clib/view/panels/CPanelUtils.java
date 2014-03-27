/*
 * CPanelUtils.java
 * Created on Mar 4, 2011 
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.panels;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 * @author macchan
 */
public class CPanelUtils {

	public static JPanel createListPanel() {
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		return panel;
	}

	private CPanelUtils() {
	}
}
