<h3 id='opensplash' style='color:red'>開機蓋屏</h3>

- 開機蓋屏廣告指第一次進入應用程式時，所出現的單則或多則蓋屏廣告。

- 若程式一開始原本有啟動背景加載資料，可參考範例程式，讓背景加載與開機蓋屏廣告同時進行<p/>[背景線程範例][OpenSplash-BackgroundTask]

<img style="display:block; margin:auto;" src="https://s3.cn-north-1.amazonaws.com.cn/intowow-common/preview/img/splash2-demo.png" alt="splash demo" width="250">

<a target="_blank" href="https://s3.cn-north-1.amazonaws.com.cn/intowow-common/preview/SPLASH2_VIDEO_GENERAL_P_ICLICK.html" target="_blank">效果預覽<a/>


<h4 id='opensplash-1' style='color:green'>整合步驟</h4>

<p/>
[程式範例][OpenSplash]
<p/>

- 宣告變數([程式範例][OpenSplash-mAd])
<codetag tag="OpenSplash-mAd"/>
```java
private SplashAD mAd = null;
```
<p/>

- 初始SDK([程式範例][I2WAPI-init])
<codetag tag="I2WAPInit"/>
```java
//	init the SDK.
//
//	you can call this API only once in your launch flow.
//
//	if you need to start the preview mode, 
//	please passing the activity(not ApplicationContext) on to the parameter
//	and the SDK will parsing the intent to launch the preview mode.
//
I2WAPI.init(this);
```
<p/>

- 向SDK請求廣告([程式範例][OpenSplash-request])

<codetag tag="OpenSplash-request"/>
```java
mAd = I2WAPI.requesSingleOfferAD(this, "OPEN_SPLASH");
```
<p/>

- 設定讀取完成`onLoaded()`,讀取失敗`onLoadFailed()`與關閉廣告`onClosed()`的回調

<p/>[程式範例][OpenSplash-setListener]<p/>

- 有別於[插頁蓋屏](./interstitial)，呼叫`I2WAPI.requesSingleOfferAD()`後，再設定回調時，線程會以阻塞模式(`Blocking Calls`)進行

<codetag tag="OpenSplash-setListener"/>
```java
if (mAd != null) {
	//	this is a Blocking calls
	//	implement onLoaded, onLoadFailed and onClosed callback
	//
	mAd.setListener(new SplashAdListener() {

		@Override
		public void onLoaded() {
			//	this callback is called 
			//	when the splash ad is ready to show
			//
			if(mHandler == null){
				return;
			}

			//	show splash ad here
			//
			mAd.show();

		}

		@Override
		public void onLoadFailed() {
			//	this callback is called
			//	when load this splash ad fail
			//
			if (mHandler != null) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						startCEStreamActivity();
					}
				});
			}
		}

		@Override
		public void onClosed() {
			//	this callback is called when:
			//	1.user clicks the close button
			//	2.user clicks the back button
			//	3.dismiss_time
			//
			if(mHandler!=null){
				startCEStreamActivity();
			}
		}
	});

} 
```
<p/>

- 加入`onConfigurationChanged()`，處理橫式廣告 ([程式範例][OpenSplash-onConfigurationChanged])
<codetag tag="OpenSplash-onConfigurationChanged"/>
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
	- 在該`Activity`裡加上android:configChanges="orientation|screenSize"


[OpenSplash-onConfigurationChanged]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master//src/com/intowow/crystalexpress/cedemo/CEOpenSplashActivity.java#L29 "CEOpenSplashActivity.java" 
[OpenSplash-setListener]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master//src/com/intowow/crystalexpress/cedemo/CEOpenSplashActivity.java#L82 "CEOpenSplashActivity.java" 
[OpenSplash-request]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master//src/com/intowow/crystalexpress/cedemo/CEOpenSplashActivity.java#L78 "CEOpenSplashActivity.java" 
[I2WAPI-init]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master//src/com/intowow/crystalexpress/cedemo/CEOpenSplashActivity.java#L46 "CEOpenSplashActivity.java" 
[OpenSplash-mAd]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master//src/com/intowow/crystalexpress/cedemo/CEOpenSplashActivity.java#L25 "CEOpenSplashActivity.java" 
[OpenSplash-BackgroundTask]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master//src/com/intowow/crystalexpress/opensplash/OpenSplashActivity.java#L58 "OpenSplashActivity.java" 
[OpenSplash]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master//src/com/intowow/crystalexpress/cedemo/CEOpenSplashActivity.java#L14 "CEOpenSplashActivity.java" 
