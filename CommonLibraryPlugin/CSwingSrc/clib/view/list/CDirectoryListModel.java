/*
 * CDirectoryListModel.java
 * Created on Jul 20, 2010 
 * Copyright(c) 2010 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.list;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import clib.common.filesystem.CDirectory;
import clib.common.filesystem.CFileSystem;

/**
 * @author macchan
 * 
 */
@SuppressWarnings("rawtypes")
public class CDirectoryListModel extends AbstractListModel implements
		ICListModel<CDirectory> {

	private static final long serialVersionUID = 1L;
	private CDirectory dir;

	private List<CDirectory> dirs = new ArrayList<CDirectory>();

	public CDirectoryListModel(CDirectory dir) {
		this.dir = dir;
		update();
	}

	public Object getElementAt(int index) {
		return dirs.get(index);
	}

	public int getSize() {
		return dirs.size();
	}

	public List<CDirectory> getList() {
		return dirs;
	}

	public void setList(List<CDirectory> list) {
		throw new RuntimeException();
	}

	public void update() {
		dirs = dir.getDirectoryChildren();
		fireContentsChanged(this, 0, 0);
	}

	/***************************************************************************
	 * test code
	 **************************************************************************/

	public static void main(String args[]) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setSize(300, 300);

		CListPanel<CDirectory> listPanel = new CListPanel<CDirectory>();
		frame.getContentPane().add(listPanel);
		frame.setVisible(true);

		CDirectoryListModel model = new CDirectoryListModel(
				CFileSystem.getExecuteDirectory());
		listPanel.setModel(model);
		listPanel.refresh();
	}

}
