package com.intowow.crystalexpress.flip;

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
import com.intowow.crystalexpress.content.CrystalExpressScrollView;
import com.intowow.sdk.FlipADDeferHelper;
import com.intowow.sdk.FlipADDeferHelper.AppADListener;

public class FlipPagerAdapter extends PagerAdapter implements OnPageChangeListener {
	
	//******************************************
	//	common UI
	//
	private List<Object> mItems = null;
	private Context mContext;
	private SparseArray<LinearLayout> canvasList;

	//*******************************************
	//	flip ad
	//
	//XXX@Flip-adapterinit@#Flip-adapterinit#
	private FlipADDeferHelper mFlipHelper;
	//end
	
	public FlipPagerAdapter(
			Context c, 
			List<Object> items,
			FlipADDeferHelper helper)
	{
		mContext = c;
		
		this.mItems = items;				
		
		canvasList = new SparseArray<LinearLayout>();
		
		//XXX@Flip-adapterhelper@#Flip-adapterhelper#
		mFlipHelper = helper;	
		
		mFlipHelper.setAppADListener(new AppADListener() {

			@Override
			public int onADLoaded(int position) {
				// you can edit the getDefaultMinPosition method
				//
				position = getDefaultMinPosition(position);
				
				if (mItems.size() >=  position) {
					mItems.add(position, null);
					notifyDataSetChanged();
					return position;
				}
				else {				
					return -1;
				}
			}
			
		});
		//end
	}
	
	public void destroy() {
		mContext = null;
		if(canvasList != null){
			canvasList.clear();
		}
	}
	
	public int getItemPosition(Object object){
		return POSITION_NONE;
	}
	
	public int getCount() {
		return mItems.size();
	}
	
	public void destroyItem(ViewGroup collection, int position, Object view){
		collection.removeView((View) view);
	}

	public Parcelable saveState(){
		return null;
	}
	
	public Object instantiateItem(View collection, final int position) {//XXX
		
		LinearLayout canvas = getCanvas(position);
		if (canvas == null) {
			
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT);
			
			//XXX@Flip-request@#Flip-request#
			//	get flip
			//
			canvas = mFlipHelper.getAD(position);
			//end
			
			if(canvas == null){
				//	use default page
				//
				canvas = new LinearLayout(mContext);
				canvas.setBackgroundColor(Color.WHITE);
				canvas.setLayoutParams(params);
				
				LayoutManager lm = LayoutManager.getInstance((Activity) mContext);
				
				final CrystalExpressScrollView sv = new CrystalExpressScrollView(mContext);
				sv.setLayoutParams(params);
				
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
				
				//	relative image
				//
				rp = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.MATCH_PARENT,
						lm.getMetric(LayoutID.CONTENT_RELATIVE_IMAGE_HEIGHT));
				rp.addRule(RelativeLayout.BELOW, 300);
				ImageView relativeContentImage = new ImageView(mContext);
				relativeContentImage.setId(400);
				relativeContentImage.setScaleType(ScaleType.MATRIX);
				relativeContentImage.setBackgroundResource(R.drawable.news_related_2);
				relativeContentImage.setLayoutParams(rp);
				
				root.addView(contentImage);
				root.addView(text1);
				root.addView(text2);
				root.addView(relativeContentImage);
				sv.addView(root);
				
				canvas.addView(sv);
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

	//XXX@Flip-listener@#Flip-listener#
	@Override
	public void onPageScrollStateChanged(int state) {
		if (mFlipHelper != null) {
			mFlipHelper.onPageScrollStateChanged(state);
		}
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int position) {	
		if (mFlipHelper != null) {
			mFlipHelper.onPageSelected(position);
		}
	}
	//end
	
	/**
	 * 
	 * you can change the logic to get the default minimum position you want to add
	 * 
	 * */
	private int getDefaultMinPosition(int position) {
		// Don't place ad at the first place
		// or use any value you want.
		//
		return Math.max(1, position);
	}
}
