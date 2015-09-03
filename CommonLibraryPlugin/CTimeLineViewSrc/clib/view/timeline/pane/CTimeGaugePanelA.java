/*
 * CTimeGaugePanelA.java
 * Created on Mar 24, 2011 
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.timeline.pane;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.JPanel;

import clib.view.timeline.model.CTimeTransformationModel;

/**
 * シンプルなゲージ． メモリ単位自動切り替え機能なし． 現在使っていない．
 * 
 * @author macchan
 */
public class CTimeGaugePanelA extends JPanel {

	private static final int GAUGE_A = 5;
	private static final int GAUGE_B = 50;
	private static final int GAUGE_C = 100;

	private static DateFormat format = new SimpleDateFormat("yyyy/MM/dd");

	private static final long serialVersionUID = 1L;

	private CTimeTransformationModel transModel;

	public CTimeGaugePanelA(CTimeTransformationModel transModel) {
		this.transModel = transModel;
		initialize();
	}

	private void initialize() {
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		Rectangle r = g2d.getClipBounds();

		int height = getHeight();

		int start = r.x - r.x % GAUGE_A;
		int end = r.x + r.width;

		for (int x = start; x < end; x += GAUGE_A) {
			g2d.drawLine(x, height * 2 / 3, x, height);

			if (x % GAUGE_B == 0) {
				g2d.drawLine(x, height / 3, x, height);
			}

			if (x % GAUGE_C == 0) {
				g2d.drawLine(x, 0, x, height);
				String text = format.format(transModel.x2Time(x).getAsDate());
				g2d.drawString(text, x + 1, height / 3);
			}
		}
	}
}
