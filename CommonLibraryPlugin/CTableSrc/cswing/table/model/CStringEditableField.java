/*
 * CStringEditableField.java
 * Created on 2012/05/15
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package cswing.table.model;

import java.lang.reflect.Field;

import javax.swing.JComponent;
import javax.swing.JTextField;

/**
 * @author macchan
 * 
 */
public class CStringEditableField extends CAbstractFieldEditableField {

	private JTextField editor = new JTextField();

	/**
	 * 
	 */
	public CStringEditableField(String name, Field field, Object model) {
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
		String value = (String) getValue();
		if (value != null) {
			editor.setText(value);
		} else {
			editor.setText("");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see test.ICEditableField#updateFromViewToModel()
	 */
	@Override
	public void updateFromViewToModel() {
		String text = editor.getText();
		setValue(text);
	}
}
