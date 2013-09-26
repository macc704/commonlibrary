/*
 * Student.java
 * Created on Mar 9, 2011 
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.timeline.testapp;

import java.util.ArrayList;
import java.util.List;

/**
 * @author macchan
 * 
 */
public class Student {

	private String name;

	private List<Record> records = new ArrayList<Record>();

	public Student(String name) {
		this.name = name;
	}

	public void add(Record r) {
		records.add(r);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the records
	 */
	public List<Record> getRecords() {
		return records;
	}
}
