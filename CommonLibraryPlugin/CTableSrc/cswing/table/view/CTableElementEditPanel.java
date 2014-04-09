/*
 * TEditPanel.java
 * Created on 2012/05/15
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package cswing.table.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cswing.table.model.ICEditableField;
import cswing.table.model.ICEditableFieldProvider;

/**
 * @author macchan
 */
public class CTableElementEditPanel<T> extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final int HMARGIN = 10;
	private static final int VMARGIN = 10;

	private ICEditableFieldProvider<T> model;
	private String title;
	private int labelwidth = 50;
	private int textlen = 15;

	/**
	 * 
	 */
	public CTableElementEditPanel(ICEditableFieldProvider<T> model,
			String title, int labelwidth, int textlen) {
		this.model = model;
		this.title = title;
		this.labelwidth = labelwidth;
		this.textlen = textlen;
		initialize();
	}

	private void initialize() {
		setLayout(new BorderLayout());
		initializeTitlePanel();
		initializeMainPanel();
	}

	private void initializeTitlePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		add(panel, BorderLayout.NORTH);

		JLabel label = new JLabel(title);
		panel.add(label);
	}

	private void initializeMainPanel() {

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		add(panel);

		hspacer(panel);

		JPanel vpanel1 = new JPanel();
		vpanel1.setLayout(new BoxLayout(vpanel1, BoxLayout.Y_AXIS));
		panel.add(vpanel1);

		hspacer(panel);

		JPanel vpanel2 = new JPanel();
		vpanel2.setLayout(new BoxLayout(vpanel2, BoxLayout.Y_AXIS));
		panel.add(vpanel2);

		hspacer(panel);

		vspacer(vpanel1, vpanel2);
		for (ICEditableField variable : model.getVariables()) {
			try {
				String name = variable.getName();
				JLabel label = new JLabel(name);
				label.setPreferredSize(new Dimension(labelwidth, 27));
				vpanel1.add(label);

				variable.updateFromModelToView();
				JComponent editor = variable.getEditor();
				if (editor instanceof JTextField) {
					JTextField textfield = (JTextField) editor;
					textfield.setMaximumSize(new Dimension(10000, 27));
					textfield.setColumns(textlen);
				}
				vpanel2.add(editor);

				vspacer(vpanel1, vpanel2);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public void updateModel() {
		for (ICEditableField field : model.getVariables()) {
			field.updateFromViewToModel();
		}
	}

	private void hspacer(JPanel panel) {
		JPanel spacer = new JPanel();
		spacer.setMinimumSize(new Dimension(HMARGIN, 0));
		panel.add(spacer);
	}

	private void vspacer(JPanel vpanel1, JPanel vpanel2) {
		JPanel vspacer1 = new JPanel();
		vspacer1.setMinimumSize(new Dimension(0, VMARGIN));
		vpanel1.add(vspacer1);
		JPanel vspacer2 = new JPanel();
		vspacer2.setMinimumSize(new Dimension(0, VMARGIN));
		vpanel2.add(vspacer2);
	}

	// /**
	// * @param labelwidth
	// * the labelwidth to set
	// */
	// public void setLabelwidth(int labelwidth) {
	// this.labelwidth = labelwidth;
	// }
	//
	// /**
	// * @param textlen
	// * the textlen to set
	// */
	// public void setTextlen(int textlen) {
	// this.textlen = textlen;
	// }

}
