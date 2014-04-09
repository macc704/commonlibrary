/*
 * CRecordableTableFrame.java
 * Created on Jul 14, 2010 
 * Copyright(c) 2010 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.table.chartbridge;

import javax.swing.JFrame;

/**
 * @author macchan
 * 
 */
public class CRecordableTableFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private CRecordableTablePanelForFrame tablePanel = new CRecordableTablePanelForFrame();

	public CRecordableTableFrame() {
		// Frame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 400);

		// Table
		getContentPane().add(tablePanel);
	}

	public CRecordableTablePanel getRecordableTablePanel() {
		return tablePanel;
	}

}
