package clib.view.textpane;

import java.awt.Container;
import java.awt.FontMetrics;

import javax.swing.BoundedRangeModel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;

public class CTextPaneUtils {

	public static void setTabs(JTextPane textPane, int charactersPerTab) {
		FontMetrics fm = textPane.getFontMetrics(textPane.getFont());
		int charWidth = fm.charWidth('w');
		int tabWidth = charWidth * charactersPerTab;

		TabStop[] tabs = new TabStop[10];

		for (int j = 0; j < tabs.length; j++) {
			int tab = j + 1;
			tabs[j] = new TabStop(tab * tabWidth);
		}

		TabSet tabSet = new TabSet(tabs);
		SimpleAttributeSet attributes = new SimpleAttributeSet();
		StyleConstants.setTabSet(attributes, tabSet);
		int length = textPane.getDocument().getLength();
		textPane.getStyledDocument().setParagraphAttributes(0, length,
				attributes, false);
	}

	public static void setTextWithoutScrolling(String text, JTextPane textArea) {
		Container c = textArea.getParent();
		while (c != null) {
			if (c instanceof JScrollPane) {
				break;
			}
			c = c.getParent();
		}
		setTextWithoutScrolling(text, textArea, (JScrollPane) c);
	}

	private static void setTextWithoutScrolling(String newText,
			JTextPane textComponent, JScrollPane scrollPane) {
		if (textComponent.getText().equals(newText)) {
			return;
		}

		// int pos = textArea.getCaretPosition();
		// textArea.setText(newText);
		// textArea.setCaretPosition(pos);

		final BoundedRangeModel xModel = scrollPane.getHorizontalScrollBar()
				.getModel();
		final int xValue = xModel.getValue();
		final BoundedRangeModel yModel = scrollPane.getVerticalScrollBar()
				.getModel();
		final int yValue = yModel.getValue();

		textComponent.setText(newText);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				xModel.setValue(xValue);
				yModel.setValue(yValue);
			}
		});
	}
}
