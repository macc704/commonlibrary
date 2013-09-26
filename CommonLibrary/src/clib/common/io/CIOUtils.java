/*
 * CIOUtils.java
 * Created on 2007/09/21 by macchan
 * Copyright(c) 2007 CreW Project
 */
package clib.common.io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import clib.common.filesystem.CDirectory;
import clib.common.filesystem.CFile;
import clib.common.filesystem.CFileElement;
import clib.common.filesystem.CFileSystem;

/**
 * CIOUtils
 */
public class CIOUtils {

	public static void main(String[] args) {
		CDirectory in = CFileSystem.getExecuteDirectory().findDirectory("src");
		CFile out = CFileSystem.getExecuteDirectory().findOrCreateFile(
				"src.zip");
		if (in != null && out != null) {
			zip(in, out);
			System.out.println("done");
		} else {
			System.out.println("undone");
		}
	}

	/**
	 * コピーする
	 * 
	 * @param in
	 * @param out
	 */
	public static void copy(InputStream in, OutputStream out) {
		copy(in, out, true);
	}

	public static void copy(InputStream in, OutputStream out, boolean outclose) {
		try {
			// コピーする
			byte[] buf = new byte[1024];
			int nByte = 0;
			while ((nByte = in.read(buf)) > 0) {
				out.write(buf, 0, nByte);
			}

			// 後処理
			in.close();
			if (outclose) {
				out.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void zip(CDirectory inDir, CFile zipfile) {
		try {
			ZipOutputStream zos = new ZipOutputStream(
					zipfile.openOutputStream());
			encode(inDir, zos, inDir);
			zos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void encode(CFileElement target, ZipOutputStream zos,
			CDirectory baseDir) throws Exception {
		if (target.isDirectory()) {
			CDirectory dir = (CDirectory) target;
			for (CFileElement elm : dir.getChildren()) {
				encode(elm, zos, baseDir);
			}
		} else if (target.isFile()) {
			CFile file = (CFile) target;
			ZipEntry ze = new ZipEntry(file.getRelativePath(baseDir).toString());
			zos.putNextEntry(ze);
			InputStream is = new BufferedInputStream(file.openInputStream());
			copy(is, zos, false);
		} else {
			throw new RuntimeException();
		}
	}

	public static void unZip(CFile zipfile, CDirectory outDir) {
		try {
			ZipFile zipFile = new ZipFile(zipfile.toJavaFile());
			Enumeration<? extends ZipEntry> e = zipFile.entries();
			while (e.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) e.nextElement();
				if (entry.isDirectory()) {
					// do nothing
					// new File(entry.getName()).mkdirs(); <--これ，何？バグ？
				} else {
					File outfile = new File(outDir.getAbsolutePath().toString()
							+ "/" + entry.getName());

					// parentDir
					File parentDir = outfile.getParentFile();
					if (parentDir != null) {
						parentDir.mkdirs();
					}

					// copy file
					InputStream in = zipFile.getInputStream(entry);
					FileOutputStream out = new FileOutputStream(outfile);
					copy(in, out, true);
				}
			}
			zipFile.close();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
