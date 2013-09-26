/*
 * CAbstractEditableField.java
 * Created on 2012/05/15
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package cswing.table.model;

import java.lang.reflect.Field;

/**
 * @author macchan
 * 
 */
public abstract class CAbstractFieldEditableField implements ICEditableField {

	private String name;
	private Field field;
	private Object model;

	public CAbstractFieldEditableField(String name, Field field, Object model) {
		this.name = name;
		this.field = field;
		this.model = model;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see test.ICEditableField#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see test.ICEditableField#getType()
	 */
	@Override
	public Class<?> getType() {
		return field.getType();
	}

	public Object getValue() {
		try {
			field.setAccessible(true);
			return field.get(model);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public void setValue(Object value) {
		try {
			field.setAccessible(true);
			field.set(model, value);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
