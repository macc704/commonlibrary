/*
 * CNullProgressMonitor.java
 * Created on Sep 16, 2010 
 * Copyright(c) 2010 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.common.utils;

/**
 * @author macchan
 * 
 */
public class CNullProgressMonitor implements ICProgressMonitor {

	public static CNullProgressMonitor INSTANCE = new CNullProgressMonitor();

	/*
	 * (non-Javadoc)
	 * 
	 * @see clib.common.utils.ICProgressMonitor#setWorkTitle(java.lang.String)
	 */
	public void setWorkTitle(String title) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see clib.common.utils.ICProgressMonitor#setMax(int)
	 */
	public void setMax(int n) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see clib.common.utils.ICProgressMonitor#progress(int)
	 */
	public void progress(int x) {
	}
}
