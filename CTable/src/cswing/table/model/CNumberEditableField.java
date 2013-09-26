/*
 * CNumberCellEditor.java
 * Created on 2012/05/15
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package cswing.table.model;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;

/**
 * @author macchan
 * 
 */
public class CNumberEditableField extends CAbstractFieldEditableField {

	private static final NumberFormat NUMBER_FORMAT = new DecimalFormat();

	private JFormattedTextField editor = new JFormattedTextField(NUMBER_FORMAT);

	/**
	 * 
	 */
	public CNumberEditableField(String name, Field field, Object model) {
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
		Number value = (Number) getValue();
		if (value != null) {
			editor.setText(value.toString());
		} else {
			editor.setText("0");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see test.ICEditableField#updateFromViewToModel()
	 */
	@Override
	public void updateFromViewToModel() {
		try {
			String text = editor.getText();
			Number number = NUMBER_FORMAT.parse(text);
			if (number.getClass() == Long.class) {
				number = (Integer) number.intValue();
			}
			setValue(number);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
