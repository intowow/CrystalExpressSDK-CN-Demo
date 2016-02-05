package com.intowow.crystalexpress;

import android.app.Activity;
import android.os.Bundle;

import com.intowow.sdk.I2WAPI;
import com.intowow.sdk.SplashAD;

/**
 * to let the SDK know the App status. (foreground or background)
 * you can let your activity extend BaseActivity simply.
 * */
public class BaseActivity extends Activity{//XXX#BaseActivity#
	
	private final static String KEY_HAS_SPLASH_AD = "KEY_HAS_SPLASH_AD";
	
	
	protected boolean mHasResume = false;
	protected boolean mHasPause = false;
	
	//	for splash ad
	//
	protected SplashAD mSplashAd = null;
	private boolean mHasSplashAd = false;
	private boolean mIsFinishSplashAd = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {// XXX
		super.onCreate(savedInstanceState);
		
		//	initial the SDK.
		//	production mode
		//
		//I2WAPI.init(this);
		
    	//	Test Mode
    	//
		I2WAPI.init(this, true);
		
		//XXX@OpenSplash-startapplication@#OpenSplash-startapplication#
		//	you can launch the BaseApplication.java
		//	for requesting the enter foreground splash ad
		//
		getApplicationContext();
		//end
		
		//	splash ad logic.
		//	when SDK start to show the landscape splash ad,
		//	your screen origination will be rotated, 
		//	and your activity will go through some life-cycle again at the same time.
		//	so to avoid request the splash ad again,
		//	we should save the status in the onSaveInstanceState()
		//	and reload it in the onCreate()
		//
		if(savedInstanceState != null) {
			if(savedInstanceState.containsKey(KEY_HAS_SPLASH_AD)) {
				mHasSplashAd = savedInstanceState.getBoolean(KEY_HAS_SPLASH_AD);
				savedInstanceState.remove(KEY_HAS_SPLASH_AD);
			}
		}
		
	}
	
	@Override 
	protected void onStart() {
		super.onStart();
		
		resumeApp();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		//	in some cases,
		//	the Actvivity's life cycle will not pass through the onStart() status
		//	so we should check if the APP is starting again
		//
		resumeApp();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		pauseApp();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
		//	in some cases,
		//	the Actvivity's life cycle will not pass through the onPause() status
		//	so we should check if the APP is stopping  again
		//
		pauseApp();
	}
	
	/**
	 * 
	 *	let the SDK know the App status. (foreground or background)
	 * 
	 * */
	private void resumeApp() {
		if(!mHasResume) {
			mHasResume = true;
			I2WAPI.onActivityResume(this);
		}
		mHasPause = false;
	}
	
	/**
	 *	let the SDK know the App status. (foreground or background)
	 * 
	 * */
	private void pauseApp() {
		if(!mHasPause) {
			mHasPause = true;
			I2WAPI.onActivityPause(this);
		}
		mHasResume = false;
	}
	
	//	==============================
	//		Splash Ad logic
	//	==============================
	//
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		//	save mHasSplashAd for showing landscape ad 
		//
		outState.putBoolean(KEY_HAS_SPLASH_AD, mSplashAd != null);
		super.onSaveInstanceState(outState);
	}
	
	protected boolean hasRequestedSplashAd() {
		return mHasSplashAd;
	}
	
	protected void onSplashAdFinish() {
		mIsFinishSplashAd = true;
		releaseSplashAd();
	}
	
	protected void releaseSplashAd() {
		if(mIsFinishSplashAd) {
			
			if(mSplashAd != null) {
				mSplashAd.release();
				mSplashAd = null;
			}
			
			mIsFinishSplashAd = false;
			mHasSplashAd = false;
		}
	}
	
}
