<h3 id='preview' style='color:red'>預覽模式</h3>

- CrystalExpress SDK提供各廣告版位預覽功能
- 利用QRCode掃描的方式進入APP，直接觀看廣告效果

<h4 id='preview-setting' style='color:green'>配置步驟</h4>

- 你必須將QRCode掃描後，所啟動的Activity裡，執行[I2WAPI.init()][I2WAPI-init]。SDK將處理Bundle，進入廣告預覽模式
- 在[AndroidManifest.xml][TAG-AndroidManifest]裡的launch activity加入

```xml
<intent-filter>
	<actionandroid:name="android.intent.action.VIEW"/>
	<category android:name="android.intent.category.DEFAULT"/>
	<category android:name="android.intent.category.BROWSABLE"/>
	<data android:scheme="{YOUR_APP_URL_SCHEME}"android:host="adpreview"/>
	<data android:scheme="{YOUR_APP_URL_SCHEME}"android:host="activate"android:pathPattern=".*"/>
</intent-filter>
```

<span style='font-weight: bold;color:red'>你需提供App的scheme給Intowow後台設定<span/>

- 若要開始預覽，需先產生QRCode，其格式如下。
```
{YOUR_APP_URL_SCHEME}://adpreview?adid={AD ID}
```

- 產生QRCode之後，即可使用手機QRCode掃描程式掃描，進入廣告預覽模式

[I2WAPI-init]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master//src/com/intowow/crystalexpress/cedemo/CEOpenSplashActivity.java#L46 "CEOpenSplashActivity.java" 
[TAG-AndroidManifest]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/AndroidManifest.xml "AndroidManifest.xml"
