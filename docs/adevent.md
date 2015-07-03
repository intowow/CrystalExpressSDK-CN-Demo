<h2 id='adcallback' style='color:red'>廣告曝光與點擊事件的回調機制</h2>

- SDK支援應用程式藉由設定回調(`listener callback`)的方式取得廣告的曝光與點擊事件

- 若你設定了回調，SDK將會使用背景線程，回傳事件給應用程式。
所以你該注意，避免在處理回調時，操作到`main-thread only`的問題 

- 為了不影響應用程式的資源釋放，SDK使用`WeakReference`的方式保存此回調。
所以你該自己維護回調的生命週期，並且主動在有需要時，將其設定給SDK

- API格式如下

```java
I2WAPI.setADEventListener(final Context context, ADEventListener listener);
```

- 範例如下

```java

// you should maintain this listener's lifecycle by yourself
//
private ADEventListener mADEventListener = null;

if(mADEventListener == null) {
	mADEventListener = new ADEventListener() {
		@Override
		public void onAdClick(final String adId) {
			//	get click event here
			//
		}
		
		@Override
		public void onAdImpression(final String adId) {
			//	get impression event here
			//
		}
	};
	
	I2WAPI.setADEventListener(Activity.this, mADEventListener);
}
```
