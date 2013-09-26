/*
 * Created on 2005/03/03
 *
 * Copyright (c) 2005 CreW Poject.  All rights reserved.
 */
package clib.view.chart.viewer.export;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import clib.view.chart.viewer.resource.ChartResource;
import clib.view.chart.viewer.view.CVChartCanvas;
import clib.view.panels.CVerticalLayoutPanel;

/**
 * Class CVExportImageDialog
 * 
 * @author bam
 * @version $Id: CVExportImageDialog.java,v 1.1 2005/03/08 03:04:44 bam Exp $
 */
public class CVExportImageDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	/******************************************
	 * Constant(s).
	 ******************************************/

	private final static int LABEL_WIDTH = 100;
	private final static int TEXT_WIDTH = 100;
	private final static int TEXT_HEIGHT = 20;

	/******************************************
	 * Attribute(s).
	 ******************************************/

	private boolean isOK = false;

	private CVChartCanvas canvas;

	// private JTextField originXField;
	// private JTextField originYField;
	private JTextField widthField;
	private JTextField heightField;

	private JButton okButton;
	private JButton cancelButton;

	/******************************************
	 * Constructor(s).
	 ******************************************/

	public CVExportImageDialog(CVChartCanvas canvas) throws HeadlessException {
		super(getOwner(canvas), true);

		this.canvas = canvas;
		this.initializeComponents();
	}

	private static Frame getOwner(Component c) {
		return (Frame) SwingUtilities.getWindowAncestor(c);
	}

	/******************************************
	 * Operation(s).
	 ******************************************/

	/*******************
	 * Initialize
	 ******************/

	private void initializeComponents() {

		this.setSize(400, 400);
		this.setTitle(ChartResource.get("CVExportImageDialog.Title")); //$NON-NLS-1$
		this.setResizable(false);
		this.setLocation(new Point((int) this.getOwner().getBounds()
				.getCenterX(), (int) this.getOwner().getBounds().getCenterY()));
		this.getContentPane().setLayout(new BorderLayout());

		this.getContentPane().add(
				new JLabel(ChartResource.get("CVExportImageDialog.Message")), //$NON-NLS-1$
				BorderLayout.NORTH);

		// main input pane
		JPanel mainPanel = new CVerticalLayoutPanel();
		mainPanel.add(this.createWidthSettingPane());
		mainPanel.add(this.createHeightSettingPane());
		this.getContentPane().add(mainPanel, BorderLayout.CENTER);

		// buttons
		this.getContentPane()
				.add(this.createButtonsPanel(), BorderLayout.SOUTH);

		this.pack();
	}

	/**
	 * @return
	 */
	private JPanel createWidthSettingPane() {

		JPanel settingPanel = new JPanel();
		settingPanel.setLayout(new FlowLayout());

		JLabel label = new JLabel(ChartResource
				.get("CVExportImageDialog.LabelWidth")); //$NON-NLS-1$
		label.setPreferredSize(new Dimension(LABEL_WIDTH, TEXT_HEIGHT));
		settingPanel.add(label);

		widthField = new JTextField();
		widthField.setPreferredSize(new Dimension(TEXT_WIDTH, TEXT_HEIGHT));
		widthField.setText(String.valueOf(canvas.getWidth()));
		settingPanel.add(widthField);
		widthField.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent e) {

			}

			public void keyReleased(KeyEvent e) {

				updateButtons();
			}

			public void keyTyped(KeyEvent e) {

			}

		});

		return settingPanel;
	}

	/**
	 * @return
	 */
	private JPanel createHeightSettingPane() {

		JPanel settingPanel = new JPanel();
		settingPanel.setLayout(new FlowLayout());

		JLabel label = new JLabel(ChartResource
				.get("CVExportImageDialog.LabelHeight")); //$NON-NLS-1$
		label.setPreferredSize(new Dimension(LABEL_WIDTH, TEXT_HEIGHT));
		settingPanel.add(label);

		heightField = new JTextField();
		heightField.setPreferredSize(new Dimension(TEXT_WIDTH, TEXT_HEIGHT));
		settingPanel.add(heightField);
		heightField.setText(String.valueOf(canvas.getHeight()));
		heightField.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
			}

			public void keyReleased(KeyEvent e) {
				updateButtons();
			}

			public void keyTyped(KeyEvent e) {
			}

		});

		return settingPanel;
	}

	/**
	 * @return
	 */
	private Component createButtonsPanel() {

		JPanel buttonsPanel = new JPanel();
		GridLayout layout = new GridLayout();
		layout.setColumns(2);
		layout.setHgap(30);
		buttonsPanel.setLayout(layout);
		buttonsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

		// okButton
		this.okButton = new JButton(ChartResource
				.get("CVExportImageDialog.ButtonOK")); //$NON-NLS-1$
		okButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				okButtonPushed(e);
			}
		});
		buttonsPanel.add(okButton);

		// Cancel Button
		this.cancelButton = new JButton(ChartResource
				.get("CVExportImageDialog.ButtonCancel")); //$NON-NLS-1$
		cancelButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				cancelButtonPushed(e);
			}
		});
		buttonsPanel.add(cancelButton);

		return buttonsPanel;
	}

	/*******************
	 * Event
	 ******************/

	private void okButtonPushed(ActionEvent e) {

		this.isOK = true;
		super.dispose();
	}

	private void cancelButtonPushed(ActionEvent e) {

		super.dispose();
	}

	private void updateButtons() {

		boolean isValid = true;
		try {
			int w = this.getWidth();
			int h = this.getHeight();

			if (w < 0 || w > canvas.getSize().width) {
				isValid = false;
			} else if (h < 0 || h > canvas.getSize().height) {
				isValid = false;
			}
		} catch (Exception e) {
			isValid = false;
		}

		this.okButton.setEnabled(isValid);
	}

	/*******************
	 * Getters
	 ******************/

	public int getWidth() {

		return Integer.parseInt(this.widthField.getText());
	}

	public int getHeight() {

		return Integer.parseInt(this.heightField.getText());
	}

	public boolean isOK() {

		return isOK;
	}
}