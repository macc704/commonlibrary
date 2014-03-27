/*
 * CSimpleTextEditor.java
 * Created on Apr 11, 2011 
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.editor;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import clib.common.filesystem.CFile;
import clib.common.filesystem.CFileSystem;
import clib.common.utils.CFrameTester;

/**
 * @author macchan
 */
public class CSimpleTextEditor extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTextPane textPane = new JTextPane();

	private CFile file;

	public CSimpleTextEditor() {
		initialize();
	}

	public CSimpleTextEditor(CFile file) {
		setFile(file);
		initialize();
	}

	private void initialize() {
		setLayout(new BorderLayout());
		JScrollPane scrollPane = new JScrollPane();

		scrollPane.setViewportView(textPane);
		add(scrollPane);
	}

	/**
	 * @param file
	 *            the file to set
	 */
	public void setFile(CFile file) {
		this.file = file;
		doLoad();
	}

	/**
	 * @return the file
	 */
	public CFile getFile() {
		return file;
	}

	/**
	 * @return the textPane
	 */
	public String getEditorText() {
		return textPane.getText();
	}

	/**
	 * @return the textPane
	 */
	public void setEditorText(String text) {
		textPane.setText(text);
	}

	public void doSave() {
		if (file != null) {
			file.saveText(getEditorText());
		} else {
			throw new RuntimeException("file is null on save()");
		}
	}

	public void doLoad() {
		if (file != null) {
			setEditorText(file.loadText("\n"));
		} else {
			throw new RuntimeException("file is null on load()");
		}
	}

	public static void main(String[] args) {
		CFile file = CFileSystem.getExecuteDirectory().findOrCreateFile(
				"test.txt");
		CFrameTester.open(new CSimpleTextEditor(file));
	}
}
