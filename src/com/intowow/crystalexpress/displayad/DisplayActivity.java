package com.intowow.crystalexpress.displayad;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.intowow.crystalexpress.BaseActivity;
import com.intowow.crystalexpress.LayoutManager;
import com.intowow.crystalexpress.LayoutManager.LayoutID;
import com.intowow.crystalexpress.R;
import com.intowow.crystalexpress.content.CrystalExpressScrollView;
import com.intowow.crystalexpress.content.CrystalExpressScrollView.ScrollViewListener;
import com.intowow.sdk.Ad;
import com.intowow.sdk.AdError;
import com.intowow.sdk.AdListener;
import com.intowow.sdk.DisplayAd;

public class DisplayActivity extends BaseActivity implements AdListener, ScrollViewListener{
	
	private final static String TAG = "DisplayActivity";
	final static String PLACEMENT 	= "STREAM";
	private CrystalExpressScrollView mScrollView = null;
	private RelativeLayout mDisplayAdLayout = null;
	private boolean mLoaded = false;
	private boolean mIsCalledStart = false;
	private DisplayAd mDisplayAd = null;
	
	private LayoutManager mLayoutMgr = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_display);
		
		//	adjust UI component
		//
		mLayoutMgr = LayoutManager.getInstance(this);
		View topinfo = findViewById(R.id.topinfo);
		View secondinfo = findViewById(R.id.secondinfo);
		topinfo.getLayoutParams().height = mLayoutMgr.getMetric(LayoutID.DISPLAY_AD_TOP_IMG_HEIGHT);
		((LinearLayout.LayoutParams)topinfo.getLayoutParams()).bottomMargin = mLayoutMgr.getMetric(LayoutID.DISPLAY_AD_MARGIN);
		secondinfo.getLayoutParams().height = mLayoutMgr.getMetric(LayoutID.DISPLAY_AD_BOTTOM_IMG_HEIGHT);
		((LinearLayout.LayoutParams)secondinfo.getLayoutParams()).topMargin = mLayoutMgr.getMetric(LayoutID.DISPLAY_AD_MARGIN);
		
		
		//	#############################
		//	Display ad init flow
		//
		mScrollView = (CrystalExpressScrollView) findViewById(R.id.scrollview);
		mDisplayAdLayout = (RelativeLayout) findViewById(R.id.displayad);
		
		//	control ad events, such as start(), stop
		//
		mScrollView.setScrollViewListener(this);
		
		//	init display ad
		//
		mDisplayAd = new DisplayAd(this, PLACEMENT);
		
		//	set listener
		//
		mDisplayAd.setAdListener(this);
		
		//	optionally, you can resize ad before loading ad by this way
		//		DisplayAd.setWidth(int width)
		//	SDK will resize ad's width and height automatically
		
		//	mDisplayAd.setWidth(500);
		
		//	if your activity has more than one display ad instance
		//	be sure to set the ad place (or means instance order)
		//	to SDK for analyzing the ad fill rate
		//	DisplayAd.setPlace(int place);
		
		//	mDisplayAd.setPlace(1);
		
		mDisplayAd.setAutoplay(true);
		
		//	load ad.
		//	SDK will use 'non-blocking' call
		//	to call onAdLoaded or onAdError
		//
		mDisplayAd.loadAd(0L);
		
		
		
		//	#############################
		//	in order to let developer know 
		//	that the App need to call play(), stop()
		//	while the ad is scrolled 
		//	'outside' or 'inside' of the screen.
		//	so we scroll this page to the bottom first
		//
		mScrollView.postDelayed(new Runnable() {
		    @Override
		    public void run() {
		    	mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
		    }
		}, 100);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		//	check if the ad is visible in the screen
		//	if does, play the ad directly
		//
		checkDisplayAD();
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		if(mLoaded && mDisplayAd != null) {
			mIsCalledStart = false;
        	mDisplayAd.stop();
		}
	}

	@Override
	protected void onDestroy() {
		mLoaded = false;
		
		if(mDisplayAd != null) {
			mDisplayAd.destroy();
			mDisplayAd = null;
		}
		
		super.onDestroy();
	}

	@Override
	public void onScrollChanged(CrystalExpressScrollView scrollView, int x,
			int y, int oldX, int oldY) {
		checkDisplayAD();
	}

	@Override
	public void onScrollViewIdle() {
	}
	
	private void checkDisplayAD(){
		
		if(mLoaded && mDisplayAd != null) {
	        boolean isAdVisiable = isADVisiable();
	        if(!isAdVisiable && mIsCalledStart){
	        	//	the ad is scrolled outside of the screen
	        	//
	        	mIsCalledStart = false;
	        	mDisplayAd.stop();
	        }else if(!mIsCalledStart && isAdVisiable){
	        	//	the ad is scrolled inside of the screen
	        	//	and never call play()
	        	//
	        	mIsCalledStart = true;
	        	mDisplayAd.play();
	        }
		}
        
	}
	
	/**
	 * 
	 * check if the ad is visible in the screen
	 * 
	 * */
	private boolean isADVisiable(){
		if(mScrollView == null || mDisplayAdLayout == null){
			return false;
		}
		int sT 	= mScrollView.getScrollY();
        int sH  = mScrollView.getHeight();
        int sB 	= sT+sH;
        int adT = mDisplayAdLayout.getTop();
        int adB = mDisplayAdLayout.getBottom();
        return (adB - sT >0) && (sB - adT >0);
	}

	@Override
	public void onError(Ad ad, AdError error) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onError");
	}

	@Override
	public void onAdLoaded(Ad ad) {
		
		Log.d(TAG, "onAdLoaded");
		
		if(mDisplayAd != ad) {
			return;
		}
		
		if(mDisplayAdLayout != null) {
			mDisplayAdLayout.removeAllViews();
		}
		
		View adView = mDisplayAd.getView();
		if(adView != null) {
			
			mLoaded = true;
			
			//	add ad view into the Display Ad Layout
			//
			mDisplayAdLayout.addView(adView);
			
			//	set layout parameter
			//
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)adView.getLayoutParams();
			params.addRule(RelativeLayout.CENTER_IN_PARENT);
			
			//	check if need auto play
			//
			if(mHasResume && isADVisiable()) {
				mDisplayAd.play();
			}
		}
	}

	@Override
	public void onAdClicked(Ad ad) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onAdClicked");
	}

	@Override
	public void onAdImpression(Ad ad) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onAdImpression");
	}

	@Override
	public void onAdMute(Ad ad) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onAdMute");
	}

	@Override
	public void onAdUnmute(Ad ad) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onAdUnmute");
	}

	@Override
	public void onVideoEnd(Ad arg0) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onVideoEnd");
	}

	@Override
	public void onVideoProgress(Ad arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onVideoProgress");
	}

	@Override
	public void onVideoStart(Ad arg0) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onVideoStart");
	}

}
