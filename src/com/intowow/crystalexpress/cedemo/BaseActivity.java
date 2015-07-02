package com.intowow.crystalexpress.cedemo;

import com.intowow.sdk.I2WAPI;

import android.app.Activity;

/**
 * to let the SDK know the App status. (foreground or background)
 * you can let your activity extend BaseActivity simply.
 * */
public class BaseActivity extends Activity{//XXX#BaseActivity#
	
	//XXX@BaseActivity-onResume@
	@Override
	public void onResume() {
		super.onResume();
		
	    //  let the SDK know the App status. (foreground or background)
	    //
	    //  you should call this API in all of your activity's onResume() status.
	    //
	    //  if you use splash ad, you can call this API
	    //  in the onStart() instead of onResume() too.
	    //
		I2WAPI.onActivityResume(this);
	}
	//end
	
	//XXX@BaseActivity-onPause@
	@Override
	public void onPause() {
		super.onPause();
		
	    //  let the SDK know the App status. (foreground or background)
	    //
	    //  you should call this API in all of your activity's onPause() status.
	    //
	    //  if you use splash ad, you can call this API
	    //  in the onStop() instead of onPause() too.
	    //
		I2WAPI.onActivityPause(this);
	}
	//end
}
