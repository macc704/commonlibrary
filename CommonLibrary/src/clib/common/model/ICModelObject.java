/*
 * ICModelObject.java
 * Created on Jun 27, 2010 
 * Copyright(c) 2010 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.common.model;

import java.io.Serializable;

public interface ICModelObject extends Serializable {

	public void addModelListener(ICModelChangeListener l);

	public void removeModelListener(ICModelChangeListener l);

	public void fireModelUpdated(Object... args);

	public void setNotifyMode(boolean notifyMode);

	public void beginTransaction();

	public void endTransaction();
}
