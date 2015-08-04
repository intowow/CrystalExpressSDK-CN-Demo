package com.intowow.crystalexpress.cedemo;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;

import com.intowow.crystalexpress.BaseActivity;
import com.intowow.crystalexpress.R;
import com.intowow.sdk.I2WAPI;
import com.intowow.sdk.SplashAD;
import com.intowow.sdk.SplashAD.SplashAdListener;

public class CEOpenSplashActivity extends BaseActivity {//XXX#OpenSplash#
	
	//************************************************
	//	common UI
	//
	private Handler mHandler = null;
	private Class<CEStreamActivity> mNextActivity = CEStreamActivity.class;
	
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
	}
	
	private Runnable mShowLogoRunnable = new Runnable() {
		
		@Override
		public void run() {
			
			if (mAd == null) {
				
				//	request the open splash ad here
				//
				//XXX@OpenSplash-request@#OpenSplash-request#
				//	we can request the splash ad 
				//	after the LOGO shows for some time
				//
				mAd = I2WAPI.requesSingleOfferAD(CEOpenSplashActivity.this, "OPEN_SPLASH");
				//end
				
				//XXX@OpenSplash-setListener@#OpenSplash-setListener#
				if (mAd != null) {
					
					//	implement onLoaded, onLoadFailed and 
					//	onClosed callback
					//
					mAd.setListener(new SplashAdListener() {

						@Override
						public void onLoaded() {
							//	this callback is called 
							//	when the splash ad is ready to show
							//
							//	show splash ad here
							//
							mAd.show(R.anim.slide_in_from_bottom, 
									R.anim.no_animation);
						}

						@Override
						public void onLoadFailed() {
							//	this callback is called
							//	when this splash ad load fail
							//
							startNextActivity();
						}

						@Override
						public void onClosed() {
							//	this callback is called when:
							//	1.user click the close button
							//	2.user press the onBackpress button
							//	3.dismiss_time setting from the server
							//
							startNextActivity();
						}
					});
				} else {
					//	the ad is not ready now
					//	start the next activity directly
					//
					startNextActivity();
				}
				//end
			}
		}
	};
	
	@Override
	public void onStart() {
		super.onStart();
		
		//	this sample code lets the user see the LOGO first
		//	then to request a open splash ad later
		//
		mHandler.postDelayed(mShowLogoRunnable, 3000);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		//	remember to remove the LOGO timer
		//
		mHandler.removeCallbacks(mShowLogoRunnable);
	}
	
	/**	
	 * you can go to the next activity here
	 * 
	 * */
	private synchronized void startNextActivity() {//TODO
		Intent intent = new Intent();
		intent.setClass(this, mNextActivity);
		startActivity(intent);
		
		finish();
	}
	
	@Override
	public void onBackPressed() {
		startNextActivity();
	}
	
}
