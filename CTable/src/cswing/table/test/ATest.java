/*
 * TableTest.java
 * Created on 2012/05/15
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package cswing.table.test;

import javax.swing.JFrame;

import cswing.table.view.CElementEditableTablePanel;


/**
 * @author macchan
 * 
 */
public class ATest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 300, 300);
		CElementEditableTablePanel<A> table = new CElementEditableTablePanel<A>(new AMetaModel());
		frame.getContentPane().add(table);
		frame.setVisible(true);
	}
}
