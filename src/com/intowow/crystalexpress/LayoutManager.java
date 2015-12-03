package com.intowow.crystalexpress;

import java.math.BigDecimal;

import android.app.Activity;
import android.util.DisplayMetrics;

public class LayoutManager {
	static public enum LayoutRatio {
		RATIO_178,	///< Screen aspect ratio near 1.78
		RATIO_167,	///< Screen aspect ratio near 1.67
		RATIO_16,	///< Screen aspect ratio near 1.6
		RATIO_15,	///< Screen aspect ratio near 1.5
	}
	
	static private final int 	DEFAULT_SCREEN_WIDTH  	  = 720;
	static private final int 	DEFAULT_SCREEN_HEIGHT 	  = 1280;
	static private final float 	DEFAULT_LOGICAL_DENSITY   = 1.0f;
	static private final double DEFAULT_SCALING_RATIO 	  = 1.0;
	static private final int 	REF_SCREEN_WIDTH 		  = 720;
	
	static private final LayoutRatio DEFAULT_LAYOUT_RATIO = LayoutRatio.RATIO_178;
			
	/// Member fields
	private LayoutRatio mRatio 		    = DEFAULT_LAYOUT_RATIO;
	private float		mLogicalDensity = DEFAULT_LOGICAL_DENSITY;
	private int		    mScreenWidth	= DEFAULT_SCREEN_WIDTH;
	private int		 	mScreenHeight   = DEFAULT_SCREEN_HEIGHT;
	private double		mScalingRatio 	= DEFAULT_SCALING_RATIO;;
	private int[]		mLayoutMetrics  = null;
	
	// Singleton
	static private LayoutManager mInstance = null;
	static public synchronized LayoutManager getInstance(final Activity activity){
		if (mInstance == null) {
			mInstance = new LayoutManager();
			DisplayMetrics dm = new DisplayMetrics();
	        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
	        mInstance.init(dm.density, dm.widthPixels, dm.heightPixels);
		}
		
		return mInstance;
	}
	
	private LayoutManager() {
		mLayoutMetrics = new int[LayoutID.values().length];
	}	
	
	// Layout ID
	static public enum LayoutID {
		// Common metrics
		SCREEN_WIDTH,
		SCREEN_HEIGHT,
		
		STREAM_TITLE_HEIGHT,//90
		STREAM_INDICATOR_HEIGHT,//4
		STREAM_MENU_TEXT_SIZE,//34
		STREAM_LIST_ITEM_WIDTH,//720
		STREAM_LIST_ITEM_HEIGHT,//288
		STREAM_LIST_ITEM_PADDING_TOP_BUTTOM,//20
		STREAM_LIST_ITEM_PADDING_LEFT_RIGHT,//18
		
		CONTENT_TITLE_HEIGHT,//90
		CONTENT_TITLE_BACK_SIZE,//90
		CONTENT_IMAGE_HEIGHT,//684
		CONTENT_RELATIVE_IMAGE_HEIGHT,//570
		CONTENT_TEXT_AREA_MARGIN_TOP,// 60
		CONTENT_TEXT_MARGIN_LEFT_RIGHT,//30
		CONTENT_TEXT_SIZE,//30
		CONTENT_TEXT_LINE_SPACING,//30
		
		STREAM_MENU_BOTTOM_LINE_HEIGHT,//2
		
	}	
	
	/**
	 * Provide the default metrics defined by visual designer
	 */
	private void reset() {
		/* Common metrics */
		sm(LayoutID.SCREEN_WIDTH, 720);
		sm(LayoutID.SCREEN_HEIGHT, 1280);
		
		sm(LayoutID.STREAM_TITLE_HEIGHT,90);
		sm(LayoutID.STREAM_INDICATOR_HEIGHT,4);
		sm(LayoutID.STREAM_MENU_TEXT_SIZE,28);
		sm(LayoutID.STREAM_LIST_ITEM_WIDTH, 720);
		sm(LayoutID.STREAM_LIST_ITEM_HEIGHT, 288);
		sm(LayoutID.STREAM_LIST_ITEM_PADDING_TOP_BUTTOM, 10);
		sm(LayoutID.STREAM_LIST_ITEM_PADDING_LEFT_RIGHT, 18);
		
		sm(LayoutID.CONTENT_TITLE_HEIGHT, 90);
		sm(LayoutID.CONTENT_TITLE_BACK_SIZE, 90);
		sm(LayoutID.CONTENT_IMAGE_HEIGHT, 684);
		sm(LayoutID.CONTENT_RELATIVE_IMAGE_HEIGHT, 570);
		sm(LayoutID.CONTENT_TEXT_AREA_MARGIN_TOP, 60);
		sm(LayoutID.CONTENT_TEXT_MARGIN_LEFT_RIGHT, 30);
		sm(LayoutID.CONTENT_TEXT_SIZE, 36);
		sm(LayoutID.CONTENT_TEXT_LINE_SPACING, 16);
		
		sm(LayoutID.STREAM_MENU_BOTTOM_LINE_HEIGHT,2);
	}
	
	private void updateLayout() {
		sm(LayoutID.SCREEN_WIDTH, mScreenWidth);
		sm(LayoutID.SCREEN_HEIGHT, mScreenHeight);
		
		as(LayoutID.STREAM_TITLE_HEIGHT);
		as(LayoutID.STREAM_INDICATOR_HEIGHT);
		as(LayoutID.STREAM_LIST_ITEM_WIDTH);
		as(LayoutID.STREAM_LIST_ITEM_HEIGHT);
		as(LayoutID.STREAM_LIST_ITEM_PADDING_TOP_BUTTOM);
		as(LayoutID.STREAM_LIST_ITEM_PADDING_LEFT_RIGHT);
		
		as(LayoutID.CONTENT_TITLE_HEIGHT);
		as(LayoutID.CONTENT_TITLE_BACK_SIZE);
		as(LayoutID.CONTENT_IMAGE_HEIGHT);
		as(LayoutID.CONTENT_RELATIVE_IMAGE_HEIGHT);
		as(LayoutID.CONTENT_TEXT_AREA_MARGIN_TOP);
		as(LayoutID.CONTENT_TEXT_MARGIN_LEFT_RIGHT);
		as(LayoutID.CONTENT_TEXT_LINE_SPACING);
		
		ts(LayoutID.CONTENT_TEXT_SIZE);
		ts(LayoutID.STREAM_MENU_TEXT_SIZE);
		
		as(LayoutID.STREAM_MENU_BOTTOM_LINE_HEIGHT);
	}
	
	public void init(float density, int width, int height) {
		mLogicalDensity = density;
		mScreenWidth 	= width;
		mScreenHeight	= height;
		if(width > height){
			// UI not rotate the orientation correctly at this moment
			mScreenHeight = width;
			mScreenWidth = height;
		}
		mScalingRatio	= mScreenWidth / (double) REF_SCREEN_WIDTH;
		
		double screenRatio = (double)mScreenHeight/(double)mScreenWidth;
		
		screenRatio= new BigDecimal(screenRatio)
        .setScale(2, BigDecimal.ROUND_HALF_UP)
        .doubleValue();
		
		if (screenRatio >= 1.78f) {
			mRatio = LayoutRatio.RATIO_178;
		}
		else if (screenRatio >= 1.67f) {
			mRatio = LayoutRatio.RATIO_167;
		}
		else if (screenRatio >= 1.6f) {
			mRatio = LayoutRatio.RATIO_16;
		}
		else {
			mRatio = LayoutRatio.RATIO_15;
		}
		
		//mRatio = LayoutRatio.RATIO_16;
		
		reset();
		updateLayout();
	}
	
	public int getMetric(LayoutID id) {
		return gm(id);
	}
	
	public int applyScaling(int value) {				
		return (int) Math.floor(value * mScalingRatio);
	}
	
	// Abbreviation of getMetric
	private int gm(LayoutID id) {
		return mLayoutMetrics[id.ordinal()];
	}
	
	// Abbreviation of setMetric
	private void sm(LayoutID id, int value) {
		mLayoutMetrics[id.ordinal()] = value;
	}
	
	// Abbreviation of applyScaling
	private void as(LayoutID id) {
		int oldValue = mLayoutMetrics[id.ordinal()];
		int newValue = (int) Math.floor(oldValue * mScalingRatio);
		mLayoutMetrics[id.ordinal()] = newValue;
	}
	
	// Abbreviation of textScaling
	private void ts(LayoutID id) {
		int oldValue = mLayoutMetrics[id.ordinal()];
		if (mScalingRatio >= 1.0f) {			
			int newValue = (int) Math.floor((oldValue / 2.0f) *  mLogicalDensity);
			mLayoutMetrics[id.ordinal()] = newValue;
		}
		else {
			double adjustRatio = (1.0 + mScalingRatio) / 2.0;
			adjustRatio = (1.0 + adjustRatio) / 2.0;
			int newValue = (int) Math.floor((oldValue / 2.0f) *  mLogicalDensity  * adjustRatio);
			mLayoutMetrics[id.ordinal()] = newValue;
		}
	}
	
	
	
	public LayoutRatio getRatio() {
		return mRatio;
	}

}