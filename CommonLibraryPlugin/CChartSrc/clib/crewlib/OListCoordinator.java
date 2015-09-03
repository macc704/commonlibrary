/*
 * OListCoordinator.java
 * Created on 2003/10/10
 * 
 * Copyright (c) 2005 PlatBox Project. All rights reserved.
 */
package clib.crewlib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class OListCoordinator.
 * 
 * @author macchan
 * @version $Id: OListCoordinator.java,v 1.2 2005/03/08 03:05:06 bam Exp $
 */
public class OListCoordinator<T> {

	private OValueChangeListener listener;
	private List<T> list = new ArrayList<T>();

	/**
	 * Constructor for OListCoordinator.
	 */
	public OListCoordinator(OValueChangeListener listener) {
		this.listener = listener;
	}

	@SuppressWarnings("unchecked")
	public void setList(List<T> newList) {

		// Argument checks.
		if (newList == null) {
			newList = Collections.EMPTY_LIST;
		}

		// Checks reorderd or added list.
		int size = newList.size();
		for (int i = 0; i < size; i++) {
			Object newValue = newList.get(i);

			if (i < list.size() && list.get(i) == newValue) {
				// do nothing
			} else if (list.contains(newValue)) {
				listener.valueReorderd(newValue, i);
			} else {
				listener.valueAdded(newValue, i);
			}
		}

		// Checks removed list.
		list.removeAll(newList);
		for (T removedValue : list) {
			listener.valueRemoved(removedValue);
		}

		// Refresh list.
		this.list = new ArrayList<T>(newList);
	}

	/**
	 * Gets the list.
	 * 
	 * @return
	 */
	public List<T> getList() {
		return new ArrayList<T>(list);
	}

}