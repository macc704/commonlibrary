package clib.common.compiler;

import java.util.ArrayList;
import java.util.List;

public class CMessageParser160_31 extends CMessageParser {

	public CMessageParser160_31(String message) {
		super(message);
		parseMessage();
	}

	@Override
	protected void parseMessage() {
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

	private void setErrorMessage(String[] lines) {
		String errorMessage = null;
		if (lines[0].split(": ").length > 2) {
			errorMessage = lines[0].split(": ")[1] + ": "
					+ lines[0].split(": ")[2];
		} else if (lines[0].split(": ").length == 2) {
			errorMessage = lines[0].split(": ")[1];
		} else {
			errorMessage = lines[0];
		}
		super.setErrorMessage(errorMessage);
	}

	private void setSymbol(String[] lines) {
		if (lines.length >= 2) {
			String[] tokens = lines[1].split(" ");
			if ("シンボル:".equals(tokens[0])) {
				super.setSymbol(tokens[2]);
				super.setSymbolKind(tokens[1]);
			}
		}
	}

	private void analyzeErrorMessage() {

		String abstractionMessage = null;
		List<String> words = new ArrayList<String>();
		List<String> wordKinds = new ArrayList<String>();

		String errorMessage = getErrorMessage();
		String lines[] = errorMessage.split(" ");

		switch (lines.length) {
		case 2:
			if (errorMessage.endsWith("は不正な文字です。")) {
				abstractionMessage = "Aは不正な文字です。";
				words.add(lines[0]);
				wordKinds.add(WORD);
			} else if (errorMessage.endsWith("にアクセスできません。")) {
				abstractionMessage = "Aにアクセスできません。";
				words.add(lines[0]);
				wordKinds.add(WORD);
			} else if (errorMessage.endsWith("は間接参照できません。")) {
				abstractionMessage = "型Aは間接参照できません。";
				words.add(lines[0]);
				wordKinds.add(TYPE);
			} else if (errorMessage.endsWith("を含む継承がループしています。")) {
				abstractionMessage = "Aを含む継承がループしています。";
				words.add(lines[0]);
				wordKinds.add(CLASS);
			} else if (errorMessage.startsWith("配列が要求")) {
				abstractionMessage = "配列が要求されましたが、型Aが見つかりました。";
				words.add(lines[0].split("、")[1]);
				wordKinds.add(TYPE);
			} else if (errorMessage.endsWith("初期化子が不正です。")) {
				abstractionMessage = "型Aの初期化子が不正です。";
				words.add(lines[0]);
				wordKinds.add(TYPE);
			}
			break;
		case 3:
			if (errorMessage.endsWith("パラメータをとりません。")) {
				abstractionMessage = "型Aはパラメータをとりません。";
				words.add(lines[1]);
				wordKinds.add(TYPE);
			} else if (errorMessage.endsWith("は初期化されていない可能性があります。")) {
				abstractionMessage = "変数Aは初期化されていない可能性があります。";
				words.add(lines[1]);
				wordKinds.add(VAR);
			} else if (errorMessage.startsWith("パッケージ")
					&& errorMessage.endsWith("は存在しません。")) {
				abstractionMessage = "パッケージAは存在しません。";
				words.add(lines[1]);
				wordKinds.add(PACK);
			} else if (errorMessage.startsWith("整数")
					&& errorMessage.endsWith("が大き過ぎます。")) {
				abstractionMessage = "整数が大き過ぎます。";
				words.add(lines[1]);
				wordKinds.add(NUM);
			} else if (errorMessage.startsWith("ラベル ")
					&& errorMessage.endsWith("未定義です。")) {
				abstractionMessage = "ラベルAは未定義です。";
				words.add(lines[1]);
				wordKinds.add(LABEL);
			} else if (errorMessage.startsWith("クラス")
					&& errorMessage.endsWith("重複しています。")) {
				abstractionMessage = "クラスAが重複しています。";
				words.add(lines[1]);
				wordKinds.add(CLASS);
			} else if (errorMessage.startsWith("例外")) {
				abstractionMessage = "例外Aは報告されません。スローするにはキャッチまたは、スロー宣言をしなければなりません。";
				words.add(lines[1]);
				wordKinds.add(EXCEPTION);
			}
			break;
		case 4:
			if (errorMessage.endsWith("で定義されています。")) {
				abstractionMessage = "変数またはメソッドAはメソッドまたはクラスまたはパッケージBで定義されています。";
				words.add(lines[0]);
				words.add(lines[2]);
				wordKinds.add(VAR);
				if (lines[2].endsWith(")")) {
					wordKinds.add(METHOD);
				} else {
					wordKinds.add(CLASS + "または" + PACK);
				}
			} else if (errorMessage.endsWith("オーバーライドしています。")) {
				abstractionMessage = "注:ファイルAは推奨されないAPIを使用またはオーバーライドしています。";
				String fileName = errorMessage.split("\\\\")[errorMessage
						.split("\\\\").length - 1].split(" ")[0];
				words.add(fileName);
				wordKinds.add(FILE);
			} else if (errorMessage.endsWith("インスタンスを生成することはできません。")) {
				abstractionMessage = "型Aはabstractです。インスタンスを生成することはできません。";
				words.add(lines[0]);
				wordKinds.add(TYPE);
			}
			break;
		case 5:
			if (errorMessage.startsWith("演算子")
					&& errorMessage.endsWith("に適用できません。")) {
				abstractionMessage = "演算子Aは型Bに適用できません。";
				words.add(lines[1]);
				words.add(lines[3]);
				wordKinds.add(OPERATOR);
				wordKinds.add(TYPE);
			} else if (errorMessage.startsWith("型")
					&& errorMessage.endsWith("は比較できません。")) {
				abstractionMessage = "型Aと型Bは比較できません。";
				words.add(lines[1]);
				words.add(lines[3]);
				wordKinds.add(TYPE);
				wordKinds.add(TYPE);
			} else if (errorMessage.endsWith("unnamed package で定義されています。")) {
				abstractionMessage = "変数またはメソッドAはメソッドまたはクラスまたはパッケージBで定義されています。";
				words.add(lines[0]);
				words.add("unnamed package");
				wordKinds.add(VAR);
				wordKinds.add(PACK);
			}
			break;
		default:
			if (errorMessage.startsWith("クラス")
					&& errorMessage.endsWith("で宣言しなければなりません。")) {
				abstractionMessage = "クラスAはpublicであり、ファイルBで宣言しなければなりません。";
				words.add(lines[1]);
				words.add(lines[5]);
				wordKinds.add(CLASS);
				wordKinds.add(FILE);
			} else if (errorMessage.endsWith("に適用できません")) {
				abstractionMessage = "メソッドAを引数Bに適用できません";
				words.add(lines[0] + lines[1] + lines[2]);
				words.add(lines[4]);
				wordKinds.add(METHOD);
				wordKinds.add(PARAM);
			} else if (errorMessage.endsWith("を static コンテキストから参照することはできません。")) {
				if (errorMessage.startsWith("static でない メソッド")) {
					abstractionMessage = "staticでないメソッドAをstaticコンテキストから参照することはできません。";
					wordKinds.add(METHOD);
				} else if (errorMessage.startsWith("static でない 変数")) {
					abstractionMessage = "staticでない変数Aをstaticコンテキストから参照することはできません。";
					wordKinds.add(VAR);
				}
				words.add(lines[3]);
			} else if (errorMessage.endsWith("で private アクセスされます。")) {
				abstractionMessage = "AはBでprivateアクセスされます。";
				words.add(lines[0]);
				words.add(lines[2]);
				wordKinds.add(VAR);
				wordKinds.add(CLASS);
			} else if (errorMessage.endsWith("インスタンス生成できません。")) {
				abstractionMessage = "内部エラーです。AをBで()にインスタンス生成できません。";
				words.add(lines[0].split("。")[1]);
				words.add(lines[2]);
				wordKinds.add(WORD);
				wordKinds.add(TYPE);
			} else if (errorMessage.endsWith("アクセス特権を割り当てようとしました。")) {
				abstractionMessage = "クラスAのメソッドBはクラスCのメソッドBをオーバーライドできません。スーパークラスでの定義(public)より弱いアクセス特権を割り当てようとしました。";
				words.add(lines[0]);
				words.add(lines[2]);
				words.add(lines[4]);
				wordKinds.add(CLASS);
				wordKinds.add(METHOD);
				wordKinds.add(CLASS);
			}
		}

		setAbstractionMessage(abstractionMessage);
		setWords(words);
		setWordKinds(wordKinds);
	}

	private void setMessageKind() {

		String message = super.getErrorMessage();
		if (getAbstractionMessage() != null) {
			message = getAbstractionMessage();
		}

		if (message.startsWith("注:")) {
			super.setMessageKind(WARNING);
			return;
		}

		List<String> semanticErrorMessages = getSemanticMessages();
		if (semanticErrorMessages.contains(message)) {
			super.setMessageKind(SEMANTIC);
		} else {
			super.setMessageKind(SYNTAX);
		}
	}

	private List<String> getSemanticMessages() {
		List<String> semanticErrorMessages = new ArrayList<String>();

		// 意味解析エラーのメッセージをリストに追加
		semanticErrorMessages.add("シンボルを見つけられません。");
		semanticErrorMessages.add("メソッドAを引数Bに適用できません");
		semanticErrorMessages.add("変数またはメソッドAはメソッドまたはクラスまたはパッケージBで定義されています。");
		semanticErrorMessages.add("互換性のない型");
		semanticErrorMessages.add("変数Aは初期化されていない可能性があります。");
		semanticErrorMessages.add("演算子Aは型Bに適用できません。");
		semanticErrorMessages.add("精度が落ちている可能性");
		semanticErrorMessages.add("この文に制御が移ることはありません。");
		semanticErrorMessages.add("AはBでprivateアクセスされます。");
		semanticErrorMessages.add("return 文が指定されていません。");
		semanticErrorMessages.add("staticでないメソッドAをstaticコンテキストから参照することはできません。");
		semanticErrorMessages.add("staticでない変数Aをstaticコンテキストから参照することはできません。");
		semanticErrorMessages.add("予期しない型");
		semanticErrorMessages.add("パッケージAは存在しません。");
		semanticErrorMessages.add("型Aと型Bは比較できません。");
		semanticErrorMessages.add("クラスAはpublicであり、ファイルBで宣言しなければなりません。");
		semanticErrorMessages.add("型Aは間接参照できません。");
		semanticErrorMessages.add("メソッド本体がないか、abstract として宣言されています。");
		semanticErrorMessages.add("戻り値の型が void のメソッドからは値を返せません。");
		semanticErrorMessages.add("変換できない型");
		semanticErrorMessages.add("Aを含む継承がループしています。");
		semanticErrorMessages.add("break が switch 文またはループの外にあります。");
		semanticErrorMessages.add("Aにアクセスできません。");
		semanticErrorMessages.add("戻り値がありません。");
		semanticErrorMessages.add("continue がループの外にあります。");
		semanticErrorMessages.add("クラスAが重複しています。");
		semanticErrorMessages.add("ラベルAは未定義です。");
		semanticErrorMessages.add("メソッドの外の return 文です。");
		semanticErrorMessages.add("ここで 'void' 型を使用することはできません。");
		semanticErrorMessages.add("定数式が必要です。");
		semanticErrorMessages.add("配列が要求されましたが、型Aが見つかりました。");
		semanticErrorMessages.add("型Aはabstractです。インスタンスを生成することはできません。");
		semanticErrorMessages
				.add("クラスAのメソッドBはクラスCのメソッドBをオーバーライドできません。スーパークラスでの定義(public)より弱いアクセス特権を割り当てようとしました。");
		semanticErrorMessages.add("メソッドはスーパータイプのメソッドをオーバーライドまたは実装しません");
		semanticErrorMessages.add("内部エラーです。AをBで()にインスタンス生成できません。");
		semanticErrorMessages.add("内部クラスが static 宣言を持つことはできません。");
		semanticErrorMessages.add("型Aはパラメータをとりません。");
		semanticErrorMessages.add("型Aの初期化子が不正です。");
		semanticErrorMessages.add("this の呼び出しはコンストラクタの先頭文でなければなりません。");
		semanticErrorMessages.add("case ラベルが重複しています。");

		return semanticErrorMessages;
	}
}
