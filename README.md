# MyRL

## このリポジトリについて
C92 金曜日 東そ－42a SIGCOWWにて委託販売を行った，【Rosenblock Chainers：進化計算と強化学習の本 １】の“強化学習で自作ゲームを学習させる”で用いたコードを公開しています．
（【進化計算と強化学習の本１】は，以下のリンクにて電子版を頒布しています．
https://rosenblock.booth.pm/items/588521
）

その名の通り，強化学習で自作ゲームを学習させることができます．
また，学習中のエージェントのプレイの様子をViewerから確認することもできます．

Viewerの画面サイズがでかすぎるなど，要望や意見がありましたら，遠慮なくissuesにお願いします．

## 実行
### 学習実行
フォルダ内で以下のコマンドを実行すると，学習が実行されます．

```
sh run_learn.sh
```

デフォルトの設定だと，416世代（評価回数50000回）学習が実行されます．

政策の評価は，ある程度の塊ごとに並列化して行っています．
並列化数は，子の政策の生成数や集団サイズによって決まります．
そのため，これらのパラメータを大きくしすぎるとメモリを大きく消費することに注意してください．

乱数シードや学習関係のパラメータを変更して実行する場合，src/SAPGA/MainSAPGA.javaのパラメータを変更してください．

### Viewer実行
フォルダ内で以下のコマンドを実行すると，エージェントがゲームをプレイする様子を確認できるViewerが実行されます．

```
sh run_viewer.sh
```

学習中に保存した政策のファイルを読み込むことで実行が行えます．

読み込む政策のファイルや乱数シードを変更する場合，src/Viewer/Viewer.javaのパラメータを変更してください．

