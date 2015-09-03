/*
 * CVChartDataSetListPanel.java
 * Created on 2004/05/07
 * 
 * Copyright (c) 2005 PlatBox Project. All rights reserved.
 */
package clib.view.chart.viewer.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import clib.view.chart.controller.CVChartController;
import clib.view.chart.viewer.model.CVDataSet;
import clib.view.chart.viewer.resource.ChartResource;
import clib.view.chart.viewer.view.dialogs.CVDataSettingDialogPanel;
import clib.view.list.CListPanel;

/**
 * Class CVChartDataSetListPanel.
 * 
 * @author macchan
 * @version $Id: CVChartDataSetListPanel.java,v 1.1 2005/03/08 03:04:44 bam Exp
 *          $
 */
public class CVChartDataSetListPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	// Relation
	private CVChartController controller;

	// Variables
	// private JList list = new JList();
	private CListPanel<CVDataSet> listPanel = new CListPanel<CVDataSet>();

	private JButton button = new JButton(
			ChartResource.get("CVChartDataSetListPanel.Setting")); //$NON-NLS-1$

	/**
	 * Constructor for CVChartDataSetListPanel.
	 */
	public CVChartDataSetListPanel() {
		initialize();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initialize() {
		setLayout(new BorderLayout());

		JList list = listPanel.getJList();
		list.setSelectionBackground(Color.white);
		list.setCellRenderer(new DataListCellRenderer());
		list.setBackground(Color.white);
		list.addListSelectionListener(new ListSelectionListener() {
			boolean selecting = false;

			public void valueChanged(ListSelectionEvent e) {
				if (controller == null) {
					return;
				}
				if (e.getValueIsAdjusting()) {
					return;
				}
				if (selecting) {
					return;
				}
				selecting = true;
				controller.setDataSelection(listPanel.getSelectedElements());
				selecting = false;
			}
		});

		JScrollPane scrollpane = new JScrollPane();
		scrollpane.setViewportView(list);

		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showDialog();
			}
		});

		add(scrollpane, BorderLayout.CENTER);
		add(button, BorderLayout.SOUTH);
	}

	public void setContainer(CVChartController container) {
		this.controller = container;
		refreshView();
	}

	public void refreshView() {
		listPanel.setList(controller.getChart().getDataSets());
		repaint();
	}

	private void showDialog() {
		CVDataSettingDialogPanel panel = new CVDataSettingDialogPanel(
				controller);
		JScrollPane scroll = new JScrollPane(panel);
		scroll.setPreferredSize(new Dimension(600, 600));
		JOptionPane.showConfirmDialog(controller.getOwner(), scroll,
				ChartResource.get("CVChartDataSetListPanel.Data_Setting"), //$NON-NLS-1$
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null);
	}

	@SuppressWarnings("rawtypes")
	class DataListCellRenderer implements ListCellRenderer {
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {

			CVDataSet data = (CVDataSet) value;

			BufferedImage buf = new BufferedImage(30, 30,
					BufferedImage.TYPE_4BYTE_ABGR);
			Graphics g = buf.createGraphics();
			g.setColor(data.getColor());
			g.drawLine(5, 15, 25, 15);
			g.dispose();

			JLabel label = new JLabel();
			label.setIcon(new ImageIcon(buf));
			// label.setForeground(data.getColor());
			label.setText(data.getModel().getLabel());

			if (data.isSelected()) {
				label.setOpaque(true);
				label.setBackground(Color.LIGHT_GRAY);
			}

			return label;
		}
	}

}