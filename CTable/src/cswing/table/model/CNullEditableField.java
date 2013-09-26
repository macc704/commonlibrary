/*
 * CStringEditableField.java
 * Created on 2012/05/15
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package cswing.table.model;

import java.lang.reflect.Field;

import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 * @author macchan
 * 
 */
public class CNullEditableField extends CAbstractFieldEditableField {

	private JLabel editor = new JLabel();

	/**
	 * 
	 */
	public CNullEditableField(String name, Field field, Object model) {
		super(name, field, model);

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
	@Override
	public void updateFromModelToView() {
		Object value = getValue();
		if (value != null) {
			editor.setText(value.toString());
		} else {
			editor.setText("(null)");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see test.ICEditableField#updateFromViewToModel()
	 */
	@Override
	public void updateFromViewToModel() {
	}
}
