/*
 * CRecordableTablePanel.java
 * Created on Jul 14, 2010 
 * Copyright(c) 2010 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.table.chartbridge;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.table.TableModel;

import clib.view.table.common.CCommonTablePanel;
import clib.view.table.model.CListTableModel;
import clib.view.table.model.ICListTableModel;
import clib.view.table.record.CTableRecorder;
import clib.view.table.record.CTableRecorderTableModel;

/**
 * @author macchan
 * @_ Degreeの並び替えができない→DescriptorのレベルでInteger->Doubleにして解決した
 * @_ resetしたときのグラフで，t=0の時の値が，前回の最終の値と等しくなってしまう→tick()のタイミング制御で解決した
 */
public abstract class CRecordableTablePanel extends CCommonTablePanel {

	private static final long serialVersionUID = 1L;

	private CTableRecorder recorder = new CTableRecorder();

	private Map<Object, CTableRecorderTableModel> recordingModels = new LinkedHashMap<Object, CTableRecorderTableModel>();

	private Action actionOpenChart;
	private Action actionOpenTable;

	public CRecordableTablePanel() {
		initialize();
		initializeActions();
	}

	private void initialize() {
		setModel(new CListTableModel<Object>());

		getTable().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				if (evt.isMetaDown()) {
					JPopupMenu menu = createPopupMenu();
					menu.show(getTable(), evt.getX(), evt.getY());
					evt.consume();
				}
			}
		});
	}

	private void initializeActions() {
		actionOpenChart = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				addSelectedCellsForRecording(createNewModelWithChart(getSelectedCulumnName()));
			}
		};
		actionOpenChart.setEnabled(true);
		actionOpenChart.putValue(Action.NAME, "Open New Chart");

		actionOpenTable = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				addSelectedCellsForRecording(createNewModelWithTable(getSelectedCulumnName()));
			}
		};
		actionOpenTable.setEnabled(true);
		actionOpenTable.putValue(Action.NAME, "Open New Table");
	}

	public void setModel(TableModel model) {
		getTable().setModel(model);
		recorder.setModel(model);
	}

	public ICListTableModel<?> getListTableModel() {
		return (ICListTableModel<?>) getTable().getModel();
	}

	// public CTableRecorder getRecorder() {
	// return recorder;
	// }

	public void clear() {
		recorder.clear();
	}

	public void tick() {
		recorder.tick();
	}

	private JPopupMenu createPopupMenu() {

		JPopupMenu menu = new JPopupMenu("");

		if (getTable().getSelectedColumnCount() != 1) {
			menu.add(new JMenuItem("Warning: Select Only One Column."));
			return menu;
		}

		menu.add(actionOpenChart);
		menu.add(actionOpenTable);

		if (recordingModels.size() > 0) {
			menu.addSeparator();
		}

		for (final Object key : recordingModels.keySet()) {
			JMenuItem item = new JMenuItem("Add Cell To: " + key);
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addSelectedCellsForRecording(recordingModels.get(key));
				}
			});
			menu.add(item);
		}

		return menu;
	}

	public CTableRecorderTableModel createNewModelWithChart(String name) {
		CTableRecorderTableModel model = new CTableRecorderTableModel(recorder);
		JComponent comp = openChartComponent(model, name);
		registerModel(comp, new CLabel("Chart: " + name), model);
		return model;
	}

	public CTableRecorderTableModel createNewModelWithTable(String name) {
		CTableRecorderTableModel model = new CTableRecorderTableModel(recorder);
		JComponent comp = openTableComponent(model, name);
		registerModel(comp, new CLabel("Table: " + name), model);
		return model;
	}

	protected void registerModel(JComponent comp, final CLabel label,
			final CTableRecorderTableModel model) {
		if (comp.isShowing()) {
			model.hook();
			recordingModels.put(label, model);
		}
		comp.addAncestorListener(new AncestorListener() {
			public void ancestorAdded(AncestorEvent event) {
				model.hook();
				recordingModels.put(label, model);
			}

			public void ancestorRemoved(AncestorEvent event) {
				model.unhook();
				recordingModels.remove(label);
			}

			public void ancestorMoved(AncestorEvent event) {
			}
		});
	}

	protected void unRegisterModel(CTableRecorderTableModel model) {
		List<Object> removeKeys = new ArrayList<Object>();
		for (Object key : recordingModels.keySet()) {
			CTableRecorderTableModel aModel = recordingModels.get(key);
			if (aModel == model) {
				removeKeys.add(key);
			}
		}
		for (Object removeKey : removeKeys) {
			recordingModels.get(removeKey);
		}
	}

	protected abstract JComponent openChartComponent(
			CTableRecorderTableModel model, String name);

	protected abstract JComponent openTableComponent(
			CTableRecorderTableModel model, String name);

	protected String getSelectedCulumnName() {
		return getListTableModel()
				.getColumnName(getTable().getSelectedColumn());
	}

	public void addSelectedCellsForRecording(CTableRecorderTableModel model) {
		addCellsForRecording(model, getTable().getSelectedColumn(),
				getSelectedModelRows());
	}

	public void addAllCellsInColumnForRecording(CTableRecorderTableModel model,
			int column) {
		List<Integer> allRows = new ArrayList<Integer>();
		int n = getTable().getRowCount();
		for (int i = 0; i < n; i++) {
			allRows.add(i);
		}
		addCellsForRecording(model, column, allRows);
	}

	public void addCellsForRecording(CTableRecorderTableModel model,
			int column, List<Integer> rows) {
		String columnName = getTable().getColumnName(column);
		for (int i = 0; i < rows.size(); i++) {
			int row = rows.get(i);
			String rowName = getListTableModel().getModel(row).toString();
			model.addCellReference(rowName, columnName, row, column);
		}
	}

	class CLabel {
		String text;

		public CLabel(String text) {
			this.text = text;
		}

		public String toString() {
			return text;
		}
	}

}
