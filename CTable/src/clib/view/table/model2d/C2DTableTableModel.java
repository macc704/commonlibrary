/*
 * C2DTableTableModel.java
 * Created on Sep 26, 2010 
 * Copyright(c) 2010 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.table.model2d;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 * @author macchan
 * 
 */
public class C2DTableTableModel implements TableModel {

	private C2DTable<? extends Object> table;

	public C2DTableTableModel(C2DTable<? extends Object> table) {
		this.table = table;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return Object.class;
	}

	@Override
	public int getColumnCount() {
		return table.getColCount() + 1;
	}

	@Override
	public String getColumnName(int columnIndex) {
		if (columnIndex == 0) {
			return "";
		}
		return table.getColkeys().get(columnIndex - 1).toString();
	}

	@Override
	public int getRowCount() {
		return table.getRowCount();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex == 0) {
			return table.getRowkeys().get(rowIndex);
		}
		return table.get(rowIndex, columnIndex - 1);
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
	}

}
