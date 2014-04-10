/*
 * CPointList.java
 * Copyright (c) 2005 PlatBox Project. All rights reserved.
 */
package clib.view.chart.model;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * GraphViewにおける、折れ線の情報を保持します 複数のGraphPointを持ちます。
 * 
 * @author rx78g
 * @version $Id: CPointList.java,v 1.1 2005/03/08 03:04:45 bam Exp $
 */
public class CPointList {

	// 折れ線を構成するGraphPoint群です
	private List<Point2D> points = new ArrayList<Point2D>();

	// 現在保持しているGraphPoint群における、X座標の最小値です
	private Point2D minX = null;
	// 現在保持しているGraphPoint群における、Y座標の最小値です
	private Point2D minY = null;
	// 現在保持しているGraphPoint群における、X座標の最大値です
	private Point2D maxX = null;
	// 現在保持しているGraphPoint群における、Y座標の最大値です
	private Point2D maxY = null;

	/**
	 * Constructor for CPointList.
	 */
	public CPointList() {
		super();
	}

	/**
	 * Constructor for CPointList.
	 */
	public CPointList(CPointList another) {
		addAll(another);
	}

	public void addAll(CPointList another) {
		Iterator<Point2D> i = another.iterator();
		while (i.hasNext()) {
			Point2D p = i.next();
			this.add(new Point2D.Double(p.getX(), p.getY()));
		}
	}

	/**
	 * GraphPointを追加します
	 */
	public void add(Point2D p) {
		this.addValid(p);
		this.points.add(p);
	}

	public boolean remove(Point2D p) {
		return this.points.remove(p);
	}

	public Point2D remove(int index) {
		return this.points.remove(index);
	}

	public void translate(int dx, int dy) {
		Iterator<Point2D> i = this.points.iterator();
		while (i.hasNext()) {
			Point p = (Point) i.next();
			p.translate(dx, dy);
		}
	}

	public void scale(double sx, double sy) {
		Iterator<Point2D> i = this.points.iterator();
		while (i.hasNext()) {
			Point2D p = i.next();
			p.setLocation(p.getX() * sx, p.getY() * sy);
		}
	}

	/**
	 * XY座標の最大値、最小値を更新します
	 */
	private void addValid(Point2D p) {
		if (p.getY() <= this.getMinY()) {
			this.minY = p;
		}
		if (p.getY() >= this.getMaxY()) {
			this.maxY = p;
		}
		if (p.getX() <= this.getMinX()) {
			this.minX = p;
		}
		if (p.getX() >= this.getMaxX()) {
			this.maxX = p;
		}
	}

	/**
	 * 全座標の中でXの最小値を返します。
	 * 
	 * @return double
	 */
	public double getMinX() {
		return minX == null ? 0.0 : minX.getX();
	}

	/**
	 * 全座標の中でXの最大値を返します。
	 * 
	 * @return double
	 */
	public double getMaxX() {
		return maxX == null ? 0.0 : maxX.getX();
	}

	/**
	 * 全座標の中でYの最小値を返します。
	 * 
	 * @return double
	 */
	public double getMinY() {
		return minY == null ? 0.0 : minY.getY();
	}

	/**
	 * 全座標の中でYの最大値を返します。
	 * 
	 * @return double
	 */
	public double getMaxY() {
		return maxY == null ? 0.0 : maxY.getY();
	}

	public void clear() {
		this.points.clear();
	}

	/**
	 * Method iterator.
	 * 
	 * @return Iterator
	 */
	public Iterator<Point2D> iterator() {
		return this.points.iterator();
	}

}