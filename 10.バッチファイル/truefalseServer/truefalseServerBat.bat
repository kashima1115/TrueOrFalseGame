rem ○×ゲームプログラム、サーバー機能の開始バッチ

rem config.properties更新バッチファイル呼び出し
call configUpdater.bat

rem ActiveMQのバッチファイルがあるディレクトリに移動
pushd .\apache-activemq-5.9.1\bin

rem ActiveMQのバッチ実行
start activemq.bat

rem 元のディレクトリに戻る
popd

rem ○×プログラムのjarファイルを実行
java -jar truefalseServer.jar

pause

exit 0