/*
 * CRecordableTablePanelForFrame.java
 * Created on May 3, 2011 
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.table.chartbridge;

import javax.swing.JComponent;
import javax.swing.JFrame;

import clib.view.chart.controller.CVChartController;
import clib.view.chart.viewer.CVChartFrame;
import clib.view.table.record.CTableRecorderTableModel;

/**
 * @author macchan
 * 
 */
public class CRecordableTablePanelForFrame extends CRecordableTablePanel {

	private static final long serialVersionUID = 1L;

	protected JComponent openChartComponent(CTableRecorderTableModel model,
			String name) {
		CVChartController controller = new CVChartController();
		CTableModelListeningTableChartAdapter adapter = new CTableModelListeningTableChartAdapter(
				model, controller, name);
		model.addTableModelListener(adapter);
		CVChartFrame newFrame = new CVChartFrame(controller);
		newFrame.setTitle(name);
		newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		newFrame.setVisible(true);
		return (JComponent) newFrame.getContentPane();
	}

	protected JComponent openTableComponent(CTableRecorderTableModel model,
			String name) {
		CRecordableTableFrame newFrame = new CRecordableTableFrame();
		newFrame.getRecordableTablePanel().setModel(model);
		newFrame.setTitle(name);
		newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		newFrame.setVisible(true);
		return (JComponent) newFrame.getContentPane();
	}
}
