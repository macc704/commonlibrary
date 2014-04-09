/*
 * CTimeModel.java
 * Created on 2011/05/30
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.timeline.model;

import clib.common.model.CAbstractModelObject;
import clib.common.time.CTime;

/**
 * @author macchan
 * 
 */
public class CTimeModel extends CAbstractModelObject {

	private static final long serialVersionUID = 1L;

	private CTime time = new CTime();

	/**
	 * Constructor.
	 */
	public CTimeModel() {
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public void setTime(CTime time) {
		if (!this.time.equals(time)) {
			this.time = time;
			fireModelUpdated();
		}
	}

	/**
	 * @return the time
	 */
	public CTime getTime() {
		return time;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return time.toString();
	}
}
