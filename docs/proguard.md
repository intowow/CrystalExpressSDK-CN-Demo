<h3 id='proguard' style='color:red'>程式碼混淆</h2>

如果應用程式有使用程式碼混淆(proguard)，請在設定檔裡加上

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
