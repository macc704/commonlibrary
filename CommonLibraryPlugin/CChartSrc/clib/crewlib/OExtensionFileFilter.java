package clib.crewlib;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * �g���q�Ŕ��ʂ���t�@�C���t�B���^
 * OExtensionFileFilter
 * 
 * @author bam
 * @version $Id: OExtensionFileFilter.java,v 1.2 2004/11/28 10:29:47 bam Exp $
 */
public class OExtensionFileFilter extends FileFilter {

	private String extension;
	private String description;

	/***************************
	 * Constructor(s)
	 ***************************/

	public OExtensionFileFilter(String extension, String description) {
		this.extension = extension;
		this.description = description;
	}

	/* (non-Javadoc)
	 * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
	 */
	public boolean accept(File f) {

		if (f.isDirectory()) {
			return true;
		}

		return f.getName().endsWith("." + extension);
	}

	/* (non-Javadoc)
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	public String getDescription() {
		return description;
	}

}
