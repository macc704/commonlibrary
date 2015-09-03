/*
 * ICTableDataDecorator.java
 * Created on Jun 27, 2010 
 * Copyright(c) 2010 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.table.model;

public interface ICTableDataDecorator<T> {

	public Class<?> getValueType();

	public String getValueName();

	public Object getValueAt(T model);

}
