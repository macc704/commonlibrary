package clib.common.compiler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import clib.common.execution.CCommandExecuter;
import clib.common.filesystem.CDirectory;
import clib.common.filesystem.CPath;
import clib.common.system.CEncoding;

public class CJavacJavaCompiler extends CJavaCompiler {

	public CJavacJavaCompiler(CDirectory base) {
		super(base);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see clib.compiler.ICCompiler#doCompile()
	 */
	public CCompileResult doCompile() {
		return doCompileByJavac();

	}

	private CCompileResult doCompileByJavac() {
		try {
			String enc = CEncoding.UTF8.toString();
			CCommandExecuter executer = new CCommandExecuter();

			ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
			PrintStream err = new PrintStream(byteArray, true, enc);
			executer.setErr(err);
			executer.setVerbose(true);
			List<String> commands = getParameters();
			commands.add(0, "javac");
			List<String> filenames = new ArrayList<String>();
			for (File file : getAllJavaSources(getSourceDir())) {
				filenames.add(file.getAbsolutePath());
				// なぜか，これでは動かない
				// filenames.add(new CPath(file.getAbsolutePath())
				// .getRelativePath(sourceDir.getAbsolutePath())
				// .toString());
			}
			commands.addAll(filenames);
			executer.executeCommandAndWait(commands);

			// 解析
			String messages = byteArray.toString(enc);
			// System.out.println(messages);
			boolean success = messages.length() <= 0;

			List<CDiagnostic> diagnostics;
			if (success) {
				diagnostics = new ArrayList<CDiagnostic>();
			} else {
				diagnostics = parseJavaError(messages);
			}
			CCompileResult result = new CCompileResult(success, /* targetPaths, */
			diagnostics);
			return result;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	private List<CDiagnostic> parseJavaError(String error) {
		String[] lines = error.split("\r?\n");
		List<String> linesList = new ArrayList<String>();
		for (int i = 0; i < lines.length; i++) {
			linesList.add(lines[i]);
		}
		return parseJavaError(linesList);
	}

	private List<CDiagnostic> parseJavaError(List<String> lines) {
		lines.remove(lines.size() - 1);// the last line
		List<List<String>> messages = new ArrayList<List<String>>();
		List<String> message = null;
		for (String line : lines) {
			if (line.indexOf(".java:") != -1) {
				if (message != null) {
					messages.add(message);
				}
				message = new ArrayList<String>();
			}

			if (message == null) {
				throw new RuntimeException();
			}
			message.add(line);
		}
		if (message != null) {
			messages.add(message);
		}
		return parseJavaErrorMessages(messages);
	}

	private List<CDiagnostic> parseJavaErrorMessages(List<List<String>> messages) {
		List<CDiagnostic> diagnostics = new ArrayList<CDiagnostic>();
		for (List<String> message : messages) {
			// System.out.println(message);
			CDiagnostic diagnostic = parseJavaErrorMessage(message);
			diagnostics.add(diagnostic);
		}
		return diagnostics;
	}

	private CDiagnostic parseJavaErrorMessage(List<String> messages) {
		CDiagnostic diag = new CDiagnostic();

		String title = messages.get(0);
		String[] a = title.split(": ");
		String[] b = a[0].split(":");
		int line = Integer.parseInt(b[b.length - 1]);
		String filename = new CPath(b[b.length - 2]).getName().toString();
		diag.setLineNumber(line);
		diag.setSourceName(filename);
		for (String message : messages) {
			if (message.matches("[\\s]*[\\^]")) {
				diag.setPosition(countSpace(message));
			}
		}

		String content = messages.get(0);
		for (int i = 1; i < messages.size(); i++) {
			content += "\n" + messages.get(i);
		}
		diag.setMessage(content);

		// String symbol = messages.get(1);
		// String [] c = symbol.split(":");
		// if(c[0].equals("シンボル")){
		// if(c[1].startsWith(" 変数 ")){
		// diag.setSymbolKind("変数");
		// diag.setSymbol(c[1].substring(4));
		// }else if(c[1].startsWith(" メソッド")){
		// diag.setSymbolKind("メソッド");
		// diag.setSymbol(c[1].substring(6));
		// }else{
		// diag.setSymbolKind("クラス");
		// diag.setSymbol(c[1].substring(5));
		// }
		// }

		return diag;
	}

	private int countSpace(String line) {
		char[] array = line.toCharArray();
		for (int i = 0; i < array.length; i++) {
			if (array[i] != ' ') {
				return i;
			}
		}
		return -1;
	}
}
