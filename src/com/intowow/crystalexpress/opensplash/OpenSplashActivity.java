package com.intowow.crystalexpress.opensplash;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.intowow.crystalexpress.MainActivity;
import com.intowow.crystalexpress.R;
import com.intowow.sdk.I2WAPI;
import com.intowow.sdk.SplashAD;
import com.intowow.sdk.SplashAD.SplashAdListener;

public class OpenSplashActivity extends Activity {
	
	private SplashAD mAd = null;
	private Handler mHandler = null;
	private Class<MainActivity> mMainActivity = MainActivity.class;
	
	private boolean mIsStartMainActivity			= false;
	private boolean mIsBackgroundTaskDone 			= true;
	private boolean mIsOpenSplashAdDone 			= false;
	private AppBackgroundTask mAppBackgroundTask 	= null; 
	
	private ProgressBar mProgressBar = null;
	
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
		mProgressBar = (ProgressBar)findViewById(R.id.progress);
		mProgressBar.setMax(100);
		
		mHandler = new Handler();
		
		//	init the SDK.
		//
		//	you can call this API only once in your launch flow.
		//
		//	if you need to start the preview mode, 
		//	please passing the activity(not ApplicationContext) as the parameter
		//	and the SDK will parsing the intent on to launch the preview mode.
		//
		I2WAPI.init(this);
		
		//XXX#OpenSplash-BackgroundTask#
		//	in order to speed up the App's initial time,
		//	when the splash ad is showing,
		//	you can start some background task at the same time.
		//
		//	if your App has no initial task in the launch flow,
		//	then you can skip this code.
		//
		startAppBackgroundTask();
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
		I2WAPI.onActivityResume(this);
		
		if (mAd == null) {
			
			//	request the open splash ad
			//
			mAd = I2WAPI.requesSingleOfferAD(this, "OPEN_SPLASH");
			
			if (mAd != null) {
				
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
						//	you can use normal transition effect
						//
						mAd.show();
						
						//	or use overridePendingTransition 
						//	(only support single-offer and portrait ad)
						//	
						//	mAd.show(R.anim.damping_in, R.anim.damping_out);
						
					}

					@Override
					public void onLoadFailed() {
						//	this callback is called
						//	when this splash ad load fail
						//
						mIsOpenSplashAdDone = true;
						if (mHandler != null) {
							mHandler.post(new Runnable() {
								@Override
								public void run() {
									startMainActivity();
								}
							});
						}
					}

					@Override
					public void onClosed() {
						//	this callback is called when:
						//	1.user click the close button
						//	2.user press the onBackpress button
						//	3.dismiss_time setting from the server
						//
						mIsOpenSplashAdDone = true;
						if(mHandler!=null){
							startMainActivity();
						}
					}
				});
			} else {
				mIsOpenSplashAdDone = true;
				startMainActivity();
			}
		}
	}
	
	@Override
	public void onStop() {
		super.onStop();
		I2WAPI.onActivityPause(this);
	}

	/**	
	 * you can go to the original launch activity here, 
	 * and remember to finish this activity
	 * */
	private synchronized void startMainActivity() {//TODO
		if(!mIsStartMainActivity && mIsBackgroundTaskDone && mIsOpenSplashAdDone) {
			Intent intent = new Intent();
			intent.setClass(this, mMainActivity);
			startActivity(intent);
			
			mIsStartMainActivity = true;
			
			Toast.makeText(getApplicationContext(), "finish Open Splash Activity", Toast.LENGTH_SHORT).show();
			
			finish();
		}

	}
	
	/**
	 * in order to speed up the App's initial time,
	 * when the splash ad is showing,
	 * you can start your background task at the same time.
	 * 
	 * if your App has no initial task in the launch flow,
	 * then you can skip this code.
	 * 
	 * */
	private void startAppBackgroundTask() {
		mIsBackgroundTaskDone = false;
		mAppBackgroundTask = new AppBackgroundTask();
		mAppBackgroundTask.execute();
	}
	
	private class AppBackgroundTask extends AsyncTask<Void, Integer, Long> {
	 
		@Override
	    protected void onPostExecute(Long result) {
	    	mIsBackgroundTaskDone = true;
	    	startMainActivity();
	    }

		@Override
		protected Long doInBackground(Void... params) {
			final int maxProgress = 100;
			try {
				for(int i = 0 ; i < maxProgress ; i++) {
					publishProgress(i);
					Thread.sleep(50);
				}
			} catch (InterruptedException e) {
			}
			return null;
		}
		
		@Override
	    protected void onProgressUpdate(Integer... progress){
			mProgressBar.setProgress(progress[0]);
	    }
	}
	
	@Override
	public void onBackPressed() {
		if(mAppBackgroundTask != null && 
				mAppBackgroundTask.getStatus() == AsyncTask.Status.RUNNING) {
			mAppBackgroundTask.cancel(true);
			mIsBackgroundTaskDone = true;
			startMainActivity();
			
			return;
		}
		
		super.onBackPressed();
	}
	
}
