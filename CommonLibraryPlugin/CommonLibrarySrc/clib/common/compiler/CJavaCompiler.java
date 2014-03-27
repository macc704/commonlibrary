/*
 * CJavaCompiler.java
 * Created on 2011/06/03
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.common.compiler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;

import clib.common.filesystem.CDirectory;
import clib.common.filesystem.CFile;
import clib.common.filesystem.CFileSystem;
import clib.common.filesystem.CPath;
import clib.common.system.CEncoding;

/**
 * @author macchan
 */
public abstract class CJavaCompiler implements ICCompiler {

	private CDirectory base;
	private CDirectory sourceDir;
	private CDirectory destDir;
	private CEncoding encoding;

	// private List<String> classpaths = new ArrayList<String>();
	private CClasspathManager classpathManager;
	private List<CPath> targetPaths = new ArrayList<CPath>();

	public CJavaCompiler(CDirectory base) {
		this.base = base;
		sourceDir = base;
		destDir = base;
		classpathManager = new CClasspathManager(base);
	}

	public void setSourcepath(CPath path) {
		if (path.isRelative()) {
			path = base.getAbsolutePath().appendedPath(path);
		}
		this.sourceDir = CFileSystem.findDirectory(path);
	}

	protected CDirectory getSourceDir() {
		return sourceDir;
	}

	public void setDestpath(CPath path) {
		if (path.isRelative()) {
			path = base.getAbsolutePath().appendedPath(path);
		}
		this.destDir = CFileSystem.findDirectory(path);
	}

	public void setEncoding(CEncoding encoding) {
		this.encoding = encoding;
	}

	public void addSource(CPath relativePath) {
		if (!relativePath.isRelative()) {
			throw new RuntimeException();
		}
		targetPaths.add(relativePath);
	}

	public void clearSource() {
		targetPaths.clear();
	}

	public void addClasspath(CPath path) {
		classpathManager.addClasspath(path);
	}

	public void addClasspathDir(CPath path) {
		classpathManager.addClasspathDir(path);
	}

	protected Iterable<? extends JavaFileObject> getJavaTargetFiles(
			StandardJavaFileManager fileManager) {
		List<File> targetFiles = new ArrayList<File>();
		if (targetPaths.size() > 0) {
			for (CPath path : targetPaths) {
				targetFiles.add(sourceDir.findOrCreateFile(path)
						.getAbsolutePath().toJavaFile());
			}
		} else { // wild card.
			targetFiles.addAll(getAllJavaSources(sourceDir));
		}
		return fileManager.getJavaFileObjectsFromFiles(targetFiles);
	}

	protected List<File> getAllJavaSources(CDirectory dir) {
		List<File> files = new ArrayList<File>();
		for (CDirectory child : dir.getDirectoryChildren()) {
			files.addAll(getAllJavaSources(child));
		}
		for (CFile child : dir.getFileChildren()) {
			if (child.getName().getExtension().equals("java")) {
				files.add(child.toJavaFile());
			}
		}
		return files;
	}

	protected List<String> getParameters() {
		List<String> params = new ArrayList<String>();
		params.add("-d");
		params.add(destDir.getAbsolutePath().toString());
		params.add("-cp");
		params.add(classpathManager.createClassPathString(destDir
				.getAbsolutePath()));
		params.add("-sourcepath");
		params.add(sourceDir.getAbsolutePath().toString());
		if (encoding != null) {
			params.add("-encoding");
			params.add(encoding.toString());
		}
		return params;
	}

}
