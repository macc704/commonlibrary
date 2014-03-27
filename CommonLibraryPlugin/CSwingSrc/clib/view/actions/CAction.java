/*
 * CAction.java
 * Created on May 5, 2011 
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.actions;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

/**
 * @author macchan
 */
public abstract class CAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	public void setAcceralator(KeyStroke stroke) {
		putValue(Action.ACCELERATOR_KEY, stroke);
	}

	public void setName(String name) {
		putValue(Action.NAME, name);
	}
}
