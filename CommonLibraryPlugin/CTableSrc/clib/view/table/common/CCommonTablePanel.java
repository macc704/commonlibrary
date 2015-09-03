/*
 * CCommonTablePanel.java
 * Created on Jul 15, 2010 
 * Copyright(c) 2010 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.table.common;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

/**
 * @author macchan
 */
public class CCommonTablePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JScrollPane scroll = new JScrollPane();
	private JTable table = new JTable();

	public CCommonTablePanel() {
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.setCellSelectionEnabled(true);
		table.setAutoCreateRowSorter(true);

		setLayout(new BorderLayout());
		scroll.setViewportView(table);
		add(scroll);
	}

	/**
	 * @return the scroll
	 */
	public JScrollPane getScroll() {
		return scroll;
	}

	public JTable getTable() {
		return table;
	}

	/**
	 * The results are rows for the model that means they are reversed from the
	 * ordered rows by the tablesorter.
	 */
	public List<Integer> getSelectedModelRows() {
		return CTableUtils.getSelectedModelRows(table);
	}

	/**
	 * The results are rows for the model that means they are reversed from the
	 * ordered rows by the tablesorter.
	 */
	public <T> List<T> getSelectedModels(List<T> models) {
		return CTableUtils.getSelectedModels(models, getSelectedModelRows());
	}

	public int getSelectedCount() {
		return getSelectedModelRows().size();
	}

	public boolean hasSelection() {
		return getSelectedCount() > 0;
	}

}
