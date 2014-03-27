/*
 * CAbstractColorChooseButton.java
 * Created on 2004/05/07
 * 
 * Copyright (c) 2005 PlatBox Project. All rights reserved.
 */
package clib.view.common;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;

/**
 * Class CAbstractColorChooseButton.
 * 
 * @author macchan
 * @version $Id: CAbstractColorChooseButton.java,v 1.1 2005/03/08 03:04:46 bam
 *          Exp $
 */
public abstract class CAbstractColorChooseButton extends JButton {

	private static final long serialVersionUID = 1L;

	public CAbstractColorChooseButton() {
		setBackground(Color.WHITE);
		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				openDialog();
			}
		});
		update();
	}

	protected void openDialog() {
		Color color = JColorChooser.showDialog(this, getTitle(), getColor());
		setColor(color);
		update();
	}

	public void update() {
		Dimension size = getIconSize();
		BufferedImage buf = new BufferedImage(size.width, size.height,
				BufferedImage.TYPE_4BYTE_ABGR);
		Graphics g = buf.createGraphics();
		g.setColor(getColor());
		drawExplanation(g);
		g.dispose();

		setIcon(new ImageIcon(buf));
	}

	public abstract String getTitle();

	public abstract Dimension getIconSize();

	public abstract void drawExplanation(Graphics g);

	public abstract Color getColor();

	public abstract void setColor(Color color);

}