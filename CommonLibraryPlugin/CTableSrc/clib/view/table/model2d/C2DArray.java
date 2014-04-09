/*
 * C2DArray.java
 * Created on Sep 26, 2010 
 * Copyright(c) 2010 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.table.model2d;

import java.util.HashMap;
import java.util.Map;

/**
 * @author macchan
 */
public class C2DArray<T> {

	private Map<String, T> data = new HashMap<String, T>();

	public C2DArray() {
	}

	public C2DArray(C2DArray<T> another) {
		this.data = new HashMap<String, T>(another.data);
	}

	public void set(int row, int col, T value) {
		data.put(key(row, col), value);
	}

	public T get(int row, int col) {
		String key = key(row, col);
		if (data.containsKey(key)) {
			return data.get(key);
		}
		return null;
	}

	private String key(int row, int col) {
		return row + "," + col;
	}

}
