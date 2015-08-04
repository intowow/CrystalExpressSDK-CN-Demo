<h3 id='interstitialsplash' style='color:red'>插頁蓋屏</h3>

- 插頁蓋屏廣告指當用戶從文章內容頁返回至文章清單頁時，出現單則蓋屏廣告。

<p/>
[程式範例][Interstitial]
<p/>

<h4 id='interstitialsplash-1' style='color:green'>整合步驟</h4>

- 讓Activity繼承[BaseActivity](./activity_setting)<p/>
<p/>

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
