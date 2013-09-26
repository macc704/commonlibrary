/*
 * PhaseTimeLinePane.java
 * Created on Mar 24, 2011 
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.timeline.testapp2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import clib.common.time.CTime;
import clib.view.timeline.model.CTimeTransformationModel;
import clib.view.timeline.pane.CAbstractTimeLinePane;
import clib.view.timeline.pane.CTimeLinePane;

import com.visutools.nav.bislider.BiSlider;
import com.visutools.nav.bislider.ColorisationEvent;
import com.visutools.nav.bislider.ColorisationListener;

/**
 * @author macchan
 * 
 */
public class PhaseTimeLinePane extends CAbstractTimeLinePane<Phase> {

	private static final long serialVersionUID = 1L;

	private CTimeLinePane upperPane;

	public PhaseTimeLinePane(CTimeLinePane upperPane) {
		this.upperPane = upperPane;
		upperPane.createIndicator(Color.RED);
		upperPane.createIndicator(Color.BLUE);
	}

	public JComponent createLeftPanel(Phase model) {
		JComponent panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new BorderLayout());
		JTextField label = new JTextField(model.getName());
		panel.add(label);
		return panel;
	}

	public JComponent createRightPanel(Phase model) {
		final JComponent panel = new JPanel();
		panel.setOpaque(false);

		// panel.setLayout(new BorderLayout());
		FlowLayout layout = new FlowLayout(FlowLayout.LEFT, 0, 0);
		panel.setLayout(layout);
		final BiSlider slider = new BiSlider();
		slider.setBackground(Color.WHITE);
		panel.add(slider);
		slider.setValues(0d, 1d);
		slider.addColorisationListener(new ColorisationListener() {
			public void newColors(ColorisationEvent evt) {
				double min = evt.getMinimum();
				double max = evt.getMaximum();
				CTimeTransformationModel transModel = getTimelinePane()
						.getTimeTransModel();
				CTime start = transModel.getTimeByRate(min);
				CTime end = transModel.getTimeByRate(max);
				upperPane.setIndicatorTime(start, end);
			}
		});
		getTimelinePane().getTimeTransModel().addPropertyChangeListener(
				new PropertyChangeListener() {
					public void propertyChange(PropertyChangeEvent evt) {
						CTimeTransformationModel transModel = getTimelinePane()
								.getTimeTransModel();
						Dimension d = panel.getSize();
						d.width = transModel.getPreferredWidth();
						slider.setPreferredSize(d);
						slider.setSize(d);
					}
				});
		// slider.addMouseListener(new MouseAdapter() {
		// public void mouseClicked(MouseEvent e) {
		// if (e.getClickCount() == 2) {
		// CTimeTransformationModel model = getTimelinePane()
		// .getTransModel();
		// double x = e.getX();
		// double rate = x / model.getPreferredWidth();
		// // CTime time = model.x2Time(x);
		// System.out.println(rate);
		// e.consume();
		// }
		// }
		// });

		return panel;
	}
	// /*
	// * (non-Javadoc)
	// *
	// * @see clib.view.timeline.pane.CAbstractTimeLinePane#getComponentHeight()
	// */
	// @Override
	// public int getComponentHeight() {
	// return 40;
	// }
}
