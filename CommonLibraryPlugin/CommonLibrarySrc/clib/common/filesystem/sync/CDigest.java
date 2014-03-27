package clib.common.filesystem.sync;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import clib.common.filesystem.CFile;
import clib.common.filesystem.CFileSystem;

public class CDigest {

	public static String getSha1(CFile file) {
		try {
			String sha1 = DigestUtils.sha1Hex(file.openInputStream());
			return sha1;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public static final String target3M = "/Users/macchan/Dropbox/Projects/2013/20131220IJODE2/20140105文献再/Graesser1995CognitivePattern.pdf";
	public static final String target300M = "/Users/macchan/Desktop/eclipses/eclipse-rcp-kepler-R-macosx-cocoa-x86_64.tar.gz";

	@Test
	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		String sha1 = getSha1(CFileSystem.findFile(target300M));
		long time = System.currentTimeMillis() - start;
		System.out.println(sha1 + ", " + time + "ms");
	}
}
