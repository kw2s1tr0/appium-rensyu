# appium-rensyu
appiumの練習

練習としてappiumで簡単な操作を実施したのでその手順の備忘として
テストコードはjava/gradle/docker
appiumはnode/ローカル
emulatorはandoroidstudio

テストコードは以下


## 手順
- node.jsをローカルにインストール

- ローカルにてappiumとdriverをインストール
```bash
npm install -g appium
appium driver install uiautomator2
```

- ローカルにandroidStudioをインストール
- エミュレーターを起動

- ホストで環境変数を設定(appiumが使用する)
ANDROID_HOME=C:\Users\<ユーザー名>\AppData\Local\Android\Sdk
ANDROID_SDK_ROOT=C:\Users\<ユーザー名>\AppData\Local\Android\Sdk
PATH:"C:\Users\<ユーザー名>\AppData\Local\Android\Sdk\platform-tools"

- devcontainerでjavaコンテナを起動
コマンドパレットでDevContainer: Open Folder in Containers...を選択し.devcontainerフォルダを選択
※拡張機能devcontainerをインストールしておくこと

- 起動（ホスト）
```bash
appium
```

- テスト実行
```bash
gradle test
```

- デモ版ではエミュレーターで通知欄が表示される

## 仕組み
- appiumはnode.js上でサーバを立てADB（Android Debug Bridge）を用いて実機やエミュレーターを操作する
※ADB：デバイス（実機やエミュレータ）を PC から操作するためのコマンドラインツール
- クライアントからのテストコードはappium用のライブラリを用いてappiumサーバーに指示を出し、appiumサーバーがその指示をデバイスに仲介する仕組みとなっている
- adb自体も実機やエミュレーターをadb serverが管理する仕組みとなっている。

test code (JUnit / Java)
        │
        ▼
Appium Client (Java library)
        │ HTTP(JSON Wire / W3C WebDriver)
        ▼
Appium Server（Node.js）
        │ ADB コマンド
        ▼
adb server（ホストPC上, ポート5037）
        │ USB / エミュレータ接続
        ▼
Android 実機 / エミュレーター

今回の構成では：

- Appium サーバ：ホスト（Windows）
- adb server：ホスト（Windows）
- エミュレーター：ホスト（Android Studio）
- テストコード：Dev Container 上の Java から HTTP で Appium に接続

という役割分担になっている。

- このためappiumサーバをdockerコンテナとするのは難しい
Appium サーバを Docker コンテナ内で動かす場合、
コンテナ内の adb がどの adb server を見るか
エミュレーターや実機がどの adb server にぶら下がっているか
という ネットワーク／プロセス構成をかなりシビアに合わせる必要がある
ホスト側で起動している Android Studio のエミュレーターは
通常「ホスト側の adb server」が管理しているため、
Appium だけをコンテナに閉じ込めると、
「コンテナの adb server」と
「ホストの adb server」
が分裂し、エミュレーターが見えなくなることが多い。
このため 「ホストで Appium を動かし、コンテナからはクライアントとして叩く」構成がシンプル。

一方で、CLI ベースのエミュレーター（ヘッドレスAVD / 実機をUSBで直接コンテナにマウント etc.） を併用する構成であれば、
adb server も
エミュレーターも
Appium サーバも
全てコンテナ内に閉じ込めて 完全コンテナ化した E2E テスト を行える可能性がある。

※　間違いもあると思われるのでご指摘いただけると幸いです

## 所感
- 機能がまだやや不安定な印象があり、日本語ドキュメントも少ない
- 環境構築の手間はそれなりに大きい
（Node.js / Appium / Android SDK / Emulator / パス設定 / Dev Container など）
- しかし、ネイティブアプリの E2E テストツールとしては実質的に有力な選択肢がほぼ Appium 一択であり、避けて通りにくい
- テスト実行速度は決して速くはないが、人間が手動でポチポチ操作するよりは圧倒的に速く、再現性も高いため、
- UI 回りのリグレッションテストを自動化する価値は十分にある