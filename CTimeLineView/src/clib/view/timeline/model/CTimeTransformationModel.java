/*
 * CTimeTransformationModel.java
 * Created on Mar 9, 2011 
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.timeline.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import clib.common.time.CTime;
import clib.common.time.CTimeInterval;
import clib.common.time.CTimeRange;

/**
 * @author macchan
 */
public class CTimeTransformationModel {

	public static final double MILLS_PER_DAY = 24 * 60 * 60 * 1000;

	private double offset = 0;
	private CTimeRange range;
	private double scale = 1d; // dpd (dot per day) dot count per 1day.

	private PropertyChangeSupport support = new PropertyChangeSupport(this);

	public CTimeTransformationModel() {
		this(new CTimeRange());
	}

	public CTimeTransformationModel(CTime start, CTime end) {
		this(new CTimeRange(start, end));
	}

	public CTimeTransformationModel(CTimeRange range) {
		setRange(range);
	}

	/**
	 * @return the range
	 */
	public CTimeRange getRange() {
		return range;
	}

	/**
	 * @param range
	 *            the range to set
	 */
	public void setRange(CTimeRange range) {
		if (range == null) {
			throw new RuntimeException();
		}
		if (range.equals(this.range)) {
			return;
		}
		this.range = range;
		firePropertyChange();
	}

	/**
	 * @return the scale
	 */
	public double getScale() {
		return scale;
	}

	/**
	 * @param scale
	 *            the scale to set
	 */
	public void setScale(double scale) {
		this.scale = scale;
		firePropertyChange();
	}

	/**
	 * @param offset
	 *            the offset to set
	 */
	public void setOffset(double offset) {
		this.offset = offset;
		firePropertyChange();
	}

	public double time2X(CTime time) {
		long start = range.getStart().getAsLong();
		long timeX = time.getAsLong();
		double x = ((double) (timeX - start) / MILLS_PER_DAY) * scale;
		return x + offset;
	}

	public int time2XAsInt(long time) {
		return (int) time2X(new CTime(time));
	}

	public CTime x2Time(double x) {
		x = x - offset;
		long start = range.getStart().getAsLong();
		long timeX = (long) (x / scale * MILLS_PER_DAY) + start;
		return new CTime(timeX);
	}

	public int getPreferredWidth() {
		long width = (long) (range.getLengthAsLong() / MILLS_PER_DAY * scale);
		width += offset * 2;
		if (width > Integer.MAX_VALUE) {
			return Integer.MAX_VALUE;
		} else {
			return (int) width;
		}
	}

	public double getFitScale(int width) {
		double w = (double) width;
		w -= offset * 2;
		double len = range.getLengthAsLong() / MILLS_PER_DAY;
		return w / len;
	}

	public CTime getTimeByRate(double rate) {
		long len = (long) (rate * (double) range.getLengthAsLong());
		long time = range.getStart().getAsLong() + len;
		return new CTime(time);
	}

	public double getRateByTime(CTime time) {
		long len = time.getAsLong() - range.getStart().getAsLong();
		double rate = (double) len / (double) range.getLengthAsLong();
		return rate;
	}

	// 1dotあたりの時間
	public CTimeInterval getDpt() {
		double dpt = 1d / scale;
		return new CTimeInterval((int) (dpt * MILLS_PER_DAY));
	}

	protected void firePropertyChange() {
		support.firePropertyChange(null, null, null);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		support.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		support.removePropertyChangeListener(listener);
	}

}
