/*
 * ChartTestMain.java
 * Created on Jul 14, 2010 
 * Copyright(c) 2010 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.chart;

import javax.swing.JFrame;
import javax.swing.UIManager;

import clib.common.model.ICModelChangeListener;
import clib.view.chart.controller.CVChartController;
import clib.view.chart.model.CAxis;
import clib.view.chart.model.CDefaultDataSet;
import clib.view.chart.model.CUnit;
import clib.view.chart.viewer.CVChartFrame;

/**
 * @author macchan
 * 
 */
public class ChartTestMain {
	public static void main(String[] args) throws Exception {
		UIManager
				.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		new ChartTestMain().run();
	}

	void run() {
		CVChartFrame frame = new CVChartFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		CVChartController controller = frame.getController();
		CUnit unit = new CUnit("unit");
		CAxis axis = new CAxis("axis", unit);
		CDefaultDataSet ds = new CDefaultDataSet("Data1", axis, axis, null);
		ds.addPoint(3, 7);
		ds.addPoint(5, 8);
		ds.addPoint(9.5, 13.5);
		controller.getChart().addData(ds);

		CDefaultDataSet ds2 = new CDefaultDataSet("Data2", axis, axis, null);
		ds2.addPoint(3, 1);
		ds2.addPoint(10, 8);
		ds2.addPoint(13, 4);
		controller.getChart().addData(ds2);

		frame.setVisible(true);
		controller.refreshViews();
		controller.setNavigationEnabled(true);

		controller.getNavigator().addModelListener(new ICModelChangeListener() {
			@Override
			public void modelUpdated(Object... args) {
				System.out.println("modelUpdated" + args.length);
			}
		});
	}
}
