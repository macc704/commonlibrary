/*
 * CSimpleTextEditor.java
 * Created on Apr 11, 2011 
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.editor;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;

import clib.common.filesystem.CFile;
import clib.common.filesystem.CFileSystem;
import clib.common.thread.ICTask;
import clib.common.utils.CFrameTester;
import clib.view.actions.CAction;
import clib.view.actions.CActionUtils;

/**
 * @author macchan
 */
public class CSimpleTextEditor extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTextPane textPane = new JTextPane();
	private ICDirtyStateListener dirtyStateListener;
	private boolean dirty;

	private CFile file;

	private UndoManager undoManager = new UndoManager();

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

		textPane.getDocument().addUndoableEditListener(
				new UndoableEditListener() {
					public void undoableEditHappened(UndoableEditEvent e) {
						setDirty(true);
					}
				});
		textPane.getDocument().addUndoableEditListener(undoManager);
	}

	public void setDirtyStateListener(ICDirtyStateListener dirtyStateListener) {
		this.dirtyStateListener = dirtyStateListener;
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
		setDirty(false);
	}

	public void doSave() {
		if (file != null) {
			file.saveText(getEditorText());
			setDirty(false);
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

	public void setDirty(boolean dirty) {
		if (this.dirty == dirty) {
			return;
		}

		this.dirty = dirty;
		if (dirtyStateListener != null) {
			dirtyStateListener.dirtyStateChanged(this.dirty);
		}
	}

	public boolean isDirty() {
		return dirty;
	}

	// public JTextPane getTextPane() {
	// return textPane;
	// }

	public CAction getCopyAction() {
		CAction action = CActionUtils.createAction("Copy", new ICTask() {
			public void doTask() {
				textPane.copy();
			}
		});
		action.setAcceralator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
				KeyEvent.META_MASK));
		return action;
	}

	public CAction getPasteAction() {
		CAction action = CActionUtils.createAction("Paste", new ICTask() {
			public void doTask() {
				textPane.paste();
			}
		});
		action.setAcceralator(KeyStroke.getKeyStroke(KeyEvent.VK_V,
				KeyEvent.META_MASK));
		return action;
	}

	public CAction getCutAction() {
		CAction action = CActionUtils.createAction("Cut", new ICTask() {
			public void doTask() {
				textPane.cut();
			}
		});
		action.setAcceralator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
				KeyEvent.META_MASK));
		return action;
	}

	public CAction getUndoAction() {
		CAction action = CActionUtils.createAction("Undo", new ICTask() {
			public void doTask() {
				undoManager.undo();
			}
		});
		action.setAcceralator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
				KeyEvent.META_MASK));
		return action;
	}

	public CAction getRedoAction() {
		CAction action = CActionUtils.createAction("Redo", new ICTask() {
			public void doTask() {
				undoManager.redo();
			}
		});
		action.setAcceralator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
				KeyEvent.META_MASK | KeyEvent.SHIFT_MASK));
		return action;
	}

	public static void main(String[] args) {
		CFile file = CFileSystem.getExecuteDirectory().findOrCreateFile(
				"test.txt");
		CFrameTester.open(new CSimpleTextEditor(file));
	}
}
