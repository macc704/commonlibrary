/*
 * CLineNumberView.java
 * Created on 2011/06/06
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.scrollpane;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.Element;

/**
 * @author macchan
 */
public class CLineNumberView extends JComponent {

	private static final long serialVersionUID = 1L;

	private static final int MARGIN = 5;

	private JTextPane textPane;
	private FontMetrics fontMetrics;
	private int topInset;
	private int fontAscent;
	private int fontHeight;

	public CLineNumberView(JTextPane textPane) {
		this.textPane = textPane;
		initialize();
	}

	private void initialize() {
		setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY));
		textPane.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				repaint();
			}

			public void removeUpdate(DocumentEvent e) {
				repaint();
			}

			public void changedUpdate(DocumentEvent e) {
			}
		});
		textPane.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				repaint();
			}
		});
		prepareFontInformation();
		textPane.addPropertyChangeListener("font",
				new PropertyChangeListener() {
					public void propertyChange(PropertyChangeEvent evt) {
						prepareFontInformation();
					}
				});
	}

	private void prepareFontInformation() {
		Font font = textPane.getFont();
		this.fontMetrics = getFontMetrics(font);
		this.fontHeight = fontMetrics.getHeight();
		this.fontAscent = fontMetrics.getAscent();
		this.topInset = textPane.getInsets().top;
	}

	private int getComponentWidth() {
		return getLineTextWidth();
	}

	private int getLineTextWidth() {
		Document doc = textPane.getDocument();
		Element root = doc.getDefaultRootElement();
		int lineCount = root.getElementIndex(doc.getLength());
		int maxDigits = Math.max(3, String.valueOf(lineCount).length());
		return maxDigits * fontMetrics.stringWidth("0") + MARGIN * 2;
	}

	private int getLineAtPoint(int y) {
		Element root = textPane.getDocument().getDefaultRootElement();
		int pos = textPane.viewToModel(new Point(0, y));
		return root.getElementIndex(pos);
	}

	public Dimension getPreferredSize() {
		return new Dimension(getComponentWidth(), textPane.getHeight());
	}

	public void paintComponent(Graphics g) {
		Rectangle clip = g.getClipBounds();

		// background
		g.setColor(getBackground());
		g.fillRect(clip.x, clip.y, clip.width, clip.height);

		// foreground
		g.setColor(getForeground());
		int base = clip.y - topInset;
		int start = getLineAtPoint(base);
		int end = getLineAtPoint(base + clip.height);
		int y = topInset - fontHeight + fontAscent + start * fontHeight;
		for (int i = start; i <= end; i++) {
			String text = String.valueOf(i + 1);
			int x = getLineTextWidth() - MARGIN - fontMetrics.stringWidth(text);
			y = y + fontHeight;
			g.drawString(text, x, y);
		}
	}

}
