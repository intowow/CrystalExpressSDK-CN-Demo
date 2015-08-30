<h3 id='flip' style='color:red'>翻頁蓋屏</h3>

- 翻頁蓋屏廣告泛指用戶左右滑動頁面時所插入的蓋屏廣告

<h4 id='flip-1' style='color:green'>串接步驟</h4>

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

<p/>[程式範例][Flip-activity]<p/>

- 讓Activity繼承[BaseActivity](./activity_setting)<p/>
<p/>

- 宣告變數 ([程式範例][Flip-init])

<codetag tag="Flip-init"/>
```java
private final static String mPlacement = Config.FLIP_PLACEMENT;
private FlipADDeferHelper mFlipHelper = null;
```
<p/>

<span style='font-weight: bold;color:red'>請先定義好版位名稱，帶入`mPlacement`，並提交給Intowow後台設定<span/>

- `onCreate()`裡初始`mFlipHelper` ([程式範例][Flip-inithelper])
<codetag tag="Flip-inithelper"/>
```java
mFlipHelper = new FlipADDeferHelper(this, mPlacement);

//	let the SDK know that this placement is active now.
//
mFlipHelper.setActive();
```
<p/>

- 將`mFlipHelper`帶入至`PagerAdapter`(建議由建構子帶入)([程式範例][Flip-constructor])
<codetag tag="Flip-constructor"/>
```java
mFlipPagerAdapter = new FlipPagerAdapter(
		this, 
		mItems, 
		mFlipHelper);
```
<p/>

- 處理`OnPageChangeListener`([程式範例][Flip-listener])
<codetag tag="Flip-listener"/>
```java
@Override
public void onPageScrollStateChanged(int state) {
	if (mFlipHelper != null) {
		mFlipHelper.onPageScrollStateChanged(state);
	}
}

@Override
public void onPageScrolled(int arg0, float arg1, int arg2) {
}

@Override
public void onPageSelected(int position) {	
	if (mFlipHelper != null) {
		mFlipHelper.onPageSelected(position);
	}
}
```
<p/>

- 在`PagerAdapter`宣告變數([程式範例][Flip-adapterinit])
<codetag tag="Flip-adapterinit"/>
```java
private FlipADDeferHelper mFlipHelper;
```
<p/>

- `PagerAdapter`建構子設定`mFlipHelper`與回調([程式範例][Flip-adapterhelper])
<codetag tag="Flip-adapterhelper"/>
```java
mFlipHelper = helper;	

mFlipHelper.setAppADListener(new AppADListener() {

	@Override
	public int onADLoaded(int position) {
		// you can edit the getDefaultMinPosition method
		//
		position = getDefaultMinPosition(position);

		if (mItems.size() >=  position) {
			mItems.add(position, null);
			notifyDataSetChanged();
			return position;
		}
		else {				
			return -1;
		}
	}

});
```
<p/>

- 於`instantiateItem()`裡向SDK要求廣告([程式範例][Flip-request])
<codetag tag="Flip-request"/>
```java
//	get flip
//
canvas = mFlipHelper.getAD(position);
```
<p/>

- `Activity`生命週期

`onResume()` ([程式範例][Flip-onResume])
<codetag tag="Flip-onResume"/>
```java
if(mFlipHelper != null) {
	mFlipHelper.onStart();
}
```
<p/>

`onPause()` ([程式範例][Flip-onPause])
<codetag tag="Flip-onPause"/>
```java
if(mFlipHelper != null) {
	mFlipHelper.onStop();
}
```
<p/>

`onDestroy()` ([程式範例][Flip-onDestroy])
<codetag tag="Flip-onDestroy"/>
```java
if(mFlipHelper != null) {
	mFlipHelper.destroy();
	mFlipHelper = null;
}
```
<p/>



[Flip-activity]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/flip/FlipActivity.java#L21 "FlipActivity.java" 
[Flip-init]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/flip/FlipActivity.java#L33 "FlipActivity.java" 
[Flip-inithelper]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/flip/FlipActivity.java#L52 "FlipActivity.java" 
[Flip-constructor]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/flip/FlipActivity.java#L60 "FlipActivity.java" 
[Flip-listener]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/flip/FlipPagerAdapter.java#L222 "FlipPagerAdapter.java" 
[Flip-adapterinit]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/flip/FlipPagerAdapter.java#L41 "FlipPagerAdapter.java" 
[Flip-adapterhelper]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/flip/FlipPagerAdapter.java#L56 "FlipPagerAdapter.java" 
[Flip-request]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/flip/FlipPagerAdapter.java#L113 "FlipPagerAdapter.java" 
[Flip-onResume]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/flip/FlipActivity.java#L78 "FlipActivity.java" 
[Flip-onPause]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/flip/FlipActivity.java#L89 "FlipActivity.java" 
[Flip-onDestroy]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/flip/FlipActivity.java#L101 "FlipActivity.java" 
