/*
 * TestCListTableModel.java
 * Created on Jun 27, 2010 
 * Copyright(c) 2010 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.table.model;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import clib.common.collections.CObservableList;
import clib.common.utils.CFrameTester;


public class TestCListTableModel {
	public static void main(String[] args) throws Exception {
		new TestCListTableModel().run();

	}

	public void run() throws Exception {
		JTable table = new JTable();
		CObservableList<A> models = new CObservableList<A>();
		CListTableModel<A> listTableModel = new CListTableModel<A>(models,
				new CFieldRefrectionTableModelDescripter<A>(A.class));
		CObservableList<ICTableDataDecorator<A>> decorators = new CObservableList<ICTableDataDecorator<A>>();
		CDecoratableListTableModel<A> decoratableModel = new CDecoratableListTableModel<A>(
				listTableModel, decorators);
		table.setModel(decoratableModel);

		ICTableDataDecorator<A> dec1 = new ICTableDataDecorator<A>() {
			public Class<?> getValueType() {
				return Double.class;
			}

			public String getValueName() {
				return "1.2x";
			}

			public Object getValueAt(A model) {
				return model.x * 1.2;
			}
		};

		decorators.add(dec1);

		JScrollPane scroll = new JScrollPane(table);
		table.setAutoCreateRowSorter(true);

		CFrameTester.open(scroll);

		int x = 1;
		int y = x;
		while (true) {
			Thread.sleep(1000);
			models.add(new A(x, y));
			x++;
			y = y + x;
		}
	}
}

class A {
	int x;
	int y;

	A(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
