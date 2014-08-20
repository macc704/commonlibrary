/*
 * COKCancelDialog.java
 * Created on Mar 7, 2011 
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.dialogs;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import clib.view.windowmanager.CWindowCentraizer;

/**
 * @author macchan
 */
public class COKCancelDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private boolean OK = false;

	private ICOKCancelDialogListener okCancelDialogListener;

	public COKCancelDialog(Frame owner, String title, JComponent component) {
		super(owner, true);// modal

		getContentPane().setLayout(new BorderLayout());

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle(title);

		JButton buttonOK = new JButton("OK");
		buttonOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (okCancelDialogListener != null
						&& okCancelDialogListener.canOkProcess() == false) {
					return;
				}
				OK = true;
				COKCancelDialog.this.setVisible(false);
			}
		});

		JButton buttonCancel = new JButton("Cancel");
		buttonCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (okCancelDialogListener != null
						&& okCancelDialogListener.canCancelProcess() == false) {
					return;
				}
				OK = false;
				COKCancelDialog.this.setVisible(false);
			}
		});

		JPanel south = new JPanel();
		south.add(buttonCancel);
		south.add(buttonOK);

		getContentPane().add(south, BorderLayout.SOUTH);

		setComponent(component);
	}

	public void setOkCancelDialogListener(
			ICOKCancelDialogListener okCancelDialogListener) {
		this.okCancelDialogListener = okCancelDialogListener;
	}

	public void setComponent(JComponent component) {
		getContentPane().add(component, BorderLayout.CENTER);
		pack();
	}

	public void showDialog() {
		CWindowCentraizer.centerWindow(this);
		setVisible(true);
	}

	/**
	 * @return the oK
	 */
	public boolean isOK() {
		return OK;
	}
}
