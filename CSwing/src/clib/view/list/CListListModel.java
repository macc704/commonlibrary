/*
 * CListListModel.java
 * Copyright (c) 2002 Boxed-Economy Project.  All rights reserved.
 */
package clib.view.list;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;

/**
 * @author macchan
 * @version $Id: CListListModel.java,v 1.1 2004/03/21 12:08:08 macchan Exp $
 */
@SuppressWarnings("rawtypes")
public class CListListModel<T> extends AbstractListModel implements
		ICListModel<T> {

	private static final long serialVersionUID = 1L;

	/*********************************
	 * Instance Variables
	 *********************************/

	private List<T> list = new ArrayList<T>();

	/*********************************
	 * Constructors
	 *********************************/

	/**
	 * Constructor for CListListModel.
	 */
	public CListListModel() {
		this(new ArrayList<T>());
	}

	/**
	 * Constructor for CListListModel.
	 */
	public CListListModel(List<T> list) {
		this.setList(list);
	}

	/*********************************
	 * implements for CListListModel
	 *********************************/

	/**
	 * @see javax.swing.ListListModel#getElementAt(int)
	 */
	public Object getElementAt(int index) {
		return this.list.get(index);
	}

	/**
	 * @see javax.swing.ListListModel#getSize()
	 */
	public int getSize() {
		return this.list.size();
	}

	/*********************************
	 * updater
	 *********************************/

	/**
	 * update this model's view
	 */
	public void update() {
		this.fireContentsChanged(this, 0, 100);
	}

	/*********************************
	 * getter & setter
	 *********************************/

	/**
	 * Returns the model.
	 * 
	 * @return List
	 */
	public List<T> getList() {
		return list;
	}

	/**
	 * Sets the model.
	 * 
	 * @param model
	 *            The model to set
	 */
	public void setList(List<T> model) {
		this.list = model;
		update();
	}

}
