/*
 * CSwingUtils.java
 * Created on 2012/05/15
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.common;

import java.awt.Container;

import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 * @author macchan
 * 
 */
public class CSwingUtils {
	public static JFrame getFrame(JComponent comp) {
		Container cont = comp.getTopLevelAncestor();
		if (cont instanceof JFrame) {
			return (JFrame) cont;
		}
		return null;
	}
}
