/*
 * ICTableModelFactory.java
 * Created on 2012/05/15
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package cswing.table.model;

import clib.view.table.model.ICTableModelDescripter;

/**
 * @author macchan
 * 
 */
public interface ICTableElementEditorDescripter<T> {

	public String getName();

	public T newInstance();

	public ICTableModelDescripter<T> getDescripter();

	public ICEditableFieldProvider<T> createEditableFieldProvider(T model);
}
