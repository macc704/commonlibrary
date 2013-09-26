package clib.common.compiler;

import java.util.List;

abstract public class CMessageParser {

	// メッセージに含まれる識別子の種類
	protected static final String VAR = "変数";
	protected static final String METHOD = "メソッド";
	protected static final String CONST = "コンストラクタ";
	protected static final String CLASS = "クラス";
	protected static final String FILE = "ファイル";
	protected static final String PACK = "パッケージ";
	protected static final String WORD = "文字";
	protected static final String OPERATOR = "演算子";
	protected static final String TYPE = "型";
	protected static final String PARAM = "引数";
	protected static final String NUM = "数値";
	protected static final String LABEL = "ラベル";
	protected static final String EXCEPTION = "例外";

	// エラーの種類
	protected static final String WARNING = "警告";
	protected static final String SEMANTIC = "意味解析エラー";
	protected static final String SYNTAX = "構文解析エラー";

	private String message;
	private String errorMessage;
	private String abstractionMessage;
	private String symbol;
	private String symbolKind;
	private List<String> words;
	private List<String> wordKinds;
	private String messageKind; // 警告・意味・構文エラー

	public CMessageParser(String message) {
		this.message = message;
	}

	abstract protected void parseMessage() throws Exception;

	public String getMessage() {
		return message;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getAbstractionMessage() {
		if (abstractionMessage == null) {
			return errorMessage;
		}
		return abstractionMessage;
	}

	public void setAbstractionMessage(String abstractionMessage) {
		this.abstractionMessage = abstractionMessage;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getSymbolKind() {
		return symbolKind;
	}

	public void setSymbolKind(String symbolKind) {
		this.symbolKind = symbolKind;
	}

	public List<String> getWords() {
		return words;
	}

	public void setWords(List<String> words) {
		this.words = words;
	}

	public List<String> getWordKinds() {
		return wordKinds;
	}

	public void setWordKinds(List<String> wordKinds) {
		this.wordKinds = wordKinds;
	}

	public String getMessageKind() {
		return messageKind;
	}

	public void setMessageKind(String messageKind) {
		this.messageKind = messageKind;
	}

	public boolean isSemantic() {
		return getMessageKind().equals(SEMANTIC);
	}

	public boolean isSyntax() {
		return getMessageKind().equals(SYNTAX);
	}

}
