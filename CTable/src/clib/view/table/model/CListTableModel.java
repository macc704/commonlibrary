/*
 * CListTableModel.java
 * Created on Jun 27, 2010 
 * Copyright(c) 2010 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.table.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import clib.common.collections.CObservableList;
import clib.common.model.ICModelChangeListener;

public class CListTableModel<T> extends AbstractTableModel implements
		ICModelChangeListener, ICListTableModel<T> {

	private static final long serialVersionUID = 1L;

	private CObservableList<T> list;
	private ICTableModelDescripter<T> descripter;

	public CListTableModel() {
		this(new CObservableList<T>(), new CToStringTableModelDescripter<T>());
	}

	public CListTableModel(List<T> list) {
		this(list, new CToStringTableModelDescripter<T>());
	}

	public CListTableModel(List<T> list, ICTableModelDescripter<T> descripter) {
		CObservableList<T> observable;
		if (list instanceof CObservableList<?>) {
			observable = (CObservableList<T>) list;
		} else {
			observable = new CObservableList<T>(list);
		}
		setModel(observable, descripter);
	}

	public void setModel(CObservableList<T> list,
			ICTableModelDescripter<T> descripter) {
		if (this.list != null) {
			this.list.removeModelListener(this);
		}
		this.list = list;
		this.descripter = descripter;
		this.list.addModelListener(this);
		this.fireTableStructureChanged();
	}

	public void modelUpdated(Object... args) {
		this.fireTableStructureChanged();
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return this.descripter.getValiableClass(columnIndex);
	}

	@Override
	public String getColumnName(int columnIndex) {
		return this.descripter.getVariableName(columnIndex);
	}

	public int getColumnCount() {
		return descripter.getVariableCount();
	}

	public int getRowCount() {
		return this.list.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		T o = this.list.get(rowIndex);
		return this.descripter.getVariableAt(o, columnIndex);
	}

	public T getModel(int rowIndex) {
		return this.list.get(rowIndex);
	}

	public List<T> getList() {
		return new ArrayList<T>(this.list);
	}
}
