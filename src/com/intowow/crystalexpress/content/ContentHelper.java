package com.intowow.crystalexpress.content;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;


public class ContentHelper {//XXX#Content-helper#
	static public interface ContentAdListener {
		public int onAdLoaded(int position);
	}
	private Context  mContext 	= null;
	private String   mPlacement 	= null;
	private boolean mImpressionHistory = false;
	private boolean mRequestHistory = false;
	private boolean mIsLoadAd	= false;
	
	private com.intowow.sdk.ContentADHelper mHelper = null;
	
	private CrystalExpressScrollView mScrollView = null;
	private RelativeLayout mContentAdLayout = null;
	
	public ContentHelper(final Activity activity, String placement, 
			CrystalExpressScrollView scrollView, RelativeLayout contentAdLayout) {
		mContext   = activity;
		mPlacement = placement;
		mScrollView = scrollView;
		mContentAdLayout = contentAdLayout;
		mHelper = new com.intowow.sdk.ContentADHelper((Activity) mContext, mPlacement);
	}
	
	public void destroy() {
		mContext = null;
		if(mHelper != null){
			mHelper.destroy();
			mHelper = null;
		}
	}
	
	public void setActive() {
		if (mHelper != null) {
			mHelper.setActive();
		}
	}
	
	public void stop() {
		if (mHelper != null) {
			mHelper.stop();
		}
	}
	
	public void start() {
		if (mHelper != null && checkADVisiable()) {
			mHelper.start();
		}
	}
	
	public void onPageSelected(int position) {
		if (mHelper != null) {
			mHelper.onPageSelected(position);
		}
	}
	
	// XXX@Content-requestAD@#Content-requestAD#
	public View requestAD(int position) {
		if (mHelper != null) {
			
			//	you can resize the ad width by
			//
			//	mHelper.requestAD(position, intWidthValue);
			
			return mHelper.requestAD(position);
		}
		
		return null;
	}
	//end
	
	public boolean checkADVisiable(){
		if(mScrollView == null || mContentAdLayout == null){
			return false;
		}
		int sT 	= mScrollView.getScrollY();
        int sH  = mScrollView.getHeight() ;
        int sB 	= sT+sH;
        int adT = mContentAdLayout.getTop();
        int adB = mContentAdLayout.getBottom();
        return (adB - sT >0) && (sB - adT >0);
	}
	
	public boolean checkContentAD(){
		final View contentAd;
		if(!mRequestHistory){//XXX
			contentAd = requestAD(0);
			addAd(contentAd);
			mRequestHistory = true;
			return (contentAd!=null);
		}
		
        boolean isAdVisiable = checkADVisiable();
        if(!isAdVisiable && mImpressionHistory){
        	mImpressionHistory = false;
        	stop();
        }else if(!mImpressionHistory && isAdVisiable){
        	mImpressionHistory = true;
        	start();
        }
        
        return isAdVisiable;
	}
	
	public void loadContentAd() {
		if(mIsLoadAd) {
			return;
		}
		mIsLoadAd = true;
		View contentAd = requestAD(0);
		addAd(contentAd);
		mRequestHistory = true;
	}
	
	// XXX@Content-background@#Content-background#
	private void addAd(View contentAd) {
		if(contentAd!=null){
			
			//	you can set your background here
			//
			contentAd.setBackgroundColor(Color.WHITE);
			
			mContentAdLayout.getLayoutParams().height = RelativeLayout.LayoutParams.WRAP_CONTENT;
			mContentAdLayout.removeAllViews();
			mContentAdLayout.addView(contentAd);
		}
	}
	//end
	
}
