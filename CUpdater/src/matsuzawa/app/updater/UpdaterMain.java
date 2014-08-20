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
 * version 1.3.0 2014.08.20 国際化&mac-command対応バージョン
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

	public static final String VERSION = "1.3.0"; //$NON-NLS-1$

	public static final String SERVER_VERSION_FILE = "version.txt"; //$NON-NLS-1$

	private final CDirectory base = CFileSystem.getExecuteDirectory();
	private final CDirectory updater = base.findOrCreateDirectory(".updater"); //$NON-NLS-1$
	private final CDirectory tmp = updater.findOrCreateDirectory("tmp"); //$NON-NLS-1$
	private final CFile locationFile = updater.findFile("location.txt"); //$NON-NLS-1$
	private final CFile localVersionFile = updater
			.findOrCreateFile("version.txt"); //$NON-NLS-1$
	private final CFile nameFile = updater.findOrCreateFile("name.txt"); //$NON-NLS-1$

	void run() {
		final CPanelProcessingMonitor panel = new CPanelProcessingMonitor();
		int height = (int) panel.getPreferredSize().getHeight();
		panel.setPreferredSize(new Dimension(300, height + 20));
		String title = "Updater - " + VERSION + " for " + nameFile.loadText(); //$NON-NLS-1$ //$NON-NLS-2$
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
				throw new RuntimeException(
						Messages.getString("Updater.FailedToRead")); //$NON-NLS-1$
			}

			String sitebase = locationFile.loadText();
			if (!sitebase.endsWith("/")) {
				sitebase += "/";
			}

			monitor.setWorkTitle(Messages.getString("Updater.Connecting")); //$NON-NLS-1$
			UpdateSiteManager manager = new UpdateSiteManager(sitebase);
			manager.checkVersion();// throws Exception

			if (!manager.necessaryUpdate()) {
				JOptionPane.showMessageDialog(null,
						Messages.getString("Updater.YoursLatest")); //$NON-NLS-1$
				return;
			}

			int result = JOptionPane
					.showConfirmDialog(
							null,
							Messages.getString("Updater.Latest") + manager.getServerVersion() //$NON-NLS-1$
									+ Messages.getString("Updater.LikeUpdate")); //$NON-NLS-1$
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
			makeExecutable(base);
			localVersionFile.saveText(manager.getServerVersion().toString());
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
		CDirectory ziptmp = tmp.findOrCreateDirectory("ziptmp"); //$NON-NLS-1$
		CIOUtils.unZip(zipfile, ziptmp);
		// ziptmp.copyTo(base);
		for (CFileElement child : ziptmp.getChildren()) {
			if (child.getName().toString().equals("updater.jar")) { //$NON-NLS-1$
				return;
			}
			child.copyTo(base);
		}
		ziptmp.delete();
	}

	private void makeExecutable(CDirectory dir) {
		for (CFileElement child : dir.getChildren()) {
			if (child.getName().toString().endsWith(".command")) { //$NON-NLS-1$
				child.toJavaFile().setExecutable(true);
			}
			if (child.isDirectory()) {
				makeExecutable((CDirectory) child);
			}
		}
	}

	class UpdateSiteManager {
		private final String sitebase;
		private Version serverVersion;
		private String serverFilename;
		private Version localVersion;
		private List<String> deletionList;

		public UpdateSiteManager(String sitebase) {
			this.sitebase = sitebase;
		}

		public Version getServerVersion() {
			return serverVersion;
		}

		public Version getLocalVersion() {
			return localVersion;
		}

		boolean checkVersion() {
			try {
				// local version
				this.localVersion = new Version(localVersionFile.loadText());
				if (localVersion == null) {
					localVersion = new Version(""); //$NON-NLS-1$
				}

				// server version
				loadServerVersion();

				return true;
			} catch (UnknownHostException ex) {
				throw new RuntimeException(
						Messages.getString("Updater.FailedToServer"), ex); //$NON-NLS-1$
			} catch (FileNotFoundException ex) {
				throw new RuntimeException(
						Messages.getString("Updater.FileNotFound"), ex); //$NON-NLS-1$
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}

		void loadServerVersion() throws Exception {
			String versionFileURL = sitebase + SERVER_VERSION_FILE;
			String serverVersionFileText = downloadAsString(versionFileURL,
					CNullProgressMonitor.INSTANCE);
			CFile file = tmp.findOrCreateFile("a"); //$NON-NLS-1$
			file.saveText(serverVersionFileText);
			List<String> lines = file.loadTextAsList();
			file.delete();

			parseServerVersion(lines);
		}

		void parseServerVersion(List<String> lines) {
			for (String line : lines) {
				if (line.startsWith("v")) { //$NON-NLS-1$
					String[] tokens = line.split(" "); //$NON-NLS-1$
					this.serverVersion = new Version(tokens[1]);
					this.serverFilename = tokens[2];
					break;
				}
			}

			if (this.serverVersion == null) {
				throw new RuntimeException();
			}

			this.deletionList = new ArrayList<String>();
			for (String line : lines) {
				if (line.startsWith("v")) { //$NON-NLS-1$
					String[] tokens = line.split(" "); //$NON-NLS-1$
					if (localVersion.equals(tokens[2])) {
						break;
					}
				} else if (line.startsWith("d")) { //$NON-NLS-1$
					String[] tokens = line.split(" "); //$NON-NLS-1$
					deletionList.add(tokens[1]);
				}
			}
		}

		boolean necessaryUpdate() {
			System.out.println(serverVersion);
			System.out.println(localVersion);
			return serverVersion.isNewer(localVersion);
		}

		String getFilename() {
			// return serverVersion + ".zip";
			return serverFilename;
		}

		String getFileURL() {
			return sitebase + "/" + getFilename(); //$NON-NLS-1$
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
		monitor.setWorkTitle(Messages.getString("Updater.Downloading") + url.toString()); //$NON-NLS-1$
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
