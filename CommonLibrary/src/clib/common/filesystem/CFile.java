/*
 * CFile.java
 * Created on 2010/02/12 by macchan
 * Copyright(c) 2010 Yoshiaki Matsuzawa, Shizuoka University
 */
package clib.common.filesystem;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import clib.common.io.CFileInputStream;
import clib.common.io.CFileOutputStream;
import clib.common.io.CIOUtils;
import clib.common.io.CStreamReader;
import clib.common.io.CStreamWriter;
import clib.common.system.CEncoding;
import clib.common.system.CEncodingDetector;
import clib.common.system.CJavaSystem;

/**
 * CFile
 */
public class CFile extends CFileElement {

	public static final CEncoding ENCODING_IN_DEFAULT = CEncoding.JISAutoDetect;
	public static final CEncoding ENCODING_OUT_DEFAULT = CEncoding
			.getSystemEncoding();

	public static final String LINESEPARATOR = CJavaSystem.getInstance()
			.getLineSparator();

	private CEncoding encodingIn = ENCODING_IN_DEFAULT;
	private CEncoding encodingOut = ENCODING_OUT_DEFAULT;

	protected CFile(CPath path) {
		super(path);
		initialCheck();
	}

	protected CFile(File javaFile) {
		super(javaFile);
		initialCheck();
	}

	private void initialCheck() {
		if (this.getJavaFile().isDirectory()) {
			throw new RuntimeException(
					"This is not a file but a directory. path:"
							+ getJavaFile().getPath());
		}
	}

	public CFileInputStream openInputStream() {
		try {
			return new CFileInputStream(this.getJavaFile());
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public CFileOutputStream openOutputStream() {
		return openOutputStream(false);
	}

	public CFileOutputStream openOutputStream(boolean append) {
		try {
			return new CFileOutputStream(this.getJavaFile(), append);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public CStreamReader openReader() {
		try {
			// when AutoDetection and find a BOM, change encodingIn
			if (getEncodingIn().equals(CEncoding.JISAutoDetect)) {
				InputStream is = openInputStream();
				byte[] firstThree = new byte[3];
				int len = is.read(firstThree);
				is.close();
				if (len == 3 && firstThree[0] == (byte) 0xEF
						&& firstThree[1] == (byte) 0xBB
						&& firstThree[2] == (byte) 0xBF) {
					return new CStreamReader(openInputStream(), CEncoding.UTF8WBOM);
				}

				if (CEncodingDetector.detect(getJavaFile()) == CEncoding.UTF8) {
					return new CStreamReader(openInputStream(), CEncoding.UTF8);
				}
			}

			return new CStreamReader(openInputStream(), encodingIn);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public CStreamWriter openWriter() {
		return openWriter(false);
	}

	public CStreamWriter openWriter(boolean append) {
		try {
			// when UTF-8 create a BOM
			if (append == false && getEncodingOut() == CEncoding.UTF8WBOM) {
				OutputStream os = openOutputStream();
				byte[] BOM = new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF };
				os.write(BOM);
				os.close();
				return new CStreamWriter(openOutputStream(true), encodingOut);
			}

			return new CStreamWriter(openOutputStream(append), encodingOut);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public byte[] loadAsByte() {
		try {
			CFileInputStream in = openInputStream();
			ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
			byte[] buf = new byte[2048];
			int len = 0;
			while ((len = in.read(buf)) > 0) {
				byteArrayStream.write(buf, 0, len);
			}
			return byteArrayStream.toByteArray();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public void saveAsByte(byte[] bytes) {
		CFileOutputStream out = openOutputStream();
		out.write(bytes);
		out.close();
	}

	public String loadTextAsIs() {
		String separator = detectSeparator();
		return loadText(separator);
	}

	// \n LF UNIX系OS
	// \r CR マッキントッシュ
	// \r\n CR+LF Windows（MS-DOSやWindowsNTも同様です）
	private String detectSeparator() {
		try {
			byte[] buf = new byte[1024];
			InputStream is = openInputStream();
			byte before = '\0';
			while (is.available() > 0) {
				int len = is.read(buf);
				for (int i = 0; i < len; i++) {
					if (buf[i] == '\n') {
						if (before == '\r') {
							return "\r\n";
						} else {
							return "\n";
						}
					} else if (buf[i] == '\r') {
						// do nothing
					} else if (before == '\r') {
						return "\r";
					}
					before = buf[i];
				}
			}
			is.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.println("null!");
		return LINESEPARATOR;
	}

	public String loadText() {
		return loadText(LINESEPARATOR);
	}

	public String loadText(String delim) {
		StringBuffer buf = new StringBuffer();
		CStreamReader reader = openReader();
		while (reader.hasMoreLine()) {
			buf.append(reader.getCurrentLine());
			reader.toNextLine();
			if (reader.hasMoreLine()) {
				buf.append(delim);
			}
		}
		reader.close();
		return buf.toString();
	}

	public List<String> loadTextAsList() {
		List<String> lines = new ArrayList<String>();
		CStreamReader reader;
		for (reader = openReader(); reader.hasMoreLine(); reader.toNextLine()) {
			lines.add(reader.getCurrentLine());
		}
		reader.close();
		return lines;
	}

	public void saveStream(InputStream in) {
		CIOUtils.copy(in, this.openOutputStream());
	}

	public void saveText(String text) {
		CStreamWriter writer = openWriter();
		Scanner scanner = new Scanner(text);
		while (scanner.hasNext()) {
			writer.write(scanner.nextLine());
			if (scanner.hasNext()) {
				writer.writeLineFeed();
			}
		}
		scanner.close();
		writer.close();
	}

	/**
	 * @param convertedLines
	 */
	public void saveTextFromList(List<String> lines) {
		CStreamWriter writer = openWriter();
		for (String line : lines) {
			writer.write(line);
			writer.writeLineFeed();
		}
		writer.close();
	}

	public void appendText(String text) {
		CStreamWriter writer = openWriter(true);
		writer.writeLineFeed(text);
		writer.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see clib.common.filesystem.CFileElement#isDirectory()
	 */
	@Override
	public boolean isDirectory() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see clib.common.filesystem.CFileElement#isFile()
	 */
	@Override
	public boolean isFile() {
		return true;
	}

	/**
	 * @param recordFileDir
	 * @param name
	 */
	public CFileElement copyTo(CDirectory toDir, CPath path) {
		CFile file = toDir.findOrCreateFile(path);
		CIOUtils.copy(this.openInputStream(), file.openOutputStream());
		return file;
	}

	/**********************************
	 * Getters and Setters
	 **********************************/

	public CEncoding getEncodingIn() {
		return encodingIn;
	}

	public void setEncodingIn(CEncoding encodingIn) {
		this.encodingIn = encodingIn;
	}

	public CEncoding getEncodingOut() {
		return encodingOut;
	}

	public void setEncodingOut(CEncoding encodingOut) {
		this.encodingOut = encodingOut;
	}

	public static void main(String[] args) throws Exception {
		CFile file = CFileSystem.getExecuteDirectory().findOrCreateFile(
				"test.txt");
		String text = "hoge";
		file.saveText(text);
		String loadText = file.loadText();
		System.out.println("result=" + text.equals(loadText) + " text=" + text
				+ " loadText=" + loadText);

		byte[] contents = file.loadAsByte();
		System.out.println("contents:" + new String(contents));
		System.out.println("contentsSring:" + new String(contents));
		file.saveAsByte(contents);
		System.out.println("loadText:" + file.loadText());
		file.delete();
	}
}
