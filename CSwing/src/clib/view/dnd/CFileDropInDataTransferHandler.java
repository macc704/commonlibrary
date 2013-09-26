/*
 * CFileDropInDataTransferHandler.java
 * Created on 2012/01/27
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.TransferHandler;

/**
 * File��DropIn���邽�߂�DataTransferHandler
 * 
 * @author macchan
 * 
 */
public class CFileDropInDataTransferHandler extends TransferHandler {

	private static final long serialVersionUID = 1L;

	public static void set(JFrame frame, ICFileDroppedListener listener) {
		set((JComponent) frame.getContentPane(), listener);
	}

	public static void set(JComponent comp, ICFileDroppedListener listener) {
		CFileDropInDataTransferHandler handler = new CFileDropInDataTransferHandler(
				listener);
		comp.setTransferHandler(handler);
	}

	private ICFileDroppedListener handler;

	/**
	 * Constructor
	 */
	public CFileDropInDataTransferHandler(ICFileDroppedListener handler) {
		this.handler = handler;
	}

	public boolean canImport(JComponent comp, DataFlavor[] flavors) {
		for (int i = 0; i < flavors.length; i++) {
			if (flavors[i].isFlavorJavaFileListType()) {
				return true;
			}
		}
		return false;
	}

	public boolean importData(JComponent comp, Transferable t) {
		try {
			DataFlavor[] flavors = t.getTransferDataFlavors();
			for (int i = 0; i < flavors.length; i++) {
				if (flavors[i].isFlavorJavaFileListType()) {
					Object o = t.getTransferData(flavors[i]);
					@SuppressWarnings("unchecked")
					List<File> files = (List<File>) o;
					if (files.size() > 0) {
						handler.fileDropped(files);
						return true;
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}
}
