package com.intowow.crystalexpress.sectionsplash;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.AbsListView.OnScrollListener;

import com.intowow.crystalexpress.Config;
import com.intowow.crystalexpress.LayoutManager;
import com.intowow.crystalexpress.LayoutManager.LayoutID;
import com.intowow.crystalexpress.R;
import com.intowow.sdk.I2WAPI;
import com.intowow.sdk.SplashAD;
import com.intowow.sdk.SplashAD.SplashAdListener;

/**
 * you can use the SplashAD to pop-up the section splash ad.
 * 
 * */
public class SectionSplashActivity extends Activity {
	
	public interface PagerEventListener {
		public void onPageChanged(int pos);
	}
	
	private SplashAD mAd = null;
	private String mPlacement = Config.SECTION_SPLASH_PLACEMENT;
	
	private int						mScrollState = OnScrollListener.SCROLL_STATE_IDLE;
	private int						mActiveIndex = -1;
	private int						mDeferIndex  = 0;
	private ViewPager 				mPager = null;
	private MenuView				mMenuView = null;
	private SectionPagerAdapter		mPagerAdapter = null;
	
	private final static int ITEM_SIZE = 200;
	
	private List<Object> mItems = new ArrayList<Object>(ITEM_SIZE);
	
	private int[] mPids = new int[5];
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		//	you have to add this method in your activity which request the splash ad, 
		//	remember to add the android:configChanges="orientation|screenSize" property
		//	in the Androidanifest.xml
		super.onConfigurationChanged(newConfig);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_section_splash);
		
		createUI();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		//	init the SDK in the onStart()
		//
		I2WAPI.init(this);
		
		//	if you use splash ad, you can use onActivityResume()
		//	in the onStart() instead of onResume()
		//
		I2WAPI.onActivityResume(this);
	}
	
	@Override
	public void onStop() {
		super.onStop();
		I2WAPI.onActivityPause(this);
	}
	
	/**
	 * you can request the section splash ad here
	 * after the menu has be selected
	 * */
	private void deferUpdate() {
    	mActiveIndex = mDeferIndex;
		
		//	request the section splash ad
		//
		mAd = I2WAPI.requesSplashAD(this, mPlacement);
		
		if (mAd != null) {
			mAd.setListener(new SplashAdListener() {

				@Override
				public void onLoaded() {
					mAd.show();
				}

				@Override
				public void onLoadFailed() {
				}

				@Override
				public void onClosed() {
					//	be sure to release the splash ad here
					//
					mAd.release();
				}
			});
		}
    }
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		//	release the splash ad in order to avoid the memory leak
		//
		if(mAd != null){
			mAd.release();
			mAd = null;
		}
		
		mPagerAdapter.release();
	}

	private void createUI() {
		LayoutManager lm = LayoutManager.getInstance(this);
		
		findViewById(R.id.title).getLayoutParams().height = 
				lm.getMetric(LayoutID.STREAM_TITLE_HEIGHT);
		
		ArrayList<String> sections = new ArrayList<String>();
		sections.add("menuA");
		sections.add("menuB");
		sections.add("menuC");
		mMenuView = (MenuView)findViewById(R.id.menu);
		mMenuView.getLayoutParams().height = 
				lm.getMetric(LayoutID.STREAM_TITLE_HEIGHT);
		mMenuView.setSectionList(sections);
		
		for(int i=0; i<ITEM_SIZE; i++) {
			mItems.add(new Object());
		}
		
		mPids[0]=R.drawable.business_1;
		mPids[1]=R.drawable.business_2;
		mPids[2]=R.drawable.business_3;
		mPids[3]=R.drawable.business_4;
		mPids[4]=R.drawable.business_5;
		
		mPagerAdapter = new SectionPagerAdapter(
				this, 
				sections,
				mItems,
				mPids,
				lm.getMetric(LayoutID.STREAM_LIST_ITEM_PADDING_LEFT_RIGHT),
				lm.getMetric(LayoutID.STREAM_LIST_ITEM_PADDING_TOP_BUTTOM),
				lm.getMetric(LayoutID.STREAM_LIST_ITEM_HEIGHT));
		
		mPager = (ViewPager)findViewById(R.id.viewpager);
		mPager.setAdapter(mPagerAdapter);
		mPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int state) {
				mScrollState = state;
				
				if (state == OnScrollListener.SCROLL_STATE_IDLE) {
					if (mActiveIndex != mDeferIndex) {
						deferUpdate();
					}					
				}

			}
			@Override
			public void onPageScrolled(int pos, float positionOffset, int positionOffsetPixels) {
				mMenuView.select(pos + positionOffset);
			}

			@Override
			public void onPageSelected(final int pos) {
				mDeferIndex = pos;
				
				if (mScrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					deferUpdate();
				}
			}			
		});
	}
	
}
