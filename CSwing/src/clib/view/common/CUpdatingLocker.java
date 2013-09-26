/*
 * CUpdatingLocker.java
 * Created on 2011/11/27
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.common;

import java.util.Stack;

/**
 * @author macchan
 * 
 */
public class CUpdatingLocker {

	private Stack<Object> locker = new Stack<Object>();

	public CUpdatingLocker() {
	}

	public void lock() {
		locker.push("A");
	}

	public void unlock() {
		locker.pop();
	}

	public boolean isLocked() {
		return locker.size() > 0;
	}

}
