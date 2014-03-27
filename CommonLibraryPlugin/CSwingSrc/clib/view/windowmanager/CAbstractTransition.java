/*
 * CAbstractTransition.java
 * Created on Jun 27, 2010 
 * Copyright(c) 2010 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.windowmanager;

import java.awt.Component;

import clib.common.thread.ICRunnable;

/**
 * @author macchan
 */
public abstract class CAbstractTransition implements ICRunnable {

	private Component component;
	private int frameCount;
	private CRectangle target;

	private int currentFrame = 0;

	/**
	 * Constructor.
	 */
	public CAbstractTransition(Component component, CRectangle target,
			int frameCount) {
		this.component = component;
		this.target = target;
		this.frameCount = frameCount;
	}

	public boolean allowStart() {
		return true;
	}

	public void handlePrepareStart() {
		currentFrame = 0;
	}

	public void handlePrepareStop() {
		getComponent().setBounds(getTarget().toRectangle());
		getComponent().validate();
	}

	public void handleProcessStep() {
		currentFrame++;
	}

	public boolean isFinished() {
		return currentFrame >= frameCount;
	}

	/**
	 * @return the component
	 */
	public Component getComponent() {
		return component;
	}

	/**
	 * @param component
	 *            the component to set
	 */
	public void setComponent(Component component) {
		this.component = component;
	}

	/**
	 * @return the frameCount
	 */
	public int getFrameCount() {
		return frameCount;
	}

	/**
	 * @param frameCount
	 *            the frameCount to set
	 */
	public void setFrameCount(int frameCount) {
		this.frameCount = frameCount;
	}

	/**
	 * @return the target
	 */
	public CRectangle getTarget() {
		return target;
	}

	/**
	 * @param target
	 *            the target to set
	 */
	public void setTarget(CRectangle target) {
		this.target = target;
	}

	/**
	 * @return the currentFrame
	 */
	public int getCurrentFrame() {
		return currentFrame;
	}
}
