<h2 id='activity' style='color:red'>Activity</h2>

<h4 style='color:green'>初始化SDK</h4>

- 請讓所有Activity繼承[BaseActivity][BaseActivity]，方便SDK初始與處理生命週期
<br/>
<a target="_blank" href="https://s3.cn-north-1.amazonaws.com.cn/intowow-sdk/android/sample/BaseActivity.zip">下載路徑</a>

```java
	change
	
	public class YourChildActivity extends Activity{

	to

	public class YourChildActivity extends BaseActivity{
	
```

<p/>

- 若子`Activity`原本不是繼承`Activity`，而是其他如`FragmentActivity`，可直接修改[BaseActivity][BaseActivity]繼承`FragmentActivity`之後，再讓子`Activity`繼承[BaseActivity][BaseActivity]，如
```java
1. change
	
	public class BaseActivity extends Activity{

	to

	public class BaseActivity extends YourParentActivity{
	
2. change
	
	public class YourChildActivity extends YourParentActivity{

	to
	
	public class YourChildActivity extends BaseActivity{
```

<p/>

<div id="testmode"></div>

- 若你想使用測試模式取得廣告，可直接修改[BaseActivity][BaseActivity]。

```java
I2WAPI.init(this);

to

I2WAPI.init(this, true);
```
 
<p/>

<span style='font-weight: bold;color:red'>
註:
</span>
<br/>

<span style='font-weight: bold;color:red'>
1.開發時，請使用測試模式抓取廣告。正式模式只有在APK發佈前(release)，後台才會加入廣告
</span>
<br/>

<span style='font-weight: bold;color:red'>
2.若要取消測試模式，還原成正式廣告，需把原本的應用程式移除重新安裝。
</span>
<br/>

<p/>

<span style='font-weight: bold;color:red'>
3.若曾使用舊版整合方式，請移除所有使用到I2WAPI.onActivityResume與I2WAPI.onActivityPause的程式碼，讓BaseActivity集中處理
</span>
<br/>

<p/>

[BaseActivity]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/src/com/intowow/crystalexpress/BaseActivity.java#L12 "BaseActivity.java" 
