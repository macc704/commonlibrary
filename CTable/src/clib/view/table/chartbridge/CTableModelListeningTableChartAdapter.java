/*
 * CTableModelListeningTableChartAdapter.java
 * Created on Jul 15, 2010 
 * Copyright(c) 2010 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.table.chartbridge;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import clib.view.chart.controller.CVChartController;
import clib.view.chart.model.CAxis;
import clib.view.chart.model.CDefaultDataSet;
import clib.view.chart.model.CUnit;

/**
 * @author macchan
 */
public class CTableModelListeningTableChartAdapter implements
		TableModelListener {

	private TableModel model;
	private CVChartController controller;
	private String unitName;

	private boolean active = false;
	private boolean autoRefresh = false;

	public CTableModelListeningTableChartAdapter(TableModel model,
			CVChartController controller, String unitName) {
		this.model = model;
		this.controller = controller;
		this.unitName = unitName;
	}

	public boolean isAutoRefresh() {
		return autoRefresh;
	}

	public void setAutoRefresh(boolean autoRefresh) {
		this.autoRefresh = autoRefresh;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		boolean oldValue = this.active;
		this.active = active;
		if (oldValue == false && active == true) {
			model.addTableModelListener(this);
			refreshChartDataFromModel();
		} else if (oldValue == true && active == false) {
			model.removeTableModelListener(this);
		}
	}

	public void tableChanged(TableModelEvent e) {
		if (e.getColumn() == -1 && e.getFirstRow() == -1
				&& e.getLastRow() == -1) {// structure changed
			refreshChartDataFromModel();
		} else if (autoRefresh) {
			refreshChartDataFromModel();
		}
	}

	public void refreshChartDataFromModel() {
		if (!active) {
			return;
		}
		if (model == null || model.getRowCount() < 1) {
			return;
		}

		controller.getChart().clear();

		CUnit unitX = new CUnit(model.getColumnName(0));
		CAxis axisX = new CAxis("X", unitX);

		int rowCount = model.getRowCount();
		int columnCount = model.getColumnCount();
		for (int i = 1; i < columnCount; i++) {
			String name = model.getColumnName(i);
			CUnit unitY = new CUnit(this.unitName);
			CAxis axisY = new CAxis("Y", unitY);
			CDefaultDataSet ds = new CDefaultDataSet(name, axisX, axisY, null);

			for (int j = 0; j < rowCount; j++) {
				ds.addPoint(getDoubleValueAt(j, 0), getDoubleValueAt(j, i));
			}
			controller.getChart().addData(ds);
		}

		controller.refreshViews();
	}

	private double getDoubleValueAt(int row, int column) {
		try {
			Object o = model.getValueAt(row, column);
			Double d = Double.parseDouble(o.toString());
			if (d.equals(Double.NaN)) {
				return 0d;
			}
			return d;
		} catch (Exception ex) {
			return 0;
		}
	}

}
