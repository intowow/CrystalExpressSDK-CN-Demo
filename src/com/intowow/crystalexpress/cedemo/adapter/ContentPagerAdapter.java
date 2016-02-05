package com.intowow.crystalexpress.cedemo.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.intowow.crystalexpress.LayoutManager;
import com.intowow.crystalexpress.LayoutManager.LayoutID;
import com.intowow.crystalexpress.R;
import com.intowow.crystalexpress.content.ContentHelper;
import com.intowow.crystalexpress.content.CrystalExpressScrollView;
import com.intowow.crystalexpress.content.CrystalExpressScrollView.ScrollViewListener;

public class ContentPagerAdapter extends PagerAdapter implements OnPageChangeListener {
	
	//**********************************************
	//	common UI
	//
	final int SCROLL_VIEW_ID = 1000;
	final int CONTENT_AD_ID = 2000;
	
	private List<Object> mNewsList = null;
	private Context mContext;
	private SparseArray<LinearLayout> canvasList;
	private int mCurrentPosition = 0;
	
	//**********************************************
	//	content and flip ad
	//
	private String mContentPlacement;
	/**every page has one ContentHelper*/
	private SparseArray<ContentHelper> mContentHelper;
	
	public ContentPagerAdapter(
			Context c, 
			List<Object> newsList,
			String contentPlacement)
	{
		mContext = c;
		this.mNewsList = newsList;				
		canvasList = new SparseArray<LinearLayout>();
		
		//	every page has one content helper
		//
		mContentHelper = new SparseArray<ContentHelper>();
		mContentPlacement = contentPlacement;
	}
	
	public Object instantiateItem(View collection, final int position) {//XXX
		
		LinearLayout canvas = getCanvas(position);
		if (canvas == null) {
			
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT);
			
			if(canvas == null){
				//	use content ad
				//
				canvas = new LinearLayout(mContext);
				canvas.setBackgroundColor(Color.WHITE);
				canvas.setLayoutParams(params);
				
				LayoutManager lm = LayoutManager.getInstance((Activity) mContext);
				
				final CrystalExpressScrollView sv = new CrystalExpressScrollView(mContext);
				sv.setLayoutParams(params);
				sv.setId(SCROLL_VIEW_ID);
				
				RelativeLayout root = new RelativeLayout(mContext);
				root.setLayoutParams(params);
				
				RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.MATCH_PARENT,
						lm.getMetric(LayoutID.CONTENT_IMAGE_HEIGHT));
				//	image1
				//
				ImageView contentImage = new ImageView(mContext);
				contentImage.setId(100);
				contentImage.setScaleType(ScaleType.MATRIX);
				contentImage.setBackgroundResource(R.drawable.article_1);
				contentImage.setLayoutParams(rp);
				
				//	text area
				//
				rp = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.MATCH_PARENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				rp.addRule(RelativeLayout.BELOW, 100);
				rp.topMargin = lm.getMetric(LayoutID.CONTENT_TEXT_AREA_MARGIN_TOP);
				rp.leftMargin = lm.getMetric(LayoutID.CONTENT_TEXT_MARGIN_LEFT_RIGHT);
				rp.rightMargin = lm.getMetric(LayoutID.CONTENT_TEXT_MARGIN_LEFT_RIGHT);
				
				TextView text1 = new TextView(mContext);
				text1.setId(200);
				text1.setText(R.string.content_text_top);
				text1.setTextColor(Color.parseColor("#929292"));
				text1.setLayoutParams(rp);
				text1.setTextSize(TypedValue.COMPLEX_UNIT_PX, lm.getMetric(LayoutID.CONTENT_TEXT_SIZE));
				text1.setLineSpacing(lm.getMetric(LayoutID.CONTENT_TEXT_LINE_SPACING), 1.0f);
				
				rp = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.MATCH_PARENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				rp.addRule(RelativeLayout.BELOW, 200);
				rp.topMargin = lm.getMetric(LayoutID.CONTENT_TEXT_AREA_MARGIN_TOP);
				rp.leftMargin = lm.getMetric(LayoutID.CONTENT_TEXT_MARGIN_LEFT_RIGHT);
				rp.rightMargin = lm.getMetric(LayoutID.CONTENT_TEXT_MARGIN_LEFT_RIGHT);
				
				TextView text2 = new TextView(mContext);
				text2.setId(300);
				text2.setLineSpacing(lm.getMetric(LayoutID.CONTENT_TEXT_LINE_SPACING), 1.0f);
				text2.setTextColor(Color.parseColor("#929292"));
				text2.setText(R.string.content_text_bottom);
				text2.setTextSize(TypedValue.COMPLEX_UNIT_PX, lm.getMetric(LayoutID.CONTENT_TEXT_SIZE));
				text2.setLayoutParams(rp);
				
				//	content ad view
				//
				rp = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.MATCH_PARENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				rp.addRule(RelativeLayout.BELOW, 300);
				final RelativeLayout contentAdLayout = new RelativeLayout(mContext);
				contentAdLayout.setLayoutParams(rp);
				contentAdLayout.setId(CONTENT_AD_ID);
				
				//	relative image
				//
				rp = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.MATCH_PARENT,
						lm.getMetric(LayoutID.CONTENT_RELATIVE_IMAGE_HEIGHT));
				rp.addRule(RelativeLayout.BELOW, CONTENT_AD_ID);
				//	image1
				//
				ImageView relativeContentImage = new ImageView(mContext);
				relativeContentImage.setId(400);
				relativeContentImage.setScaleType(ScaleType.MATRIX);
				relativeContentImage.setBackgroundResource(R.drawable.news_related_2);
				relativeContentImage.setLayoutParams(rp);
				
				root.addView(contentImage);
				root.addView(text1);
				root.addView(text2);
				root.addView(contentAdLayout);
				root.addView(relativeContentImage);
				sv.addView(root);
				
				canvas.addView(sv);
				
				//	create content ad helper
				//	be sure to init the scroll view and the content relative layout
				//
				final ContentHelper contentHelper = new ContentHelper(
						(Activity)mContext, 
						mContentPlacement, 
						sv, 
						contentAdLayout);
				
				//	set content ad callback
				// 
				sv.setScrollViewListener(new ScrollViewListener(){
					public void onScrollChanged(CrystalExpressScrollView scrollView,int x, int y, int oldX, int oldY){
					}
			        public void onScrollViewIdle(){
			        	contentHelper.checkContentAD();
			        }
				});
				
				//	you can load the content ad directly,
				//	or load it when the pager is onPageSelected.
				//
				if(position == 0) {
					contentHelper.loadContentAd();
					contentHelper.onPageSelected(0);
				}
				
				mContentHelper.put(position, contentHelper);
				
			}
						
			canvasList.put(position, canvas);			
		}
		
		((ViewPager) collection).addView(canvas);
		return canvas;
	}
	
	public void destroyItem(View collection, int position, Object view) {
		((ViewPager) collection).removeView((View) view);
		view = null;
	}
	
	public LinearLayout getCanvas(int position) {
		return canvasList.get(position);		
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public void onPageScrollStateChanged(int state) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int position) {
		
		//	stop the previous content ad
		//
		if (mContentHelper != null) {
			ContentHelper helper = mContentHelper.get(mCurrentPosition);
			if(helper != null) {
				helper.stop();
			}
		}
		
		mCurrentPosition = position;
		
		if (mContentHelper != null) {
			ContentHelper helper = mContentHelper.get(mCurrentPosition);
			if(helper != null) {
				
				//	let the SDK know that this placement is active now.
				//
				helper.setActive();
				helper.onPageSelected(0);
				
				//	load the content ad
				//
				helper.loadContentAd();
				
				//	start the ad
				//
				helper.start();
			}
		}
	}
	
	public void onStart() {
		
		if(mContentHelper != null) {
			ContentHelper helper = mContentHelper.get(mCurrentPosition);
			if(helper != null) {
				helper.start();
			}
		}
		
	}
	
	public void onStop() {
		if(mContentHelper != null){
			ContentHelper helper = mContentHelper.get(mCurrentPosition);
			if(helper != null) {
				helper.stop();
			}
		}
	}
	
	public void destroy() {
		mContext = null;
		if(mContentHelper != null){
			for(int i = 0 ; i < mContentHelper.size() ; i++) {
				mContentHelper.get(mContentHelper.keyAt(i)).destroy();
			}
			mContentHelper = null;
		}
		if(canvasList != null){
			canvasList.clear();
		}
	}
	
	public int getItemPosition(Object object){
		return POSITION_NONE;
	}
	
	public int getCount() {
		return mNewsList.size();
	}
	
	public void destroyItem(ViewGroup collection, int position, Object view){
		collection.removeView((View) view);
	}

	public Parcelable saveState(){
		return null;
	}
}
