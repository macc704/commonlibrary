/*
 * CMapTableModel.java
 * Created on Jul 21, 2010 
 * Copyright(c) 2010 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.table.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

/**
 * @author macchan
 */
public class CMapTableModel<K, V> extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private Map<K, V> map;
	private List<String> titles;

	private ArrayList<K> cash;

	public CMapTableModel(Map<K, V> map) {
		setMap(map);
	}

	private void setMap(Map<K, V> map) {
		this.map = map;
		refresh();
	}
	
	public void setTitles(List<String> titles) {
		this.titles = titles;
	}

	@Override
	public int getColumnCount() {
		return 2;
	}	

	@Override
	public int getRowCount() {
		return map.size();
	}
	
	@Override
	public String getColumnName(int column) {
		if(titles != null && titles.size() >= 2){
			return titles.get(column);
		}
		return super.getColumnName(column);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		K key = cash.get(rowIndex); // cash‚µ‚È‚¢‚ÆŒƒ’x‚©‚Á‚½
		switch (columnIndex) {
		case 0:
			return key;
		case 1:
			return map.get(key);
		default:
			throw new RuntimeException();
		}
	}

	public void refresh() {
		cash = new ArrayList<K>(map.keySet());
		fireTableDataChanged();
	}

	
}
