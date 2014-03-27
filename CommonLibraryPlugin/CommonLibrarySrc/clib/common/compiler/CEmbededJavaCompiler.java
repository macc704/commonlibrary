package clib.common.compiler;

import java.util.ArrayList;
import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;

import clib.common.filesystem.CDirectory;

public class CEmbededJavaCompiler extends CJavaCompiler {

	private JavaCompiler compiler;

	public CEmbededJavaCompiler(CDirectory base, JavaCompiler compiler) {
		super(base);
		this.compiler = compiler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see clib.compiler.ICCompiler#doCompile()
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public CCompileResult doCompile() {

		DiagnosticCollector<JavaFileObject> collector = new DiagnosticCollector<JavaFileObject>();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(
				collector, null, null);
		JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager,
				collector, getParameters(), null,
				getJavaTargetFiles(fileManager));
		boolean success = task.call();
		List<Diagnostic> diagnostics = (List) collector.getDiagnostics();
		List<CDiagnostic> cDiagnostics = new ArrayList<CDiagnostic>();
		for (Diagnostic diagnostic : diagnostics) {
			cDiagnostics.add(new CDiagnostic(diagnostic));
		}
		CCompileResult result = new CCompileResult(success, /* targetPaths, */
		cDiagnostics);
		return result;
	}

}
