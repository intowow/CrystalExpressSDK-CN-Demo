package com.intowow.crystalexpress.flip;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;

import com.intowow.crystalexpress.LayoutManager;
import com.intowow.crystalexpress.LayoutManager.LayoutID;
import com.intowow.crystalexpress.R;
import com.intowow.sdk.I2WAPI;
import com.intowow.sdk.FlipADDeferHelper;

public class FlipActivity extends Activity {//XXX#Flip-activity#
	
	//**************************************
	//	common UI
	//
	private ViewPager pager;
	private final static int ITEM_SIZE = 50;
	private List<Object> mItems = new ArrayList<Object>();
	
	//**************************************
	//	flip ad
	//
	//XXX@Flip-init@#Flip-init#
	private final static String mPlacement = "FLIP";
	private FlipADDeferHelper mFlipHelper = null;
	//end
	private FlipPagerAdapter mFlipPagerAdapter = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {// XXX
		super.onCreate(savedInstanceState);
		
		//**************************************
		//	common UI
		//
		setContentView(R.layout.activity_flip);
		initUI();
		
		//**************************************
		//	flip ad
		//
		//XXX@Flip-inithelper@#Flip-inithelper#
		mFlipHelper = new FlipADDeferHelper(this, mPlacement);
		
		//	let the SDK know that this placement is active now.
		//
		mFlipHelper.setActive();
		//end
		
		//XXX@Flip-constructor@#Flip-constructor#
		mFlipPagerAdapter = new FlipPagerAdapter(
				this, 
				mItems, 
				mFlipHelper);
		//end
		
		pager = (ViewPager)findViewById(R.id.viewpager);
		pager.setAdapter(mFlipPagerAdapter);
		pager.setOnPageChangeListener(mFlipPagerAdapter);
		pager.setCurrentItem(0);
		
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		I2WAPI.init(this);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		I2WAPI.onActivityResume(this);
		//XXX@Flip-onResume@#Flip-onResume#
		if(mFlipHelper != null) {
			mFlipHelper.onStart();
		}
		//end
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		I2WAPI.onActivityPause(this);
		//XXX@Flip-onPause@#Flip-onPause#
		if(mFlipHelper != null) {
			mFlipHelper.onStop();
		}
		//end
	}

	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		//XXX@Flip-onDestroy@#Flip-onDestroy#
		if(mFlipHelper != null) {
			mFlipHelper.destroy();
			mFlipHelper = null;
		}
		//end
	}
	
	private void initUI() {
		for(int i=0; i<ITEM_SIZE; i++) {
			mItems.add(new Object());
		}
		
		LayoutManager lm = LayoutManager.getInstance(this);
		
		//	title
		//
		findViewById(R.id.title).getLayoutParams().height = 
				lm.getMetric(LayoutID.CONTENT_TITLE_HEIGHT);
		
		//	title back
		//
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				lm.getMetric(LayoutID.CONTENT_TITLE_BACK_SIZE),
				lm.getMetric(LayoutID.CONTENT_TITLE_BACK_SIZE));
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		final View titleBack = findViewById(R.id.titleback);
		titleBack.setLayoutParams(params);
		titleBack.setBackgroundResource(R.drawable.back_nm);
		titleBack.setOnTouchListener( new OnTouchListener() {
			@Override
			public boolean onTouch(final View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					v.setBackgroundResource(R.drawable.back_at);
		        } 
				else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
		        	v.setBackgroundResource(R.drawable.back_nm);
		        }
				return false;
			}
		});
		titleBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				titleBack.setEnabled(false);
				onBackPressed();
			}
			
		});
	}
}
