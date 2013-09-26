/*
 * ICListModel.java
 * Created on Jul 20, 2010 
 * Copyright(c) 2010 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.list;

import java.util.List;

import javax.swing.ListModel;

/**
 * @author macchan
 */
@SuppressWarnings("rawtypes")
public interface ICListModel<T> extends ListModel {
	/**
	 * update this model's view
	 */
	public void update();

	/**
	 * Returns the model.
	 * 
	 * @return List
	 */
	public List<T> getList();

	/**
	 * Sets the list.
	 * 
	 * @param list
	 *            The list to set
	 */
	public void setList(List<T> list);
}
