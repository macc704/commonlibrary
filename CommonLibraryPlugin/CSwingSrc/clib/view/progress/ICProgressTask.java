/*
 * ICProgressTask.java
 * Created on May 6, 2011 
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.progress;

import clib.common.utils.ICProgressMonitor;

/**
 * @author macchan
 * 
 */
public interface ICProgressTask {

	public void doTask(ICProgressMonitor monitor);

}
