/*
 * ICListTableModel.java
 * Created on Jul 15, 2010 
 * Copyright(c) 2010 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.table.model;

import java.util.List;

import javax.swing.table.TableModel;

import clib.common.model.ICModelChangeListener;

/**
 * @author macchan
 * 
 */
public interface ICListTableModel<T> extends TableModel, ICModelChangeListener {

	public T getModel(int rowIndex);

	public List<T> getList();

	public void fireTableDataChanged();

}
