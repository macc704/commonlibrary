/*
 * CDefaultTableModel.java
 * Created on 2012/05/15
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package cswing.table.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import clib.common.utils.CReflectionUtils;

/**
 * @author macchan
 * 
 */
public abstract class CAbstractEditableFieldProvider<T> implements
		ICEditableFieldProvider<T> {

	private T model;
	private List<ICEditableField> variables = new ArrayList<ICEditableField>();

	/**
	 * 
	 */
	public CAbstractEditableFieldProvider(T model, String... variableNames) {
		this.model = model;
		initialize();
	}

	protected void initialize() {
	}

	protected void add(ICEditableField variable) {
		variables.add(variable);
	}

	protected Field getField(String name) {
		return privateGetField(getModelClass(), name);
	}

	private Field privateGetField(Class<?> clazz, String name) {
		try {
			return clazz.getDeclaredField(name);
		} catch (NoSuchFieldException ex) {
			Class<?> superClass = clazz.getSuperclass();
			if (superClass != null) {
				return privateGetField(superClass, name);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	protected ICEditableField createDefault(String name, Field field) {
		Class<?> type = CReflectionUtils.toObjectClass(field.getType());
		if (type == String.class) {
			return new CStringEditableField(name, field, model);
		} else if (Number.class.isAssignableFrom(type)) {
			return new CNumberEditableField(name, field, model);
		} else {
			return new CNullEditableField(name, field, model);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see test.ICTableModel#getModelClass()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Class<T> getModelClass() {
		return (Class<T>) model.getClass();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see test.ICTableModel#getModel()
	 */
	@Override
	public T getModel() {
		return model;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see test.ICTableModel#getVariables()
	 */
	@Override
	public List<ICEditableField> getVariables() {
		return variables;
	}
}
