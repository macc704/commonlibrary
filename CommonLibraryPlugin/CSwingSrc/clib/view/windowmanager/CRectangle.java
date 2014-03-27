/*
 * CRectangle.java
 * Created on Jun 27, 2010 
 * Copyright(c) 2010 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.windowmanager;

import java.awt.Rectangle;

public class CRectangle {

	private CVector location;
	private CVector size;

	public CRectangle() {
		location = new CVector();
		size = new CVector();
	}

	public CRectangle(double x, double y, double w, double h) {
		location = new CVector(x, y);
		size = new CVector(w, h);
	}

	public CRectangle(CRectangle another) {
		location = new CVector(another.location);
		size = new CVector(another.size);
	}

	public CRectangle(Rectangle r) {
		location = new CVector(r.getLocation());
		size = new CVector(r.getSize());
	}

	public Rectangle toRectangle() {
		return new Rectangle(location.toPoint(), size.toDimension());
	}

	public CRectangle add(CRectangle another) {
		location.add(another.location);
		size.add(another.size);
		return this;
	}

	public CRectangle remove(CRectangle another) {
		location.remove(another.location);
		size.remove(another.size);
		return this;
	}

	public CRectangle divide(double n) {
		location.divide(n);
		size.divide(n);
		return this;
	}

	public CRectangle multiply(double n) {
		location.multiply(n);
		size.multiply(n);
		return this;
	}
}
