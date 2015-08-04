<h3 id='stream_intro' style='color:red'>信息流廣告</h3>

﻿<h4 id='stream_intro_1' style='color:green;margin-bottom:15px'>信息流廣告介紹</h4>

- 信息流廣告指用戶滑動文章列表(`ListView`)時，所插入的廣告

<img style="display:block; margin:auto;" src="https://s3.cn-north-1.amazonaws.com.cn/intowow-common/preview/img/card.png" alt="stream demo" width="250" />

<a target="_blank" href="https://s3.cn-north-1.amazonaws.com.cn/intowow-common/preview/STREAM_VIDEO_CUSTOMCARD.html?video=http%3A%2F%2Fintowow-demo.oss-cn-beijing.aliyuncs.com%2Fpreview%2Fmaterial%2FStream-Video-CustomCard.mp4&customCard=http%3A%2F%2Fintowow-demo.oss-cn-beijing.aliyuncs.com%2Fpreview%2Fmaterial%2FStream-Video-CustomCard.jpg"  target="_blank">效果預覽</a>

---------------------------------------

﻿<h4 id='stream_import' style='color:green;margin-bottom:15px'>整合重點</h4>

- 請先讓Activity繼承[BaseActivity](./activity_setting)<p/>
<p/>

<span style='font-weight: bold;color:red'>
註:若您曾使用舊版整合方式，請將I2WAPI.onActivityResume與I2WAPI.onActivityPause移除，讓BaseActivity處理
</span>
<br/>

<font size=2 >信息流廣告整合時，須注意以下重點:</font>

1. [準備廣告版位](./stream/#stream_placement)
2. [初始`StreamHelper`類別](./stream/#stream_adapter)
3. [`ListView`設定`onScrollListener()`](./stream/#stream_scroll_status)
4. [`ListView`處理`onItemClickListener()`](./stream/#stream_itemclick)
5. [`StreamHelper`設定`onADLoaded()`回調](./stream/#stream_adload)
6. [處理`Adapter.notifyDataSetChanged()`](./stream/#stream_notify)
7. [處理`Adapter.getItemViewType()`](./stream/#stream_itemview)
8. [處理`Adapter.getView()`](./stream/#stream_getview)
9. [執行`StreamHelper.setActive()`](./stream/#stream_active)
10. [處理`Activity`的生命週期](./stream/#stream_activity)

---------------------------------------

﻿<h4 id='stream_placement' style='color:green;margin-bottom:15px'>準備廣告版位</h4>

- 請先準備好[廣告版位](./naming/#placement)，一個列表只能綁定一個[廣告版位](./naming/#placement)
- 廣告版位可直接宣告至應用程式裡，或是由呼叫伺服器來取得，以增加擴充性

<p/>
[程式範例][Stream-init]
<p/>

<codetag tag="Stream-init"/>
```java
/**
 *	you can hardcode this placement value in the source code, 
 *	or replace it by calling your server API 	
**/
private final static String  mPlacement = Config.STREAM_PLACEMENT;
```
<p/>

---------------------------------------

﻿<h4 id='stream_adapter' style='color:green;margin-bottom:15px'>初始StreamHelper類別</h4>

- `StreamHelper`類別能協助應用程式管理信息流廣告，如廣告啟動與暫停，要求與釋放
- 若應用程式有多個列表，每一個列表就需要有各自的`StreamHelper`來管理列表內的廣告
- 其初始方式有兩種類別可選擇
	1. `DeferStreamAdapter`類別(`建議使用`)
		- 本身繼承`BaseAdapter`
		- 本身已實作`OnScrollListener`供`ListView`設定
		- 繼承後可直接實作`onAdLoaded()`
		- 已將大部分整合邏輯包覆起來，整合較容易
	2. `StreamHelper`類別，若應用程式不適合使用`DeferStreamAdapter`時再使用

<p/>
[初始範例-StreamHelper][Stream-StreamHelper-init]
<p/>
[初始範例-DeferStreamHelper][Stream-defer-init]
<p/>

<codetag tag="Stream-defer-init"/>
```java
mAdapter = new ExtendDeferStreamAdapter(
		this, 
		mPlacement, 
		mItems);
```
<p/>

- 兩者比較如下
	
<table border="1" >
	<thead>
		<tr>
			<td align="center" style='color:green;'>整合要項</td>
			<td width='250px' align="center" style='color:green;word-break:break-all;'>DeferStreamAdapter</td>
			<td width='250px' align="center" style='color:green;word-break:break-all;'>StreamHelper</td>
		</tr>
	</thead>
	<tbody style='font-size:15px'>
		<tr>
			<td align="center">整合優先權</td>
			<td width='250px' style='word-break:break-all;'>
				建議優先使用
			</td>
			<td width='250px' style='word-break:break-all;'>
				若<span style='color:#E74C3C'>DeferStreamAdapter</span>不適合讓應用程式<span style='color:#E74C3C'>Adapter</span>繼承時再使用
	  		</td>
		</tr>
		<tr>
			<td align="center">初始方式</td>
			<td width='250px' style='word-break:break-all;'>
				此類別已將<span style='color:#E74C3C'>StreamHelper</span>包覆在內，應用程式只需讓<span style='color:#E74C3C'>Adapter</span>繼承即可
			</td>
			<td width='250px' style='word-break:break-all;'>
				應用程式需在Activity內額外宣告<span style='color:#E74C3C'>StreamHelper</span>做為成員變數，並將其設定給<span style='color:#E74C3C'>Adapter</span>
	  		</td>
		</tr>
		<tr>
			<td align="center">廣告版位</td>
			<td width='250px' style='word-break:break-all;'>
				於<span style='color:#E74C3C'>Adapter</span>建構子帶入
			</td>
			<td width='250px' style='word-break:break-all;'>
				於<span style='color:#E74C3C'>StreamHelper</span>建構子帶入
	  		</td>
		</tr>
		<tr>
			<td align="center">onScrollListener()</td>
			<td width='250px' style='word-break:break-all;'>
				須設定<span style='color:#E74C3C'>listview.setOnScrollListener(DeferStreamAdapter)</span>
			</td>
			<td width='250px' style='word-break:break-all;'>
				須設定<span style='color:#E74C3C'>listview.setOnScrollListener(StreamHelper)</span>
	  		</td>
		</tr>
		<tr>
			<td align="center">onADLoaded()</td>
			<td width='250px' style='word-break:break-all;'>
				<span style='color:#E74C3C'>adapter</span>繼承<span style='color:#E74C3C'>DeferStreamAdapter</span>之後，直接在<span style='color:#E74C3C'>Adapter</span>裡實作
	  		</td>
			<td width='250px' style='word-break:break-all;'>
				<span style='color:#E74C3C'>StreamHelper</span>初始後，加上<span style='color:#E74C3C'>streamHelper.setListener()</span>回調
	  		</td>
		</tr>
		
		<tr>
			<td align="center">notifyDataSetChanged()</td>
			<td width='250px' style='word-break:break-all;'>
				需將邏輯實作於<span style='color:#E74C3C'>Adapter</span>的<span style='color:#E74C3C'>notifyDataSetChanged()</span>
	  		</td>
			<td width='250px' style='word-break:break-all;'>
				需將邏輯實作於<span style='color:#E74C3C'>Adapter</span>的<span style='color:#E74C3C'>notifyDataSetChanged()</span>
	  		</td>
		</tr>

		<tr>
			<td align="center">getItemViewType()</td>
			<td width='250px' style='word-break:break-all;'>
				若<span style='color:#E74C3C'>Adapter</span>本身未實作<span style='color:#E74C3C'>getItemViewType()</span>則可略過。若已實作過則需加上邏輯修改，若該位置為廣告，則須回傳<span style='color:#E74C3C'>super.getItemViewType(position)</span>
	  		</td>
			<td width='250px' style='word-break:break-all;'>
				需加上邏輯修改，若該位置為廣告，則須回傳<span style='color:#E74C3C'>mStreamHelper.getItemViewType(position)</span>
	  		</td>
		</tr>
		
		<tr>
			<td align="center">getView</td>
			<td width='250px' style='word-break:break-all;'>
				在<span style='color:#E74C3C'>Adapter</span>裡的<span style='color:#E74C3C'>getView()</span>呼叫<span style='color:#E74C3C'>getAD()</span>
			</td>
			<td width='250px' style='word-break:break-all;'>
				在<span style='color:#E74C3C'>adapter</span>裡的<span style='color:#E74C3C'>getView()</span>呼叫<span style='color:#E74C3C'>streamHelper.getAD()</span>
	  		</td>
		</tr>

		<tr>
			<td align="center">setActive()</td>
			<td width='250px' style='word-break:break-all;'>
				當用戶切換至該列表時，執行<span style='color:#E74C3C'>adapter.setActive()</span>
	  		</td>
			<td width='250px' style='word-break:break-all;'>
				當用戶切換至該列表時，執行<span style='color:#E74C3C'>streamHelper.setActive()</span>
	  		</td>
		</tr>
		
	</tbody>
</table>

[回到頂端](./stream/#stream_import)

---------------------------------------

﻿<h4 id='stream_scroll_status' style='color:green;margin-bottom:15px'>ListView設定onScrollListener()</h4>

- 應用程式須將`ListView`的滑動狀態設定給SDK
- SDK只有在`ListView`閒置(`OnScrollListener.SCROLL_STATE_IDLE`)的狀態下才會播放廣告
- 設定的方式是直接帶入`StreamHelper`(`StreamHelper`本身已實作`OnScrollListener`)

<p/>
[設定範例-StreamHelper][Stream-onScroll-StreamHelper]
<p/>
[設定範例-DeferStreamHelper][Stream-onScroll-defer]
<p/>

<codetag tag="Stream-onScroll-defer"/>
```java
//	let the SDK know the scroll status
//
mListView.setOnScrollListener(mAdapter);
```
<p/>

- 若應用程式已設定過`onScrollListener()`，可參考如下範例
<br/>
[範例程式][Stream-onScroll]
<codetag tag="Stream-onScroll"/>
```java
mListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view,
					int scrollState) {
				// ...
				// if you have already implemented this listener,
				// add your original code here
				// ...

				if (mAdapter != null) {
					mAdapter.onScrollStateChanged(view,
							scrollState);
				}
			}

			@Override
			public void onScroll(AbsListView view,
					int firstVisibleItem, int visibleItemCount,
					int totalItemCount) {
				// ...
				// if you have already implemented this listener,
				// add your original code here
				// ...

				if (mAdapter != null) {
					mAdapter.onScroll(
							view,
							firstVisibleItem,
							visibleItemCount,
							totalItemCount);
				}
			}
		});
```
<p/>

- 若列表使用下拉刷新式功能(`PullToRefreshListView`)，因為此函式庫會在列表第一個位置加上一個`header view`，造成`position`位移一筆，所以需將`onScrollListener()`的`onScroll()`回調的`firstVisibleItem`值減1
<br/>
[範例程式][Stream-Pull-OnScrollListener]
<codetag tag="Stream-Pull-OnScrollListener"/>
```java
pullToRefreshListView
		.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view,
					int scrollState) {
				// ...
				// if you have already implemented this listener,
				// add your original code here
				// ...

				if (adapter != null) {
					adapter.onScrollStateChanged(view,
							scrollState);
				}
			}

			@Override
			public void onScroll(AbsListView view,
					int firstVisibleItem, int visibleItemCount,
					int totalItemCount) {
				// ...
				// if you have already implemented this listener,
				// add your original code here
				// ...

				if (adapter != null) {
					final int FIRST_VISIBLE_ITEM_OFFSET = -1;
					// pass the right position on to the SDK
					//
					adapter.onScroll(
							view,
							firstVisibleItem
									+ FIRST_VISIBLE_ITEM_OFFSET,
							visibleItemCount,
							totalItemCount);
				}
			}
		});
```
<p/>

[回到頂端](./stream/#stream_import)

---------------------------------------

<h4 id='stream_itemclick' style='color:green;margin-bottom:15px'>ListView處理onItemClickListener()</h4>

- 若應用程式沒有設定過`onItemClickListener()`的話，可跳過此步驟
- 若應用程式有設定`onItemClickListener()`，需在`onItemClick()`回調裡優先判斷該位置是否為廣告

[範例程式][Stream-setOnItemClickListener]
<codetag tag="Stream-setOnItemClickListener"/>
```java
//	if you have not implemented setOnItemClickListener,
//	skip this code
//
mListView.setOnItemClickListener(new OnItemClickListener() {

	@Override
	public void onItemClick(AdapterView<?> parent, View view,
			int position, long id) {
		//	check is this position is a ad first
		//
		if(mAdapter != null && mAdapter.isAd(position)) {
			return;
		}

		//	...
		//	then add your original code here
		//	...

	}

});
```
<p/>

- 若列表使用下拉刷新式功能(`PullToRefreshListView`)，因為此函式庫會在列表第一個位置加上一個`header view`，造成`position`位移一筆，所以則需將回調帶入的值減1

[範例程式][Stream-OnItemClickListener]
<codetag tag="Stream-OnItemClickListener"/>
```java
//	if you use PullToRefresh library, 
//	then you should check the position offset 
//	in the scroll listener and item click listener
//
pullToRefreshListView
		.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent,
					View view, int position, long id) {

				final int FIRST_VISIBLE_ITEM_OFFSET = -1;
				position = position + FIRST_VISIBLE_ITEM_OFFSET;

				//	you should check is this position is ad first
				//	then do your original logic later
				//
				if (adapter != null && adapter.isAd(position)) {
					return;
				}

				// ...
				// if you have already implemented this listener,
				// add your original code here
				// ...

				Intent intent = new Intent();
				intent.setClass(CEStreamActivity.this, CEContentActivity.class);
				startActivity(intent);
				finish();

			}
		});
```
<p/>

[回到頂端](./stream/#stream_import)

---------------------------------------

<h4 id='stream_adload' style='color:green;margin-bottom:15px'>StreamHelper設定onADLoaded()回調</h4>

- 當廣告讀取完成時，SDK需要知道廣告將安插至列表哪個位置。等到`Adapter`在`getView()`裡向SDK要求廣告時，SDK便可依據此位置決定是否投放廣告
- 當廣告讀取完成時，SDK會主動呼叫`onADLoaded()`回調，此回調的回傳位置會儲存於`StreamHelper`裡
- 在`onADLoaded()`回傳位置之前，須先動態地在列表數據(`DataSet`)中分配一個位置給廣告，接者呼叫`adapter.notifyDataSetChanged()`之後，再回傳位置給SDK
- 若位置回傳`-1`，代表應用程式不需要此廣告
- 若應用程式使用`StreamHelper`，而不是`DeferStreamAdapter`，請確認設定`onADLoaded()`在`ListView.setAdapter`之前

[範例程式-StreamHelper][Stream-StreamHelper-onADLoaded]
<p/>
[範例程式-DeferStreamAdapter][Stream-onADLoaded]
<codetag tag="Stream-onADLoaded"/>
```java
@Override
public int onADLoaded(int position) {
	//
	// 	when one stream ad has been loaded,
	//	the SDK will need to know which position 
	//	can show this ad in your DataSet.
	//	so the SDK will call onADLoaded(position) for getting 
	//	one position that you have already allocate in your DataSet.
	//	then, if you call getAD(position) in the getView() later,
	//	the SDK will return one ad or null refer to onADLoaded's 
	//	return value.
	//
	//	if you return "-1", it means that the ad is not added in your 
	//	DataSet.

	//	for example:
	//	if you return "5" from the onADLoaded(position) first,
	//	and you request a ad by calling the getAD(5) in the getView() 
	//	later,
	//	the SDK will know that this position(5) can response a ad for 
	//	the App 
	//	

	position = getDefaultMinPosition(position);

	if (mList.size() >  position) {	

		// just allocate one position for stream ad
		//
		mList.add(position, null);

		//	be sure to call the notifyDataSetChanged()
		//
		notifyDataSetChanged();

		return position;
	}
	else {				
		return -1;
	}
}
```
<p/>


```
你必須先設定好onADLoaded()回調，再執行setActive()，
否則會因為沒有決定廣告安插位置而被SDK釋放掉廣告
```

[回到頂端](./stream/#stream_import)

---------------------------------------

<h4 id='stream_notify' style='color:green;margin-bottom:15px'>處理Adapter.notifyDataSetChanged()</h4>

- 當列表數據(`DataSet`)更新時，原本被安插好的廣告位置有可能被移除，造成列表數據與`StreamHelper`所儲存的廣告位置不匹配
- 此時若`StreamHelper`還記錄原先已安插的位置，會影響`getView()`裡取得廣告的判斷，造成資料排列出錯
- 所以在執行`notifyDataSetChanged()`之前，需再重新檢查之前已安插好的廣告，再執行`notifyDataSetChanged()`

[範例程式-使用StreamHelper][Stream-StreamHelper-notifyDataSetChanged]
<p/>
[範例程式-使用DeferStreamAdapter][Stream-notifyDataSetChanged]
<codetag tag="Stream-notifyDataSetChanged"/>
```java
@Override
public void notifyDataSetChanged() {
	//	if your DataSet has been changed
	//	the SDK will need to re-allocate the ad 
	//	which you have added in the DataSet before
	//
	for (Integer pos : getAddedPosition()) {
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
	super.notifyDataSetChanged();
}
```
<p/>

[回到頂端](./stream/#stream_import)

---------------------------------------

<h4 id='stream_itemview' style='color:green;margin-bottom:15px'>處理Adapter.getItemViewType()</h4>

- 正確處理`getItemViewType()`後，可讓SDK的視頻廣告正常撥放
- 當`getItemViewType()`要回傳前，應用程式需先判斷該位置是否為廣告(`onADLoaded()`所回傳的值)。

[範例程式-使用StreamHelper][Stream-StreamHelper-getItemViewType]
<p/>
[範例程式-使用DeferStreamAdapter][Stream-getItemViewType]
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

[回到頂端](./stream/#stream_import)

---------------------------------------

<h4 id='stream_getview' style='color:green;margin-bottom:15px'>處理Adapter.getView()</h4>

- 當`Adapter`要產生`View`之前，應用程式需先向SDK要求廣告
- SDK會更據`onADLoaded()`所回傳的值，回傳廣告給應用程式

[範例程式-使用StreamHelper][Stream-StreamHelper-getView]
<p/>
[範例程式-使用DeferStreamAdapter][Stream-getView]
<codetag tag="Stream-getView"/>
```java
// Get ad view if possible
final View adView =  getAD(position);	

//	or you can resize the ad width by this way
//	final View adView =  getAD(position, someIntWidth);
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

```
當你呼叫getAD()時，SDK會檢查此位置是否已加入廣告。

若不是廣告，SDK會進一步利用非主線程來讀取廣告

當廣告讀取完成時，SDK會呼叫onADLoaded()，讓應用程式決定該把廣告安插至哪個位置

此時onADLoaded()為主線程所執行
```

[回到頂端](./stream/#stream_import)

---------------------------------------

<h4 id='stream_active' style='color:green;margin-bottom:15px'>執行StreamHelper.setActive()</h4>

- 應用程式需執行`setActive()`，SDK才會處理該列表的廣告
- `StreamHelper`只有在[onResume()](./stream/#stream_activity)狀態與`setActive()`後，才會撥放視頻
- 若應用程式有多個列表，SDK只會處理最後一次呼叫`setActive()`的列表
- 所以若應用程式有多個列表，須注意應讓用戶正在觀看的列表執行`setActive()`
- 假設應用程式是使用`ViewPager`產生多個列表，應考慮以下幾個時機執行`setActive()`
	1. `PageAdapter`的`instantiateItem()`，但要確認`position`與用戶正在觀看的位置一致時，再執行`setActive()`
	<br/>
	[範例程式][Stream-ViewPager-init]
	2. `OnPageChangeListener`的`onPageSelected()`與`onPageScrollStateChanged()`，
	但要在`OnScrollListener.SCROLL_STATE_IDLE`時再執行`setActive()`
	<br/>
	[範例程式][Stream-ViewPager-setActive]
	
<br/>
[範例程式-使用StreamHelper][Stream-StreamHelper-active]
<br/>
[範例程式-使用DeferStreamAdapter][Stream-active]

[回到頂端](./stream/#stream_import)

---------------------------------------

<h4 id='stream_activity' style='color:green;margin-bottom:15px'>處理Activity的生命週期</h4>

- 應用程式需在`Activity`的`onResume()`、`onPause()`與`onDestroy()`處理`StreamHelper`
- 於`onResume()`裡呼叫`StreamHelper.onResume()`可讓SDK知道該`StreamHelper`正處於前景
- 於`onPause()`裡呼叫`StreamHelper.onPause()`可讓SDK知道該`StreamHelper`正處於背景
- 於`onDestroy()`裡呼叫`StreamHelper.release()`可讓SDK釋放廣告，避免記憶體洩漏`memory leak`

```
假設應用程式在同一個Activity內使用多個列表(如ViewPager)，須注意:

1. 每個StreamHelper有各自的resume與pause狀態
   
2. 只有處於resume狀態與最後一個呼叫setActive()的StreamHelper會啟用廣告。
```

<br/>
[範例程式-使用多個列表][Stream-CEStreamActivity]
<br/>
[範例程式-使用StreamHelper][Stream-StreamHelper-life]
<br/>
[範例程式-使用DeferStreamAdapter][Stream-life]
<codetag tag="Stream-life"/>
```java
@Override
public void onResume() {
	super.onResume();

	if(mAdapter != null) {
		mAdapter.onResume();
	}
}

@Override
public void onPause() {
	super.onPause();

	if(mAdapter != null) {
		mAdapter.onPause();
	}
}

@Override
public void onDestroy() {
	super.onDestroy();

	if(mAdapter != null) {
		mAdapter.release();
		mAdapter = null;
	}

}
```
<p/>




[回到頂端](./stream/#stream_import)

---------------------------------------

<p/>

[Stream-init]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/stream/defer/SingleDeferAdapterActivity.java#L32 "SingleDeferAdapterActivity.java" 
[Stream-onScroll-defer]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/stream/defer/SingleDeferAdapterActivity.java#L77 "SingleDeferAdapterActivity.java" 
[Stream-onScroll]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/stream/defer/SingleDeferAdapterActivity.java#L114 "SingleDeferAdapterActivity.java" 
[Stream-notifyDataSetChanged]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/stream/defer/ExtendDeferStreamAdapter.java#L224 "ExtendDeferStreamAdapter.java" 
[Stream-StreamHelper-notifyDataSetChanged]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/stream/streamhelper/StreamHelperAdapter.java#L70 "StreamHelperAdapter.java" 
[Stream-getItemViewType]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/stream/defer/ExtendDeferStreamAdapter.java#L72 "ExtendDeferStreamAdapter.java" 
[Stream-StreamHelper-getItemViewType]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/stream/streamhelper/StreamHelperAdapter.java#L99 "StreamHelperAdapter.java" 
[Stream-getView]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/stream/defer/ExtendDeferStreamAdapter.java#L92 "ExtendDeferStreamAdapter.java" 
[Stream-StreamHelper-getView]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/stream/streamhelper/StreamHelperAdapter.java#L118 "StreamHelperAdapter.java" 
[Stream-StreamHelper-active]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/stream/streamhelper/MultipleStreamHelperActivity.java#L431 "MultipleStreamHelperActivity.java" 
[Stream-active]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/stream/defer/SingleDeferAdapterActivity.java#L69 "SingleDeferAdapterActivity.java" 
[Stream-ViewPager-setActive]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/stream/defer/MultipleDeferAdapterActivity.java#L138 "MultipleDeferAdapterActivity.java" 
[Stream-ViewPager-init]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/stream/defer/MultipleDeferAdapterActivity.java#L504 "MultipleDeferAdapterActivity.java" 
[Stream-defer-init]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/stream/defer/SingleDeferAdapterActivity.java#L62 "SingleDeferAdapterActivity.java" 
[Stream-StreamHelper-init]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/stream/streamhelper/SingleStreamHelperActivity.java#L83 "SingleStreamHelperActivity.java" 
[Stream-onScroll-StreamHelper]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/stream/streamhelper/SingleStreamHelperActivity.java#L140 "SingleStreamHelperActivity.java" 
[Stream-onADLoaded]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/stream/defer/ExtendDeferStreamAdapter.java#L180 "ExtendDeferStreamAdapter.java" 
[Stream-StreamHelper-onADLoaded]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/stream/streamhelper/SingleStreamHelperActivity.java#L87 "SingleStreamHelperActivity.java" 
[Stream-life]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/stream/defer/SingleDeferAdapterActivity.java#L154 "SingleDeferAdapterActivity.java" 
[Stream-StreamHelper-life]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/stream/streamhelper/SingleStreamHelperActivity.java#L160 "SingleStreamHelperActivity.java" 
[Stream-OnItemClickListener]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/cedemo/CEStreamActivity.java#L663 "CEStreamActivity.java" 
[Stream-Pull-OnScrollListener]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/cedemo/CEStreamActivity.java#L699 "CEStreamActivity.java" 
[Stream-setOnItemClickListener]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/stream/defer/SingleDeferAdapterActivity.java#L85 "SingleDeferAdapterActivity.java" 
[Stream-CEStreamActivity]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/cedemo/CEStreamActivity.java#L54 "CEStreamActivity.java" 
