package clib.common.compiler;

import java.util.ArrayList;
import java.util.List;

public class CMessageParser170_07 extends CMessageParser {

	public CMessageParser170_07(String message) {
		super(message);
		try {
			parseMessage();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void parseMessage() throws Exception {
		String[] lines = getMessage().split("\r?\n");

		// エラーメッセージの設定
		setErrorMessage(lines);

		// シンボルの設定
		setSymbol(lines);

		// メッセージの解析
		analyzeErrorMessage();

		// 意味構文解析
		setMessageKind();

	}

	private void setErrorMessage(String[] lines) throws Exception {
		String errorMessage = null;
		if (lines[0].split(": ").length > 2) {
			errorMessage = lines[0].split(": ")[2];
		} else if (lines[0].split(": ").length == 2) {
			errorMessage = lines[0].split(": ")[1];
		} else {
			errorMessage = lines[0];
		}
		super.setErrorMessage(errorMessage);
	}

	private void setSymbol(String[] lines) throws Exception {
		for (int i = 0; i < lines.length; i++) {
			if (lines[i].startsWith("  シンボル:")) {
				String[] tokens = lines[i].split(" ");
				if (tokens.length == 7) {
					super.setSymbol(tokens[6]);
					super.setSymbolKind(tokens[5]);
				} else if (tokens.length == 6) {
					super.setSymbol(tokens[5]);
					super.setSymbolKind(tokens[6]);
				}
			}
		}
	}

	private void analyzeErrorMessage() throws Exception {

		String abstractionMessage = null;
		List<String> words = new ArrayList<String>();
		List<String> wordKinds = new ArrayList<String>();

		String errorMessage = super.getErrorMessage();
		String lines[] = errorMessage.split(" ");

		if (super.getMessage().contains("警告:")) {
			return;
		}

		// TODO contains使えばもっと計算量削れそう
		// 以下未実装
		// 例外Aは報告されません。スローするにはキャッチまたは、スロー宣言をしなければなりません。
		switch (lines.length) {
		case 1:
			if (errorMessage.endsWith("は不正な文字です")) {
				abstractionMessage = "Aは不正な文字です";
				words.add(errorMessage.substring(0, errorMessage.indexOf("は不正")));
				wordKinds.add(WORD);
			} else if (errorMessage.endsWith("にアクセスできません")) {
				abstractionMessage = "Aにアクセスできません";
				words.add(errorMessage.substring(0,
						errorMessage.indexOf("にアクセス")));
				wordKinds.add(WORD);
			} else if (errorMessage.endsWith("間接参照できません")) {
				abstractionMessage = "型Aは間接参照できません";
				words.add(errorMessage.substring(1, errorMessage.indexOf("は間接")));
				wordKinds.add(TYPE);
			} else if (errorMessage.endsWith("継承がループしています")) {
				abstractionMessage = "Aを含む継承がループしています";
				words.add(errorMessage.substring(0, errorMessage.indexOf("を含む")));
				wordKinds.add(CLASS);
			} else if (errorMessage.startsWith("配列が要求")) {
				abstractionMessage = "配列が要求されましたが、Aが見つかりました";
				words.add(errorMessage.substring(12,
						errorMessage.indexOf("が見つかり")));
				wordKinds.add(TYPE);
			} else if (errorMessage.endsWith("初期化子が不正です")) {
				abstractionMessage = "Aの初期化子が不正です";
				words.add(errorMessage.substring(0,
						errorMessage.indexOf("の初期化子")));
				wordKinds.add(TYPE);
			} else if (errorMessage.endsWith("パラメータをとりません")) {
				abstractionMessage = "型Aはパラメータをとりません";
				words.add(errorMessage.substring(0,
						errorMessage.indexOf("はパラメータ")));
				wordKinds.add(TYPE);
			} else if (errorMessage.contains("初期化されていない可能性")) {
				abstractionMessage = "変数Aは初期化されていない可能性があります";
				words.add(errorMessage.substring(3,
						errorMessage.indexOf("は初期化")));
				wordKinds.add(VAR);
			} else if (errorMessage.startsWith("パッケージ")) {
				abstractionMessage = "パッケージAは存在しません";
				words.add(errorMessage.substring(6,
						errorMessage.indexOf("は存在しません")));
				wordKinds.add(PACK);
			} else if (errorMessage.startsWith("整数")) {
				abstractionMessage = "整数が大きすぎます";
				words.add(errorMessage.substring(2, errorMessage.indexOf("が大き")));
				wordKinds.add(NUM);
			} else if (errorMessage.endsWith("重複しています")) {
				abstractionMessage = "クラスAが重複しています";
				words.add(errorMessage.substring(3, errorMessage.indexOf("が重複")));
				wordKinds.add(CLASS);
			} else if (errorMessage.endsWith("インスタンスを生成することはできません")) {
				abstractionMessage = "Aはabstractです。インスタンスを生成することはできません";
				words.add(errorMessage.substring(0,
						errorMessage.indexOf("はabst")));
				wordKinds.add(CLASS);
			} else if (errorMessage.endsWith("比較できません")) {
				abstractionMessage = "型Aと型Bは比較できません";
				words.add(errorMessage.substring(1, errorMessage.indexOf("と")));
				words.add(errorMessage.substring(errorMessage.indexOf("と") + 1,
						errorMessage.indexOf("は比較")));
				wordKinds.add(TYPE);
				wordKinds.add(TYPE);
			} else if (errorMessage.startsWith("クラス")
					&& errorMessage.endsWith("で宣言する必要があります")) {
				abstractionMessage = "クラスAはpublicであり、ファイルBで宣言する必要があります";
				String ident = errorMessage.substring(3,
						errorMessage.indexOf("はpublic"));
				words.add(ident);
				words.add(ident + ".java");
				wordKinds.add(CLASS);
				wordKinds.add(FILE);
			} else if (errorMessage.endsWith("privateアクセスされます")) {
				abstractionMessage = "AはBでprivateアクセスされます";
				words.add(errorMessage.substring(0, errorMessage.indexOf("は")));
				words.add(errorMessage.substring(errorMessage.indexOf("は"),
						errorMessage.indexOf("でprivate")));
				wordKinds.add(VAR);
				wordKinds.add(CLASS);
			} else if (errorMessage.endsWith("protectedアクセスされます")) {
				abstractionMessage = "AはBでprotectedアクセスされます";
				words.add(errorMessage.substring(0, errorMessage.indexOf("は")));
				words.add(errorMessage.substring(errorMessage.indexOf("は"),
						errorMessage.indexOf("でprotected")));
				wordKinds.add(METHOD);
				wordKinds.add(CLASS);
			} else if (errorMessage.startsWith("内部クラス")) {
				abstractionMessage = "内部クラスAの静的宣言が不正です";
				words.add(errorMessage.substring(5, errorMessage.indexOf("の静的")));
				wordKinds.add(CLASS);
			} else if (errorMessage.endsWith("オーバーライドできません")) {
				abstractionMessage = "クラスAのメソッドBはクラスCのメソッドBをオーバーライドできません";
				// 日本語クラスやメソッドが出てくるとバグになりやすい
				words.add(errorMessage.substring(0, errorMessage.indexOf("の")));
				words.add(errorMessage.substring(errorMessage.indexOf("の") + 1,
						errorMessage.indexOf("は")));
				words.add(errorMessage.substring(errorMessage.indexOf("は") + 1,
						errorMessage.lastIndexOf("の")));
				words.add(errorMessage.substring(
						errorMessage.lastIndexOf("の") + 1,
						errorMessage.indexOf("をオーバー")));
				wordKinds.add(CLASS);
				wordKinds.add(METHOD);
				wordKinds.add(CLASS);
			} else if (errorMessage.startsWith("単項演算子")) {// 変化あり
				abstractionMessage = "単項演算子Aのオペランド型Bが不正です";
				words.add(errorMessage.substring(6, 8));
				words.add(errorMessage.substring(16,
						errorMessage.indexOf("が不正です")));
				wordKinds.add(OPERATOR);
				wordKinds.add(TYPE);
			} else if (errorMessage.startsWith("二項演算子")) {// 変化あり
				abstractionMessage = "二項演算子Aのオペランド型が不正です";
				words.add(errorMessage.substring(6, 8));
				wordKinds.add(OPERATOR);
			} else if (errorMessage.contains("適切なメソッドが見つかりません")) {// 新しいエラー文
				abstractionMessage = "Aに適切なメソッドが見つかりません";
				words.add(errorMessage.substring(0,
						errorMessage.indexOf("に適切な")));
				words.add(errorMessage.substring(errorMessage.indexOf("("),
						errorMessage.indexOf(")")));
				wordKinds.add(METHOD);
				wordKinds.add(TYPE);
			} else if (errorMessage.contains("適切なコンストラクタが見つかりません")) {// 新しいエラー文
				abstractionMessage = "Aに適切なコンストラクタが見つかりません";
				words.add(errorMessage.substring(0,
						errorMessage.indexOf("に適切な")));
				words.add(errorMessage.substring(errorMessage.indexOf("("),
						errorMessage.indexOf(")")));
				wordKinds.add(METHOD);
				wordKinds.add(TYPE);
			} else if (errorMessage.startsWith("型引数")) { // 新しいエラー文
				abstractionMessage = "型引数Aは型変数Bの境界内にありません";
				words.add(errorMessage.substring(3,
						errorMessage.indexOf("は型変数")));
				words.add(errorMessage.substring(
						errorMessage.indexOf("型変数") + 3,
						errorMessage.indexOf("の境界")));
				wordKinds.add(TYPE);
				wordKinds.add(TYPE);
			} else if (errorMessage.startsWith("例外")) {
				abstractionMessage = "例外Aは報告されません。スローするには、捕捉または宣言する必要があります";
				words.add(errorMessage.substring(2,
						errorMessage.indexOf(("は報告"))));
				wordKinds.add(EXCEPTION);
			} else if (errorMessage.startsWith("ラベル")
					&& errorMessage.endsWith("未定義です")) {
				abstractionMessage = "ラベルAは未定義です";
				words.add(errorMessage.substring(3,
						errorMessage.indexOf("は未定義")));
				wordKinds.add(LABEL);
			}
			break;

		case 2:
			if (errorMessage.startsWith("staticでない")) {
				if (errorMessage.contains("変数")) {
					abstractionMessage = "staticでない変数Aをstaticコンテキストから参照することはできません";
					words.add(lines[1].substring(0, lines[1].indexOf("をsta")));
					wordKinds.add(VAR);
				} else if (errorMessage.contains("メソッド")) {
					abstractionMessage = "staticでないメソッドAをstaticコンテキストから参照することはできません";
					words.add(lines[1].substring(0, lines[1].indexOf("をsta")));
					wordKinds.add(METHOD);
				}
			}
			break;

		case 3:

			if (errorMessage.endsWith("指定された型に適用できません。")) { // 変化あり
				if (errorMessage.contains("メソッド")) {
					abstractionMessage = "クラスAのメソッドBは指定された型に適用できません。";
					words.add(lines[1].substring(0, lines[1].indexOf("のメソッド")));
					words.add(lines[2].substring(0, lines[2].indexOf("は指定")));
					wordKinds.add(CLASS);
					wordKinds.add(METHOD);
				} else if (errorMessage.contains("コンストラクタ")) {
					abstractionMessage = "クラスAのコンストラクタBは指定された型に適用できません。";
					words.add(lines[1].substring(0,
							lines[1].indexOf("のコンストラクタ")));
					words.add(lines[2].substring(0, lines[2].indexOf("は指定")));
					wordKinds.add(CLASS);
					wordKinds.add(CONST);
				}
			} else if (errorMessage.startsWith("変数")
					&& errorMessage.endsWith("で定義されています")) { // 変化あり
				abstractionMessage = "変数AはすでにメソッドBで定義されています";
				wordKinds.add(VAR);
				wordKinds.add(METHOD);
				words.add(lines[1].substring(0, lines[1].indexOf("はすでに")));
				words.add(lines[2].substring(0, lines[2].indexOf("で定義")));
			} else if (errorMessage.startsWith("メソッド")
					&& errorMessage.endsWith("で定義されています")) {
				abstractionMessage = "メソッドAはすでにクラスBで定義されています";
				wordKinds.add(METHOD);
				wordKinds.add(CLASS);
				words.add(lines[1].substring(0, lines[1].indexOf("はすでに")));
				words.add(lines[2].substring(0, lines[2].indexOf("で定義")));
			} else if (errorMessage.startsWith("クラス")
					&& errorMessage.endsWith("で定義されています")) {
				abstractionMessage = "クラスAはすでにパッケージBで定義されています";
				wordKinds.add(CLASS);
				wordKinds.add(PACK);
				words.add(lines[1].substring(0, lines[1].indexOf("はすでに")));
				words.add(lines[2].substring(0, lines[2].indexOf("で定義")));
			}
			break;
		}

		super.setAbstractionMessage(abstractionMessage);
		super.setWords(words);
		super.setWordKinds(wordKinds);
	}

	private void setMessageKind() {

		String message = super.getErrorMessage();
		if (getAbstractionMessage() != null) {
			message = getAbstractionMessage();
		}

		if (super.getMessage().contains("再コンパイル")
				|| super.getMessage().contains("推奨されない")
				|| super.getMessage().endsWith("未チェックまたは安全ではありません。")) {
			super.setMessageKind(WARNING);
			return;
		}

		List<String> semanticMessages = getSemanticMessages();

		if (semanticMessages.contains(message)) {
			super.setMessageKind(SEMANTIC);
		} else {
			super.setMessageKind(SYNTAX);
		}
	}

	// TODO 未実装
	private List<String> getSemanticMessages() {
		List<String> semanticMessages = new ArrayList<String>();

		// 意味解析エラーのメッセージをリストに追加
		semanticMessages.add("");

		return semanticMessages;
	}

}
