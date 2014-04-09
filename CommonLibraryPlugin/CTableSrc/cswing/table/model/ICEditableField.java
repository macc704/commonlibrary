/*
 * ITCellEditor.java
 * Created on 2012/05/15
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package cswing.table.model;

import javax.swing.JComponent;

/**
 * @author macchan
 * 
 */
public interface ICEditableField {

	public String getName();

	public JComponent getEditor();

	public Class<?> getType();

	public void setValue(Object value);

	public Object getValue();

	public void updateFromModelToView();

	public void updateFromViewToModel();
}
