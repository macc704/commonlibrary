/*
 * OMultipleValueMap.java
 * Created on 2003/10/10
 * 
 * Copyright (c) 2005 PlatBox Project. All rights reserved.
 */
package clib.crewlib;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Class OMultipleValueMap.
 * 
 * @author macchan
 * @version $Id: OMultipleValueMap.java,v 1.3 2005/03/08 03:05:06 bam Exp $
 */
public class OMultipleValueMap<K, V> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Map<K, List<V>> tableByKey = new LinkedHashMap<K, List<V>>();
	private Map<V, K> tableByValue = new LinkedHashMap<V, K>();
	private List<V> values = new ArrayList<V>();

	/**
	 * Constructor for OMultipleValueMap.
	 */
	public OMultipleValueMap() {
		super();
	}

	/**
	 * Puts the element.
	 * 
	 * @param key
	 * @param value
	 */
	public void put(K key, V value) {

		// Argument checks.
		if (containtsValue(value)) {
			throw new IllegalArgumentException("The element " + value
					+ " has already put");
		}

		// Puts to values List.
		values.add(value);

		// Puts to tableByValue
		tableByValue.put(value, key);

		// Puts to tableByKey
		if (tableByKey.get(key) == null) {
			tableByKey.put(key, new ArrayList<V>());
		}
		List<V> values = tableByKey.get(key);
		values.add(value);
	}

	/**
	 * Gets the element(s).
	 * 
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<V> get(K key) {

		// Gets the element(s)
		List<V> values = tableByKey.get(key);

		// Returns suitable List.
		if (values != null) {
			return new ArrayList<V>(values);
		} else {
			return Collections.EMPTY_LIST;
		}
	}

	/**
	 * Removes the element by value.
	 * 
	 * @param value
	 */
	public void removeByValue(V value) {

		// Argument checks.
		if (!containtsValue(value)) {
			throw new IllegalArgumentException("The element " + value
					+ " has not put");
		}

		// Removes from values List.
		values.remove(value);

		// Removes from tableByKey
		K key = tableByValue.get(value);
		List<V> values = tableByKey.get(key);
		values.remove(value);
		if (values.isEmpty()) {
			tableByKey.remove(key);
		}

		// Removes from tableByValue
		tableByValue.remove(value);
	}

	/**
	 * Removes the element(s) by key.
	 * 
	 * @param key
	 * @return
	 */
	public List<V> removeByKey(K key) {

		// Argument checks.
		if (!containtsKey(key)) {
			throw new IllegalArgumentException("The key " + key
					+ " has not put");
		}

		// Removes from values List, and tableByValue.
		List<V> values = tableByKey.get(key);

		for (V removeValue : values) {
			// Removes from values List.
			this.values.remove(removeValue);

			// Removes from tableByValue.
			tableByValue.remove(removeValue);
		}

		// Removes from tableByKey
		tableByKey.remove(key);

		return values;
	}

	/**
	 * Checks if containts the key.
	 * 
	 * @param key
	 * @return
	 */
	public boolean containtsKey(K key) {
		return tableByKey.containsKey(key);
	}

	/**
	 * Checks if containts the value.
	 * 
	 * @param value
	 * @return
	 */
	public boolean containtsValue(V value) {
		return tableByValue.containsKey(value);
	}

	/**
	 * Gets the all value.
	 * 
	 * @return
	 */
	public List<K> getKeys() {
		return new ArrayList<K>(tableByKey.keySet());
	}

	/**
	 * Gets the all value.
	 * 
	 * @return
	 */
	public List<V> getValues() {
		return new ArrayList<V>(values);
	}

}