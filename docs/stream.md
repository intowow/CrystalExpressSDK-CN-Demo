<h3 id='stream' style='color:red'>信息流廣告(Stream Ad)</h3>

- 信息流廣告(Stream Ad)廣告指用戶滑動文章清單(`ListView`)時，所插入的廣告(也稱作"IN-STREAM"廣告)

<a href="https://s3.cn-north-1.amazonaws.com.cn/intowow-common/preview/STREAM_VIDEO_CUSTOMCARD.html?video=http://intowow-demo.oss-cn-beijing.aliyuncs.com/preview/material/Stream-Video-CustomCard.mp4&customCard=http://intowow-demo.oss-cn-beijing.aliyuncs.com/preview/material/Stream-Video-CustomCard.jpg" target="_blank">
<img style="display:block; margin:auto;" src="https://s3.cn-north-1.amazonaws.com.cn/intowow-common/preview/img/card.png" alt="stream demo" width="250">
<a/>

<p/>
<a target="_blank" href="https://s3.cn-north-1.amazonaws.com.cn/intowow-common/preview/STREAM_VIDEO_CUSTOMCARD.html?video=http://intowow-demo.oss-cn-beijing.aliyuncs.com/preview/material/Stream-Video-CustomCard.mp4&customCard=http://intowow-demo.oss-cn-beijing.aliyuncs.com/preview/material/Stream-Video-CustomCard.jpg">
效果預覽
<a/>
<p/>

- 如果你使用一般由android SDK提供的list view，你可替換它改用SDK提供的`CrystalExpressListView`<p/>[程式範例][Stream-ListView]<p/>

- 如果你的list view使用第三方PullToRefresh，請特別注意`onItemClickListener`與`setOnScrollListener`。因為PullToRefresh list view為了實作下拉刷新功能，
會在清單的開頭加上一個head view，造成這兩個listener的position位移。你可參考demo app的處理
<p/>[onItemClickListener處理範例][Stream-onItemClickListener]<p/>
<p/>[onScroll處理範例][Stream-onScroll]<p/>

- 為了方便整合adapter，SDK提供了DeferStreamAdapter，你可讓adapter繼承他，並且實作SDK所需的功能
<p/>[程式範例][Stream-DeferStreamAdapter]<p/>

- 若activity同時使用多個listview，也可參考demo app，處理多個Adapter
<p/>[程式範例][Stream-CEStreamActivity]<p/>

<h4 id='commonlistview-1' style='color:green'>一般ListView整合</h4>

<p/>[程式範例][Stream-StreamActivity]<p/>

- 宣告變數 ([程式範例][Stream-init])
<codetag tag="Stream-init"/>
```java
private final static String  mPlacement = "STREAM";
private CrystalExpressListView mListView = null;
private StreamAdapter mAdapter = null;
```
<p/>

<span style='font-weight: bold;color:red'>請先定義好版位名稱，帶入mPlacement，並提交給Intowow後台設定<span/>

- 將原來的list view取代成[CrystalExpressListView][Stream-ListView].

- 讓adapter(或是你建立的父adapter)繼承[DeferStreamAdapter][Stream-DeferStreamAdapter].並將mPlacement[帶入建構子][Stream-placement].

- 此時你必須實作SDK所需要的[initADListener()][Stream-initADListener], 
[initStreamADListener()][Stream-initStreamADListener]與[getDefaultMinPosition()][Stream-getDefaultMinPosition]. 

- 實作[initADListener()][Stream-initADListener]的目的是當SDK讀取廣告後，希望App在資料清單(`DataSet`)裡，提供一個新位置，並將此位置回傳給SDK。
SDK將依據此位置處理`getView()`裡 `getStreamAd(position)`事件
- 你應該先安插好一個位置後，再回傳位置給SDK。如果你不希望再安插任何廣告，請直接回傳"-1"。

[程式範例][Stream-initADListener]

<codetag tag="Stream-initADListener"/>
```java
@Override
public void initADListener() {
	setAdListener(new com.intowow.sdk.StreamHelper.ADListener() {

		@Override
		public int onADLoaded(int position) {
			// 	when the SDK load one stream ad,
			//	it will call this callback for getting 
			//	the position you add in the DataSet.
			//
			//	if you call getStreamAd in the getView(),
			//	the SDK will return the ad refer to this position.
			//
			//	if you return "-1", it means that the ad is not added in your DataSet
			//

			position = getDefaultMinPosition(position);

			if (mList.size() >  position) {				
				// just allocate one position for stream ad
				//
				mList.add(position, null);
				notifyDataSetChanged();
				return position;
			}
			else {				
				return -1;
			}
		}

	});
}
```
<p/>

- 實作[initStreamADListener()][Stream-initStreamADListener]的目的是當你需呼叫onDataSetChanged時，SDK需要重新安插之前已加入的廣告至原來的位置。 
若該位置已是廣告，不會重新安插。

[程式範例][Stream-initStreamADListener]

<codetag tag="Stream-initStreamADListener"/>
```java
@Override
public void initStreamADListener() {
	setStreamADListener(new StreamADListener() {

		@Override
		public void onDataSetChanged() {
			for (Integer pos : getAddedStreamAdPosition()) {
				if(pos > mList.size()) {
					return;
				}

				//	check ad case
				//
				if(mList.get(pos) == null || mList.get(pos).equals("null")) {
					continue;
				}

				mList.add(pos , null);
			}
		}});
}
```
<p/>

- 實作[getDefaultMinPosition()][Stream-getDefaultMinPosition]的目的是讓你決定安插廣告的最小位置。

[程式範例][Stream-getDefaultMinPosition]

<codetag tag="Stream-getDefaultMinPosition"/>
```java
@Override
public int getDefaultMinPosition(int position) {
	// Don't place ad at the first place
	return Math.max(1, position);
}
```
<p/>

- 在`getView()`裡，你必須先檢查該位置是否為廣告，同時你也可以直接設定廣告的背景與寬度。

[程式範例][Stream-getView]

<codetag tag="Stream-getView"/>
```java
// Get ad view if possible
final View adView =  getStreamAd(position);	

//	or you can resize the ad width by this way
//	final View adView =  getStreamAd(position, 700);
//

if(adView != null) {
	//	you can set the background
	//	such as
	//	adView.setBackgroundColor(Color.BLACK);
	//	adView.setBackgroundResource(your resid);
	//	adView.setBackground(your background drawable);
	//	adView.setBackgroundDrawable(your background drawable);
	return adView;
}
```
<p/>

- 若你已經實作過`getItemViewType()`，請參考如下

[程式範例][Stream-getItemViewType]

<codetag tag="Stream-getItemViewType"/>
```java
@Override
public int getItemViewType(int position) {
	//	if you have implemented getItemViewType(), 
	//	be sure to check if the item is an ad 
	//	in this position.
	if(isAd(position)) {
		return super.getItemViewType(position);
	}else{
		//	return your view type here
		//
		//
	}
}
```
<p/>

- Activity生命週期

`onResume()` ([程式範例][Stream-onResume])

<codetag tag="Stream-onResume"/>
```java
if(mAdapter != null) {
	mAdapter.onResume();
}
```
<p/>

`onPause()` ([程式範例][Stream-onPause])

<codetag tag="Stream-onPause"/>
```java
if(mAdapter != null) {
	mAdapter.onPause();
}
```
<p/>

`onPause()` ([程式範例][Stream-onDestroy])

<codetag tag="Stream-onDestroy"/>
```java
if(mAdapter != null) {
	mAdapter.release();
	mAdapter = null;
}
```
<p/>

<h4 id='pulltorefreshlistview-1' style='color:green'>PullToRefresh ListView整合</h4>

<p/>[程式範例][Stream-PullToRefreshStreamActivity]<p/>

- 整合方式與[一般ListView](#commonlistview-1)整合內容相似。

- 為了實作下拉功能，PullToRefresh函式庫會在資料清單(`DataSet`)的開頭加上一個head view。因此，
你必須額外地去確認[onItemClickListener][Stream-onItemClickListener]與[onScrollListener][Stream-onScroll]，
將position的值減1，得到真正的item位置。

<p/>[onItemClickListener處理範例][Stream-onItemClickListener]<p/>
<p/>[onScroll處理範例][Stream-onScroll]<p/>

- 宣告變數([程式範例][Stream-pullinit])
<codetag tag="Stream-pullinit"/>
```java
private final static int FIRST_VISIBLE_ITEM_OFFSET = -1;
```
<p/>

- 參考[一般ListView](#commonlistview-1)整合內容，請讓你的adapter繼承[DeferStreamAdapter][Stream-DeferStreamAdapter]，並且實作[initADListener()][Stream-initADListener], 
[initStreamADListener()][Stream-initStreamADListener]與[getDefaultMinPosition()][Stream-getDefaultMinPosition]

- 檢查onItemClickListener
<p/>[程式範例][Stream-onItemClickListener]<p/>
<codetag tag="Stream-onItemClickListener"/>
```java
mPullToRefreshListView.setOnItemClickListener(new OnItemClickListener() {

	@Override
	public void onItemClick(AdapterView<?> parent, View view,
			int position, long id) {


		position = position + FIRST_VISIBLE_ITEM_OFFSET;
```
<p/>

- 檢查onScrollListener，將scroll事件傳給SDK
<p/>[程式範例][Stream-onScroll]<p/>
<codetag tag="Stream-onScroll"/>
```java
mPullToRefreshListView.setOnScrollListener(new OnScrollListener() {

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		//	...
		//	if you have already implemented this listener,
		//	add your original code here 
		//	...

		if(mAdapter != null) {
			mAdapter.onScrollStateChanged(view, scrollState);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		//	...
		//	if you have already implemented this listener,
		//	add your original code here 
		//	...

		if(mAdapter != null) {
			//	pass the right position on to the SDK
			//
			mAdapter.onScroll(view, 
					firstVisibleItem + FIRST_VISIBLE_ITEM_OFFSET, 
					visibleItemCount + FIRST_VISIBLE_ITEM_OFFSET, 
					totalItemCount);
```
<p/>

- 仿照[一般ListView](#commonlistview-1)整合內容，檢查activity生命週期



[Stream-ListView]:https://github.com/ddad-daniel/CrystalExpressSDK-CN/blob/master/CrystalExpressDemo/src/com/intowow/crystalexpress/stream/StreamActivity.java#L85 "StreamActivity.java" 
[Stream-onScroll]:https://github.com/ddad-daniel/CrystalExpressSDK-CN/blob/master/CrystalExpressDemo/src/com/intowow/crystalexpress/stream/PullToRefreshStreamActivity.java#L146 "PullToRefreshStreamActivity.java" 
[Stream-onItemClickListener]:https://github.com/ddad-daniel/CrystalExpressSDK-CN/blob/master/CrystalExpressDemo/src/com/intowow/crystalexpress/stream/PullToRefreshStreamActivity.java#L119 "PullToRefreshStreamActivity.java" 
[Stream-DeferStreamAdapter]:https://github.com/ddad-daniel/CrystalExpressSDK-CN/blob/master/CrystalExpressDemo/src/com/intowow/crystalexpress/stream/StreamAdapter.java#L19 "StreamAdapter.java" 
[Stream-CEStreamActivity]:https://github.com/ddad-daniel/CrystalExpressSDK-CN/blob/master/CrystalExpressDemo/src/com/intowow/crystalexpress/cedemo/CEStreamActivity.java#L53 "CEStreamActivity.java" 
[Stream-StreamActivity]:https://github.com/ddad-daniel/CrystalExpressSDK-CN/blob/master/CrystalExpressDemo/src/com/intowow/crystalexpress/stream/StreamActivity.java#L23 "StreamActivity.java" 
[Stream-init]:https://github.com/ddad-daniel/CrystalExpressSDK-CN/blob/master/CrystalExpressDemo/src/com/intowow/crystalexpress/stream/StreamActivity.java#L35 "StreamActivity.java" 
[Stream-placement]:https://github.com/ddad-daniel/CrystalExpressSDK-CN/blob/master/CrystalExpressDemo/src/com/intowow/crystalexpress/stream/StreamAdapter.java#L36 "StreamAdapter.java" 
[Stream-initADListener]:https://github.com/ddad-daniel/CrystalExpressSDK-CN/blob/master/CrystalExpressDemo/src/com/intowow/crystalexpress/stream/StreamAdapter.java#L165 "StreamAdapter.java" 
[Stream-initStreamADListener]:https://github.com/ddad-daniel/CrystalExpressSDK-CN/blob/master/CrystalExpressDemo/src/com/intowow/crystalexpress/stream/StreamAdapter.java#L203 "StreamAdapter.java" 
[Stream-getDefaultMinPosition]:https://github.com/ddad-daniel/CrystalExpressSDK-CN/blob/master/CrystalExpressDemo/src/com/intowow/crystalexpress/stream/StreamAdapter.java#L227 "StreamAdapter.java" 
[Stream-getView]:https://github.com/ddad-daniel/CrystalExpressSDK-CN/blob/master/CrystalExpressDemo/src/com/intowow/crystalexpress/stream/StreamAdapter.java#L77 "StreamAdapter.java" 
[Stream-getItemViewType]:https://github.com/ddad-daniel/CrystalExpressSDK-CN/blob/master/CrystalExpressDemo/src/com/intowow/crystalexpress/stream/StreamAdapter.java#L57 "StreamAdapter.java" 
[Stream-onResume]:https://github.com/ddad-daniel/CrystalExpressSDK-CN/blob/master/CrystalExpressDemo/src/com/intowow/crystalexpress/stream/StreamActivity.java#L106 "StreamActivity.java" 
[Stream-onPause]:https://github.com/ddad-daniel/CrystalExpressSDK-CN/blob/master/CrystalExpressDemo/src/com/intowow/crystalexpress/stream/StreamActivity.java#L118 "StreamActivity.java" 
[Stream-onDestroy]:https://github.com/ddad-daniel/CrystalExpressSDK-CN/blob/master/CrystalExpressDemo/src/com/intowow/crystalexpress/stream/StreamActivity.java#L129 "StreamActivity.java" 
[Stream-PullToRefreshStreamActivity]:https://github.com/ddad-daniel/CrystalExpressSDK-CN/blob/master/CrystalExpressDemo/src/com/intowow/crystalexpress/stream/PullToRefreshStreamActivity.java#L26 "PullToRefreshStreamActivity.java" 
[Stream-pullinit]:https://github.com/ddad-daniel/CrystalExpressSDK-CN/blob/master/CrystalExpressDemo/src/com/intowow/crystalexpress/stream/PullToRefreshStreamActivity.java#L40 "PullToRefreshStreamActivity.java" 
