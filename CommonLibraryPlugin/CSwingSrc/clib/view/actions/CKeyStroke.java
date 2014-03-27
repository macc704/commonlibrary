/*
 * CKeyStroke.java
 * Created on Apr 11, 2011 
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.actions;

import java.awt.event.KeyEvent;

import clib.common.system.CJavaSystem;

/**
 * @author macchan
 * 
 */
public class CKeyStroke {

	public static int CTRL_MASK = KeyEvent.CTRL_MASK;

	static {
		if (CJavaSystem.getInstance().isMac()) {
			CTRL_MASK = KeyEvent.META_MASK;
		}
	}
}
