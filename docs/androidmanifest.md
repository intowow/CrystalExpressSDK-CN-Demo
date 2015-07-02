﻿﻿<h3 id='androidmanifest' style='color:red'>AndroidManifest.xml設定</h3>

[XML範例][TAG-AndroidManifest]

<h4 id='Permission' style='color:green'>設定Permission</h4>

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.SET_ORIENTATION" />
```

<h4 id='Activity' style='color:green'>新增Activity</h4>

```xml
<activity
	android:name="com.intowow.sdk.SplashAdActivity"
	android:configChanges="orientation|screenSize"
	android:theme="@android:style/Theme.Translucent.NoTitleBar.Fullscreen"
	android:launchMode="singleTask"
	android:screenOrientation="portrait" >
</activity>
```

<h4 id='Receiver' style='color:green'>新增Receiver</h4>

```xml
<receiver android:name="com.intowow.sdk.ScheduleReceiver">
	<intent-filter >
	<action android:name="com.intowow.sdk.prefetch"/>
	</intent-filter>
</receiver>
```

<h4 id='meta-data' style='color:green'>新增meta-data</h4>

```xml
<meta-data android:name="CRYSTAL_ID" android:value="申請的CRYSTAL_ID" />
```

[TAG-AndroidManifest]:https://github.com/ddad-daniel/CrystalExpressSDK-CN-Demo/tree/master/AndroidManifest.xml "AndroidManifest.xml"
