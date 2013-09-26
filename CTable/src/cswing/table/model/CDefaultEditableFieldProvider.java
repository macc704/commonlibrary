/*
 * CDefaultTableModel.java
 * Created on 2012/05/15
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package cswing.table.model;

/**
 * @author macchan
 * 
 */
public class CDefaultEditableFieldProvider<T> extends
		CAbstractEditableFieldProvider<T> {

	private String[] variableNames;

	/**
	 * 
	 */
	public CDefaultEditableFieldProvider(T model, String... variableNames) {
		super(model);
		this.variableNames = variableNames;
	}

	protected void initialize() {
		for (int i = 0; i < variableNames.length; i++) {
			String name = variableNames[i];
			add(createDefault(name, getField(name)));
		}
	}
}
