/*
 * CEncoding.java
 * Created on 2010/02/15 by macchan
 * Copyright(c) 2010 Yoshiaki Matsuzawa, Shizuoka University
 */
package clib.common.system;

/**
 * CEncoding
 */
public class CEncoding {
	public static final CEncoding UNKNOWN = new CEncoding("UNKNOWN");
	public static final CEncoding Shift_JIS = new CEncoding("SJIS");
	public static final CEncoding UTF8 = new CEncoding("UTF-8");
	public static final CEncoding UTF8WBOM = new CEncoding("UTF-8");
	public static final CEncoding JISAutoDetect = new CEncoding("JISAutoDetect");

	public static CEncoding getVMEncoding() {
		return new CEncoding(System.getProperty("file.encoding"));
	}

	public static CEncoding getSystemEncoding() {
		return new CEncoding(System.getProperty("sun.jnu.encoding"));
	}

	public static CEncoding get(String text) {
		String upperCase = text.toUpperCase();
		if ("UTF8".equals(upperCase)) {
			return UTF8;
		} else if ("UTF-8".equals(upperCase)) {
			return UTF8;
		} else if ("SHIFT_JIS".equals(upperCase)) {
			return Shift_JIS;
		} else if ("SJIS".equals(upperCase)) {
			return Shift_JIS;
		} else if ("JISAUTODETECT".equals(upperCase)) {
			return JISAutoDetect;
		} else if ("UNKNOWN".equals(upperCase)) {
			return UNKNOWN;
		}
		// else
		return new CEncoding(text);
	}

	private String text;

	private CEncoding(String text) {
		this.text = text;
	}

	public String toString() {
		return text;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return text.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof CEncoding)) {
			return false;
		}
		return text.equals(((CEncoding) obj).text);
	}
}
