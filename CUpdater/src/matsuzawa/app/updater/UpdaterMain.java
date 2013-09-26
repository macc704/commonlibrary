package matsuzawa.app.updater;

import java.awt.Dimension;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import clib.common.filesystem.CDirectory;
import clib.common.filesystem.CFile;
import clib.common.filesystem.CFileElement;
import clib.common.filesystem.CFileSystem;
import clib.common.filesystem.CPath;
import clib.common.io.CIOUtils;
import clib.common.thread.ICTask;
import clib.common.utils.CNullProgressMonitor;
import clib.common.utils.ICProgressMonitor;
import clib.view.dialogs.CErrorDialog;
import clib.view.progress.CPanelProcessingMonitor;

/*
 * Updater
 * 
 * version 1.0.0 2012.09.26 基礎バージョン 
 * version 1.0.1 2012.10.03 batファイルを追加
 * version 1.1.0 2012.10.03 updater.jarは更新しないように変更（更新すると現在実行中のプログラムでエラーとなる．）
 * version 1.2.0 2012.10.23 name.txtの導入
 * version 1.2.1 2012.10.26 英語バージョン
 * 
 * @author macchan
 * 
 */
public class UpdaterMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new UpdaterMain().run();
	}

	public static final String VERSION = "1.2.1";

	public static final String SERVER_VERSION_FILE = "version.txt";

	private final CDirectory base = CFileSystem.getExecuteDirectory();
	private final CDirectory updater = base.findOrCreateDirectory(".updater");
	private final CDirectory tmp = updater.findOrCreateDirectory("tmp");
	private final CFile locationFile = updater.findFile("location.txt");
	private final CFile localVersionFile = updater
			.findOrCreateFile("version.txt");
	private final CFile nameFile = updater.findOrCreateFile("name.txt");

	void run() {
		final CPanelProcessingMonitor panel = new CPanelProcessingMonitor();
		int height = (int) panel.getPreferredSize().getHeight();
		panel.setPreferredSize(new Dimension(300, height + 20));
		String title = "Updater - " + VERSION + " for " + nameFile.loadText();
		panel.doTaskWithDialog(title, new ICTask() {
			public void doTask() {
				doUpdate(panel);
			}
		});
		tmp.delete();
		System.exit(0);
	}

	private void doUpdate(ICProgressMonitor monitor) {
		try {
			if (locationFile == null) {
				// throw new RuntimeException("設定ファイルが読み込めません．");
				throw new RuntimeException(
						"Failed to read the property file on the server.");
			}

			String sitebase = locationFile.loadText();
			if (!sitebase.endsWith("/")) {
				sitebase += "/";
			}

			// monitor.setWorkTitle("サーバに問い合わせ中");
			monitor.setWorkTitle("Connecting to the server.");
			UpdateSiteManager manager = new UpdateSiteManager(sitebase);
			manager.checkVersion();// throws Exception

			// System.out.println("necessaryUpdate = " +
			// manager.necessaryUpdate());
			if (!manager.necessaryUpdate()) {
				// JOptionPane.showMessageDialog(null, "お使いのソフトウエアは最新バージョンです．");
				JOptionPane.showMessageDialog(null,
						"Your software is the newest one.");
				return;
			}

			// int result = JOptionPane.showConfirmDialog(null, "最新バージョン"+
			// manager.getServerVersion() + "があります．更新しますか？");
			int result = JOptionPane.showConfirmDialog(null,
					"The newest version " + manager.getServerVersion()
							+ " is available. Would you like to update?");
			if (result == JOptionPane.CANCEL_OPTION
					|| result == JOptionPane.NO_OPTION) {
				return;
			}

			String newFilename = manager.getFilename();
			String newFileURL = manager.getFileURL();
			CFile newFile = tmp.findOrCreateFile(newFilename);
			downloadAsFile(newFileURL, newFile, monitor);

			delete(manager);
			extract(newFile);
			localVersionFile.saveText(manager.getServerVersion());
		} catch (Exception ex) {
			// JOptionPane.showMessageDialog(null, ex.getMessage());
			CErrorDialog.show(null, ex.getMessage(), ex);
		}
	}

	private void delete(UpdateSiteManager manager) {
		for (String deletionPath : manager.deletionList) {
			// System.out.println(deletionPath);
			CFileElement element = base.findChild(new CPath(deletionPath));
			if (element != null) {
				element.delete();
			}
		}
	}

	private void extract(CFile zipfile) {
		CDirectory ziptmp = tmp.findOrCreateDirectory("ziptmp");
		CIOUtils.unZip(zipfile, ziptmp);
		// ziptmp.copyTo(base);
		for (CFileElement child : ziptmp.getChildren()) {
			if (child.getName().toString().equals("updater.jar")) {
				return;
			}
			child.copyTo(base);
		}
		ziptmp.delete();
	}

	class UpdateSiteManager {
		private final String sitebase;
		private String serverVersion;
		private String serverFilename;
		private String localVersion;
		private List<String> deletionList;

		public UpdateSiteManager(String sitebase) {
			this.sitebase = sitebase;
		}

		public String getServerVersion() {
			return serverVersion;
		}

		public String getLocalVersion() {
			return localVersion;
		}

		boolean checkVersion() {
			try {
				// local version
				this.localVersion = localVersionFile.loadText();
				if (localVersion == null) {
					localVersion = "";
				}

				// server version
				loadServerVersion();

				return true;
			} catch (UnknownHostException ex) {
				// throw new RuntimeException("サーバに接続できません．", ex);
				throw new RuntimeException("Failed to connect the server.", ex);
			} catch (FileNotFoundException ex) {
				// throw new RuntimeException("サーバにファイルが見つかりません．", ex);
				throw new RuntimeException(
						"Failed to find a software file on the server.", ex);
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}

		void loadServerVersion() throws Exception {
			String versionFileURL = sitebase + SERVER_VERSION_FILE;
			String serverVersionFileText = downloadAsString(versionFileURL,
					CNullProgressMonitor.INSTANCE);
			CFile file = tmp.findOrCreateFile("a");
			file.saveText(serverVersionFileText);
			List<String> lines = file.loadTextAsList();
			file.delete();

			parseServerVersion(lines);
		}

		void parseServerVersion(List<String> lines) {
			for (String line : lines) {
				if (line.startsWith("v")) {
					String[] tokens = line.split(" ");
					this.serverVersion = tokens[1];
					this.serverFilename = tokens[2];
					break;
				}
			}

			if (this.serverVersion == null) {
				throw new RuntimeException();
			}

			this.deletionList = new ArrayList<String>();
			for (String line : lines) {
				if (line.startsWith("v")) {
					String[] tokens = line.split(" ");
					if (localVersion.equals(tokens[2])) {
						break;
					}
				} else if (line.startsWith("d")) {
					String[] tokens = line.split(" ");
					deletionList.add(tokens[1]);
				}
			}
		}

		boolean necessaryUpdate() {
			return !serverVersion.equals(localVersion);
		}

		String getFilename() {
			// return serverVersion + ".zip";
			return serverFilename;
		}

		String getFileURL() {
			return sitebase + "/" + getFilename();
		}
	}

	private String downloadAsString(String urlString, ICProgressMonitor monitor)
			throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		download(urlString, out, monitor);
		return out.toString();
	}

	private void downloadAsFile(String urlString, CFile outfile,
			ICProgressMonitor monitor) throws Exception {
		OutputStream out = outfile.openOutputStream();
		download(urlString, out, monitor);
	}

	private void download(String urlString, OutputStream out,
			ICProgressMonitor monitor) throws Exception {
		URL url = new URL(urlString);
		monitor.setWorkTitle("Downloading " + url.toString());
		URLConnection conn = url.openConnection();
		monitor.setMax(conn.getContentLength());

		InputStream in = conn.getInputStream();
		int bufsize = 4096;
		byte buf[] = new byte[bufsize];
		while (true) {
			int len = in.read(buf);
			if (len <= 0) {
				break;
			}
			out.write(buf, 0, len);
			monitor.progress(len);
		}
		in.close();
		out.close();
	}
}
