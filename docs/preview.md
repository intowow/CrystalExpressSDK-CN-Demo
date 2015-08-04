<h3 id='preview' style='color:red'>預覽模式</h3>

- CrystalExpress SDK提供各廣告版位預覽功能，方便業務人員展示廣告效果，以拓展業務
- 可利用QRCode掃描的方式進入應用程式，直接觀看廣告效果

[配置步驟](./preview/#preview-setting)
<br/>
[測試步驟](./preview/#preview-test)

---------------------------------------

<h4 id='preview-setting' style='color:green'>配置步驟</h4>

- 在[AndroidManifest.xml][TAG-AndroidManifest]裡的起始`Activity`加入

```xml
<intent-filter>
	<actionandroid:name="android.intent.action.VIEW"/>
	<category android:name="android.intent.category.DEFAULT"/>
	<category android:name="android.intent.category.BROWSABLE"/>
	<data android:scheme="{YOUR_APP_URL_SCHEME}"android:host="crystalexpress"/>
	<data android:scheme="{YOUR_APP_URL_SCHEME}"android:host="activate"android:pathPattern=".*"/>
</intent-filter>
```

- 讓起始`Activity`繼承[BaseActivity][BaseActivity]。SDK將處理`Bundle`，進入廣告預覽模式


<span style='font-weight: bold;color:red'>你需提供應用程式的`scheme`給Intowow後台設定<span/>

- 若要開始預覽，需先產生QRCode，其格式如下。
```
{YOUR_APP_URL_SCHEME}://crystalexpress?adid={AD ID}
```

---------------------------------------

<h4 id='preview-test' style='color:green'>測試步驟</h4>

1. 測試前請先確認有連上網路
2. 產生QRCode之後，開啟手機QRCode掃描程式，進行掃描
3. QRCode掃描程式會開啟應用程式
4. 應用程式會進入黑屏的處理畫面，待處理完成後，會請用戶按確定，強制關閉應用程式
5. 重新開啟應用程式，當SDK下載完廣告後，會自動出現提示，請用戶去該廣告版位觀看廣告
	- 若是開機蓋屏，待看到下載完成提示之後，請按back跳出應用程式，再重新進入一次應用程式
	- 下載完廣告過程中請不要刷掉或強制停止應用程式
6. 若要取消預覽模式，需強制停止應用程式

[BaseActivity]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/BaseActivity.java#L12 "BaseActivity.java" 
[TAG-AndroidManifest]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/AndroidManifest.xml "AndroidManifest.xml"
