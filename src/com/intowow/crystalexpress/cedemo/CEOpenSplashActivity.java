package com.intowow.crystalexpress.cedemo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.intowow.crystalexpress.BaseActivity;
import com.intowow.crystalexpress.R;
import com.intowow.crystalexpress.setting.SettingConfig;
import com.intowow.sdk.I2WAPI;
import com.intowow.sdk.SplashAD.SplashAdListener;

public class CEOpenSplashActivity extends BaseActivity {//XXX#OpenSplash#
	
	//************************************************
	//	common UI
	//
	private Handler mHandler = null;
	private Class<CEStreamActivity> mNextActivity = CEStreamActivity.class;
	
	private SharedPreferences mPreferences = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ce_open_splash);
		
		mHandler = new Handler();
		
		//	for audience targeting
		mPreferences = getSharedPreferences(SettingConfig.PREFERENCES_NAME, Context.MODE_PRIVATE);
		initAudienceTargetingTagList();
	}
	
	private Runnable mShowLogoRunnable = new Runnable() {
		
		@Override
		public void run() {
			
			//	request the open splash ad here
			//
			//XXX@OpenSplash-request@#OpenSplash-request#
			//	check it for landscape ad case
			//
			if(hasRequestedSplashAd()) {
				return;
			}
			
			//	we can request the splash ad 
			//	after the LOGO shows for some time
			//
			mSplashAd = I2WAPI.requesSingleOfferAD(CEOpenSplashActivity.this, "OPEN_SPLASH");
			//end
			
			//XXX@OpenSplash-setListener@#OpenSplash-setListener#
			if (mSplashAd != null) {
				
				//	implement onLoaded, onLoadFailed and 
				//	onClosed callback
				//
				mSplashAd.setListener(new SplashAdListener() {

					@Override
					public void onLoaded() {
						//	this callback is called 
						//	when the splash ad is ready to show
						//
						//	show splash ad here
						//
						mSplashAd.show(R.anim.slide_in_from_bottom, 
								R.anim.no_animation);
					}

					@Override
					public void onLoadFailed() {
						//	this callback is called
						//	when this splash ad load fail
						//
						onSplashAdFinish();
						startNextActivity();
					}

					@Override
					public void onClosed() {
						//	this callback is called when:
						//	1.user click the close button
						//	2.user press the onBackpress button
						//	3.dismiss_time setting from the server
						//
						onSplashAdFinish();
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
	};
	
	@Override
	public void onStart() {
		super.onStart();
		//	this sample code let the user see the LOGO first
		//	then to request a open splash ad later
		//
		mHandler.postDelayed(mShowLogoRunnable, 0);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		//	remember to remove the LOGO timer
		//
		mHandler.removeCallbacks(mShowLogoRunnable);
	}
	
	//XXX@OpenSplash-release@#OpenSplash-release#
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		releaseSplashAd();
	}
	//end
	
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
	
	private void initAudienceTargetingTagList() {
		List<String> tagList = new ArrayList<String>();
		
		if (mPreferences != null) {
			String tagString = mPreferences.getString(SettingConfig.PREFERENCES_AUDIENCE_TARGETING_TAGS, "");
			tagList.addAll(Arrays.asList(tagString.split(",")));
		}
		
		I2WAPI.setAudienceTargetingTags(this, tagList);
	}
	
}
