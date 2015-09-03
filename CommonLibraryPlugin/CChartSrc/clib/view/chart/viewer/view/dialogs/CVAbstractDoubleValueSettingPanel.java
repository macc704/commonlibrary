/*
 * CVAbstractDoubleValueSettingPanel.java
 * Created on 2004/05/07
 * 
 * Copyright (c) 2005 PlatBox Project. All rights reserved.
 */
package clib.view.chart.viewer.view.dialogs;

import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.NumberFormatter;

import clib.view.chart.viewer.resource.ChartResource;

/**
 * Class CVAbstractDoubleValueSettingPanel.
 * 
 * @author macchan
 * @version $Id: CVAbstractDoubleValueSettingPanel.java,v 1.1 2005/03/08 03:04:45
 *          bam Exp $
 */
public abstract class CVAbstractDoubleValueSettingPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private static NumberFormatter FORMATTER = new NumberFormatter(
			new DecimalFormat("0.##")); //$NON-NLS-1$

	private JLabel manualLabel = new JLabel(ChartResource
			.get("CVAbstractDoubleValueSettingPanel.Manual")); //$NON-NLS-1$
	private JTextField valueTextfield = new JTextField();
	private JLabel valueUnitTitle = new JLabel(""); //$NON-NLS-1$
	private JButton applyButton = new JButton(ChartResource
			.get("CVAbstractDoubleValueSettingPanel.Apply")); //$NON-NLS-1$

	/**
	 * Constructor for CVAbstractDoubleValueSettingPanel.
	 */
	public CVAbstractDoubleValueSettingPanel() {

	}

	protected void initialize() {

		initializeComponent();
		hook();
	}

	protected void initializeComponent() {

		applyButton.setMargin(new Insets(0, 5, 0, 5));

		valueTextfield.setColumns(7);
		add(manualLabel);
		add(valueTextfield);
		add(valueUnitTitle);
		add(applyButton);

		update();
	}

	private void hook() {

		applyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				apply();
			}
		});
	}

	protected void update() {

		setUnit(getUnit());
		valueTextfield.setText(formatValue(getValue()));
	}

	public void apply() {

		try {
			setValue(Double.parseDouble(valueTextfield.getText()));
			update();
			applied();
		} catch (NumberFormatException ex) {
			throw new RuntimeException(ex);
		}
	}

	protected void setUnit(String unit) {

		valueUnitTitle.setText(unit);
	}

	protected String formatValue(double value) {

		try {
			return FORMATTER.valueToString(new Double(value));
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Overriden.
	 */
	public void setEnabled(boolean enabled) {

		super.setEnabled(enabled);
		Component[] components = getComponents();
		for (int i = 0; i < components.length; i++) {
			components[i].setEnabled(enabled);
		}
	}

	protected abstract void applied();

	protected abstract String getUnit();

	protected abstract double getValue();

	protected abstract void setValue(double value);

}