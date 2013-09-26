/*
 * CTableRecorderTableModel.java
 * Created on Jul 15, 2010 
 * Copyright(c) 2010 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.table.record;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import clib.common.model.ICModelChangeListener;

/**
 * @author macchan
 */
public class CTableRecorderTableModel extends AbstractTableModel implements
		ICModelChangeListener {

	private static final long serialVersionUID = 1L;

	private CTableRecorder recorder;
	private List<CellReference> references = new ArrayList<CellReference>();

	private boolean hooked = false;

	public CTableRecorderTableModel(CTableRecorder recorder) {
		this.recorder = recorder;
		hook();
	}

	public synchronized void hook() {
		if (hooked == false) {
			recorder.addModelListener(this);
			hooked = true;
		}
	}

	public synchronized void unhook() {
		if (hooked) {
			recorder.removeModelListener(this);
			hooked = false;
		}
	}

	public void modelUpdated(Object... args) {
		this.fireTableDataChanged();
	}

	public String getColumnName(int columnIndex) {
		if (columnIndex <= 0) {
			return "Tick";
		}

		CellReference pointer = references.get(columnIndex - 1);
		return pointer.name;
	}

	public int getColumnCount() {
		return references.size() + 1;
	}

	public int getRowCount() {
		return recorder.getTick() + 1;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex <= 0) {
			return rowIndex;
		}

		CellReference pointer = references.get(columnIndex - 1);
		List<Record> history = recorder.getHistory(pointer.row);

		if (history.size() <= 0) {
			return 0d;
		}

		int startTick = history.get(0).tick;

		if (rowIndex < startTick) {
			return 0d;
		}

		Record h = history.get(rowIndex - startTick);
		return h.columns.get(pointer.column);
	}

	/**
	 * @param p1
	 * @param column
	 */
	public void addCellReference(String name, String unit, int row, int column) {
		// System.out.println(row + "," + column);
		this.references.add(new CellReference(name, unit, row, column));
		this.fireTableStructureChanged();
	}

	class CellReference {
		String name;
		String unit;
		int row;
		int column;

		public CellReference(String name, String unit, int row, int column) {
			this.name = name;
			this.unit = unit;
			this.row = row;
			this.column = column;
		}
	}

}
