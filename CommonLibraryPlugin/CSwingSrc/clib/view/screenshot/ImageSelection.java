/*
 * ImageSelection.java
 * Created on 2011/11/12
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.screenshot;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * ここからコピーした．
 * http://www.ne.jp/asahi/hishidama/home/tech/java/clipboard.html#setImage
 */
public class ImageSelection implements Transferable, ClipboardOwner {

	public static void setClipboardImage(Image img) {
		Toolkit kit = Toolkit.getDefaultToolkit();
		Clipboard clip = kit.getSystemClipboard();

		ImageSelection is = new ImageSelection(img);
		clip.setContents(is, is);
	}

	protected Image data;

	/** コンストラクター */
	public ImageSelection(Image image) {
		this.data = image;
	}

	/** 対応しているフレーバーを返す */
	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[] { DataFlavor.imageFlavor };
	}

	/** フレーバーが対応しているかどうか */
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return DataFlavor.imageFlavor.equals(flavor);
	}

	/** 保持している画像を返す */
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		if (DataFlavor.imageFlavor.equals(flavor)) {
			return data;
		}
		throw new UnsupportedFlavorException(flavor);
	}

	/** クリップボードのデータとして不要になった時に呼ばれる */
	public void lostOwnership(Clipboard clipboard, Transferable contents) {
		this.data = null;
	}
}
