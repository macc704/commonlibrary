/*
 * CDiagnostic.java
 * Created on 2011/06/08
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.common.compiler;

import java.io.Serializable;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * @author macchan
 * 
 */
public class CDiagnostic implements Serializable {

	private static final long serialVersionUID = 2L;

	private Diagnostic.Kind kind;
	private String sourceName;
	private long position;
	private long startPosition;
	private long endPosition;
	private long lineNumber;
	private long columnNumber;
	private String code;
	private String message;
	private String version = "1.7.0_07"; // Javac version

	public CDiagnostic() {
		this.kind = Diagnostic.Kind.ERROR;
		this.sourceName = "no name";
		this.position = 0;
		this.startPosition = 0;
		this.endPosition = 0;
		this.lineNumber = 0;
		this.columnNumber = 0;
		this.code = "";
		this.message = "";
	}

	public CDiagnostic(Diagnostic<JavaFileObject> original) {
		this.kind = original.getKind();
		if (original.getSource() != null) {
			this.sourceName = original.getSource().getName();
		} else {
			this.sourceName = "no name";
		}
		this.position = original.getPosition();
		this.startPosition = original.getStartPosition();
		this.endPosition = original.getEndPosition();
		this.lineNumber = original.getLineNumber();
		this.columnNumber = original.getColumnNumber();
		this.code = original.getCode();
		this.message = original.getMessage(null);
	}

	public CDiagnostic(String message) {

		String[] line = message.split("\n");
		String[] text = line[0].split(":");

		this.sourceName = text[0];
		this.lineNumber = Integer.parseInt(text[1]);
		this.message = message;
	}

	public Diagnostic.Kind getKind() {
		return kind;
	}

	public String getSourceName() {
		return sourceName;
	}

	public String getNoPathSourceName() {
		int lastIndex = sourceName.lastIndexOf("\\") + 1;
		return sourceName.substring(lastIndex);
	}

	public long getPosition() {
		return position;
	}

	public long getStartPosition() {
		return startPosition;
	}

	public long getEndPosition() {
		return endPosition;
	}

	public long getLineNumber() {
		return lineNumber;
	}

	public long getColumnNumber() {
		return columnNumber;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public String getVersion() {
		return version;
	}

	public CMessageParser getMessageParser() {
		if (version.equals("1.6.0_31")) {
			return new CMessageParser160_31(message);
		} else if (version.equals("1.7.0_07")) {
			return new CMessageParser170_07(message);
		}
		return null;
	}

	/**
	 * @param kind
	 *            the kind to set
	 */
	public void setKind(Diagnostic.Kind kind) {
		this.kind = kind;
	}

	/**
	 * @param sourceName
	 *            the sourceName to set
	 */
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(long position) {
		this.position = position;
	}

	/**
	 * @param startPosition
	 *            the startPosition to set
	 */
	public void setStartPosition(long startPosition) {
		this.startPosition = startPosition;
	}

	/**
	 * @param endPosition
	 *            the endPosition to set
	 */
	public void setEndPosition(long endPosition) {
		this.endPosition = endPosition;
	}

	/**
	 * @param lineNumber
	 *            the lineNumber to set
	 */
	public void setLineNumber(long lineNumber) {
		this.lineNumber = lineNumber;
	}

	/**
	 * @param columnNumber
	 *            the columnNumber to set
	 */
	public void setColumnNumber(long columnNumber) {
		this.columnNumber = columnNumber;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	// public String getErrorMessage() {
	// return messageParser.getErrorMessage();
	// }
	//
	// public String getSymbol() {
	// return messageParser.getSymbol();
	// }
	//
	// public String getSymbolKind() {
	// return messageParser.getSymbolKind();
	// }
	//
	// public String getAbstractionMessage() {
	// String abstractionMessage = messageParser.getAbstractionMessage();
	// if (abstractionMessage == null) {
	// return messageParser.getErrorMessage();
	// }
	// return abstractionMessage;
	// }
	//
	// public List<String> getWord() {
	// return messageParser.getWords();
	// }
	//
	// public List<String> getWordKind() {
	// return messageParser.getWordKinds();
	// }
	//
	// public String getMessageKind() {
	// return messageParser.getMessageKind();
	// }
	//
	// public boolean isSemantic() {
	// return messageParser.isSemantic();
	// }
	//
	// public boolean isSyntax() {
	// return messageParser.isSyntax();
	// }
	//
	/**
	 * 2つのエラーが同じエラーかを判断します
	 */
	public boolean isSameKind(CDiagnostic another) {

		// TODO wordでも判断する必要あり
		CMessageParser parser = getMessageParser();
		if (parser.getSymbol() != null) {
			if (parser.getErrorMessage().equals(
					another.getMessageParser().getErrorMessage())
					&& parser.getSymbol().equals(
							another.getMessageParser().getSymbol())
					&& parser.getSymbolKind().equals(
							another.getMessageParser().getSymbolKind())) {
				return true;
			}
		} else {
			if (parser.getErrorMessage().equals(
					another.getMessageParser().getErrorMessage())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return getLineNumber() + "行目: " + getMessageParser().getErrorMessage();
	}
}
