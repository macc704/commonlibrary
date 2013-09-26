/*
 * C2DTable.java
 * Created on Sep 26, 2010 
 * Copyright(c) 2010 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.table.model2d;

import java.util.ArrayList;
import java.util.List;

/**
 * @author macchan
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class C2DTable<T> {

	private List<T> rowkeys = new ArrayList<T>();
	private List rowFilter;
	private List<T> colkeys = new ArrayList<T>();

	private C2DArray<T> data = new C2DArray<T>();

	public C2DTable() {
	}

	public C2DTable(C2DTable<T> another) {
		this.rowkeys = new ArrayList<T>(another.rowkeys);
		this.colkeys = new ArrayList<T>(another.colkeys);
		this.data = new C2DArray<T>(another.data);
	}

	public void set(T rowkey, T colkey, T value) {
		createIndexIfNothing(rowkey, colkey);
		data.set(rowIndex(rowkey), colIndex(colkey), value);
	}

	public void set(int row, int col, T value) {
		data.set(getFilteredRowIndex(row), col, value);
	}

	public T get(T rowkey, T colkey) {
		return data.get(rowIndex(rowkey), colIndex(colkey));
	}

	public T get(int row, int col) {
		return data.get(getFilteredRowIndex(row), col);
	}

	public int getRowCount() {
		if (rowFilter != null) {
			return rowFilter.size();
		}
		return rowkeys.size();
	}

	public int getColCount() {
		return colkeys.size();
	}

	/**
	 * @return the rowkeys
	 */
	public List<T> getRowkeys() {
		if (rowFilter == null) {
			return rowkeys;
		}
		return rowFilter;
	}

	/**
	 * @return the colkeys
	 */
	public List<T> getColkeys() {
		return colkeys;
	}

	/**
	 * @param rowFilter
	 *            the rowFilter to set
	 */
	public void setRowFilter(List rowFilter) {
		this.rowFilter = rowFilter;
	}

	public int getFilteredRowIndex(int index) {
		if (rowFilter == null) {
			return index;
		}
		return rowkeys.indexOf(rowFilter.get(index));
	}

	private void createIndexIfNothing(T rowkey, T colkey) {
		if (!rowkeys.contains(rowkey)) {
			rowkeys.add(rowkey);
		}
		if (!colkeys.contains(colkey)) {
			colkeys.add(colkey);
		}
	}

	private int rowIndex(T key) {
		return rowkeys.indexOf(key);
	}

	private int colIndex(T key) {
		return colkeys.indexOf(key);
	}
}
