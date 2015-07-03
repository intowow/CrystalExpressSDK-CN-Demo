package com.intowow.crystalexpress.cedemo;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;

import com.intowow.crystalexpress.R;
import com.intowow.sdk.I2WAPI;
import com.intowow.sdk.SplashAD;
import com.intowow.sdk.SplashAD.SplashAdListener;

public class CEOpenSplashActivity extends Activity {//XXX#OpenSplash#
	
	//************************************************
	//	common UI
	//
	private Handler mHandler = null;
	private Class<CEStreamActivity> mMainActivity = CEStreamActivity.class;
	
	//************************************************
	//	Open Splash Ad
	//
	//XXX@OpenSplash-mAd@#OpenSplash-mAd#
	private SplashAD mAd = null;
	//end
	
	//XXX@OpenSplash-onConfigurationChanged@#OpenSplash-onConfigurationChanged#
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		//	you have to add this method in the activity,
		//	remember to add the android:configChanges="orientation|screenSize" property
		//	in the Androidanifest.xml
		super.onConfigurationChanged(newConfig);
	}
	//end
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ce_open_splash);
		
		mHandler = new Handler();
		
		//XXX@I2WAPI-init@#I2WAPI-init#
		//	init the SDK.
		//
		//	you can call this API only once in your launch flow.
		//
		//	if you need to start the preview mode, 
		//	please passing the activity(not ApplicationContext) on to the parameter
		//	and the SDK will parsing the intent to launch the preview mode.
		//
		I2WAPI.init(this);
		//end
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		//	let the SDK know the App status. (foreground or background)
		//
		//	you should call this API in all of your activity's onResume() status.
		//
		//	if you use splash ad, you can call this API
		//	in the onStart() instead of onResume() too.
		//
		//XXX#I2WAPI-onStart#
		I2WAPI.onActivityResume(this);
		//end

		if (mAd == null) {
			
			//	request the open splash ad here
			//
			//XXX@OpenSplash-request@#OpenSplash-request#
			mAd = I2WAPI.requesSingleOfferAD(this, "OPEN_SPLASH");
			//end
			
			//XXX@OpenSplash-setListener@#OpenSplash-setListener#
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
			//end
			else {
				startCEStreamActivity();
			}
		}
	}
	
	@Override
	public void onStop() {
		super.onStop();
		
		//	let the SDK know the App status. (foreground or background)
		//
		//	you should call this API in all of your activity's onPause() status.
		//
		//	if you use splash ad, you can call this API
		//	in the onStop() instead of onPause() too.
		//
		//XXX#I2WAPI-onStop#
		I2WAPI.onActivityPause(this);
		//end
	}

	/**	
	 * you can go to the original launch activity here, 
	 * and remember to finish this activity
	 * */
	private synchronized void startCEStreamActivity() {//TODO
		Intent intent = new Intent();
		intent.setClass(this, mMainActivity);
		startActivity(intent);
		
		finish();
	}
	
	@Override
	public void onBackPressed() {
		startCEStreamActivity();
	}
	
}
