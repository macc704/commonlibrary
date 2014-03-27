/*
 * CLinearTransition.java
 * Created on Jun 27, 2010 
 * Copyright(c) 2010 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.windowmanager;

import java.awt.Component;

/**
 * @author macchan
 * 
 */
public class CLinearTransition extends CAbstractTransition {

	/**
	 * Constructor.
	 */
	public CLinearTransition(Component component, CRectangle target,
			int frameCount) {
		super(component, target, frameCount);
	}

	private CRectangle source;
	private CRectangle current;
	private CRectangle dr;

	public void handlePrepareStart() {
		super.handlePrepareStart();
		source = new CRectangle(getComponent().getBounds());
		dr = new CRectangle(getTarget()).remove(source).divide(getFrameCount());
		current = new CRectangle(getComponent().getBounds());
	}

	public void handleProcessStep() {
		super.handleProcessStep();
		current.add(dr);
		getComponent().setBounds(current.toRectangle());
	}

}
