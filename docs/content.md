<h3 id='content' style='color:red'>內文廣告</h3>

- 內文廣告指文章內所加入的廣告
- 本範例以`ScrollView`為主，若你使用的是`WebView`，可調整你的布局xml檔，將`WebView`包入`ScrollView`，如下示意
```xml
	<CrystalExpressScrollView>
		<ViewGroup>
			<WebView/>
			<ContentAd>
		<ViewGroup/>
	<CrystalExpressScrollView/>
```

- `ScrollView`布局排列方式，可參考範例程式[activity_content.xml][activity_content.xml]

<h4 id='content-1' style='color:red'>串接步驟</h4>

<p/>[程式範例][Content-activity]<p/>

- 加入[CrystalExpressScrollView][Content-scroll]與[ContentHelper.java][Content-helper]

- 在`Activity`裡宣告變數 ([程式範例][Content-init])

<codetag tag="Content-init"/>
```java
private final static String mPlacement = "CONTENT";
private ContentHelper mContentHelper = null;

```
<p/>
	
<span style='font-weight: bold;color:red'>請先定義好版位名稱，帶入`mPlacement`，並提交給Intowow後台設定<span/>

- `Activity`初始`mContentHelper`([程式範例][Content-inithelper])

<codetag tag="Content-inithelper"/>
```java
// content ad view
//
final RelativeLayout contentAdLayout = (RelativeLayout) findViewById(R.id.contentad);
final CrystalExpressScrollView sv = (CrystalExpressScrollView) findViewById(R.id.scrollview);

mContentHelper = new ContentHelper(this, mPlacement, sv,
		contentAdLayout);
mContentHelper.setActive();
mContentHelper.onPageSelected(0);
```
<p/>

- 處理`ScrollView`([程式範例][Content-initscroll])
<codetag tag="Content-initscroll"/>
```java
// callback
//
sv.setScrollViewListener(new ScrollViewListener() {
	public void onScrollChanged(CrystalExpressScrollView scrollView,
			int x, int y, int oldX, int oldY) {
	}

	public void onScrollViewIdle() {
		if (mContentHelper != null) {
			mContentHelper.checkContentAD();
		}
	}
});
```
<p/>

- 若`Activity`一開始便要讀取廣告，可直接在文章初始流程加上下面讀取的程式。若`contentAdLayout`是放在`WebView`之後，也可考慮將此段程式放在`WebViewClient`的`onPageFinished()`裡

<p/>[程式範例][Content-load]<p/>
<codetag tag="Content-load"/>
```java
if (mContentHelper != null) {
	mContentHelper.loadContentAd();
}
```
<p/>

- 若要修改廣告的寬度，可修改[ContentHelper.java][Content-helper]

<p/>[程式範例][Content-requestAD]<p/>
<codetag tag="Content-requestAD"/>
```java
public View requestAD(int position) {
	if (mHelper != null) {

		//	you can resize the ad width by
		//
		//	mHelper.requestAD(position, intWidthValue);

		return mHelper.requestAD(position);
	}

	return null;
}
```
<p/>

- 若要修改廣告的背景，可修改[ContentHelper.java][Content-helper]
<p/>[程式範例][Content-background]<p/>
<codetag tag="Content-background"/>
```java
private void addAd(View contentAd) {
	if(contentAd!=null){

		//	you can set your background here
		//
		contentAd.setBackgroundColor(Color.WHITE);

		mContentAdLayout.getLayoutParams().height = RelativeLayout.LayoutParams.WRAP_CONTENT;
		mContentAdLayout.removeAllViews();
		mContentAdLayout.addView(contentAd);
	}
}
```
<p/>

- 生命週期處理

`onResume()` ([程式範例][Content-onResume])

<codetag tag="Content-onResume"/>
```java
if (mContentHelper != null) {
	mContentHelper.start();
}
```
<p/>

`onPause()` ([程式範例][Content-onPause])

<codetag tag="Content-onPause"/>
```java
if (mContentHelper != null) {
	mContentHelper.stop();
}
```
<p/>

`onDestroy()` ([程式範例][Content-onDestroy])

<codetag tag="Content-onDestroy"/>
```java
if (mContentHelper != null) {
	mContentHelper.destroy();
	mContentHelper = null;
}
```
<p/>
 
 

[activity_content.xml]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/res/layout/activity_content.xml "activity_content.xml"
[Content-helper]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master//src/com/intowow/crystalexpress/content/ContentHelper.java#L10 "ContentHelper.java" 
[Content-init]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master//src/com/intowow/crystalexpress/content/ContentActivity.java#L25 "ContentActivity.java" 
[Content-scroll]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master//src/com/intowow/crystalexpress/content/CrystalExpressScrollView.java#L8 "CrystalExpressScrollView.java" 
[Content-inithelper]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master//src/com/intowow/crystalexpress/content/ContentActivity.java#L129 "ContentActivity.java" 
[Content-initscroll]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master//src/com/intowow/crystalexpress/content/ContentActivity.java#L141 "ContentActivity.java" 
[Content-load]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master//src/com/intowow/crystalexpress/content/ContentActivity.java#L157 "ContentActivity.java" 
[Content-activity]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master//src/com/intowow/crystalexpress/content/ContentActivity.java#L23 "ContentActivity.java" 
[Content-requestAD]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master//src/com/intowow/crystalexpress/content/ContentHelper.java#L66 "ContentHelper.java" 
[Content-background]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master//src/com/intowow/crystalexpress/content/ContentHelper.java#L124 "ContentHelper.java" 
[Content-onResume]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master//src/com/intowow/crystalexpress/content/ContentActivity.java#L176 "ContentActivity.java" 
[Content-onPause]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master//src/com/intowow/crystalexpress/content/ContentActivity.java#L188 "ContentActivity.java" 
[Content-onDestroy]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master//src/com/intowow/crystalexpress/content/ContentActivity.java#L199 "ContentActivity.java" 
