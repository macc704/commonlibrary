/*
 * CVDataSettingDialogPanel.java
 * Created on 2004/05/07
 * 
 * Copyright (c) 2005 PlatBox Project. All rights reserved.
 */
package clib.view.chart.viewer.view.dialogs;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import clib.view.chart.controller.CVChartController;
import clib.view.chart.viewer.model.CVDataSet;
import clib.view.chart.viewer.resource.ChartResource;
import clib.view.common.CAbstractColorChooseButton;

/**
 * Class CVDataSettingDialogPanel.
 * 
 * @author macchan
 * @version $Id: CVDataSettingDialogPanel.java,v 1.1 2005/03/08 03:04:45 bam Exp
 *          $
 */
public class CVDataSettingDialogPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private CVChartController container;

	private List<CVAbstractDataDependentSettingPanel> xDependentSettingPanels = new ArrayList<CVAbstractDataDependentSettingPanel>();
	private List<CVAbstractDataDependentSettingPanel> yDependentSettingPanels = new ArrayList<CVAbstractDataDependentSettingPanel>();

	/**
	 * Constructor for CVDataSettingDialogPanel.
	 */
	public CVDataSettingDialogPanel(CVChartController container) {
		this.container = container;
		initialize();
	}

	private void initialize() {
		// setLayout(new GridLayout(4, 0));
		// add(new JLabel(
		//				ChartResource.get("CVDataSettingDialogPanel.Data_Name"), JLabel.CENTER)); //$NON-NLS-1$
		// add(new JLabel(
		//				ChartResource.get("CVDataSettingDialogPanel.Line_Color"), JLabel.CENTER)); //$NON-NLS-1$
		// add(new JLabel(ChartResource
		//				.get("CVDataSettingDialogPanel.Marge_X_Axis"), JLabel.CENTER)); //$NON-NLS-1$
		// add(new JLabel(ChartResource
		//				.get("CVDataSettingDialogPanel.Marge_Y_Axis"), JLabel.CENTER)); //$NON-NLS-1$

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		// add title
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 4));
		panel.add(new JLabel(ChartResource
				.get("CVDataSettingDialogPanel.Data_Name"), JLabel.CENTER)); //$NON-NLS-1$
		panel.add(new JLabel(ChartResource
				.get("CVDataSettingDialogPanel.Line_Color"), JLabel.CENTER)); //$NON-NLS-1$
		panel.add(new JLabel(ChartResource
				.get("CVDataSettingDialogPanel.Marge_X_Axis"), JLabel.CENTER)); //$NON-NLS-1$
		panel.add(new JLabel(ChartResource
				.get("CVDataSettingDialogPanel.Marge_Y_Axis"), JLabel.CENTER)); //$NON-NLS-1$
		addSpecial(panel);

		// add each datasets
		for (Iterator<CVDataSet> i = container.getChart().getDataSets()
				.iterator(); i.hasNext();) {
			CVDataSet data = (CVDataSet) i.next();
			DataSettingPanel dataSettingPanel = new DataSettingPanel(data);
			addSpecial(dataSettingPanel);
		}
	}

	private void addSpecial(JComponent comp) {
		comp.setMaximumSize(new Dimension(1000, 50));
		add(comp);
	}

	class DataSettingPanel extends JComponent {
		private static final long serialVersionUID = 1L;

		private CVDataSet data;

		DataSettingPanel(CVDataSet data) {
			this.data = data;
			initialize();
		}

		public void initialize() {
			setLayout(new GridLayout(0, 4));

			// Label
			JLabel label = new JLabel(data.getModel().getLabel(), JLabel.CENTER);
			add(label);

			// ColorChooser
			JPanel buttonPanel = new JPanel();
			buttonPanel.setLayout(new CenterLayout());
			ColorChooseButton button = new ColorChooseButton();
			buttonPanel.add(button);
			add(buttonPanel);

			// XDependency
			XDataDependentSettingPanel xDependencyPanel = new XDataDependentSettingPanel(
					container, data);
			add(xDependencyPanel);
			xDependentSettingPanels.add(xDependencyPanel);

			// YDependency
			YDataDependentSettingPanel yDependencyPanel = new YDataDependentSettingPanel(
					container, data);
			add(yDependencyPanel);
			yDependentSettingPanels.add(yDependencyPanel);
		}

		class ColorChooseButton extends CAbstractColorChooseButton {
			private static final long serialVersionUID = 1L;

			public void drawExplanation(Graphics g) {
				g.fillRect(0, 0, 20, 5);
			}

			public Color getColor() {
				return data.getColor();
			}

			public Dimension getIconSize() {
				return new Dimension(20, 5);
			}

			public String getTitle() {
				return ChartResource
						.get("CVDataSettingDialogPanel.Choose_Color"); //$NON-NLS-1$
			}

			public void setColor(Color color) {
				data.setColor(color);
			}
		}

		class XDataDependentSettingPanel extends
				CVAbstractDataDependentSettingPanel {
			private static final long serialVersionUID = 1L;

			public XDataDependentSettingPanel(CVChartController container,
					CVDataSet data) {
				super(container, data);
			}

			public CVDataSet getDependent() {
				return getData().getDependentX();
			}

			public void setDependent(CVDataSet data) {
				getData().setDependentX(data);
			}

			public List<CVDataSet> getDependentList(CVDataSet data) {
				return data.getDependentXList();
			}

			public boolean isDependent() {
				return getData().getDependentX() != null;
			}

			public void updated() {
				for (CVAbstractDataDependentSettingPanel panel : xDependentSettingPanels) {
					panel.update();
				}
				container.refreshViews();
			}

		}

		class YDataDependentSettingPanel extends
				CVAbstractDataDependentSettingPanel {
			private static final long serialVersionUID = 1L;

			public YDataDependentSettingPanel(CVChartController container,
					CVDataSet data) {
				super(container, data);
			}

			public CVDataSet getDependent() {
				return getData().getDependentY();
			}

			public void setDependent(CVDataSet data) {
				getData().setDependentY(data);
			}

			public List<CVDataSet> getDependentList(CVDataSet data) {
				return data.getDependentYList();
			}

			public boolean isDependent() {
				return getData().getDependentY() != null;
			}

			public void updated() {
				for (CVAbstractDataDependentSettingPanel panel : yDependentSettingPanels) {
					panel.update();
				}
				container.refreshViews();
			}
		}

	}
}

class CenterLayout implements LayoutManager, Serializable {
	private static final long serialVersionUID = 1L;

	public void addLayoutComponent(String name, Component comp) {

	}

	public void removeLayoutComponent(Component comp) {

	}

	public Dimension preferredLayoutSize(Container container) {

		Component c = container.getComponent(0);
		if (c != null) {
			Dimension size = c.getPreferredSize();
			Insets insets = container.getInsets();

			return new Dimension(size.width + insets.left + insets.right,
					size.height + insets.top + insets.bottom);
		} else {
			return new Dimension(0, 0);
		}
	}

	public Dimension minimumLayoutSize(Container cont) {

		return preferredLayoutSize(cont);
	}

	public void layoutContainer(Container container) {

		if (container.getComponentCount() > 0) {
			Component c = container.getComponent(0);
			Dimension pref = c.getPreferredSize();
			int containerWidth = container.getWidth();
			int containerHeight = container.getHeight();
			Insets containerInsets = container.getInsets();

			containerWidth -= containerInsets.left + containerInsets.right;
			containerHeight -= containerInsets.top + containerInsets.bottom;

			int left = (containerWidth - pref.width) / 2 + containerInsets.left;
			int right = (containerHeight - pref.height) / 2
					+ containerInsets.top;

			c.setBounds(left, right, pref.width, pref.height);
		}
	}
}