/*
 * CEditableLabel.java
 * Created on Apr 12, 2011 
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import clib.common.utils.CFrameTester;

/**
 * @author macchan
 * 
 */
public class CEditableLabel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JLayeredPane layer = new JLayeredPane();
	private JLabel label = new JLabel();
	private JTextField textField = new JTextField();

	private boolean labelState = true;

	private List<ICLabelChangedListener> labelListeners = new ArrayList<ICLabelChangedListener>();

	public CEditableLabel(String labelText) {
		setText(labelText);
		initialize();
	}

	private void initialize() {
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);
		layer.setLayout(new CLayerLayout());
		layer.add(label, JLayeredPane.DEFAULT_LAYER);
		add(layer);

		label.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					startTyping();
				}
			}
		});
		textField.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				finishTyping();
			}
		});
		textField.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					finishTyping();
				}
			}
		});
	}

	public void setText(String text) {
		label.setText(text);
		textField.setText(text);
	}

	public String getText() {
		return label.getText();
	}

	private void startTyping() {
		if (labelState == true) {
			textField.setText(label.getText());
			layer.add(textField, JLayeredPane.POPUP_LAYER);
			layer.validate();
			layer.repaint();
			textField.requestFocus();
			labelState = false;
		}
	}

	private void finishTyping() {
		if (labelState == false) {
			String newText = textField.getText();
			if (!label.getText().equals(newText)) {
				label.setText(newText);
				fireLabelChanged(textField.getText());
			}
			layer.remove(textField);
			layer.validate();
			layer.repaint();
			labelState = true;
		}
	}

	public void addLabelChangedListener(ICLabelChangedListener listener) {
		labelListeners.add(listener);
	}

	public void removeLabelChangedListener(ICLabelChangedListener listener) {
		labelListeners.remove(listener);
	}

	protected void fireLabelChanged(String newName) {
		for (ICLabelChangedListener l : labelListeners) {
			l.labelChanged(newName);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#setBackground(java.awt.Color)
	 */
	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		if (label != null) {
			label.setBackground(bg);
			textField.setBackground(bg);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#setForeground(java.awt.Color)
	 */
	@Override
	public void setForeground(Color fg) {
		super.setForeground(fg);
		if (label != null) {
			label.setForeground(fg);
			textField.setForeground(fg);
		}
	}

	public static void main(String[] args) {
		CEditableLabel label = new CEditableLabel("hoge");
		JLabel jLabel = new JLabel("hage");
		JTextField field = new JTextField("huge");
		JPanel panel = new JPanel();
		panel.add(label);
		panel.add(jLabel);
		panel.add(field);
		CFrameTester.open(panel);
	}
}
