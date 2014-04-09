/*
 * ICTableModelDescriptor.java
 * Created on Jun 27, 2010 
 * Copyright(c) 2010 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.table.model;

public interface ICTableModelDescripter<T> {

	public int getVariableCount();

	public Object getVariableAt(T model, int index);

	public Class<?> getValiableClass(int index);

	public String getVariableName(int index);

}
