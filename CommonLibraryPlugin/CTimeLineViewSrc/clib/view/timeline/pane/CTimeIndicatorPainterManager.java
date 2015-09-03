/*
 * CTimeIndicationManager.java
 * Created on 2011/05/28
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.timeline.pane;

import java.util.ArrayList;
import java.util.List;

import clib.view.timeline.model.CTimeTransformationModel;

/**
 * @author macchan
 */
public class CTimeIndicatorPainterManager {

	private List<CTimeIndicatorPainter> indicators = new ArrayList<CTimeIndicatorPainter>();
	private CTimeTransformationModel model;

	/**
	 * @return the indicators
	 */
	public List<CTimeIndicatorPainter> getIndicators() {
		return indicators;
	}

	public void add(CTimeIndicatorPainter indicator) {
		indicator.setTransModel(this.model);
		indicators.add(indicator);
	}

	/**
	 * @param transModel
	 */
	public void setModel(CTimeTransformationModel model) {
		this.model = model;
		for (CTimeIndicatorPainter indicator : indicators) {
			indicator.setTransModel(model);
		}
	}

}
