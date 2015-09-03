/*
 * CProviderTableModelDescriptor.java
 * Created on 2012/05/15
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package cswing.table.model;

import java.util.List;

import clib.view.table.model.ICTableModelDescripter;

/**
 * @author macchan
 * 
 */
public class CProviderTableModelDescriptor<T> implements
		ICTableModelDescripter<T> {

	private List<ICEditableField> fields;

	/**
	 * 
	 */
	public CProviderTableModelDescriptor(ICEditableFieldProvider<T> provider) {
		this.fields = provider.getVariables();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see clib.view.table.model.ICTableModelDescripter#getValiableClass(int)
	 */
	@Override
	public Class<?> getValiableClass(int index) {
		return fields.get(index).getType();
	}

	public Object getVariableAt(T model, int index) {
		return fields.get(index).getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see clib.view.table.model.ICTableModelDescripter#getVariableCount()
	 */
	@Override
	public int getVariableCount() {
		return fields.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see clib.view.table.model.ICTableModelDescripter#getVariableName(int)
	 */
	@Override
	public String getVariableName(int index) {
		return fields.get(index).getName();
	}
}
