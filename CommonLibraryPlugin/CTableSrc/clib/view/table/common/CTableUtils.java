/*
 * CTableUtils.java
 * Created on Apr 20, 2011 
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.table.common;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

/**
 * @author macchan
 * 
 */
public class CTableUtils {

	public static List<Integer> getSelectedModelRows(JTable table) {
		List<Integer> rows = new ArrayList<Integer>();
		int[] sortedRows = table.getSelectedRows();
		for (int i = 0; i < sortedRows.length; i++) {
			int row = table.getRowSorter()
					.convertRowIndexToModel(sortedRows[i]);
			rows.add(row);
		}
		return rows;
	}

	public static <T> List<T> getSelectedModels(List<T> models,
			List<Integer> rows) {
		List<T> selected = new ArrayList<T>();
		for (int i = 0; i < rows.size(); i++) {
			selected.add(models.get(rows.get(i)));
		}
		return selected;
	}
}
