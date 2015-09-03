/*
 * CTableRecorder.java
 * Created on Jul 15, 2010 
 * Copyright(c) 2010 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.table.record;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import clib.common.model.CAbstractModelObject;

/**
 * @author macchan
 */
public class CTableRecorder extends CAbstractModelObject {

	private static final long serialVersionUID = 1L;

	private TableModel target;
	private int columnCount;
	private int tick;
	private Map<Integer, List<Record>> allRecords = new LinkedHashMap<Integer, List<Record>>();

	public CTableRecorder() {
		setModel(new DefaultTableModel());
	}

	public CTableRecorder(TableModel target) {
		setModel(target);
	}

	public void setModel(TableModel target) {
		this.target = target;
		clear();
	}

	public void clear() {
		allRecords.clear();
		columnCount = target.getColumnCount();
		tick = -1;
		// tick();
	}

	public void tick() {
		tick++;

		int rowCount = target.getRowCount();
		for (int row = 0; row < rowCount; row++) {
			record(tick, row, getColumns(row));
		}

		this.fireModelUpdated();
	}

	private void record(int tick, int row, List<Object> columns) {
		List<Record> rowRecords;
		if (allRecords.containsKey(row)) {
			rowRecords = allRecords.get(row);
		} else {
			rowRecords = new ArrayList<Record>();
			allRecords.put(row, rowRecords);
		}
		rowRecords.add(new Record(tick, columns));
	}

	private List<Object> getColumns(int row) {
		List<Object> columns = new ArrayList<Object>();
		for (int column = 0; column < columnCount; column++) {
			columns.add(target.getValueAt(row, column));
		}
		return columns;
	}

	public int getTick() {
		return tick;
	}

	public List<Record> getHistory(int index) {
		if (!allRecords.containsKey(index)) {
			return Collections.emptyList();
		}
		return allRecords.get(index);
	}

}

class Record {
	int tick;
	List<Object> columns;

	Record(int tick, List<Object> columns) {
		this.tick = tick;
		this.columns = columns;
	}
}
