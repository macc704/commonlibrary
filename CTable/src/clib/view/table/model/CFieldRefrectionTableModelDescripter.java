/*
 * CFieldRefrectionTableModelDescripter.java
 * Created on Jun 27, 2010 
 * Copyright(c) 2010 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.table.model;

import java.lang.reflect.Field;

import clib.common.utils.CReflectionUtils;

public class CFieldRefrectionTableModelDescripter<T> implements
		ICTableModelDescripter<T> {

	private Class<T> c;
	private String[] variableNames;

	public CFieldRefrectionTableModelDescripter(Class<T> c,
			String... variableNames) {
		this.c = c;
		if (variableNames.length > 0) {
			this.variableNames = variableNames;
		} else {
			Field[] fields = c.getDeclaredFields();
			String[] names = new String[fields.length];
			for (int i = 0; i < fields.length; i++) {
				fields[i].setAccessible(true);
				names[i] = fields[i].getName();
			}
			this.variableNames = names;
		}
	}

	public int getVariableCount() {
		return variableNames.length;
	};

	public String getVariableName(int index) {
		return variableNames[index];
	}

	public Class<?> getValiableClass(int index) {
		try {
			Field f = c.getDeclaredField(variableNames[index]);
			f.setAccessible(true);
			Class<?> type = CReflectionUtils.toObjectClass(f.getType());
			return type;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public Object getVariableAt(T value, int index) {
		try {
			Field f = c.getDeclaredField(variableNames[index]);
			f.setAccessible(true);
			return f.get(value);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

}
