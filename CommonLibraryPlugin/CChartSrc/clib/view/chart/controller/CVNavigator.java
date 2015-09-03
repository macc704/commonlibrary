/*
 * CAxisNavigator.java
 * Created on 2011/11/25
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.chart.controller;

import clib.common.model.CAbstractModelObject;

/**
 * @author macchan
 */
public class CVNavigator extends CAbstractModelObject {

	private static final long serialVersionUID = 1L;

	public enum Event {
		VALUE_SET_FINISHED
	};

	private double value = 0;

	/**
	 * @return the value
	 */
	public double getValue() {
		return value;
	}

	/**
	 * @param graphX
	 */
	public void setValue(double value) {
		if (this.value != value) {
			this.value = value;
			fireModelUpdated();
		}
	}

	public void fireValueSetFinished() {
		fireModelUpdated(Event.VALUE_SET_FINISHED);
	}

}
