/*
 * CDecoratableListTableModel.java
 * Created on Jun 27, 2010 
 * Copyright(c) 2010 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.table.model;

import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import clib.common.collections.CObservableList;
import clib.common.model.ICModelChangeListener;

public class CDecoratableListTableModel<T> implements TableModel,
		ICModelChangeListener, ICListTableModel<T> {

	private CListTableModel<T> delegate;
	private CObservableList<? extends ICTableDataDecorator<T>> decorators;

	public CDecoratableListTableModel(CListTableModel<T> delegate,
			CObservableList<? extends ICTableDataDecorator<T>> decorators) {
		this.delegate = delegate;
		this.decorators = decorators;
		decorators.addModelListener(this);
	}

	public void modelUpdated(Object... args) {
		this.delegate.modelUpdated(args);
	}

	public void fireTableDataChanged() {
		this.delegate.fireTableDataChanged();
	}

	public void addTableModelListener(TableModelListener l) {
		this.delegate.addTableModelListener(l);
	}

	public void removeTableModelListener(TableModelListener l) {
		this.delegate.removeTableModelListener(l);
	}

	public Class<?> getColumnClass(int columnIndex) {
		int relativeIndex = columnIndex - delegate.getColumnCount();
		if (relativeIndex < 0) {
			return delegate.getColumnClass(columnIndex);
		}
		return this.decorators.get(relativeIndex).getValueType();
	}

	public int getColumnCount() {
		return delegate.getColumnCount() + this.decorators.size();
	}

	public String getColumnName(int columnIndex) {
		int relativeIndex = columnIndex - delegate.getColumnCount();
		if (relativeIndex < 0) {
			return delegate.getColumnName(columnIndex);
		}
		return this.decorators.get(relativeIndex).getValueName();
	}

	public int getRowCount() {
		return delegate.getRowCount();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		int relativeIndex = columnIndex - delegate.getColumnCount();
		if (relativeIndex < 0) {
			return delegate.getValueAt(rowIndex, columnIndex);
		}
		return this.decorators.get(relativeIndex).getValueAt(
				delegate.getModel(rowIndex));
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return this.delegate.isCellEditable(rowIndex, columnIndex);
	}

	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		int relativeIndex = columnIndex - delegate.getColumnCount();
		if (relativeIndex < 0) {
			delegate.setValueAt(aValue, rowIndex, columnIndex);
		}
	}

	public T getModel(int rowIndex) {
		return this.delegate.getModel(rowIndex);
	}

	public List<T> getList() {
		return this.delegate.getList();
	}

}
