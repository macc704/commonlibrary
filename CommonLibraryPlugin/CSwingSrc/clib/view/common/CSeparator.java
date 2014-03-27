/*
 * CSeparator.java
 * Created on May 5, 2011 
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.common;

import java.awt.Dimension;

import javax.swing.JPopupMenu;

/**
 * @author macchan
 * 
 */
public class CSeparator extends JPopupMenu.Separator {

	private static final long serialVersionUID = 1L;

	public Dimension getMaximumSize() {
		return new Dimension(Integer.MAX_VALUE, 15);
	}
}
