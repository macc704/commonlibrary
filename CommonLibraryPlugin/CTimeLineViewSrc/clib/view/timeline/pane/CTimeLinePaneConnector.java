/*
 * CTimeLinePaneConnector.java
 * Created on Apr 4, 2011 
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.timeline.pane;

import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author macchan
 * 
 */
public class CTimeLinePaneConnector {

	public static void connect(CTimeLinePane pane1, CTimeLinePane pane2) {
		new CTimeLinePaneConnector(pane1, pane2).hook();
	}

	private CTimeLinePane pane1;
	private CTimeLinePane pane2;

	private CTimeLinePaneConnector(CTimeLinePane pane1, CTimeLinePane pane2) {
		this.pane1 = pane1;
		this.pane2 = pane2;
	}

	private PropertyChangeListener listenerSplit12 = new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			handleSplitChanged(pane1, pane2);
		}
	};

	private PropertyChangeListener listenerSplit21 = new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			handleSplitChanged(pane2, pane1);
		}
	};

	private ChangeListener listenerScroll12 = new ChangeListener() {
		public void stateChanged(ChangeEvent e) {
			handleScrollChanged(pane1, pane2);
		}
	};

	private ChangeListener listenerScroll21 = new ChangeListener() {
		public void stateChanged(ChangeEvent e) {
			handleScrollChanged(pane2, pane1);
		}
	};

	private void handleSplitChanged(CTimeLinePane pane1, CTimeLinePane pane2) {
		unhook();
		JSplitPane split1 = pane1.getSplitPaneWestEast();
		JSplitPane split2 = pane2.getSplitPaneWestEast();
		split2.setDividerLocation(split1.getDividerLocation());
		hook();
	}

	private void handleScrollChanged(CTimeLinePane pane1, CTimeLinePane pane2) {
		unhook();
		JScrollPane main1 = pane1.getScrollPaneMain();
		JScrollPane main2 = pane2.getScrollPaneMain();
		Point p = main2.getViewport().getViewPosition();
		p.x = main1.getViewport().getViewPosition().x;
		main2.getViewport().setViewPosition(p);
		hook();
	}

	private void hook() {
		hookImpl(pane1, listenerSplit12, listenerScroll12);
		hookImpl(pane2, listenerSplit21, listenerScroll21);
	}

	private void unhook() {
		unhookImpl(pane1, listenerSplit12, listenerScroll12);
		unhookImpl(pane2, listenerSplit21, listenerScroll21);
	}

	private void hookImpl(CTimeLinePane pane,
			PropertyChangeListener splitListener, ChangeListener scrollListener) {
		pane.getSplitPaneWestEast().addPropertyChangeListener(
				JSplitPane.DIVIDER_LOCATION_PROPERTY, splitListener);
		pane.getScrollPaneMain().getViewport()
				.addChangeListener(scrollListener);
	}

	private void unhookImpl(CTimeLinePane pane,
			PropertyChangeListener splitListener, ChangeListener scrollListener) {
		pane.getSplitPaneWestEast().removePropertyChangeListener(
				JSplitPane.DIVIDER_LOCATION_PROPERTY, splitListener);
		pane.getScrollPaneMain().getViewport().removeChangeListener(
				scrollListener);
	}
}
