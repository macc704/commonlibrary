/*
 * CVAbstractAxisContainerPanel.java
 * Created on 2004/05/05
 * 
 * Copyright (c) 2005 PlatBox Project. All rights reserved.
 */
package clib.view.chart.viewer.view.axis;

import java.awt.LayoutManager;
import java.awt.Point;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import clib.crewlib.OListCoordinator;
import clib.crewlib.OValueChangeListener;
import clib.view.chart.controller.CVChartController;
import clib.view.chart.viewer.model.CVAxis;

/**
 * Class CVAbstractAxisContainerPanel.
 * 
 * @author macchan
 * @version $Id: CVAbstractAxisContainerPanel.java,v 1.1 2005/03/08 03:04:46 bam
 *          Exp $
 */
public abstract class CVAbstractAxisContainerPanel extends JPanel implements
		OValueChangeListener, ChangeListener {

	private static final long serialVersionUID = 1L;

	/**********************
	 * Variables
	 **********************/

	// Relations
	private CVChartController container;

	// Components
	private JViewport viewport;
	private OListCoordinator<CVAxis> coordinator = new OListCoordinator<CVAxis>(
			this);
	private Map<CVAxis, CVAbstractAxisPanel> panels = new HashMap<CVAxis, CVAbstractAxisPanel>();

	/**********************
	 * Constructor
	 **********************/

	/**
	 * Constructor for CVAbstractAxisContainerPanel.
	 */
	public CVAbstractAxisContainerPanel() {
		setLayout(createLayout());
	}

	/**********************
	 * Accessors
	 **********************/

	public CVChartController getController() {
		return container;
	}

	public void setController(CVChartController container) {
		this.container = container;
	}

	public Collection<CVAbstractAxisPanel> getAxisPanels() {
		return this.panels.values();
	}

	/*************************
	 * Setter for Viewport
	 *************************/

	public void setViewport(JViewport viewport) {
		if (this.viewport != null) {
			unhookListeners();
		}

		this.viewport = viewport;

		if (this.viewport != null) {
			hookListeners();
		}
	}

	private void unhookListeners() {
		viewport.removeChangeListener(this);
	}

	private void hookListeners() {
		viewport.addChangeListener(this);
	}

	/***************************
	 * Update Strategy
	 ***************************/

	public void refreshView() {
		if (viewport != null && container != null) {
			coordinator.setList(getAxisList());
		}
		for (CVAbstractAxisPanel panel : panels.values()) {
			panel.update();
		}
	}

	/***************************
	 * Event Handlders
	 ***************************/

	/**
	 * @see OValueChangeListener.ac.keio.sfc.crew.collection.ValueChangeListener#valueAdded(java.lang.Object,
	 *      int)
	 */
	public void valueAdded(Object value, int index) {
		CVAxis axis = (CVAxis) value;
		CVAbstractAxisPanel panel = createPanel(axis);
		panels.put(axis, panel);
		add(panel, index);
	}

	/**
	 * @see OValueChangeListener.ac.keio.sfc.crew.collection.ValueChangeListener#valueRemoved(java.lang.Object)
	 */
	public void valueRemoved(Object value) {
		CVAxis axis = (CVAxis) value;
		JPanel panel = (JPanel) panels.get(axis);
		remove(panel);
	}

	/**
	 * @see OValueChangeListener.ac.keio.sfc.crew.collection.ValueChangeListener#valueReorderd(java.lang.Object,
	 *      int)
	 */
	public void valueReorderd(Object value, int index) {
		CVAxis axis = (CVAxis) value;
		JPanel panel = panels.get(axis);
		remove(panel);
		add(panel, index);
	}

	/**
	 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 */
	public void stateChanged(ChangeEvent e) {
		Point position = viewport.getViewPosition();
		for (CVAbstractAxisPanel panel : panels.values()) {
			panel.viewPositionChanged(position);
		}
	}

	public abstract List<CVAxis> getAxisList();

	public abstract LayoutManager createLayout();

	public abstract CVAbstractAxisPanel createPanel(CVAxis axis);

}