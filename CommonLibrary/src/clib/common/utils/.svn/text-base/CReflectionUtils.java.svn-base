/*
 * CReflectionUtils.java
 * Created on 2012/05/15
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.common.utils;

/**
 * @author macchan
 * 
 */
public class CReflectionUtils {

	public static Class<?> toObjectClass(Class<?> type) {
		if (type.isPrimitive()) {
			return toObjectClass0(type);
		}
		return type;
	}

	private static Class<?> toObjectClass0(Class<?> type) {
		if (type.equals(int.class)) {
			return Integer.class;
		} else if (type.equals(short.class)) {
			return Short.class;
		} else if (type.equals(long.class)) {
			return Long.class;
		} else if (type.equals(float.class)) {
			return Float.class;
		} else if (type.equals(double.class)) {
			return Double.class;
		} else if (type.equals(char.class)) {
			return Character.class;
		} else if (type.equals(byte.class)) {
			return Byte.class;
		} else if (type.equals(boolean.class)) {
			return Boolean.class;
		}
		throw new RuntimeException();
	}
}
