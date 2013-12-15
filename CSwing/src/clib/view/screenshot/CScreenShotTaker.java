/*
 * CScreenShot.java
 * Created on 2011/11/12
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.screenshot;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.FontUIResource;

import clib.common.thread.ICTask;
import clib.common.utils.CFrameTester;
import clib.view.actions.CAction;
import clib.view.actions.CActionUtils;
import clib.view.dialogs.CErrorDialog;
import clib.view.progress.CPanelProcessingMonitor;

/**
 * @author macchan
 * 
 */
public class CScreenShotTaker {

	public static final int IMG_TYPE = BufferedImage.TYPE_3BYTE_BGR;

	private Component comp;
	private ICMessageCreater messenger;
	private Rectangle clipbounds;

	private Font font;
	
	private JFileChooser chooser;

	public CScreenShotTaker(Component comp) {
		this(comp, null);
	}

	public CScreenShotTaker(Component comp, ICMessageCreater messenger) {
		this.comp = comp;
		this.messenger = messenger;

		String name = ((FontUIResource) UIManager.get("Label.font"))
				.getFontName();
		this.font = new Font(name, Font.PLAIN, 10);
		initializeFileChooser();
	}
	
	private void initializeFileChooser(){
		this.chooser = new JFileChooser();
		chooser.setFileFilter(new FileFilter() {
			@Override
			public String getDescription() {
				return "JPEG File";
			}

			@Override
			public boolean accept(File f) {
				if(f.isDirectory()){
					return true;
				}
				return f.getName().endsWith(".jpg");
			}
		});
	}
	
	public JFileChooser getChooser() {
		return chooser;
	}	

	public void setClipbounds(Rectangle clipbounds) {
		this.clipbounds = clipbounds;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public BufferedImage takeScreen() {
		int w = comp.getWidth();
		int h = comp.getHeight();
		BufferedImage img = new BufferedImage(w, h, IMG_TYPE);
		Graphics2D g2d = (Graphics2D) img.getGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, w, h);
		comp.printAll(img.getGraphics());

		if (clipbounds != null) {
			img = img.getSubimage(clipbounds.x, clipbounds.y, clipbounds.width,
					clipbounds.height);
		}
		return img;
	}

	public BufferedImage takeMessage(int w) {
		assert messenger != null;
		List<String> messages = new ArrayList<String>();
		messenger.getMessages(messages);
		int strHeight = comp.getFontMetrics(font).getHeight() * 2 / 3;
		int h = strHeight * messages.size();
		BufferedImage img = new BufferedImage(w, h, IMG_TYPE);
		Graphics2D g2d = (Graphics2D) img.getGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, w, h);
		g2d.setFont(font);
		g2d.setColor(Color.BLACK);
		for (int i = 0; i < messages.size(); i++) {
			String message = messages.get(i);
			g2d.drawString(message, 0, ((i + 1) * strHeight) - 2);
		}
		return img;
	}

	public BufferedImage take() {
		BufferedImage scrImg = takeScreen();
		if (messenger == null) {
			return scrImg;
		}
		BufferedImage msgImg = takeMessage(scrImg.getWidth());

		int w = scrImg.getWidth();
		int h = scrImg.getHeight() + msgImg.getHeight();
		BufferedImage img = new BufferedImage(w, h, IMG_TYPE);
		Graphics2D g2d = (Graphics2D) img.getGraphics();
		g2d.drawImage(scrImg, null, 0, 0);
		g2d.drawImage(msgImg, null, 0, scrImg.getHeight());
		return img;
	}

	public void takeToClipboard() {
		BufferedImage img = take();
		ImageSelection.setClipboardImage(img);
	}
	


	public void takeToFile() {
		int result = chooser.showSaveDialog(SwingUtilities
				.windowForComponent(comp));
		if (result != JFileChooser.APPROVE_OPTION) {
			return;
		}

		takeToFile(chooser.getSelectedFile());
	}

	public void takeToFile(File f) {
		try {
			if (f.getName().indexOf(".") == -1) {
				f = new File(f.getParentFile(), f.getName() + ".jpg");
			}
			BufferedImage img = take();
			ImageIO.write(img, "jpg", f);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public CAction createToClipboardAction() {
		return CActionUtils.createAction("To Clipboard", new ICTask() {
			public void doTask() {
				takeToClipboard();
			}
		});
	}

	public CAction createToFileAction() {
		return CActionUtils.createAction("To File", new ICTask() {
			public void doTask() {
				try {
					takeToFile();
				} catch (Exception ex) {
					CErrorDialog.show(null, ex);
				}
			}
		});
	}

	public static void main(String[] args) {
		final JFrame f = CFrameTester.open(new CPanelProcessingMonitor());
		JMenuBar bar = new JMenuBar();
		f.setJMenuBar(bar);
		JMenu menu = new JMenu("Menu");
		bar.add(menu);
		CScreenShotTaker taker = new CScreenShotTaker(f,
				new ICMessageCreater() {
					public void getMessages(List<String> messages) {
						messages.add("にほんご");
						messages.add("Eigo");
						messages.add("にほんご");
						messages.add(new Date().toString());
					}
				});
		menu.add(taker.createToClipboardAction());
		menu.add(taker.createToFileAction());
		f.pack();
	}
}
