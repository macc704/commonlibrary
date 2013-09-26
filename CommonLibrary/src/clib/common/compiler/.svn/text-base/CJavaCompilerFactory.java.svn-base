package clib.common.compiler;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import clib.common.filesystem.CDirectory;

public class CJavaCompilerFactory {

	private static JavaCompiler compiler;

	static {
		compiler = ToolProvider.getSystemJavaCompiler();
	}

	public static boolean hasEmbededJavaCompiler() {
		return compiler != null;
	}

	public static CJavaCompiler createCompiler(CDirectory base) {
		if (hasEmbededJavaCompiler()) {
			return new CEmbededJavaCompiler(base, compiler);
		} else {
			return new CJavacJavaCompiler(base);
		}
	}
}
