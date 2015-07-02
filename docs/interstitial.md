<h3 id='interstitialsplash' style='color:red'>插頁蓋屏(Interstitial Splash)</h3>

- 插頁蓋屏(Interstitial Splash)廣告指當用戶從文章內容頁返回至文章清單頁時，出現單則蓋屏廣告。

<p/>
[程式範例][Interstitial]
<p/>

<h4 id='interstitialsplash-1' style='color:red'>整合步驟</h4>

- 宣告變數([程式範例][Interstitial-init])
<codetag tag="Interstitial-init"/>
```java
private final static String mInterstitialPlacement = "INTERSTITIAL_SPLASH";
private SplashAD mInterstitialSplashAd = null;
```
<p/>

<span style='font-weight: bold;color:red'>請先定義好版位名稱，帶入mInterstitialPlacement，並提交給Intowow後台設定<span/>

- 向SDK要求廣告([程式範例][Interstitial-request])
<codetag tag="Interstitial-request"/>
```java
mInterstitialSplashAd = I2WAPI.requesSplashAD(CEStreamActivity.this, mInterstitialPlacement);
```
<p/>

- 設定listener ([程式範例][Interstitial-setListener])
- 有別於[開機蓋屏(Open Splash)](../opensplash.md)，此步驟(`setListener()`)為`Non-Blocking calls`

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

- 在onDestroy()釋放廣告 ([程式範例][Interstitial-release])
<codetag tag="Interstitial-release"/>
```java
if (mInterstitialSplashAd != null) {
	mInterstitialSplashAd.release();
	mInterstitialSplashAd = null;
}
```
<p/>

[Interstitial-release]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master//src/com/intowow/crystalexpress/cedemo/CEStreamActivity.java#L364 "CEStreamActivity.java" 
[OpenSplash-request]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master//src/com/intowow/crystalexpress/cedemo/CEOpenSplashActivity.java#L78 "CEOpenSplashActivity.java" 
[Interstitial]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master//src/com/intowow/crystalexpress/cedemo/CEStreamActivity.java#L334 "CEStreamActivity.java" 
[Interstitial-init]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master//src/com/intowow/crystalexpress/cedemo/CEStreamActivity.java#L91 "CEStreamActivity.java" 
[Interstitial-request]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master//src/com/intowow/crystalexpress/cedemo/CEStreamActivity.java#L153 "CEStreamActivity.java" 
[Interstitial-setListener]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master//src/com/intowow/crystalexpress/cedemo/CEStreamActivity.java#L157 "CEStreamActivity.java" 
