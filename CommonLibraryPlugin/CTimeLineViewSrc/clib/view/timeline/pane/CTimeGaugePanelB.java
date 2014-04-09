/*
 * CTimeGaugePanelA.java
 * Created on Mar 24, 2011 
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.timeline.pane;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import clib.common.time.CTime;
import clib.view.timeline.model.CTimeTransformationModel;

/**
 * @author macchan
 */
public class CTimeGaugePanelB extends JPanel {

	private static final long serialVersionUID = 1L;

	private static Map<Integer, DateFormat> formats1 = new HashMap<Integer, DateFormat>();
	private static Map<Integer, DateFormat> formats2 = new HashMap<Integer, DateFormat>();

	static {
		formats1.put(Calendar.MILLISECOND, new SimpleDateFormat(
				"yyyy/MM/dd HH:mm:ss :SSSS"));
		formats1.put(Calendar.SECOND, new SimpleDateFormat(
				"yyyy/MM/dd HH:mm:ss"));
		formats1.put(Calendar.MINUTE, new SimpleDateFormat("yyyy/MM/dd HH:mm"));
		formats1.put(Calendar.HOUR_OF_DAY, new SimpleDateFormat(
				"yyyy/MM/dd HH:00"));
		formats1.put(Calendar.DAY_OF_MONTH, new SimpleDateFormat("yyyy/MM/dd"));
		formats1.put(Calendar.MONTH, new SimpleDateFormat("yyyy/MM"));
		formats1.put(Calendar.YEAR, new SimpleDateFormat("yyyy"));

		formats2.put(Calendar.MILLISECOND, new SimpleDateFormat("SSSS"));
		formats2.put(Calendar.SECOND, new SimpleDateFormat("ss"));
		formats2.put(Calendar.MINUTE, new SimpleDateFormat("mm"));
		formats2.put(Calendar.HOUR_OF_DAY, new SimpleDateFormat("HH"));
		formats2.put(Calendar.DAY_OF_MONTH, new SimpleDateFormat("dd"));
		formats2.put(Calendar.MONTH, new SimpleDateFormat("MM"));
		formats2.put(Calendar.YEAR, new SimpleDateFormat("yyyy"));
	}

	private CTimeTransformationModel transModel;
	private CTimeIndicatorPainterManager indicators = new CTimeIndicatorPainterManager();

	public CTimeGaugePanelB(CTimeTransformationModel transModel,
			CTimeIndicatorPainterManager indicators) {
		this.transModel = transModel;
		this.indicators = indicators;

		setBackground(Color.WHITE);
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		int offset = 3;

		Graphics2D g2d = (Graphics2D) g;
		Rectangle r = g2d.getClipBounds();

		int height = getHeight() - offset;

		long start = transModel.x2Time(r.x).getAsLong();
		long end = transModel.x2Time(r.x + r.width).getAsLong();

		int field1 = Calendar.YEAR;
		int field2 = Calendar.MONTH;

		long dpt = transModel.getDpt().getTime();
		long thr = dpt * 3;
		int labelDraw = -1;

		if (thr < CTime.SECOND) {
			field1 = Calendar.MINUTE;
			field2 = Calendar.SECOND;
			if (thr * 4 < CTime.SECOND) {
				labelDraw = height;
			}
		} else if (thr < CTime.MINUTE) {
			field1 = Calendar.HOUR_OF_DAY;
			field2 = Calendar.MINUTE;
			if (thr * 4 < CTime.MINUTE) {
				labelDraw = height;
			}
		} else if (thr < CTime.HOUR) {
			field1 = Calendar.DAY_OF_MONTH;
			field2 = Calendar.HOUR_OF_DAY;
			if (thr * 4 < CTime.HOUR) {
				labelDraw = height;
			}
		} else if (thr < CTime.DAY) {
			field1 = Calendar.MONTH;
			field2 = Calendar.DAY_OF_MONTH;
			if (thr * 4 < CTime.DAY) {
				labelDraw = height;
			}
		} else if (thr < CTime.MONTH) {
			// go by default
			if (thr * 4 < CTime.MONTH) {
				labelDraw = height;
			}
		}

		int height1 = height * 1 / 3 + offset;
		int height2 = height * 2 / 3;
		int height3 = height * 3 / 3;
		drawGauge(start, end, field1, g2d, formats1.get(field1), 0, height2,
				height1);
		drawGauge(start, end, field2, g2d, formats2.get(field2), height2,
				height3, labelDraw);

		for (CTimeIndicatorPainter painter : indicators.getIndicators()) {
			painter.draw(g2d, getHeight(), 15, 15);
		}
	}

	private void drawGauge(long start, long end, int field, Graphics2D g2d,
			DateFormat format, int upper, int under, int strBase) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(start);
		floor(cal, field);

		long time = cal.getTimeInMillis();
		while (time < end) {
			{ // x軸ににマッピングしたことによる誤差が出ないようにすること
				int x = transModel.time2XAsInt(cal.getTimeInMillis());
				g2d.drawLine(x, upper, x, under);

				if (strBase >= 0) {
					String text = format.format(new Date(time));
					g2d.drawString(text, x + 1, strBase);
				}

				// 次のUnitへ
				cal.add(field, 1);
				time = cal.getTimeInMillis();
			}
		}
	}

	private void floor(Calendar cal, int field) {
		// Calendar.YEAR < Calendar.MILLISECOND
		if (field < Calendar.MONTH) {
			cal.set(Calendar.MONTH, 0);
		}
		if (field < Calendar.DAY_OF_MONTH) {
			cal.set(Calendar.DAY_OF_MONTH, 1);
		}
		if (field < Calendar.HOUR_OF_DAY) {
			cal.set(Calendar.HOUR_OF_DAY, 0);
		}
		if (field < Calendar.MINUTE) {
			cal.set(Calendar.MINUTE, 0);
		}
		if (field < Calendar.SECOND) {
			cal.set(Calendar.SECOND, 0);
		}
		if (field < Calendar.MILLISECOND) {
			cal.set(Calendar.MILLISECOND, 0);
		}
	}
}
