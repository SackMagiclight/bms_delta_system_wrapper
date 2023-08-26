## BMS DELTA SYSTEM WRAPPER

[BMS Liblary](http://www.lm-t.com/content/bmslibrary/index.html) の DELTA SYSTEM 値抽出に特化したWrapperです。  
`com.lmt.lib.bms-${version}.jar` を/libs に入れればコンパイル＆ビルドできるはずです。

### 使い方

1. cmdから `java -jar .\{build jar}.jar "{BMS file path}"` -> 標準出力 `{"COMPLEX":XXX,"GIMMICK":XXX,"SCRATCH":XXX,"POWER":XXX,"RHYTHM":XXX,"HOLDING":XXX}`
2. includeして `getRating` を呼び出す -> return JSON string