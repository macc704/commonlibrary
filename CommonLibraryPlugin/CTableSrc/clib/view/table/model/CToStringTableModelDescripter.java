/*
 * CToStringTableModelDescripter.java
 * Created on Jun 27, 2010 
 * Copyright(c) 2010 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.table.model;

public class CToStringTableModelDescripter<T> implements
		ICTableModelDescripter<T> {

	public CToStringTableModelDescripter() {
	}

	public int getVariableCount() {
		return 1;
	}

	public String getVariableName(int index) {
		return "toString()";
	}

	public Class<?> getValiableClass(int index) {
		return String.class;
	}

	public Object getVariableAt(T value, int index) {
		return value.toString();
	}
}
