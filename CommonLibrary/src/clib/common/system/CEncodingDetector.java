/*
 * CEncodingDetector.java
 * Created on 2012/02/18
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.common.system;

import java.io.File;
import java.io.InputStream;

import jp.matsuzawa.ed.EncodingDetector;

/**
 * @author macchan
 */
public class CEncodingDetector {

	public static CEncoding detect(File file) {
		return CEncoding.get(EncodingDetector.detect(file));
	}

	public static CEncoding detect(InputStream in) {
		return CEncoding.get(EncodingDetector.detect(in));
	}
}
