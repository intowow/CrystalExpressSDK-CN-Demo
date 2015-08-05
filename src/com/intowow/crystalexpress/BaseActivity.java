package com.intowow.crystalexpress;

import android.app.Activity;
import android.os.Bundle;

import com.intowow.sdk.I2WAPI;

/**
 * to let the SDK know the App status. (foreground or background)
 * you can let your activity extend BaseActivity simply.
 * */
public class BaseActivity extends Activity{//XXX#BaseActivity#
	
	private boolean mHasResume = false;
	private boolean mHasPause = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {// XXX
		super.onCreate(savedInstanceState);
		
		//	init the SDK.
		//
		I2WAPI.init(this);
		
    	//	Test Mode
    	//
		//I2WAPI.init(this, true);
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
}
