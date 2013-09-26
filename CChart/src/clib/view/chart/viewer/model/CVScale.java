/*
 * CVScale.java
 * Created on 2004/05/05
 * 
 * Copyright (c) 2005 PlatBox Project. All rights reserved.
 */
package clib.view.chart.viewer.model;

/**
 * Class CVScale.
 * 
 * @author macchan
 * @version $Id: CVScale.java,v 1.1 2005/03/08 03:04:46 bam Exp $
 */
public class CVScale {

	private boolean auto = false;
	private double value = 1;

	public CVScale() {
		super();
	}

	public void zoom(double power) {
		setValue(value * power);
	}

	public void setAuto(boolean auto) {
		this.auto = auto;
	}

	public boolean isAuto() {
		return auto;
	}

	public void setValue(double value) {
		this.value = getProperValue(value);
	}

	public double getValue() {
		return value;
	}

	/**
	 * 小数点の有効桁数が1桁になるように倍率を計算しなおす
	 */
	private double getProperValue(double value) {

		// 有効桁数を求める
		int validPlace = getValidPlace(value);

		// 有効桁数以下を切り捨てる
		int power = (int) Math.pow(10, validPlace);
		int tmp = (int) (value * (double) power);
		value = ((double) tmp / power);

		return value;
	}

	/**
	 * 有効桁数を求めます
	 */
	private int getValidPlace(double value) {

		int place = 0;
		do {
			value = value * 10;
			place++;
		} while ((int) value == 0);

		return place;
	}

	public static void main(String args[]) {

		CVScale scale = new CVScale();
		System.out.println(scale.getProperValue(4.2));
		System.out.println(scale.getProperValue(3));
		System.out.println(scale.getProperValue(0.18));
		System.out.println(scale.getProperValue(0.92));
		System.out.println(scale.getProperValue(0.092));
	}

}