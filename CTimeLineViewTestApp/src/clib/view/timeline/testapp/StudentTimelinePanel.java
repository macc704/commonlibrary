/*
 * StudentTimelinePanel.java
 * Created on Mar 10, 2011 
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.timeline.testapp;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;

import clib.common.time.CTime;
import clib.view.timeline.model.CTimeTransformationModel;

/**
 * @author macchan
 */
public class StudentTimelinePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private CTimeTransformationModel time;
	private Student student;

	public StudentTimelinePanel(CTimeTransformationModel time, Student student) {
		this.time = time;
		this.student = student;
		setBackground(Color.WHITE);
		initLayout();

		for (Record r : student.getRecords()) {
			RecordPanel p = new RecordPanel(r);
			add(p);
		}

		setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#getPreferredSize()
	 */
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(super.getPreferredSize().width, 30);
	}

	/**
	 * @return the student
	 */
	public Student getStudent() {
		return student;
	}

	private PropertyChangeListener listener = new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			layoutRecords();
		}
	};

	private void initLayout() {
		setLayout(null);

		// 効率は悪い，かつremoveListenerしていないのが問題　
		time.addPropertyChangeListener(listener);

		// addComponentListener(new ComponentAdapter() {
		// public void componentResized(ComponentEvent e) {
		// layoutRecords();
		// }
		// });
	}

	private void layoutRecords() {
		int y = getHeight() / 2;
		for (Component c : getComponents()) {
			RecordPanel panel = (RecordPanel) c;
			Record record = panel.getRecord();
			int x = (int) time.time2X(new CTime(record.getDate()));
			c.setSize(c.getPreferredSize());
			Rectangle r = c.getBounds();
			r.x = x - r.width / 2;
			r.y = y - r.height / 2;
			c.setBounds(r);
		}
	}

	class RecordPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		private Record record;

		/**
		 * @return the record
		 */
		public Record getRecord() {
			return record;
		}

		public RecordPanel(Record record) {
			setPreferredSize(new Dimension(4, 4));
			setBackground(Color.BLACK);
			this.record = record;
			addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					System.out.println("click:" + RecordPanel.this.record);
				}
			});
		}
	}
}
