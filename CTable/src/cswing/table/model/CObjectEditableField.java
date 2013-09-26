/*
 * ObjectField.java
 * Created on 2012/05/15
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package cswing.table.model;

import java.lang.reflect.Field;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;

/**
 * @author macchan
 * 
 */
@SuppressWarnings("rawtypes")
public class CObjectEditableField extends CAbstractFieldEditableField {

	private static final Object NULL = new Object() {
		public String toString() {
			return "(null)";
		}
	};

	private JComboBox editor = new JComboBox();

	private List items;

	@SuppressWarnings("unchecked")
	public CObjectEditableField(String name, Field field, Object model,
			List items) {
		super(name, field, model);

		this.items = items;
		editor.addItem(NULL);
		for (Object item : items) {
			editor.addItem(item);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see test.ICEditableField#getEditor()
	 */
	@Override
	public JComponent getEditor() {
		return editor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see test.ICEditableField#updateFromModelToView()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void updateFromModelToView() {
		Object value = getValue();
		if (value == null) {
			editor.setSelectedItem(NULL);
			return;
		}
		if (!items.contains(value)) {
			editor.addItem(value);
			items.add(value);
		}
		editor.setSelectedItem(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see test.ICEditableField#updateFromViewToModel()
	 */
	@Override
	public void updateFromViewToModel() {
		Object value = editor.getSelectedItem();
		if (value == NULL) {
			setValue(null);
			return;
		}
		setValue(value);
	}
}
