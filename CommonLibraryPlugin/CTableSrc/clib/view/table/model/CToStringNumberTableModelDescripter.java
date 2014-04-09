/*
 * CToStringNumberTableModelDescripter.java
 * Created on May 2, 2011 
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.table.model;

/**
 * @author macchan
 * 
 */
public class CToStringNumberTableModelDescripter<T> implements
		ICTableModelDescripter<T> {

	public int getVariableCount() {
		return 1;
	}

	public Class<?> getValiableClass(int index) {
		return Long.class;
	}

	public Object getVariableAt(T model, int index) {
		return Long.parseLong(model.toString());
	}

	public String getVariableName(int index) {
		return "ID";
	}
}
