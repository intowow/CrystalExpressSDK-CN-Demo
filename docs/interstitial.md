<h3 id='interstitialsplash' style='color:red'>插頁蓋屏</h3>

- 插頁蓋屏廣告指當用戶從文章內容頁返回至文章清單頁時，出現單則蓋屏廣告。

---------------------------------------

<h4 id='interstitialsplash-1' style='color:green'>整合步驟</h4>

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

<p/>
[程式範例][Interstitial]
<p/>

- 讓Activity繼承[BaseActivity](./activity_setting)，BaseActivity內已處理蓋屏廣告大部分邏輯<p/>
<p/>

- 將Demo app裡的[slide_in_from_bottom.xml][slide_in_from_bottom]與[no_animation.xml][no_animation]，複製到應用程式的res/anim/目錄底下

<p/>


- 宣告變數([程式範例][Interstitial-init])
<codetag tag="Interstitial-init"/>
```java
private final static String mInterstitialPlacement = Config.INTERSTITIAL_PLACEMENT;
```
<p/>

<span style='font-weight: bold;color:red'>請先定義好版位名稱，帶入`mInterstitialPlacement`，並提交給Intowow後台設定<span/>

- 向SDK要求廣告([程式範例][Interstitial-request])
<codetag tag="Interstitial-request"/>
```java
//	check it for landscape ad case
//
if(hasRequestedSplashAd()) {
	return;
}
mSplashAd = I2WAPI.requesSingleOfferAD(CEStreamActivity.this, mInterstitialPlacement);
```
<p/>

- 設定回調([程式範例][Interstitial-setListener])
- 呼叫`I2WAPI.requesSingleOfferAD()`後，再設定回調時，線程會以阻塞模式(`Blocking Calls`)進行

<codetag tag="Interstitial-setListener"/>
```java
if (mSplashAd != null) {
	mSplashAd.setListener(new SplashAdListener() {

		@Override
		public void onLoaded() {
			mSplashAd.show(R.anim.slide_in_from_bottom, 
					R.anim.no_animation);
		}

		@Override
		public void onLoadFailed() {
			onSplashAdFinish();
		}

		@Override
		public void onClosed() {
			onSplashAdFinish();
		}
	});
}
```
<p/>

- 在`onDestroy()`釋放廣告 ([程式範例][Interstitial-release])
<codetag tag="Interstitial-release"/>
```java
releaseSplashAd();
```
<p/>

- 整合完成後，請參考<a target="_blank" href="../checkpoint">檢查要點</a>

[Interstitial-release]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/cedemo/CEStreamActivity.java#L360 "CEStreamActivity.java" 
[OpenSplash-request]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/cedemo/CEOpenSplashActivity.java#L35 "CEOpenSplashActivity.java" 
[Interstitial]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/cedemo/CEStreamActivity.java#L330 "CEStreamActivity.java" 
[Interstitial-init]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/cedemo/CEStreamActivity.java#L82 "CEStreamActivity.java" 
[Interstitial-request]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/cedemo/CEStreamActivity.java#L148 "CEStreamActivity.java" 
[Interstitial-setListener]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/cedemo/CEStreamActivity.java#L157 "CEStreamActivity.java" 
[slide_in_from_bottom]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/blob/master/res/anim/slide_in_from_bottom.xml
[no_animation]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/blob/master/res/anim/no_animation.xml
