/*
 * Test.java
 * Created on Mar 9, 2011 
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.timeline.testapp;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.UIManager;

import clib.common.filesystem.CFile;
import clib.common.filesystem.CFileSystem;
import clib.common.system.CEncoding;
import clib.common.table.CCSVFileIO;
import clib.common.time.CTime;
import clib.common.time.CTimeRange;
import clib.common.utils.CFrameTester;

/**
 * @author macchan
 */
public class Main {
	public static void main(String[] args) throws Exception {
		UIManager
				.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		new Main().run();
	}

	void run() {

		URL url = getClass().getResource("data.csv");
		CFile file = CFileSystem.findFile(url.getFile());
		file.setEncodingIn(CEncoding.JISAutoDetect);

		String[][] values = CCSVFileIO.load(file);
		List<Record> records = new ArrayList<Record>();
		for (int i = 0; i < values.length; i++) {
			String name = values[i][1];
			Date date = new Date(Long.parseLong(values[i][4]));
			String contents = values[i][2];
			records.add(new Record(name, date, contents));
		}

		Map<String, Student> students = new HashMap<String, Student>();
		for (Record r : records) {
			String name = r.getName();
			if (!students.containsKey(name)) {
				students.put(name, new Student(name));
			}
			students.get(name).add(r);
		}

		// set pane
		final StudentTimeLinePane pane = new StudentTimeLinePane();

		Date start = records.get(0).getDate();
		Date end = records.get(records.size() - 1).getDate();

		pane.getTimelinePane().getTimeTransModel()
				.setRange(new CTimeRange(new CTime(start), new CTime(end)));

		for (Student student : students.values()) {
			pane.addModel(student);
		}

		// open
		JFrame f = CFrameTester.open(pane);

		pane.getTimelinePane().fitScale();

		// MenuBar
		JMenuBar bar = new JMenuBar();
		f.setJMenuBar(bar);
		JMenu menu = new JMenu("A");
		bar.add(menu);
		menu.add(new AbstractAction("Open") {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				CFrameTester.open(pane.getTimelinePane().createOverviewPanel());
			}
		});

	}
}
