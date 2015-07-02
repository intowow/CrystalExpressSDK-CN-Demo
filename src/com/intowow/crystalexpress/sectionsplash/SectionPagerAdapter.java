package com.intowow.crystalexpress.sectionsplash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class SectionPagerAdapter extends PagerAdapter {

	ArrayList<String> mSections;
	HashMap<Integer, RelativeLayout>  mCanvas;
	SparseArray<SectionAdapter> mAdapters;
	List<Object> mItems = null;
	Context mContext;
	int[] mPics;
	int mStreamListItemPaddingTopButtom = 0;
	int mStreamListItemPaddingLeftRight = 0;
	int mStreamListItemHeight = 0;
	
	@SuppressLint("UseSparseArrays") 
	public SectionPagerAdapter(
		final Context c, 
		ArrayList<String> sections,
		List<Object> items,
		int[] pics,
		int streamListItemPaddingLeftRight,
		int streamListItemPaddingTopButtom,
		int streamListItemHeight) 
	{
		mContext 	= c;
		mSections 	= sections;
		mCanvas 	= new HashMap<Integer, RelativeLayout>();
		mAdapters   = new SparseArray<SectionAdapter>();
		mItems 		= items;
		mPics		= pics;
		mStreamListItemPaddingLeftRight = streamListItemPaddingLeftRight;
		mStreamListItemPaddingTopButtom = streamListItemPaddingTopButtom;
		mStreamListItemHeight = streamListItemHeight;
	}
	
	public void release() {
		mContext = null;
		mCanvas = null;
		mAdapters = null;
	}
	
	@Override
	public int getCount() {
		return mSections.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}		

	private int[] mLastVisibleItem1= new int[]{0,0,0};
	private int[] mLastItemPos1 = new int[]{0,0,0};
	
	public Object instantiateItem(View collection, final int position) {
		RelativeLayout canvas = null;
		if (mCanvas.containsKey(position)) {
			canvas = mCanvas.get(position);
		}
		else {			
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT
			);
			
			canvas = new RelativeLayout(mContext);
			canvas.setLayoutParams(params);		
			mCanvas.put(position, canvas);
			
			RelativeLayout.LayoutParams rParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT
			);
			final ListView lv = new ListView(mContext);
			lv.setBackgroundColor(Color.parseColor("#e7e7e7"));
			lv.setLayoutParams(rParams);
			lv.setDivider(null);
			lv.setDividerHeight(0);
			mLastVisibleItem1[position] = 0;
			mLastItemPos1[position] = 0;
			
			final SectionAdapter adapter = new SectionAdapter(
					(Activity)mContext, 
					mItems, 
					mPics, 
					mStreamListItemPaddingLeftRight, 
					mStreamListItemPaddingTopButtom, 
					mStreamListItemHeight );
			lv.setAdapter(adapter);
		
			canvas.addView(lv);
			mAdapters.put(position, adapter);
		}
		
		((ViewPager) collection).addView(canvas);
		
		return canvas;
	}
	
	 public void destroyItem(View collection, int position, Object view) {
		((ViewPager) collection).removeView((View) view);
		view = null;
	}

}
