/*
 * CVerticalFlowLayout.java
 * Copyright (c) 2002 Boxed-Economy Project.  All rights reserved.
 */
package clib.view.panels;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.io.Serializable;

/**
 * @author macchan
 * @version $Id: CVerticalFlowLayout.java,v 1.2 2004/07/15 10:27:03 bam Exp $
 */
public class CVerticalFlowLayout implements LayoutManager, Serializable {

	private static final long serialVersionUID = 1L;

	private int margin = 5;

	/**
	 * Constructor for CVerticalFlowLayout.
	 */
	public CVerticalFlowLayout() {
	}

	/**
	 * Constructor for CVerticalFlowLayout.
	 */
	public CVerticalFlowLayout(int margin) {
		this.margin = margin;
	}

	/**
	 * @see java.awt.LayoutManager#addLayoutComponent(String, Component)
	 */
	public void addLayoutComponent(String name, Component comp) {
	}

	/**
	 * @see java.awt.LayoutManager#removeLayoutComponent(Component)
	 */
	public void removeLayoutComponent(Component comp) {
	}

	/**
	 * @see java.awt.LayoutManager#preferredLayoutSize(Container)
	 */
	public Dimension preferredLayoutSize(Container parent) {
		synchronized (parent.getTreeLock()) {
			Insets insets = parent.getInsets();
			int componentCount = parent.getComponentCount();
			int width = 0;
			int height = 0;
			for (int i = 0; i < componentCount; i++) {
				Component component = parent.getComponent(i);
				Dimension d = component.getPreferredSize();
				if (width < d.width) {
					width = d.width;
				}
				height = height + margin + d.height;
			}
			return new Dimension(insets.left + insets.right + width, insets.top
					+ insets.bottom + height);
		}
	}

	/**
	 * @see java.awt.LayoutManager#minimumLayoutSize(Container)
	 */
	public Dimension minimumLayoutSize(Container parent) {
		return this.preferredLayoutSize(parent);
	}

	/**
	 * @see java.awt.LayoutManager#layoutContainer(Container)
	 */
	public void layoutContainer(Container parent) {
		synchronized (parent.getTreeLock()) {
			Insets insets = parent.getInsets();

			int x = insets.left;
			int y = insets.top;
			int width = parent.getWidth() - (insets.left + insets.right);

			int componentCount = parent.getComponentCount();
			for (int i = 0; i < componentCount; i++) {
				Component component = parent.getComponent(i);
				Dimension d = component.getPreferredSize();
				component.setBounds(x, y, width, d.height);
				y = y + margin + d.height;
			}
		}
	}

	/**
	 * returns the margin
	 */
	public int getMargin() {
		return margin;
	}

	/**
	 * sets the margin
	 */
	public void setMargin(int margin) {
		this.margin = margin;
	}

}
