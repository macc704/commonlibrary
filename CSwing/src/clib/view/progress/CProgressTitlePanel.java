/*
 * CProgressTitlePanel.java
 * Created on Jul 31, 2010 
 * Copyright(c) 2010 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.progress;

import java.awt.BorderLayout;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author macchan
 * 
 */
public class CProgressTitlePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JLabel label = new JLabel("Now Processing...");

	public CProgressTitlePanel() {
		setLayout(new BorderLayout());
		URL url = getClass().getResource("loading.gif");
		ImageIcon icon = new ImageIcon(url);
		label.setIcon(icon);
		add(label);
	}

	public void setTitle(String label) {
		this.label.setText(label);
	}

	public static void main(String[] args) {
	}
}
