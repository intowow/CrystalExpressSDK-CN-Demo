<h3 id='interstitialsplash' style='color:red'>插頁蓋屏</h3>

- 插頁蓋屏廣告指當用戶從文章內容頁返回至文章清單頁時，出現單則蓋屏廣告。

<p/>
[程式範例][Interstitial]
<p/>

<h4 id='interstitialsplash-1' style='color:green'>整合步驟</h4>

- 讓Activity繼承[BaseActivity](./activity_setting)<p/>
<p/>

- 將Demo app裡的[slide_in_from_bottom.xml][slide_in_from_bottom]與[no_animation.xml][no_animation]，複製到應用程式的res/anim/目錄底下

<p/>

<p/>

- 加入`onConfigurationChanged()`，處理橫式廣告 
<codetag tag="OpenSplash-onConfigurationChanged" id="OpenSplash-onConfigurationChanged"/>
```java
@Override
public void onConfigurationChanged(Configuration newConfig) {
	//	you have to add this method in the activity,
	//	remember to add the android:configChanges="orientation|screenSize" property
	//	in the Androidanifest.xml
	super.onConfigurationChanged(newConfig);
}
```
<p/>


- 設定AndroidManifest.xml
	- 在該`Activity`裡加上`android:configChanges="orientation|screenSize"`	

<p/>
<p/>

<span style='font-weight: bold;color:red'>
註:若未加上onConfigurationChanged()與android:configChanges，會導致橫式廣告出現錯誤
<br/>

- 宣告變數([程式範例][Interstitial-init])
<codetag tag="Interstitial-init"/>
```java
private final static String mInterstitialPlacement = Config.INTERSTITIAL_PLACEMENT;
private SplashAD mInterstitialSplashAd = null;
```
<p/>

<span style='font-weight: bold;color:red'>請先定義好版位名稱，帶入`mInterstitialPlacement`，並提交給Intowow後台設定<span/>

- 向SDK要求廣告([程式範例][Interstitial-request])
<codetag tag="Interstitial-request"/>
```java
mInterstitialSplashAd = I2WAPI.requesSplashAD(CEStreamActivity.this, mInterstitialPlacement);
```
<p/>

- 設定回調([程式範例][Interstitial-setListener])
- 有別於[開機蓋屏](./opensplash)，呼叫`I2WAPI.requesSplashAD()`後，再設定回調時，線程會以非阻塞模式(`Non-Blocking Calls`)進行

<codetag tag="Interstitial-setListener"/>
```java
if (mInterstitialSplashAd != null) {
	//	this is a Non-Blocking calls
	//
	mInterstitialSplashAd.setListener(new SplashAdListener() {

		@Override
		public void onLoaded() {
			mInterstitialSplashAd.show();
		}

		@Override
		public void onLoadFailed() {
		}

		@Override
		public void onClosed() {
			//	be sure to release the splash ad here
			//
			mInterstitialSplashAd.release();
		}
	});
}
```
<p/>

- 在`onDestroy()`釋放廣告 ([程式範例][Interstitial-release])
<codetag tag="Interstitial-release"/>
```java
if (mInterstitialSplashAd != null) {
	mInterstitialSplashAd.release();
	mInterstitialSplashAd = null;
}
```
<p/>

[Interstitial-release]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/cedemo/CEStreamActivity.java#L355 "CEStreamActivity.java" 
[OpenSplash-request]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/cedemo/CEOpenSplashActivity.java#L56 "CEOpenSplashActivity.java" 
[Interstitial]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/cedemo/CEStreamActivity.java#L325 "CEStreamActivity.java" 
[Interstitial-init]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/cedemo/CEStreamActivity.java#L84 "CEStreamActivity.java" 
[Interstitial-request]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/cedemo/CEStreamActivity.java#L151 "CEStreamActivity.java" 
[Interstitial-setListener]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/cedemo/CEStreamActivity.java#L155 "CEStreamActivity.java" 
[slide_in_from_bottom]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/blob/master/res/anim/slide_in_from_bottom.xml
[no_animation]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/blob/master/res/anim/no_animation.xml
