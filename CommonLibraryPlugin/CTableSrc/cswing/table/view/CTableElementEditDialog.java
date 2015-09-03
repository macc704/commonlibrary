/*
 * TTableEditDialog.java
 * Created on 2012/05/15
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package cswing.table.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JPanel;

import clib.common.thread.ICTask;
import clib.view.actions.CAction;
import clib.view.actions.CActionUtils;
import clib.view.dialogs.CDialog;

/**
 * @author macchan
 * 
 */
public class CTableElementEditDialog extends CDialog {

	private static final long serialVersionUID = 1L;

	private CTableElementEditPanel<?> panel;
	private boolean ok = false;

	public CTableElementEditDialog(Frame owner, String title,
			CTableElementEditPanel<?> panel) {
		super(owner);
		setTitle(title);
		this.panel = panel;
		initialize();
	}

	private void initialize() {
		setLayout(new BorderLayout());
		initializeButtonPanel();
		add(panel);
	}

	private void initializeButtonPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.CENTER));
		add(panel, BorderLayout.SOUTH);

		{
			JButton button = new JButton();
			CAction action = CActionUtils.createAction("Cancel", new ICTask() {
				@Override
				public void doTask() {
					doCancel();
				}

			});
			button.setAction(action);
			panel.add(button);
		}
		{
			JButton button = new JButton();
			CAction action = CActionUtils.createAction("OK", new ICTask() {
				@Override
				public void doTask() {
					doOK();
				}

			});
			button.setAction(action);
			panel.add(button);
		}
	}

	public boolean isOk() {
		return ok;
	}

	private void doCancel() {
		ok = false;
		dispose();
	}

	private void doOK() {
		ok = true;
		panel.updateModel();
		dispose();
	}
}
