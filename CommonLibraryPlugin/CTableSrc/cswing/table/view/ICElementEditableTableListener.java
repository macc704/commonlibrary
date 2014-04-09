/*
 * PPTaskDesignerListener.java
 * Created on 2012/05/17
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package cswing.table.view;

/**
 * @author macchan
 * 
 */
public interface ICElementEditableTableListener<T> {

	public void elementAdded(T object);

	public void elementRemoved(T object);

}
