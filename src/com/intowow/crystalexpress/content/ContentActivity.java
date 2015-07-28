package com.intowow.crystalexpress.content;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.intowow.crystalexpress.Config;
import com.intowow.crystalexpress.LayoutManager;
import com.intowow.crystalexpress.LayoutManager.LayoutID;
import com.intowow.crystalexpress.R;
import com.intowow.crystalexpress.cedemo.BaseActivity;
import com.intowow.crystalexpress.content.CrystalExpressScrollView.ScrollViewListener;
import com.intowow.sdk.I2WAPI;

public class ContentActivity extends BaseActivity {//XXX#Content-activity#

	// XXX@Content-init@#Content-init#
	private final static String mPlacement = Config.CONTENT_PLACEMENT;
	private ContentHelper mContentHelper = null;

	// end

	@Override
	protected void onCreate(Bundle savedInstanceState) {// XXX
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_content);

		LayoutManager lm = LayoutManager.getInstance(this);

		// title
		//
		findViewById(R.id.title).getLayoutParams().height = lm
				.getMetric(LayoutID.CONTENT_TITLE_HEIGHT);

		// title back
		//
		RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(
				lm.getMetric(LayoutID.CONTENT_TITLE_BACK_SIZE),
				lm.getMetric(LayoutID.CONTENT_TITLE_BACK_SIZE));
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		final View titleBack = findViewById(R.id.titleback);
		titleBack.setLayoutParams(rp);
		titleBack.setBackgroundResource(R.drawable.back_nm);
		titleBack.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(final View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					v.setBackgroundResource(R.drawable.back_at);
				} else if (event.getAction() == MotionEvent.ACTION_UP
						|| event.getAction() == MotionEvent.ACTION_CANCEL) {
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

		// image 1
		//
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				lm.getMetric(LayoutID.CONTENT_IMAGE_HEIGHT));
		ImageView image1 = (ImageView) findViewById(R.id.image1);
		image1.setLayoutParams(params);

		// text1
		//
		params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params.topMargin = lm.getMetric(LayoutID.CONTENT_TEXT_AREA_MARGIN_TOP);
		params.leftMargin = lm
				.getMetric(LayoutID.CONTENT_TEXT_MARGIN_LEFT_RIGHT);
		params.rightMargin = lm
				.getMetric(LayoutID.CONTENT_TEXT_MARGIN_LEFT_RIGHT);
		TextView text1 = (TextView) findViewById(R.id.text1);
		text1.setLayoutParams(params);
		text1.setText(R.string.content_text_top);
		text1.setTextColor(Color.parseColor("#929292"));
		text1.setTextSize(TypedValue.COMPLEX_UNIT_PX,
				lm.getMetric(LayoutID.CONTENT_TEXT_SIZE));
		text1.setLineSpacing(lm.getMetric(LayoutID.CONTENT_TEXT_LINE_SPACING),
				1.0f);

		params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params.topMargin = lm.getMetric(LayoutID.CONTENT_TEXT_AREA_MARGIN_TOP);
		params.leftMargin = lm
				.getMetric(LayoutID.CONTENT_TEXT_MARGIN_LEFT_RIGHT);
		params.rightMargin = lm
				.getMetric(LayoutID.CONTENT_TEXT_MARGIN_LEFT_RIGHT);

		TextView text2 = (TextView) findViewById(R.id.text2);
		text2.setLineSpacing(lm.getMetric(LayoutID.CONTENT_TEXT_LINE_SPACING),
				1.0f);
		text2.setTextColor(Color.parseColor("#929292"));
		text2.setText(R.string.content_text_bottom);
		text2.setTextSize(TypedValue.COMPLEX_UNIT_PX,
				lm.getMetric(LayoutID.CONTENT_TEXT_SIZE));
		text2.setLayoutParams(params);

		// relative image
		//
		params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				lm.getMetric(LayoutID.CONTENT_RELATIVE_IMAGE_HEIGHT));
		ImageView relativeContentImage = (ImageView) findViewById(R.id.image2);
		relativeContentImage.setScaleType(ScaleType.MATRIX);
		relativeContentImage.setLayoutParams(params);

		// XXX@Content-inithelper@#Content-inithelper#
		// content ad view
		//
		final RelativeLayout contentAdLayout = (RelativeLayout) findViewById(R.id.contentad);
		final CrystalExpressScrollView sv = (CrystalExpressScrollView) findViewById(R.id.scrollview);

		mContentHelper = new ContentHelper(this, mPlacement, sv,
				contentAdLayout);
		mContentHelper.setActive();
		mContentHelper.onPageSelected(0);
		// end

		// XXX@Content-initscroll@#Content-initscroll#
		// callback
		//
		sv.setScrollViewListener(new ScrollViewListener() {
			public void onScrollChanged(CrystalExpressScrollView scrollView,
					int x, int y, int oldX, int oldY) {
			}

			public void onScrollViewIdle() {
				if (mContentHelper != null) {
					mContentHelper.checkContentAD();
				}
			}
		});
		// end

		// XXX@Content-load@#Content-load#
		if (mContentHelper != null) {
			mContentHelper.loadContentAd();
		}
		// end
	}

	//XXX@Content-life@#Content-life#
	@Override
	public void onResume() {
		super.onResume();

		if (mContentHelper != null) {
			mContentHelper.start();
		}
	}

	@Override
	public void onPause() {
		super.onPause();

		if (mContentHelper != null) {
			mContentHelper.stop();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (mContentHelper != null) {
			mContentHelper.destroy();
			mContentHelper = null;
		}
		
	}
	//end
}
