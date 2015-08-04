package com.intowow.crystalexpress.cedemo;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.intowow.crystalexpress.BaseActivity;
import com.intowow.crystalexpress.Config;
import com.intowow.crystalexpress.LayoutManager;
import com.intowow.crystalexpress.LayoutManager.LayoutID;
import com.intowow.crystalexpress.R;
import com.intowow.crystalexpress.cedemo.adapter.ContentPagerAdapter;
import com.intowow.sdk.FlipADDeferHelper;

/**
 * to let the SDK know the App status. (foreground or background)
 * you can let your activity extend BaseActivity simply.
 * */
public class CEContentActivity extends BaseActivity {
	
	//************************************************
	//	common UI
	//
	public interface PagerEventListener {
		public void onPageScrollStateChanged(int state);
		public void onPageChanged(int pos);
	}
	
	private ViewPager pager;
	private final static int PAGE_SIZE = 50;
	private List<Object> mItems = new ArrayList<Object>();
	
	

	//************************************************
	//	content and flip ad
	//
	private final static String mFlipPlacement = Config.FLIP_PLACEMENT;
	private final static String mContentPlacement = Config.CONTENT_PLACEMENT;
	private FlipADDeferHelper mFlipHelper = null;
	private ContentPagerAdapter 	mPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {// XXX
		super.onCreate(savedInstanceState);

		// *************************************************
		// common UI setting
		//
		for(int i = 0 ; i < PAGE_SIZE ; i++){
			mItems.add(new Object());
		}
		
		LayoutManager lm = LayoutManager.getInstance(this);
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		
		RelativeLayout root = new RelativeLayout(this);
		root.setLayoutParams(params);
		root.setBackgroundColor(Color.WHITE);
		
		//	title
		//
		params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				lm.getMetric(LayoutID.CONTENT_TITLE_HEIGHT));
		RelativeLayout title = new RelativeLayout(this);
		title.setId(100);
		title.setLayoutParams(params);
		title.setBackgroundResource(R.drawable.article_topbar);
		
		//	title back
		//
		params = new RelativeLayout.LayoutParams(
				lm.getMetric(LayoutID.CONTENT_TITLE_BACK_SIZE),
				lm.getMetric(LayoutID.CONTENT_TITLE_BACK_SIZE));
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		final ImageButton titleBack = new ImageButton(this);
		titleBack.setLayoutParams(params);
		titleBack.setBackgroundResource(R.drawable.back_nm);
		titleBack.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(final View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					((ImageButton) v).setBackgroundResource(R.drawable.back_at);
		        } 
				else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
		        	((ImageButton) v).setBackgroundResource(R.drawable.back_nm);
		        }
				return false;
			}
		});
		
		
		// *************************************************
		// content and flip ad
		//
		
		// you can use bundle to let the CEStreamActivity launch the
		// interstitial ad
		//
		titleBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				titleBack.setEnabled(false);
				startCEStreamActivity();
			}

		});
		
		mFlipHelper = new FlipADDeferHelper(this, mFlipPlacement);
		
		mPagerAdapter = new ContentPagerAdapter(
				this, 
				mItems, 
				mFlipHelper,
				mContentPlacement);
		
		//	pager
		//
		params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		params.addRule(RelativeLayout.BELOW, 100);
		pager = new ViewPager(this);
		pager.setLayoutParams(params);
		pager.setAdapter(mPagerAdapter);
		pager.setOnPageChangeListener(mPagerAdapter);
		pager.setCurrentItem(0);
		
		title.addView(titleBack);
		root.addView(title);
		root.addView(pager);
		
		setContentView(root);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		mFlipHelper.onStart();
		mPagerAdapter.onStart();
	}

	@Override
	public void onPause() {
		super.onPause();

		mFlipHelper.onStop();
		mPagerAdapter.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		mFlipHelper.destroy();
		mFlipHelper = null;
		
		mPagerAdapter.destroy();
		mFlipHelper = null;
	}
	
	@Override
	public void onBackPressed() {
		startCEStreamActivity();
	}

	private void startCEStreamActivity() {
		// you can load the interstitial ad
		// after back to the stream activity
		//
		Bundle b = new Bundle();
		b.putString(CEStreamActivity.KEY_LOAD_INTERSTITIAL_AD, "Y");

		Intent intent = new Intent();
		intent.putExtras(b);
		intent.setClass(this, CEStreamActivity.class);
		startActivity(intent);
		finish();
	}

}
