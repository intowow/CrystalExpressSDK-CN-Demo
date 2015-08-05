package com.intowow.crystalexpress.opensplash;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.intowow.crystalexpress.BaseActivity;
import com.intowow.crystalexpress.MainActivity;
import com.intowow.crystalexpress.R;
import com.intowow.sdk.I2WAPI;
import com.intowow.sdk.SplashAD;
import com.intowow.sdk.SplashAD.SplashAdListener;

/**
 * this sample lets the user see the LOGO first,
 * then, requests a open splash ad and go to the
 * next Activity later
 * 
 * */
public class OpenSplashActivity extends BaseActivity {
	
	private SplashAD mAd = null;
	private Handler mHandler = null;
	private Class<MainActivity> mMainActivity = MainActivity.class;
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		//	you have to add this method in the activity,
		//	remember to add the android:configChanges="orientation|screenSize" property
		//	in the Androidanifest.xml
		super.onConfigurationChanged(newConfig);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_open_splash);
		
		mHandler = new Handler();
		
	}
	
	private Runnable mShowLogoRunnable = new Runnable() {
		
		@Override
		public void run() {
			
			//	before we start the next activity
			//	please to request the open splash ad first
			//	then start the next activity later
			//
			if (mAd == null) {
				
				//	request the open splash ad
				//
				mAd = I2WAPI.requesSingleOfferAD(
						OpenSplashActivity.this, 
						"OPEN_SPLASH");
				
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
							mAd.show(
									R.anim.slide_in_from_bottom, 
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
					
					Toast.makeText(
							OpenSplashActivity.this, 
							"the ad is not ready now", 
							Toast.LENGTH_SHORT).show();
					
					startNextActivity();
				}
			}
		}
	};
	
	@Override
	protected void onStart() {
		super.onStart();
		
		//	this sample code lets the user see the LOGO first
		//	then to request a open splash ad later
		//
		mHandler.postDelayed(mShowLogoRunnable, 800);
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
	 * */
	private synchronized void startNextActivity() {//TODO
		Intent intent = new Intent();
		intent.setClass(this, mMainActivity);
		startActivity(intent);
		
		finish();
	}
	
}
