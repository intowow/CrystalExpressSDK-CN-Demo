<h2 id='activity' style='color:red'>Activity設定</h2>

- 請在起始流程裡加上[I2WAPI.init()][I2WAPI-init]，此API只需要呼叫一次即可
- 若你想啟動[預覽模式](./preview.md)，請將`Activity`帶入[I2WAPI.init()][I2WAPI-init]裡，SDK將會處理`Activity`本身的`Bundle`，進入廣告預覽模式

<p/>
[程式範例][I2WAPI-init]
<p/>

<codetag tag="I2WAPI-init"/>
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

若你想使用測試廣告，則可改用下列方式初始SDK。

```java
I2WAPI.init(this, true);
```

<span style='font-weight: bold;color:red'>註:若要還原成使用正式廣告，需把原本的測試APP移除。<span/>

- 為了讓SDK判斷App是否為前景，請在每一隻Activity的`onResume()`與`onPause()`加上`I2WAPI.onActivityResume()`與`I2WAPI.onActivityPause()`
- 或者你也可使用一支父Activity，實作如下程式碼，讓其他Activity繼承

<p/>
[程式範例][BaseActivity]
<p/>

<codetag tag="BaseActivity-onResume"/>
```java
@Override
public void onResume() {
	super.onResume();

    //  let the SDK know the App status. (foreground or background)
    //
    //  you should call this API in all of your activity's onResume() status.
    //
    //  if you use splash ad, you can call this API
    //  in the onStart() instead of onResume() too.
    //
	I2WAPI.onActivityResume(this);
}
```
<p/>

<codetag tag="BaseActivity-onPause"/>
```java
@Override
public void onPause() {
	super.onPause();

    //  let the SDK know the App status. (foreground or background)
    //
    //  you should call this API in all of your activity's onPause() status.
    //
    //  if you use splash ad, you can call this API
    //  in the onStop() instead of onPause() too.
    //
	I2WAPI.onActivityPause(this);
}
```
<p/>

<p/>

- 若是APP有整合[開機蓋屏(Open Splash)廣告](./opensplash)，請將此API加至[onStart()][I2WAPI-onStart]與[onStop()][I2WAPI-onStop]裡


[I2WAPI-onStart]:https://github.com/ddad-daniel/CrystalExpressSDK-CN/blob/master/CrystalExpressDemo/src/com/intowow/crystalexpress/cedemo/CEOpenSplashActivity.java#L70 "CEOpenSplashActivity.java" 
[I2WAPI-onStop]:https://github.com/ddad-daniel/CrystalExpressSDK-CN/blob/master/CrystalExpressDemo/src/com/intowow/crystalexpress/cedemo/CEOpenSplashActivity.java#L148 "CEOpenSplashActivity.java" 
[I2WAPI-init]:https://github.com/ddad-daniel/CrystalExpressSDK-CN/blob/master/CrystalExpressDemo/src/com/intowow/crystalexpress/cedemo/CEOpenSplashActivity.java#L46 "CEOpenSplashActivity.java" 
[BaseActivity]:https://github.com/ddad-daniel/CrystalExpressSDK-CN/blob/master/CrystalExpressDemo/src/com/intowow/crystalexpress/cedemo/BaseActivity.java#L11 "BaseActivity.java" 
