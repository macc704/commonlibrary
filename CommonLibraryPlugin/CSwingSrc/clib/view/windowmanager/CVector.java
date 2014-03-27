/*
 * CVector.java
 * Created on Jun 27, 2010 
 * Copyright(c) 2010 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.windowmanager;

import java.awt.Dimension;
import java.awt.Point;

public class CVector {

	private double x;
	private double y;

	public CVector() {
	}

	public CVector(CVector another) {
		this.x = another.x;
		this.y = another.y;
	}

	public CVector(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public CVector(Point p) {
		this.x = p.getX();
		this.y = p.getY();
	}

	public CVector(Dimension d) {
		this.x = d.getWidth();
		this.y = d.getHeight();
	}

	public CVector add(CVector another) {
		this.x = this.x + another.x;
		this.y = this.y + another.y;
		return this;
	}

	public CVector remove(CVector another) {
		this.x = this.x - another.x;
		this.y = this.y - another.y;
		return this;
	}

	public CVector multiply(double n) {
		this.x = this.x * n;
		this.y = this.y * n;
		return this;
	}

	public CVector divide(double n) {
		this.x = this.x / n;
		this.y = this.y / n;
		return this;
	}

	public Point toPoint() {
		return new Point((int) this.x, (int) this.y);
	}

	public Dimension toDimension() {
		return new Dimension((int) this.x, (int) this.y);
	}

}
