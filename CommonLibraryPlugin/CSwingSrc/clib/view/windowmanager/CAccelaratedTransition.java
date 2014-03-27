/*
 * CAccelaratedTransition.java
 * Created on Jun 27, 2010 
 * Copyright(c) 2010 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.windowmanager;

import java.awt.Component;

/**
 * @author macchan
 * 
 */
public class CAccelaratedTransition extends CAbstractTransition {

	/**
	 * Constructor.
	 */
	public CAccelaratedTransition(Component component, CRectangle target,
			int frameCount) {
		super(component, target, frameCount);
	}

	public void handleProcessStep() {
		super.handleProcessStep();
		CRectangle current = new CRectangle(getComponent().getBounds());
		CRectangle dr = new CRectangle(getTarget()).remove(current);
		double n = (getFrameCount() - getCurrentFrame()) / 2d;
		n = n > 1d ? n : 1;
		dr.divide(n);
		current.add(dr);
		// synchronized (getComponent().getTreeLock()) {
		getComponent().setBounds(current.toRectangle());
		getComponent().repaint();
		// }
	}
}
