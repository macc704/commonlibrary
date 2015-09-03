/*
 * CFreeTable.java
 * Created on 2012/05/15
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package cswing.table.view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPopupMenu;

import clib.common.collections.CObservableList;
import clib.common.model.ICModelObject;
import clib.common.thread.ICTask;
import clib.view.actions.CAction;
import clib.view.actions.CActionUtils;
import clib.view.common.CSwingUtils;
import clib.view.table.common.CCommonTablePanel;
import clib.view.table.model.CListTableModel;
import cswing.table.model.ICTableElementEditorDescripter;

/**
 * @author macchan
 * 
 */
public class CElementEditableTablePanel<T> extends CCommonTablePanel {

	private static final long serialVersionUID = 1L;

	private ICTableElementEditorDescripter<T> metaModel;
	private List<T> models = new CObservableList<T>();
	private CListTableModel<T> tableModel;

	private int labelwidth = 50;
	private int textlen = 15;

	private List<ICElementEditableTableListener<T>> listeners = new ArrayList<ICElementEditableTableListener<T>>();

	public CElementEditableTablePanel(
			ICTableElementEditorDescripter<T> metaModel) {
		this.metaModel = metaModel;
		initialize();
	}

	private void initialize() {
		getTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.isMetaDown()) {
					JPopupMenu menu = createPopup();
					menu.show(e.getComponent(), e.getX(), e.getY());
				}
				if (!e.isMetaDown() && e.getClickCount() == 2) {
					modifySelected();
				}
			}

		});
		getScroll().addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.isMetaDown()) {
					JPopupMenu menu = createPopup();
					menu.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});
		this.tableModel = new CListTableModel<T>(models,
				metaModel.getDescripter());
		getTable().setModel(this.tableModel);

		getTable().setColumnSelectionAllowed(false);
	}

	protected JPopupMenu createPopup() {
		JPopupMenu menu = new JPopupMenu();
		{
			int count = getSelectedCount();
			if (count == 1) {
				menu.add(modifyAction);
			}
			if (count > 0) {
				menu.add(removeAction);
			}
			menu.add(addAction);
		}
		return menu;
	}

	public CAction addAction = CActionUtils.createAction("Add", new ICTask() {
		@Override
		public void doTask() {
			try {
				doAdd();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	});

	public CAction modifyAction = CActionUtils.createAction("Modify",
			new ICTask() {
				@Override
				public void doTask() {
					try {
						modifySelected();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			});

	public CAction removeAction = CActionUtils.createAction("Remove",
			new ICTask() {
				@Override
				public void doTask() {
					try {
						doDelete();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			});

	public void doAdd() {
		T newInstance = metaModel.newInstance();
		if (newInstance == null) {
			return;
		}
		boolean ok = privateModify(newInstance, "Add");
		if (!ok) {
			return;
		}

		models.add(newInstance);
		fireTaskAdded(newInstance);
	}

	private void modifySelected() {
		List<T> selectedList = getSelectedModels(models);
		if (selectedList.size() == 1) {
			modify(selectedList.get(0));
		}
	}

	public void modify(T model) {
		boolean done = privateModify(model, "Modify");
		if (done) {
			tableModel.fireTableDataChanged();
			// @TODO reflection•ûŽ®‚ÅŽÀŽ{‚µ‚Ä‚¢‚é‚½‚ß
			if (model instanceof ICModelObject) {
				((ICModelObject) model).fireModelUpdated();
			}
		}
	}

	private boolean privateModify(T model, String title) {
		CTableElementEditPanel<T> panel = new CTableElementEditPanel<T>(
				metaModel.createEditableFieldProvider(model),
				metaModel.getName(), labelwidth, textlen);
		CTableElementEditDialog dialog = new CTableElementEditDialog(
				CSwingUtils.getFrame(this), title, panel);
		dialog.pack();
		dialog.open();
		return dialog.isOk();
	}

	private void doDelete() {
		List<T> copy = new ArrayList<T>(getSelectedModels(models));
		for (T selected : copy) {
			doRemove(selected);
		}
	}

	public void doRemove(T model) {
		models.remove(model);
		fireTaskRemoved(model);
	}

	public List<T> getSelectedModels() {
		return getSelectedModels(models);
	}

	/**
	 * @return the models
	 */
	public List<T> getModels() {
		return models;
	}

	/**
	 * @return the models
	 */
	public void setModels(List<T> newModels) {
		models.clear();
		fireTaskRemoved(null);
		for (T model : newModels) {
			models.add(model);
		}
		fireTaskAdded(null);
	}

	/**
	 * @param labelwidth
	 *            the labelwidth to set
	 */
	public void setLabelwidth(int labelwidth) {
		this.labelwidth = labelwidth;
	}

	/**
	 * @param textlen
	 *            the textlen to set
	 */
	public void setTextlen(int textlen) {
		this.textlen = textlen;
	}

	/**
	 * @return the tableModel
	 */
	public CListTableModel<T> getTableModel() {
		return tableModel;
	}

	public void addElementEditableTableListener(
			ICElementEditableTableListener<T> listener) {
		listeners.add(listener);
	}

	public void removeElementEditableTableListener(
			ICElementEditableTableListener<T> listener) {
		listeners.remove(listener);
	}

	protected void fireTaskAdded(T object) {
		for (ICElementEditableTableListener<T> listener : listeners) {
			listener.elementAdded(object);
		}
	}

	protected void fireTaskRemoved(T object) {
		for (ICElementEditableTableListener<T> listener : listeners) {
			listener.elementRemoved(object);
		}
	}

}
