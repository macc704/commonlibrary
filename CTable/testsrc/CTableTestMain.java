import java.awt.Point;

import clib.common.collections.CObservableList;
import clib.view.table.chartbridge.CRecordableTableFrame;
import clib.view.table.model.CFieldRefrectionTableModelDescripter;
import clib.view.table.model.CListTableModel;

/*
 * CTableTestMain.java
 * Created on Jul 14, 2010 
 * Copyright(c) 2010 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */

/**
 * @author macchan
 * 
 */
public class CTableTestMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		new CTableTestMain().run();
	}

	void run() throws Exception {
		CRecordableTableFrame frame = new CRecordableTableFrame();
		frame.setVisible(true);

		CObservableList<Point> list = new CObservableList<Point>();
		frame.getRecordableTablePanel().setModel(
				new CListTableModel<Point>(list,
						new CFieldRefrectionTableModelDescripter<Point>(
								Point.class, "x", "y")));

		// CTableRecorder recorder =
		// frame.getRecordableTablePanel().getRecorder();

		list.add(new Point(3, 3));
		// recorder.tick();
		frame.getRecordableTablePanel().tick();
		list.add(new Point(4, 4));
		// recorder.tick();
		frame.getRecordableTablePanel().tick();
		list.add(new Point(5, 5));
		// recorder.tick();
		frame.getRecordableTablePanel().tick();

		// int count = 5;
		// while (true) {
		// Thread.sleep(1000);
		// p1.setLocation(p1.getX(), p1.getY() * 2);
		// ticker.tick();
		// frame.getTablePanel().getListTableModel().modelUpdated();
		// count++;
		// }
	}
}
