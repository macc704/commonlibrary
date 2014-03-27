/*
 * CLayerLayout.java
 * Created on Apr 9, 2011 
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.panels;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

/**
 * @author macchan
 * 
 */
public class CLayerLayout implements LayoutManager {

	public CLayerLayout() {
	}

	public void addLayoutComponent(String name, Component comp) {
	}

	public void layoutContainer(Container parent) {
		Dimension d = parent.getSize();
		for (Component c : parent.getComponents()) {
			c.setBounds(0, 0, d.width, d.height);
		}
	}

	public Dimension minimumLayoutSize(Container parent) {
		Dimension d = new Dimension();
		for (Component c : parent.getComponents()) {
			Dimension cd = c.getMinimumSize();
			d.width = Math.max(d.width, cd.width);
			d.height = Math.max(d.height, cd.height);
		}
		return d;
	}

	public Dimension preferredLayoutSize(Container parent) {
		Dimension d = new Dimension();
		for (Component c : parent.getComponents()) {
			Dimension cd = c.getPreferredSize();
			d.width = Math.max(d.width, cd.width);
			d.height = Math.max(d.height, cd.height);
		}
		return d;
	}

	public void removeLayoutComponent(Component comp) {
	}

}
