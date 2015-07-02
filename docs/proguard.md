<h3 id='proguard' style='color:red'>Proguard</h2>

如果APP有使用proguard，請在proguard的設定檔裡加上

```
##################################
#	intowow sdk
#
-keep class com.intowow.sdk.* { *; }
##################################
#	android-support-v4
#
-keep interface android.support.v4.app.** { *; }
-keep class android.support.v4.** { *; }
##################################
```
