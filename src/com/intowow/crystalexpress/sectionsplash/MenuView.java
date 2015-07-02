package com.intowow.crystalexpress.sectionsplash;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.intowow.crystalexpress.LayoutManager;
import com.intowow.crystalexpress.LayoutManager.LayoutID;
import com.intowow.crystalexpress.sectionsplash.SectionSplashActivity.PagerEventListener;

public class MenuView extends LinearLayout implements OnClickListener {
	
	public MenuView(Context context) {
		super(context);
		init((Activity)context);
	}
	
	public MenuView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init((Activity)context);
	}
	
	public MenuView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init((Activity)context);
	}

	static private final int ID_TEXTBOX = 1001;
	
	int 	mSectionWidth;
	int 	mScreenWidth;
	int 	mCenter;
	
	Context mContext;
	int		mSectionPadding;

	View 				 v;
	HorizontalScrollView mController;
	LinearLayout		 mTextBox;
	View 				 mIndicator; 
	
	PagerEventListener	 mOnSectionSelectListener;
	
	LayoutManager mlm;

	private void init(Activity activity) {
		mlm = LayoutManager.getInstance(activity);

		setBackgroundColor(Color.WHITE);
		mContext = activity;
		
		DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
		mScreenWidth = metrics.widthPixels;
		mSectionWidth = metrics.densityDpi / 2;		    		
		mSectionPadding = (int) (8 * metrics.density);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				mlm.getMetric(LayoutID.STREAM_TITLE_HEIGHT));
		mController = new HorizontalScrollView(mContext);
		mController.setHorizontalScrollBarEnabled(false);
		mController.setLayoutParams(params);
		
		params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		
		RelativeLayout outerWrapper = new RelativeLayout(mContext);
		outerWrapper.setLayoutParams(params);
		
		RelativeLayout.LayoutParams rParams = new RelativeLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				mlm.getMetric(LayoutID.STREAM_INDICATOR_HEIGHT));
		rParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		
		mIndicator = new View(mContext);
		mIndicator.setLayoutParams(rParams);
		mIndicator.setBackgroundColor(Color.parseColor("#ea5a31"));
		
		rParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		mTextBox = new LinearLayout(mContext);
		mTextBox.setId(ID_TEXTBOX);
		mTextBox.setOrientation(LinearLayout.HORIZONTAL);
		mTextBox.setLayoutParams(rParams);
		    		
		outerWrapper.addView(mTextBox);
		outerWrapper.addView(mIndicator);
		
		
		mController.addView(outerWrapper);	
		
		addView(mController);
	}
	
	public void setColor(int bg, int highlight){
		mController.setBackgroundColor(bg);
		mIndicator.setBackgroundColor(highlight);
	}
	
	public void setSectionList(ArrayList<String> sections){
		if (sections.isEmpty()) {
			return;
		}
		
		if (mScreenWidth / mSectionWidth > sections.size()) {
			mSectionWidth = mScreenWidth / sections.size();
		}
		mCenter = (mScreenWidth - mSectionWidth) / 2;
		
		LayoutParams lp = new LayoutParams(
			mSectionWidth, 
			LayoutParams.WRAP_CONTENT
		);
		
		for (int i = 0 ; i < sections.size() ; i++) {			
			TextView tv = new TextView(mContext);
			tv.setText(sections.get(i));
			tv.setPadding(0, mSectionPadding, 0, mSectionPadding);
			tv.setGravity(Gravity.CENTER);
			tv.setTypeface(null, Typeface.BOLD);
			tv.setTextColor(Color.parseColor("#ea5a31"));    			
			tv.setTag(i);
			tv.setOnClickListener(this);
			tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,
					mlm.getMetric(LayoutID.STREAM_MENU_TEXT_SIZE));
			
			mTextBox.addView(tv, lp);
		}
		
		ViewGroup.LayoutParams indicatorLp = (ViewGroup.LayoutParams) mIndicator.getLayoutParams();
		indicatorLp.width = mSectionWidth;
	}
	
	public void select(float position){
		final int indicatorPos = (int) (mSectionWidth * position);

		// Move indicator
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mIndicator.getLayoutParams();
		lp.setMargins(indicatorPos, 0, 0, 0);
		mIndicator.setLayoutParams(lp);
		
		//	reset text color
		//
		for(int i = 0 ; i< mTextBox.getChildCount() ; i++){
			TextView tv = (TextView) mTextBox.getChildAt(i);
			if(position!=i){
				tv.setTextColor(Color.parseColor("#b4b4b4")); 
			}else{
				tv.setTextColor(Color.parseColor("#ea5a31")); 
			}
		}
		
		// Move scroller
		mController.post(new Runnable() {
			public void run(){
				if (indicatorPos > mCenter) {
					mController.scrollTo(indicatorPos - mCenter, 0);
				} else {
					mController.scrollTo(0, 0);
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		select((Integer) v.getTag());
		if (mOnSectionSelectListener != null) {
			mOnSectionSelectListener.onPageChanged((Integer) v.getTag());
		}
	}

	public void setOnSectionSelecteListener(final PagerEventListener listener) {
		mOnSectionSelectListener = listener;
	}
}
