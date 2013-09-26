/*
 * CCellEditorFactory.java
 * Created on 2012/05/15
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package cswing.table.model;

import java.util.List;

/**
 * @author macchan
 */
public interface ICEditableFieldProvider<T> {

	public T getModel();

	public Class<T> getModelClass();

	public List<ICEditableField> getVariables();

}
