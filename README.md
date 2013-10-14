commonlibrary
=============

OS Xの文字コード問題

条件： OS X, JDK7以上
症状：Fileクラスを使ったプログラムで，日本語ファイル名，フォルダ名のものが動かない．
（exists()メソッドがfalseを返す）

調べたサイト：
OS XのJava 7ではFileにバグがある【と思っていた】
http://qiita.com/ripplestep/items/fbdd5765a64a00be6410
http://piyopiyoducky.net/blog/2013/06/03/encoding-of-java-application-in-os-x/

事実：
・sun.jnu.encodingはfile.encodingと同様に環境変数LANGによって値が判別され、ja_JP.UTF-8であればUTF-8が設定される．
・LANG設定がFileクラスで名前検索をするときに使われる．

・LANG環境変数は，シェルでは自動的にja_JP.UTF-8に設定される．
→jarダブルクリックでは動かないが，コマンドから起動すると動く現象はこのため．

・実験の結果，-Dsun.jnu.encoding=UTF-8 は，Fileクラスのバグにはあまり効果がない．
（実験の結果，上記JVM変数を指定しても，オーバーライドできなそうである）

解決案：
・すべてのアプリ起動時にLANG環境変数を設定
＞ launchctl setenv LANG ja_JP.UTF-8
・Eclipseはこのコマンドをうった後，立ち上げ直す．

2013.10.14, matsuzawa
（一時的に設定 export LANG=ja_JP.UTF-8）


