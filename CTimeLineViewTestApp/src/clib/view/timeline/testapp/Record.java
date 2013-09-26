/*
 * Record.java
 * Created on Mar 9, 2011 
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.timeline.testapp;

import java.util.Date;

/**
 * @author macchan
 * 
 */
public class Record {

	private String name;
	private Date date;
	private String contents;

	public Record(String name, Date date, String contents) {
		this.name = name;
		this.date = date;
		this.contents = contents;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @return the contents
	 */
	public String getContents() {
		return contents;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return date + ": " + name + "\t: " + contents + "";
	}
}
