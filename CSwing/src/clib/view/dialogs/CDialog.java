/*
 * CDialog.java
 * Created on 2007/09/18 by macchan
 * Copyright(c) 2007 CreW Project
 */
package clib.view.dialogs;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Window;

import javax.swing.JDialog;

/**
 * CDialog
 */
public class CDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private boolean hasOwner = false;

	public CDialog(Frame owner) {
		super(owner, true);
		this.hasOwner = (owner != null);
	}

	protected final void setWindowAtCenter() {
		int selfW = this.getSize().width;
		int selfH = this.getSize().height;

		Window window = getOwner();
		Dimension d;
		if (hasOwner) {
			d = window.getSize();
		} else {
			d = Toolkit.getDefaultToolkit().getScreenSize();
		}
		int ownerW = d.width;
		int ownerH = d.height;

		int x = window.getX() + ownerW / 2 - selfW / 2;
		int y = window.getY() + ownerH / 2 - selfH / 2;

		this.setLocation(x, y);
	}

	public void open() {
		setWindowAtCenter();
		setVisible(true);
	}

}
