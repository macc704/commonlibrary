/*
 * CDefaultTableModelFactory.java
 * Created on 2012/05/15
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package cswing.table.model;

import clib.view.table.model.CFieldRefrectionTableModelDescripter;
import clib.view.table.model.ICTableModelDescripter;

/**
 * @author macchan
 * 
 */
public class CDefaultTableElementMetaModel<T> implements
		ICTableElementEditorDescripter<T> {

	private String[] vairableNames;
	private Class<T> clazz;

	public CDefaultTableElementMetaModel(Class<T> clazz,
			String... variableNames) {
		this.vairableNames = variableNames;
		this.clazz = clazz;
	}

	public T newInstance() {
		try {
			return clazz.newInstance();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public String getName() {
		return clazz.getSimpleName();
	}

	public ICTableModelDescripter<T> getDescripter() {
		return new CFieldRefrectionTableModelDescripter<T>(clazz, vairableNames);
	}

	public ICEditableFieldProvider<T> createEditableFieldProvider(T model) {
		return new CDefaultEditableFieldProvider<T>(model, vairableNames);
	}
}
