<h2 id='activity' style='color:red'>Activity</h2>

<h4 style='color:green'>初始化SDK</h4>

- 請在起始流程裡加上`I2WAPI.init()`
- 若你想啟動[預覽模式](./preview.md)，請將`Activity`的實例帶入`I2WAPI.init()`裡，SDK將會處理`Activity`本身的`Intent`，進入廣告預覽模式

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

若你想使用測試模式取得廣告，則可改用下列方式初始SDK。

```java
I2WAPI.init(this, true);
```

<span style='font-weight: bold;color:red'>
註:I2WAPI.init(this, true)請在第一次呼叫時執行。
<span/>
<br/>
<span style='font-weight: bold;color:red'>
若您使用Demo App執行測試廣告，請直接修改MainActiviy.java的I2WAPI.init
<span/>
<br/>
<span style='font-weight: bold;color:red'>
若要還原成正式廣告，需把原本的測試應用程式移除。
<br/>
<span style='font-weight: bold;color:red'>
同一隻手機上，不可有相同CrystalId的應用程式並存
<span/>

<h4 style='color:green'>應用程式前景判斷</h4>

- 為了不影響應用程式的效能，SDK需準確地判斷應用程式當下是否為前景，並動態調整廣告下載策略
- 請在每一隻`Activity`的`onResume()`與`onPause()`加上`I2WAPI.onActivityResume()`與`I2WAPI.onActivityPause()`
- 為了整合方便，建議你也可考慮使用一支[父Activity][BaseActivity]，實作如下程式碼，讓其他`Activity`繼承

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

- 若是應用程式有整合[開機蓋屏廣告](./opensplash)，請將`I2WAPI.onActivityResume()`加至[onStart()][I2WAPI-onStart]，`I2WAPI.onActivityPause()`加至[onStop()][I2WAPI-onStop]裡


[I2WAPI-onStart]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/cedemo/CEOpenSplashActivity.java#L70 "CEOpenSplashActivity.java" 
[I2WAPI-onStop]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/cedemo/CEOpenSplashActivity.java#L158 "CEOpenSplashActivity.java" 
[I2WAPI-init]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/cedemo/CEOpenSplashActivity.java#L46 "CEOpenSplashActivity.java" 
[BaseActivity]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/cedemo/BaseActivity.java#L12 "BaseActivity.java" 
