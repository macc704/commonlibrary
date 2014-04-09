/*
 * A.java
 * Created on 2012/05/15
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package cswing.table.test;

/**
 * @author macchan
 */
@SuppressWarnings("unused")
public class A {
	private String a;
	private String b;
	private int c;
	private Object d;
	private A e;

	public A() {
	}

	/**
	 * 
	 */
	public A(String a) {
		this.a = a;
	}

	/**
	 * @return the a
	 */
	public String getA() {
		return a;
	}

	/**
	 * @return the c
	 */
	public int getC() {
		return c;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return a;
	}
}
