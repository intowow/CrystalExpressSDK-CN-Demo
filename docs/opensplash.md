<h3 id='opensplash' style='color:red'>開機蓋屏</h3>

- 開機蓋屏廣告指進入應用程式時，所出現的蓋屏廣告。
- 其出現時機包含
	- 首次啟動應用程式(`launch flow`)
	- 按`HOME`鍵離開應用程式後，再次開啟應用程式(`enter foreground`)

<img style="display:block; margin:auto;" src="https://s3.cn-north-1.amazonaws.com.cn/intowow-common/preview/img/splash2-demo.png" alt="splash demo" width="250">

<a target="_blank" href="http://s3.cn-north-1.amazonaws.com.cn/intowow-common/preview/globe_slideup.html">效果預覽</a>

---------------------------------------


<h4 id='opensplash-1' style='color:green'>整合步驟</h4>

<span style='font-weight: bold;color:red'>
註:
</span>
<br/>
<span style='font-weight: bold;color:red'>
整合前請先移除手機上的Demo App，一支手機上不可有相同的crystal id。
</span>
<br/>
<span style='font-weight: bold;color:red'>
若整合後看不到廣告，請參考<a target="_blank" href="../faq">問題集</a>
</span>
<br/>

<h3 id='opensplash-launch' style='color:blue'>1.  修改起始流程</h3>

<p/>
[程式範例][OpenSplash]
<p/>

- 讓起始Activity繼承[BaseActivity](./activity_setting)，BaseActivity內已處理蓋屏廣告大部分邏輯<p/>
<p/>

- 將Demo app裡的[slide_in_from_bottom.xml][slide_in_from_bottom]與[no_animation.xml][no_animation]，複製到應用程式的res/anim/目錄底下

<p/>

<p/>

- 向SDK請求廣告([程式範例][OpenSplash-request])

<codetag tag="OpenSplash-request"/>
```java
//	check it for landscape ad case
//
if(hasRequestedSplashAd()) {
	return;
}

//	we can request the splash ad 
//	after the LOGO shows for some time
//
mSplashAd = I2WAPI.requesSingleOfferAD(CEOpenSplashActivity.this, "OPEN_SPLASH");
```
<p/>

<span style='font-weight: bold;color:red'>
註:
</span>
<br/>
<span style='font-weight: bold;color:red'>
1.呼叫requesSingleOfferAD的時機點，請在用戶看到應用程式LOGO之後
</span>
<br/>
<span style='font-weight: bold;color:red'>
2.建議讓LOGO等待約1秒再開啟廣告，以達較佳用戶體驗，
效果如下
</span>
<br/><br/>
<a target="_blank" href="http://s3.cn-north-1.amazonaws.com.cn/intowow-common/preview/globe_slideup.html">效果預覽</a>

- 設定讀取完成`onLoaded()`,讀取失敗`onLoadFailed()`與關閉廣告`onClosed()`的回調
<br/>[程式範例][OpenSplash-setListener]

<br/>
<span style='font-weight: bold;color:red'>
註:呼叫`I2WAPI.requesSingleOfferAD()`後，再設定回調時，線程會以阻塞模式(`Blocking Calls`)進行
</span>
 
<codetag tag="OpenSplash-setListener" id="OpenSplash-callback"/>
```java
if (mSplashAd != null) {

	//	implement onLoaded, onLoadFailed and 
	//	onClosed callback
	//
	mSplashAd.setListener(new SplashAdListener() {

		@Override
		public void onLoaded() {
			//	this callback is called 
			//	when the splash ad is ready to show
			//
			//	show splash ad here
			//
			mSplashAd.show(R.anim.slide_in_from_bottom, 
					R.anim.no_animation);
		}

		@Override
		public void onLoadFailed() {
			//	this callback is called
			//	when this splash ad load fail
			//
			onSplashAdFinish();
			startNextActivity();
		}

		@Override
		public void onClosed() {
			//	this callback is called when:
			//	1.user click the close button
			//	2.user press the onBackpress button
			//	3.dismiss_time setting from the server
			//
			onSplashAdFinish();
			startNextActivity();
		}
	});
} else {
	//	the ad is not ready now
	//	start the next activity directly
	//
	startNextActivity();
}
```
<p/>

- 釋放廣告
<br/>[程式範例][OpenSplash-release]

<codetag tag="OpenSplash-release" id="OpenSplash-release"/>
```java
@Override
protected void onDestroy() {
	super.onDestroy();

	releaseSplashAd();
}
```
<p/>

---------------------------------------

<h3 id='opensplash-enterforeground' style='color:blue'>2. 按`HOME`鍵離開應用程式後，再次開啟應用程式(enter foreground)</h3>

<span style='font-weight: bold;color:red'>
註:
</span>
<br/>
<span style='font-weight: bold;color:red'>
此部分因為使用Application的ActivityLifecycleCallbacks判斷應用程式的前景與背景，
</span>
<br/>
<span style='font-weight: bold;color:red'>
所以只支援Android 4.0以上。
</span>
<br/>
<span style='font-weight: bold;color:red'>
若應用程式已有自己的Application，可改將BaseApplication的邏輯直接加入應用程式的Application裡
</span>
<br/>

- 將`BaseApplication.java`加入至程式中
	- [下載路徑](https://s3.cn-north-1.amazonaws.com.cn/intowow-sdk/android/sample/BaseApplication.java)

- 設定`AndroidManifest.xml`的`application`標籤 ，指定使用`BaseApplication`類別

```xml
<application
    android:name="{your BaseApplication package}.BaseApplication"
    android:XXX
    android:XXX
    XXX >
```

- 修改[BaseActivity.java](./activity_setting)，在`onCreate()`裡啟動`BaseApplication`
<br/>[程式範例][OpenSplash-startapplication]

<codetag tag="OpenSplash-startapplication" id="OpenSplash-startapplication"/>
```java
//	you can launch the BaseApplication.java
//	for requesting the enter foreground splash ad
//
getApplicationContext();
```
<p/>


- 修改`BaseApplication.java`裡的`FILTER_ACTIVITY_NAMES`字串陣列，此陣列列出`不呼叫`開機蓋屏的`Activity`。
請填入起始`Activity`，或其他不須呼叫開機蓋屏的`Activity`

<br/>[程式範例][OpenSplash-FILTER_ACTIVITY_NAMES]

<codetag tag="OpenSplash-FILTER_ACTIVITY_NAMES" id="OpenSplash-FILTER_ACTIVITY_NAMES"/>
```java
//	TODO
//	you can modify this String array.
//	these classes added here will not show 
//	the enter foreground splash ad
//
private final static String[] FILTER_ACTIVITY_NAMES = new String[] {

	//	================================================
	//	replace these classes to your launcher activity
	//	or any other class which you don't want to show the splash ad
	//
	MainActivity.class.getName(),
	CEOpenSplashActivity.class.getName(),
	OpenSplashActivity.class.getName(),
	MultipleDeferAdapterActivity.class.getName(),
	SingleDeferAdapterActivity.class.getName(),
	MultipleStreamHelperActivity.class.getName(),
	SingleStreamHelperActivity.class.getName(),
	ContentActivity.class.getName(),
	FlipActivity.class.getName(),
	SingleFixPositionAdapterActivity.class.getName(),
	MultipleFixPositionAdapterActivity.class.getName(),
	SettingActivity.class.getName(),

	//=====  do not remove SDK's activity
	//
	SplashAdActivity.class.getName(),// this is SDK's activity, don't remove it
	WebViewActivity.class.getName()// this is SDK's activity, don't remove it
}; 
```
<p/>

<span style='font-weight: bold;color:red'>
註:
</span>
<br/>
<span style='font-weight: bold;color:red'>
請勿移除SDK的SplashAdActivity與WebViewActivity
</span>

- 除錯方式
```
當應用程式從主頁面按HOME出去後，
checkBackground()裡的mActiveReferenceCount會等於0

過兩秒後，mEnterBackgroundTimer裡
會將mIsEnterFromBackground設為true

此時再開啟APP後，onActivityResumed()的callback裡
會因為mIsEnterFromBackground = true
而進入requestEnterForegroundSplashAd()
排除過濾邏輯後
最後會呼叫I2WAPI.requesSingleOfferAD()
向SDK要求蓋屏廣告
```

---------------------------------------

- 整合完成後，請參考<a target="_blank" href="../checkpoint">檢查要點</a>

<span style='font-weight: bold;color:red'>
註:
</span>
<br/>
<span style='font-weight: bold;color:red'>
1.頁面轉場效果只支援直式廣告
</span>
<br/>
<span style='font-weight: bold;color:red'>
2.可自行使用res/anim/底下的xml來客製
</span>
<br/>
<span style='font-weight: bold;color:red'>
3.Demo app的res/anim也提供了阻尼特效，可供應用程式參考使用
</span>
<br/>
<span style='font-weight: bold;color:red'>
    res/anim/damping_in.xml
</span>
<br/>
<span style='font-weight: bold;color:red'>
    res/anim/damping_out.xml
</span>
<p/>

[OpenSplash-release]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/cedemo/CEOpenSplashActivity.java#L127 "CEOpenSplashActivity.java" 
[OpenSplash-startapplication]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/BaseActivity.java#L40 "BaseActivity.java" 
[OpenSplash-FILTER_ACTIVITY_NAMES]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/BaseApplication.java#L54 "BaseApplication.java" 
[OpenSplash-mAd]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/cedemo/CEOpenSplashActivity.java#L25 "CEOpenSplashActivity.java" 
[OpenSplash]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/cedemo/CEOpenSplashActivity.java#L19 "CEOpenSplashActivity.java" 
[slide_in_from_bottom]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/blob/master/res/anim/slide_in_from_bottom.xml
[no_animation]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/blob/master/res/anim/no_animation.xml
[OpenSplash-onConfigurationChanged]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/cedemo/CEOpenSplashActivity.java#L29 "CEOpenSplashActivity.java" 
[OpenSplash-request]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/cedemo/CEOpenSplashActivity.java#L48 "CEOpenSplashActivity.java" 
[OpenSplash-setListener]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/cedemo/CEOpenSplashActivity.java#L61 "CEOpenSplashActivity.java" 
